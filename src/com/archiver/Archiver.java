package com.archiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Archiver {

    public static void main(String[] args) throws Exception {

        System.out.println("Write below the full path of a file to be archived:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String filePath = reader.readLine();
        ZipFileManager zipFileManager = new ZipFileManager(Paths.get(filePath));

        System.out.println("Write below the path to the archive:");
        String archivePath = reader.readLine();
        zipFileManager.createZip(Paths.get(archivePath));
        reader.close();

    }

}
