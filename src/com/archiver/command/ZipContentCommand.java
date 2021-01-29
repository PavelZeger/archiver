package com.archiver.command;

import com.archiver.ConsoleHelper;
import com.archiver.FileProperties;
import com.archiver.ZipFileManager;
import java.util.List;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 22/01/2021
 */
public class ZipContentCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Просмотр содержимого архива.");
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Содержимое архива:");
        List<FileProperties> filePropertiesList = zipFileManager.getFilesList();
        filePropertiesList.stream()
                .map(FileProperties::toString)
                .forEach(System.out::println);
        ConsoleHelper.writeMessage("Содержимое архива прочитано.");
    }

}
