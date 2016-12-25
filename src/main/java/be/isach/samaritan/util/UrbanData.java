package be.isach.samaritan.util;

/**
 * Created by sacha on 25/12/16.
 */
public class UrbanData {

    private String word;
    private String definition;
    private String link;

    public UrbanData(String word, String definition, String link) {
        this.word = word;
        this.definition = definition;
        this.link = link;
    }

    public String getDefinition() {
        return definition;
    }

    public String getWord() {
        return word;
    }

    public String getLink() {
        return link;
    }
}
