package ru.sberbank.mobile.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private static final int BUFFER_SIZE = 1024;

    public static void copy(InputStream source, OutputStream sink) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = source.read(buffer);
        while (len != -1) {
            sink.write(buffer, 0, len);
            len = source.read(buffer);
        }
    }

    public static void copyFromFile(File sourceFile, OutputStream sink) throws IOException {
        InputStream source = null;
        try {
            source = new BufferedInputStream(new FileInputStream(sourceFile));
            copy(source, sink);
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyToFile(InputStream source, File sinkFile) throws IOException {
        OutputStream sink = null;
        try {
            sink = new BufferedOutputStream(new FileOutputStream(sinkFile));
            copy(source, sink);
        } finally {
            if (sink != null) {
                try {
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
