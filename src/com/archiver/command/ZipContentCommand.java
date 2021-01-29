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
        ConsoleHelper.writeMessage("Reading the content of the archive.");
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("The content of the archive:");
        List<FileProperties> filePropertiesList = zipFileManager.getFilesList();
        filePropertiesList.stream()
                .map(FileProperties::toString)
                .forEach(System.out::println);
        ConsoleHelper.writeMessage("The content of the archive was read.");
    }

}
