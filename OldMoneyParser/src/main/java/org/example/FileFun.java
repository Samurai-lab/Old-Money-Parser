package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileFun {
    protected static void updateFile(String fileName, String text, Boolean rewriteFile) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".txt", rewriteFile), "Cp1251"))) {
            writer.write(text);
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static ArrayList readFileInfo(String filename) {
        ArrayList<String> words = new ArrayList<>();
        int iteration = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename + ".txt"));
            String line = reader.readLine();
            while (line != null) {
                words.add(iteration, line);
                System.out.println(line);
                line = reader.readLine();
                iteration++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
