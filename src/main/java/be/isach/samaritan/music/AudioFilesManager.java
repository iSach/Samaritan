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
        System.out.println("Trying to convert: " + file.getName());
        String spacedName = file.getName().replace(".webm", ".mp3");
        File webmFile = file.getAbsoluteFile();
        webmFile.renameTo(new File(webmFile.getAbsolutePath().replace(" ", "")));
        File mp3File = new File(webmFile.getAbsolutePath().replace(".webm", ".mp3"));

        System.out.println("SpacedName: " + spacedName);

            try {
                String[] command = {
                        "ffmpeg",
                        "-i",
                        webmFile.getAbsolutePath(),
                        mp3File.getAbsolutePath()
                };
                Process p = Runtime.getRuntime().exec(command);
                BufferedReader output = getOutput(p);
                BufferedReader error = getError(p);
                String ligne = "";

                while ((ligne = output.readLine()) != null) {
                    System.out.println(ligne);
                }

                while ((ligne = error.readLine()) != null) {
                    System.out.println(ligne);
                }

                p.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        mp3File.renameTo(new File(spacedName));
    }

    private static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }

    private static void cleanMp4() {
        File file = new File("/home/samaritan/music");
        Arrays.asList(file.listFiles()).stream().filter(file1 -> file1.getName().endsWith(".mp4")).forEach(File::delete);
    }
}
