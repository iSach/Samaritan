package be.isach.samaritan.level;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.utils.ApplicationUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.level
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 20:42
 */
public class AccessLevelManager {

    /**
     * Access Level Map.
     */
    private Map<String, Integer> levelsMap;

    /**
     * Samaritan Instance.
     */
    private Samaritan samaritan;

    public AccessLevelManager(Samaritan samaritan) {
        this.samaritan = samaritan;
        this.levelsMap = new HashMap<>();
    }

    public void loadUsers() {
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("users.json"))));
            samaritan.getJda().getUsers().stream().filter(user -> !obj.has(user.getId())).forEachOrdered(user -> {
                JSONObject userObject = new JSONObject();
                if (user.getId().equals(samaritan.getOwnerId()))
                    userObject.put("access-level", 4);
                else
                    userObject.put("access-level", 0);
                obj.put(user.getId(), userObject);
            });
            try {
                Files.write(Paths.get("users.json"), obj.toString(4).getBytes());
            } catch (IOException e1) {
                System.out.println("No users.json was found, and Samaritan failed to generate a new one.");
                e1.printStackTrace();
            }
            for (String s : obj.keySet()) {
                JSONObject jsonObject = obj.getJSONObject(s);
                try {
                    User user = samaritan.getJda().getUserById(s);
                    int level = jsonObject.getInt("access-level");
                    levelsMap.put(user.getId(), level);
                } catch (Exception ignored) {

                }
            }
        } catch (IOException e) {
            JSONObject object = new JSONObject();
            for (Guild guild : samaritan.getJda().getGuilds()) {
                for (User user : guild.getUsers()) {
                    JSONObject userObject = new JSONObject();
                    if (user.getId().equals(samaritan.getOwnerId())) {
                        userObject.put("access-level", 4);
                        continue;
                    }
                    userObject.put("access-level", 0);
                    object.put(user.getId(), userObject);
                }
            }
            try {
                Files.write(Paths.get("users.json"), object.toString(4).getBytes());
            } catch (IOException e1) {
                System.out.println("No users.json was found, and Samaritan failed to generate a new one.");
                e1.printStackTrace();
            }
        }
    }

    public void setLevel(User user, int level) throws IOException {
        levelsMap.put(user.getId(), level);
        JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("users.json"))));
        JSONObject userObject = new JSONObject();
        userObject.put("access-level", level);
        obj.put(user.getId(), userObject);
        Files.write(Paths.get("users.json"), obj.toString(4).getBytes());
    }

    public int getAccessLevel(User user) {
        if (user.getId().equals(samaritan.getJda().getSelfInfo().getId())) return 4;
        try {
            return levelsMap.get(user.getId());
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasAccessLevel(int level, User user) {
        return getAccessLevel(user) >= level;
    }

}
