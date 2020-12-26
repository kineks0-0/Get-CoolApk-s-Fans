package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileWrite {

    public static void write2File(String FileName, String str) {

        File file = new File(FileName);
        if (!file.exists()) {
            try {
                file.createNewFile(); // 如果文件不存在则创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeInFile(file, str); // 写入文件
    }

    private static void writeInFile(File file, String content) {
        Writer writer = null;
        StringBuilder outputString = new StringBuilder();
        try {
            outputString.append(content);
            writer = new FileWriter(file, false); // true表示追加
            writer.write(outputString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
