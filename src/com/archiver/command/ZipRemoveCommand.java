package com.archiver.command;

import com.archiver.ConsoleHelper;
import com.archiver.ZipFileManager;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 22/01/2021
 */
public class ZipRemoveCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Please write which file need to remove (full path inside the archive):");
        ZipFileManager zipFileManager = getZipFileManager();
        Path fileToRemovePath = Paths.get(ConsoleHelper.readString());
        zipFileManager.removeFile(fileToRemovePath);
        ConsoleHelper.writeMessage("Remove was executed.");
    }
}
