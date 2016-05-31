package be.isach.samaritan.music;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.Guild;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.music
 * Created by: Sacha
 * Created on: 19th May, 2016
 * at 20:12
 */
public class SongPlayer extends Thread {

    /**
     * JDA Player.
     */
    private Player player = null;

    /**
     * Shuffle Mode active.
     */
    private boolean shuffleModeActive = false;

    /**
     * Current Song ID, for Shuffle Mode.
     */
    private int currentId = 0;

    /**
     * SongPlayer's Guild.
     */
    private Guild guild;

    /**
     * Samaritan Instance.
     */
    private Samaritan samaritan;

    public SongPlayer(Guild guild, Samaritan samaritan) {
        this.guild = guild;
        this.samaritan = samaritan;
    }

    @Override
    public void run() {
        while (true) {
            if (player == null && shuffleModeActive) {
                playSong(currentId);
                continue;
            }
            if (player != null && !player.isPlaying() && shuffleModeActive) {
                currentId++;
                if (currentId > getSongs().entrySet().size() - 1) currentId = getSongs().entrySet().size() - 1;
                if (currentId < 0) currentId = 0;
                System.out.println("SONG_PLAYER: Current Song finished, switching to: " + getSongs().get(currentId).getName());
                playSong(currentId);
                continue;
            }
            if (shuffleModeActive) {
                System.out.println("SONG_PLAYER: Waiting for current Song to finish.");
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Plays a song by ID.
     * @param id Song ID.
     */
    public void playSong(int id) {
        if (id > getSongs().entrySet().size() - 1) id = getSongs().entrySet().size() - 1;
        if (id < 0) id = 0;

        interruptCurrentSong();

        File audioFile = getSongs().get(id);

        try {
            player = new FilePlayer(audioFile);

            getGuild().getAudioManager().setSendingHandler(player);

            player.play();
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        // (TODO PLAY MESSAGE) Message message = Samaritan.laboratoryChannel.sendMessage("♫ Playing " + Samaritan.getInstance().getSongPlayer().getSongs().get(id).getName().replace(".mp3", "") + " ♫");
    }

    /**
     * @return {@code true} if the code shuffle mode is on, otherwise {@code false}.
     */
    public boolean isShuffleModeActive() {
        return shuffleModeActive;
    }

    /**
     * Toggles the Shuffle Mode.
     */
    public void toggleShuffleMode() {
        shuffleModeActive = !shuffleModeActive;
    }

    /**
     * @return Available songs.
     */
    public Map<Integer, File> getSongs() {
        Map<Integer, File> songs = new HashMap<>();
        File musicDirectory = new File("music");
        final int[] i = {0};
        Arrays.asList(musicDirectory.listFiles()).stream().filter(f -> f.getPath().endsWith(".mp3")).forEach(songFile -> {
            songs.put(i[0], songFile);
            i[0]++;
        });
        return songs;
    }

    /**
     * @return The SongPlayer Guild.
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * Stops current Song.
     */
    private void interruptCurrentSong() {
        if (player == null) return;
        player.stop();
    }

}
