package ru.bars.commonDirs;

import java.io.File;
import net.lingala.zip4j.exception.ZipException;

/**
 * Директория в которой находится вся серверная часть с настройками, логами и сервером приложений
 */
public class BarsimDirectory extends Directory {

  private static final String DIR_NAME = "barsim_dir";

  public final BarsimClient client = new BarsimClient(file("client"));
  public final TomcatDir tomcat = new TomcatDir(file("tomcat"));
  public final File springProp = file("spring.properties");

  /**
   * констр
   * @param parent родительский каталог в котором находится директория
   */
  public BarsimDirectory(File parent) {
    super(parent, DIR_NAME);
  }

  /**
   * Распаковать клиента из серверного приложения в эту директорию
   * @return клиент
   */
  public BarsimClient unpackClient() throws ZipException {
    tomcat.clientZip.extractAll(client.getAbsolutePath());
    return client;
  }
}
