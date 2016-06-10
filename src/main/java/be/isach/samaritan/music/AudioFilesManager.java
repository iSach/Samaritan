package be.isach.samaritan.music;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.music
 * Created by: Sacha
 * Created on: 10th juin, 2016
 * at 23:11
 *
 * REQUIRES FFMPEG.
 */
public class AudioFilesManager {

    public static void checkforSongsToConvert() {
        cleanMp4();

        File directory = new File("/home/samaritan/music");

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".webm")) {
                convert(file);
            }
        }
    }

    private static void convert(File file) {
        File webmFile = file.getAbsoluteFile();
        String spacedName = webmFile.getName().replace(".webm", ".mp3");
        webmFile.renameTo(new File(webmFile.getAbsolutePath().replace(" ", "")));
        File mp3File = new File(webmFile.getAbsolutePath().replace(".webm", ".mp3"));

        try {
            FFmpeg ffmpeg = null;
            ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");

            FFmpegBuilder builder = new FFmpegBuilder()

                    .setInput(webmFile.getAbsolutePath())
                    .overrideOutputFiles(true)
                    .addOutput(mp3File.getAbsolutePath())
                    .setFormat("mp3")
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

            executor.createJob(builder).run();

            executor.createTwoPassJob(builder).run();

            mp3File.renameTo(new File(spacedName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanMp4() {
        File file = new File("/home/samaritan/music");
        Arrays.asList(file.listFiles()).stream().filter(file1 -> file1.getName().endsWith(".mp4")).forEach(File::delete);
    }

}
