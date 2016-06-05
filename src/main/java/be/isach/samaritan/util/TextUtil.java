package be.isach.samaritan.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.util
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 13:32
 */
public class TextUtil {

    public static String getSpaces(int spaces) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < spaces; i++)
            stringBuilder.append(" ");
        return stringBuilder.toString();
    }

    public static String formatAliasesList(List<String> aliases) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(aliases.get(0));
        if(aliases.size() > 1) {
            stringBuilder.append(" (");
            for(int i = 1; i < aliases.size(); i++) {
                stringBuilder.append(aliases.get(i) + (i == aliases.size() - 1 ? "" : ", "));
            }
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    public static String postToHastebin(String body) throws UnirestException {
        return Unirest.post("http://hastebin.com/documents").body(body).asJson().getBody().getObject().getString("key");
    }

    public static String postToHastebin(String body, boolean asURL) throws UnirestException {
        if(asURL){
            return "http://hastebin.com/" + postToHastebin(body);
        } else {
            return postToHastebin(body);
        }
    }

}
