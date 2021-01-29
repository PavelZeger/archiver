package com.archiver.command;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;
import com.archiver.exception.PathIsNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 22/01/2021
 */
public class ZipCreateCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Creating archive.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Please write the full path to the archive file:");
            String archiveFileString = ConsoleHelper.readString();
            Path archiveFilePath = Paths.get(archiveFileString);
            zipFileManager.createZip(archiveFilePath);
            ConsoleHelper.writeMessage("The archive was created.");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("You've inserted the wrong file name or directory name.");
        }
    }

}
