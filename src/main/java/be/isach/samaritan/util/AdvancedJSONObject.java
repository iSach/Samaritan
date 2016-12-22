package be.isach.samaritan.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Package: be.isach.samaritan.json
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class AdvancedJSONObject extends JSONObject {

    public AdvancedJSONObject() {
        super();
    }

    public AdvancedJSONObject(String source) {
        super(source);
    }

    public void addDefault(String path, Object jsonObject) {
        try {
            if (get(path) == null) {
                put(path, jsonObject);
            }
        } catch (JSONException exc) {
            put(path, jsonObject);
        }
    }

}
