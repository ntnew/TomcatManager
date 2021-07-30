package ru.bars.commonDirs;

import java.io.File;
import java.io.IOException;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Представляет директорию, сервера приложений Tomcat, но не сам сервер приложений.
 */
public class TomcatDir extends Directory {

  public File setenv = file("bin/", WinLinuxUtils.setEnv());
  public File tomcatUsers = file("conf/tomcat-users.xml");
  public File serverXml = file("conf/server.xml");
  public ZipFile clientZip = new ZipFile(file("webapps/ROOT/upd/client.zip"));
  public Directory soft = new Directory(file("webapps/soft/"));


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
  public void start() throws IOException {
    File startupFile = file("bin/", WinLinuxUtils.startup());
    ProcessBuilder pb = new ProcessBuilder(startupFile.getAbsolutePath());
    pb.directory(this);
    pb.start();
  }
}
