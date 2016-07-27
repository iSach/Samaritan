package be.isach.samaritan.pokemongo;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.command.CommandPokeGo;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import com.pokegoapi.api.PokemonGo;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 27/07/16
 * Project: samaritan
 */
public class PokemonRouteThread extends Thread {

    private boolean running;
    private DirectionsStep[] steps;
    private int currentStep;
    private Samaritan samaritan;
    private PokemonGo go;
    private boolean startLoc = true, catchingPokemons = false;
    private CommandPokeGo commandPokeGo;

    public PokemonRouteThread(DirectionsStep[] steps, Samaritan samaritan, CommandPokeGo commandPokeGo) {
        this.steps = steps;
        this.samaritan = samaritan;
        this.commandPokeGo = commandPokeGo;
        this.currentStep = 0;
        this.go = samaritan.getPokemonGo();
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("running...");
        while (running) {
            long toSleep = 3500L;

            if (currentStep >= steps.length) {
                cancel();
                return;
            }

            try {
                DirectionsStep step = steps[currentStep];

                LatLng latLng = startLoc ? step.startLocation : step.endLocation;

                go.setLatitude(latLng.lat);
                go.setLongitude(latLng.lng);
                commandPokeGo.catchPokemon(true);

                if(catchingPokemons) {
                    commandPokeGo.catchPoke(go.getMap().getCatchablePokemon().get(0).getEncounterId());
                }

                catchingPokemons = !go.getMap().getCatchablePokemon().isEmpty();

                if (!catchingPokemons) {
                    if(!startLoc) {
                        currentStep++;
                    }
                    startLoc = !startLoc;

                    // 2 sec for 100meters.
                    double distance = 0;
                    if(startLoc) {
                        distance = distance(go.getLatitude(), go.getLongitude(),
                                step.endLocation.lat, step.endLocation.lng);
                        toSleep += 3500 * ((int)distance / 50);
                    } else {
                        if(currentStep == (steps.length - 1)) return;
                        DirectionsStep otherStep = steps[currentStep + 1];
                        distance = distance(go.getLatitude(), go.getLongitude(),
                                otherStep.startLocation.lat, otherStep.startLocation.lng);
                        toSleep += 3500 * ((int)distance / 50);
                    }
                    System.out.println("Distance: " + distance + "m   (" + toSleep + "ms)");
                }

                sleep(toSleep);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000;
    }

    @Override
    public synchronized void start() {
        running = true;
        super.start();
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    private void cancel() {
        running = false;
    }
}