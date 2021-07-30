package ru.bars.commonDirs;

import java.io.File;
import java.io.IOException;

/**
 * Представляет клиента для серверного приложения.
 */
public class BarsimClient extends Directory {

  public final BarsimClientConfig clientConfig = new BarsimClientConfig(this);

  private final File executable = file(WinLinuxUtils.clientExecutable());

  /**
   * констр
   * @param dir директория с клиентом
   */
  public BarsimClient(File dir) {
    super(dir);
  }

  /**
   * Запустить нового клиента
   */
  public void start() throws IOException, InterruptedException {
    setExecutableLinux();
    ProcessBuilder pb = new ProcessBuilder(executable.getAbsolutePath());
    pb.directory(this);
    System.out.println("Запуск клиента: " + executable.getAbsolutePath());
    pb.start();
    System.out.println("Запущен");
  }

  /**
   * Сделать файл исполняемым если не в винде
   */
  private void setExecutableLinux() throws InterruptedException, IOException {
    if (!net.lingala.zip4j.util.FileUtils.isWindows()) {
      WinLinuxUtils.setExecutable(executable);
      WinLinuxUtils.setExecutable(file("run.command"));
    }
  }
}
