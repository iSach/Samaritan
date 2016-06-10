package be.isach.samaritan.command;

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
//                    DownloadInfo i2 = i1.getInfo();

                // notify app or save download state
                // you can extract information from DownloadInfo info;
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
                        break;
                    case RETRYING:
                        System.out.println(i1.getState() + " " + i1.getDelay());
                        break;
                    case DOWNLOADING:
                        long now = System.currentTimeMillis();
                        if (now - 1000 > last) {
                            last = now;

                            String parts = "";

//                                List<Part> pp = i2.getParts();
//                                if (pp != null) {
//                                    // multipart download
//                                    for (Part p : pp) {
//                                        if (p.getState().equals(States.DOWNLOADING)) {
//                                            parts += String.format("Part#%d(%.2f) ", p.getNumber(), p.getCount()
//                                                    / (float) p.getLength());
//                                        }
//                                    }
//                                }

//                                System.out.println(String.format("%s %.2f %s", i1.getState(),
//                                        i2.getCount() / (float) i2.getLength(), parts));
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

            File path = new File("/home/samaritan/music");

            VGet v = new VGet(info, path);

            v.extract(user, stop, notify);

            System.out.println("Title: " + info.getTitle());
            System.out.println("Download URL: " + info.getSource());

            v.download(user, stop, notify);
        } catch (MalformedURLException | RuntimeException e) {
            e.printStackTrace();
        }
    }
}
