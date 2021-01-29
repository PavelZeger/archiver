package com.archiver;

import com.archiver.exception.PathIsNotFoundException;
import com.archiver.exception.WrongZipFileException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 22/01/2021
 */
public class ZipFileManager {

    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;

    }

    public void createZip(Path source) throws Exception {
        if (!Files.exists(zipFile.getParent())) Files.createDirectories(zipFile.getParent());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());

            } else if (Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();
                fileNames.forEach(fileName -> {
                    try {
                        addNewZipEntry(zipOutputStream, source, fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                throw new PathIsNotFoundException();

            }

        }

    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        try (InputStream inputStream = Files.newInputStream(filePath.resolve(fileName))) {
            ZipEntry zipEntry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(zipEntry);
            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();

        }

    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);

        }

    }

    public List<FileProperties> getFilesList() throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        List<FileProperties> filesProperties = new ArrayList<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                copyData(zipInputStream, byteArrayOutputStream);
                FileProperties file = new FileProperties(
                        zipEntry.getName(),
                        zipEntry.getSize(),
                        zipEntry.getCompressedSize(),
                        zipEntry.getMethod());
                filesProperties.add(file);
                byteArrayOutputStream.close();
            }
            zipInputStream.closeEntry();
        }

        return filesProperties;
    }

    public void extractAll(Path outputFolder) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            if (Files.notExists(outputFolder)) Files.createDirectories(outputFolder);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                Path fileFullName = outputFolder.resolve(fileName);
                Path parent = fileFullName.getParent();
                if (Files.notExists(parent)) Files.createDirectories(parent);
                try (OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

}
