package be.isach.samaritan.json;

import org.json.JSONObject;

/**
 * Package: be.isach.samaritan.json
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class AdvancedJSONObject extends JSONObject {

    public AdvancedJSONObject(String source) {
        super(source);
    }

    public void addDefault(String path, JSONObject jsonObject) {
        if(get(path) == null) {
            put(path, jsonObject);
        }
    }

}
