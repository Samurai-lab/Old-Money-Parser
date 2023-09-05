package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileFun {
    protected static void updateFile(String fileName, String text, Boolean rewriteFile) {
        try (FileWriter writer = new FileWriter(fileName + ".txt", rewriteFile)) {
            // запись всей строки
            writer.write(text);
            // запись по символам
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
                System.out.println(line); /** При продакшене удалить!!**/
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
