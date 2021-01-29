package com.archiver.command;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;
import com.archiver.exception.WrongZipFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 22/01/2021
 */
public class ZipExtractCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Распаковка архива.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Введите путь для распаковки:");
            Path destinationPath = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(destinationPath);
            ConsoleHelper.writeMessage("Архив был распакован.");
        } catch (WrongZipFileException e) {
            ConsoleHelper.writeMessage("Архив не существует.");
        }
    }
}
