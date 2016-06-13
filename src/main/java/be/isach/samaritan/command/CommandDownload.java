package be.isach.samaritan.command;

import be.isach.samaritan.music.AudioFilesManager;
import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.vget.vhs.VimeoInfo;
import net.dv8tion.jda.entities.MessageChannel;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 19th May, 2016
 * at 19:08
 * <p>
 * Description: Downloads a song from Youtube.
 */
class CommandDownload extends Command {

    private VideoInfo info;
    private long last;

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandDownload(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        try {
            AtomicBoolean stop = new AtomicBoolean(false);
            Runnable notify = () -> {
                VideoInfo i1 = info;
                switch (i1.getState()) {
                    case EXTRACTING:
                    case EXTRACTING_DONE:
                    case DONE:
                        if (i1 instanceof com.github.axet.vget.vhs.YouTubeInfo) {
                            com.github.axet.vget.vhs.YouTubeInfo i = (com.github.axet.vget.vhs.YouTubeInfo) i1;
                            System.out.println(i1.getState() + " " + i.getVideoQuality());
                        } else if (i1 instanceof VimeoInfo) {
                            VimeoInfo i = (VimeoInfo) i1;
                            System.out.println(i1.getState() + " " + i.getVideoQuality());
                        } else {
                            System.out.println("downloading unknown quality");
                        }

                        AudioFilesManager.checkforSongsToConvert();
                        break;
                    case RETRYING:
                        System.out.println(i1.getState() + " " + i1.getDelay());
                        break;
                    case DOWNLOADING:
                        long now = System.currentTimeMillis();
                        if (now - 1000 > last) {
                            last = now;
                        }
                        break;
                    default:
                        break;
                }
            };

            URL web = null;
            web = new URL(buildStringFromArgs());
            VGetParser user = null;
            user = VGet.parser(web);
            info = user.info(web);

            File path = new File("music");

            VGet v = new VGet(info, path);

            v.extract(user, stop, notify);

            System.out.println("Title: " + info.getTitle());
            System.out.println("Download URL: " + info.getSource());

            v.download(user, stop, notify);

            System.out.println("Download finished. Converting.");
        } catch (MalformedURLException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
