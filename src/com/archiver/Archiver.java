package com.archiver;

import com.archiver.exception.WrongZipFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * @author Pavel Zeger
 * @implNote Archiver
 * @since 22/01/2021
 */
public class Archiver {

    public static void main(String[] args) throws Exception {

//        System.out.println("Write below the full path of a file to be archived:");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        String filePath = reader.readLine();
//        ZipFileManager zipFileManager = new ZipFileManager(Paths.get(filePath));
//
//        System.out.println("Write below the path to the archive:");
//        String archivePath = reader.readLine();
//        zipFileManager.createZip(Paths.get(archivePath));
//        reader.close();

        Operation operation = null;
        while (operation != Operation.EXIT) {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);

            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл." );
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
            }

        }

    }

    public static Operation askOperation() throws IOException {
        String message = String.format("Выберите операцию:\n" +
                "%s - упаковать файлы в архив\n" +
                "%s - добавить файл в архив\n" +
                "%s - удалить файл из архива\n" +
                "%s - распаковать архив\n" +
                "%s - просмотреть содержимое архива\n" +
                "%s - выход",
                Operation.CREATE.ordinal(),
                Operation.ADD.ordinal(),
                Operation.REMOVE.ordinal(),
                Operation.EXTRACT.ordinal(),
                Operation.CONTENT.ordinal(),
                Operation.EXIT.ordinal());
        ConsoleHelper.writeMessage(message);
        int operationNumber = ConsoleHelper.readInt();
        return Operation.values()[operationNumber];

    }

}
