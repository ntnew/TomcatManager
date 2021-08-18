package ru.bars.commonDirs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import ru.bars.Main;
import ru.bars.entities.TomcatProcess;

/**
 * Представляет директорию, сервера приложений Tomcat, но не сам сервер приложений.
 */
public class TomcatDir extends Directory {

  public File setenv = file("bin/", WinLinuxUtils.setEnv());
  public File tomcatUsers = file("conf/tomcat-users.xml");
  public File serverXml = file("conf/server.xml");

  /**
   * констр
   *
   * @param dir директория с сервером
   */
  public TomcatDir(File dir) {
    super(dir);
  }

  /**
   * Распаковать архив с приложением в вебаппс, и оно будет развернуто при перезапуске
   *
   * @param war архив с приложением
   */
  public void unpackWar(File war) throws ZipException {
    new ZipFile(war).extractAll(file("webapps/ROOT").getAbsolutePath());
  }

  /**
   * Запустить сервер приложений
   */
  public void start() throws IOException, InterruptedException {
    File startupFile = file("bin/", WinLinuxUtils.startup());
    ProcessBuilder pb = new ProcessBuilder(startupFile.getAbsolutePath());
    pb.directory(this);
    Process start = pb.start();
  }

  /**
   * Выключить сервер приложений
   */
  public void shutdown() throws IOException, InterruptedException {
      File startupFile = file("bin/", WinLinuxUtils.shutdown());
      ProcessBuilder pb = new ProcessBuilder(startupFile.getAbsolutePath());
      pb.directory(this);
      pb.start();
  }
}
//      BufferedReader input = new BufferedReader(new InputStreamReader(start.getInputStream()));
//      String line;
//      while ((line = input.readLine()) != null) {
//        System.out.println(line);
//      }
//      input.close();
//      System.out.println(start.waitFor());
//      File startupFile = file("bin/", WinLinuxUtils.shutdown());
//      String[] command = new String[]{"cmd.exe", "/C", "start", startupFile.getAbsolutePath()};
//
//      ProcessBuilder pb = new ProcessBuilder(command);
//      pb.directory(file());
//      Process start = pb.start();

//      Process child = Runtime.getRuntime().exec(command);
//      Main.processes.add(new TomcatProcess(tomcatId, start));
//      BufferedReader input = new BufferedReader(new InputStreamReader(start.getInputStream()));
//      while ((line = input.readLine()) != null) {
//        System.out.println(line);
//      }
//      input.close();
//      System.out.println(start.waitFor());
