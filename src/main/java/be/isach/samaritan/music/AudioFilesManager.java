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
        System.out.println("Trying to convert: " + file.getName());
        String spacedName = file.getName().replace(".webm", ".mp3");
        File webmFile = file.getAbsoluteFile();
        webmFile.renameTo(new File(webmFile.getAbsolutePath().replace(" ", "")));
        File mp3File = new File(webmFile.getAbsolutePath().replace(".webm", ".mp3"));

        System.out.println("SpacedName: " + spacedName);

        try {
            Process process = Runtime.getRuntime().exec("ffmpeg -i " + webmFile.getAbsolutePath() + " " + mp3File.getAbsolutePath());
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
