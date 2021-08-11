package ru.bars.entities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import ru.bars.Main;
import ru.bars.commonDirs.WinLinuxUtils;
import ru.bars.windows.MainController;

public class TomThread extends Thread {

  private Tomcat tomcat;
  private boolean turnOn;


  public TomThread(Tomcat tomcat, boolean turnOn) {
    this.tomcat = tomcat;
    this.turnOn = turnOn;
  }

  @Override
  public void run() {
    try {
      File startupFile;
      if (turnOn) {
        startupFile = new File(tomcat.getBarsimDirectory().tomcat + "/bin/" + WinLinuxUtils.startup());
      } else {
        startupFile = new File(tomcat.getBarsimDirectory().tomcat + "/bin/" + WinLinuxUtils.shutdown());
      }
      ProcessBuilder pb = new ProcessBuilder(startupFile.getAbsolutePath());
      pb.directory(tomcat.getBarsimDirectory().tomcat);
      Process start = pb.start();

      for (int i = 0; i < 10; i++) {
        Thread.sleep(10000);
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

  }
}
