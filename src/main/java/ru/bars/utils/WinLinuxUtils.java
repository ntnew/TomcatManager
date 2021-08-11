package ru.bars.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.FileUtils;
import ru.bars.commonDirs.Directory;

/**
 * Утилита для поддержки кроссплатформенности.
 */
public class WinLinuxUtils {

  /**
   * Файл tomcat/bin/setenv
   *
   * @return имя файла в зависимости от ОС
   */
  public static String setEnv() {
    return FileUtils.isWindows()
        ? "setenv.bat"
        : "setenv.sh";
  }

  /**
   * JRE директория
   *
   * @return имя директории в зависимости от ОС
   */
  public static String jre() {
    return FileUtils.isWindows()
        ? "JRE_win"
        : "JRE_linux";
  }

  /**
   * Файл для запуска tomcat
   *
   * @return Файл для запуска tomcat в зависимости от ОС
   */
  public static String startup() {
    return FileUtils.isWindows()
        ? "startup.bat"
        : "startup.sh";
  }

  /**
   * Файл для запуска клиента
   *
   * @return run.sh или client.exe в зависимости от ОС
   */
  public static String clientExecutable() {
    return FileUtils.isWindows()
        ? "client.exe"
        : "run.sh";
  }

  /**
   * Сделать файл исполняемым
   *
   * @param file файл
   */
  public static void setExecutable(File file) throws IOException, InterruptedException {
    String command = "chmod a+x " + file.getAbsolutePath();
    System.out.println("Делаем файл исполняемым: " + command);
    Process exec = Runtime.getRuntime().exec(command);
    if (exec.waitFor() != 0) {
      throw new RuntimeException("Не получилось сделать файл исполняемым: " + file.getAbsolutePath());
    }
  }

  /**
   * Сделать файл исполняемым
   *
   * @param file файл
   */
  public static void relocateFile(File file, String pathTo) throws IOException, InterruptedException {
    String command = String.format("cp %1$s %2$s", file.getAbsolutePath(), pathTo.replaceAll(" ", "\" \""));
    System.out.println("Делаем файл исполняемым: " + command);
    Process exec = Runtime.getRuntime().exec(command);
    if (exec.waitFor() != 0) {
      throw new RuntimeException("Не выполнить команду: " + command);
    }
  }

  /**
   * Записать права папки в файл
   *
   * @param file файл
   */
  public static void writeFileAccessRight(File file) throws IOException, InterruptedException {

    Files.write(Paths.get(file.getAbsolutePath() + "/script.sh"),
        String.format("#!/bin/bash\nls -l %1$s > %1$s/logRight.txt", file.getAbsolutePath()).getBytes());

    System.out.println("Файл logRight.txt записан");

    String commandChmod = "chmod 777 " + file.getAbsolutePath() + "/script.sh";
    Process exec = Runtime.getRuntime().exec(commandChmod);

    ProcessBuilder processBuilder = new ProcessBuilder(file.getAbsolutePath() + "/script.sh");
    Process process = processBuilder.start();
    process.waitFor();

    Files.delete(Paths.get(file.getAbsolutePath() + "/script.sh"));

    if (exec.waitFor() != 0) {
      throw new RuntimeException("Не получилось выполнить скрипт: " + file.getAbsolutePath() + "/script.sh");
    }

  }

  /**
   * Установить права папки из файла
   *
   * @param file                       файл
   * @param fileNameForTakeAccessRight Имя файла/папки, у которой берем права
   * @param fileNameForSetAccessRight  Имя файла/папки, которой устанавливаем права
   */
  public static void setFolderAccessRight(File file, String fileNameForTakeAccessRight,
      String fileNameForSetAccessRight) throws IOException, InterruptedException {

    String userRight = "";

    List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath() + "/logRight.txt"));
    for (int i = 1; i < lines.size(); i++) {
      String[] arr = lines.get(i).split(" ");
      if (arr[arr.length - 1].equals(fileNameForTakeAccessRight)) {
        userRight = arr[3] + ":" + arr[4];
        break;
      }
    }

    String command = "chown -R " + userRight + " " + fileNameForSetAccessRight;
    System.out.println("Выполнение..." + command);
    Process exec = Runtime.getRuntime().exec(command);

    Files.delete(Paths.get(file.getAbsolutePath() + "/logRight.txt"));

    if (exec.waitFor() != 0) {
      throw new RuntimeException("Не получилось выполнить команду: " + command);
    }
  }

  /**
   * Распаковать зип архив
   *
   * @param clientZip зип архив
   * @return директория с распакованным клиентом
   */
  public static File unzipArchive(File clientZip, Directory target) throws ZipException {
    System.out.println("Распаковка " + clientZip.getName() + ": " + target.getAbsolutePath());
    new ZipFile(clientZip).extractAll(target.getAbsolutePath());
    System.out.println("Распаковка завершена");
    return target;
  }

  /**
   * Сделать исполняемыми все файлы в папке и даже в папках этой папки
   *
   * @param dir начальная директория
   */
  public static void setExecutableAllFiles(File dir) throws IOException, InterruptedException {
    List<File> files = new ArrayList<>();
    for (File file : Objects.requireNonNull(dir.listFiles())) {
      if (file.isFile()) {
        files.add(file);
      } else if (file.isDirectory()) {
        setExecutableAllFiles(file);
      }
    }
    for (File file : files) {
      setExecutable(file);
    }
  }
}

