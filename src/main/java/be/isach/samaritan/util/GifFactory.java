package be.isach.samaritan.util;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.util
 * Created by: Sacha
 * Created on: 30th mai, 2016
 * at 19:12
 * <p>
 * Allows to easily get a random GIF URL from a key.
 * Fetches GIFs on Giphy.
 */
public class GifFactory {

    private Giphy giphy;

    public GifFactory() {
        this.giphy = new Giphy("dc6zaTOxFJmzC");
    }

    /**
     * Fetches a random GIF about the given subject on Giphy.
     *
     * @param subject
     * @return
     */
    public String getRandomGif(String subject) {
        SearchFeed feed = null;
        String url = null;
        try {
            feed = giphy.search(subject, 1, 0);
            url = feed.getDataList().get(MathUtils.getRandom().nextInt(feed.getDataList().size())).getImages().getOriginal().getUrl();
        } catch (GiphyException e) {
            e.printStackTrace();
        }
        return url;
    }

}
