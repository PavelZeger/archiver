package com.archiver;

import com.archiver.command.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 23/01/2021
 */
public class CommandExecutor {

    private static final Map<Operation, Command> ALL_KNOWN_COMMANDS_MAP = new HashMap<>();

    private CommandExecutor() {
    }

    static {
        ALL_KNOWN_COMMANDS_MAP.put(Operation.CREATE, new ZipCreateCommand());
        ALL_KNOWN_COMMANDS_MAP.put(Operation.ADD, new ZipAddCommand());
        ALL_KNOWN_COMMANDS_MAP.put(Operation.REMOVE, new ZipRemoveCommand());
        ALL_KNOWN_COMMANDS_MAP.put(Operation.EXTRACT, new ZipExtractCommand());
        ALL_KNOWN_COMMANDS_MAP.put(Operation.CONTENT, new ZipContentCommand());
        ALL_KNOWN_COMMANDS_MAP.put(Operation.EXIT, new ExitCommand());

    }

    public static void execute(Operation operation) throws Exception {
        if (ALL_KNOWN_COMMANDS_MAP.containsKey(operation)) {
            ALL_KNOWN_COMMANDS_MAP.get(operation).execute();

        }

    }

}
