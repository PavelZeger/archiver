package com.archiver;

import com.archiver.exception.PathIsNotFoundException;
import com.archiver.exception.WrongZipFileException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
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
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
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

    public void removeFiles(List<Path> pathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        Path tempFile = Files.createTempFile(null, null);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while(zipEntry != null) {
                    Path archivedFile = Paths.get(zipEntry.getName());
                    if (!pathList.contains(archivedFile)) {
                        String fileName = zipEntry.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));
                        copyData(zipInputStream, zipOutputStream);
                        zipInputStream.closeEntry();
                        zipOutputStream.closeEntry();
                    } else {
                        ConsoleHelper.writeMessage(String.format("The file %s was removed from the archive.", archivedFile.toString()));
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        Path tempFile = Files.createTempFile(null, null);
        List<Path> previousFiles = new ArrayList<>();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while(zipEntry != null) {
                    String fileName = zipEntry.getName();
                    previousFiles.add(Paths.get(fileName));
                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    copyData(zipInputStream, zipOutputStream);
                    zipInputStream.closeEntry();
                    zipOutputStream.closeEntry();
                    zipEntry = zipInputStream.getNextEntry();
                }
            }

            for (Path path : absolutePathList) {
                if (Files.isRegularFile(path)) {
                    if (previousFiles.contains(path.getFileName())) {
                        ConsoleHelper.writeMessage(String.format("The file %s already exists", path.toString()));
                    } else {
                        addNewZipEntry(zipOutputStream, path.getParent(), path.getFileName());
                        ConsoleHelper.writeMessage(String.format("The file %s was added to the archive", path.toString()));
                    }
                } else {
                    throw  new PathIsNotFoundException();
                }
            }
        }
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }

}
