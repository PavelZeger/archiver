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
public class ZipAddCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Adding a new file to the archive.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Insert a new file name path:");
            Path sourcePath = Paths.get(ConsoleHelper.readString());
            zipFileManager.addFile(sourcePath);
            ConsoleHelper.writeMessage("Adding the file was completed.");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("The file wasn't found.");
        }
    }

}
