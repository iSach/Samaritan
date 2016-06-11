package be.isach.samaritan.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.music
 * Created by: Sacha
 * Created on: 10th juin, 2016
 * at 23:11
 * <p>
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
//        System.out.println("Trying to convert: " + file.getName());
//        String spacedName = file.getName().replace(".webm", ".mp3");
//        File webmFile = file.getAbsoluteFile();
//        webmFile.renameTo(new File(webmFile.getAbsolutePath().replace(" ", "")));
//        File mp3File = new File(webmFile.getAbsolutePath().replace(" ", "").replace(".webm", ".mp3"));
//
//        System.out.println("\nExists: " + webmFile.exists() + "\n");

        String[] commandCD = {
                "cd music",
        };
        String[] command = {
                "ffmpeg",
                "-i",
                "\"" + "a b.webm".replace(" ", "\\ ") + "\"",
                "\"" + "a b.mp3" + "\""
        };

        System.out.println(Arrays.asList(command));
        execProcess(commandCD);
        execProcess(command);
//        mp3File.renameTo(new File(spacedName));
    }

    private static void execProcess(String[] args) {
        try {
            Process p = Runtime.getRuntime().exec(args);
            BufferedReader output = getOutput(p);
            BufferedReader error = getError(p);
            String line = "";

            while ((line = output.readLine()) != null) {
                System.out.println(line);
            }

            while ((line = error.readLine()) != null) {
                System.out.println(line);
            }

            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }

    private static void cleanMp4() {
        File file = new File("/home/samaritan/music");
//        Arrays.asList(file.listFiles()).stream().filter(file1 -> file1.getName().endsWith(".mp4")).forEach(File::delete);
    }
}
