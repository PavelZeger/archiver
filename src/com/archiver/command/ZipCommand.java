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
public abstract class ZipCommand implements Command {

    @Override
    public void execute() throws Exception { }

    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Please write the full path to the archive file:");
        Path archiveFilePath = Paths.get(ConsoleHelper.readString());
        return new ZipFileManager(archiveFilePath);
    }

}
