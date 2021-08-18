package ru.bars.entities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import ru.bars.Main;
import ru.bars.commonDirs.WinLinuxUtils;
import ru.bars.utils.CollectionsHelper;

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
      if (turnOn) {
        File startupFile = new File(tomcat.getBarsimDirectory().tomcat + "/bin/" + WinLinuxUtils.startup());

        ProcessBuilder pb = new ProcessBuilder(startupFile.getAbsolutePath());
        pb.directory(tomcat.getBarsimDirectory().tomcat);
        Process start = pb.start();

        Thread thread = new Thread(new StreamSink(start.getInputStream()));
        Thread thread2 = new Thread(new StreamSink(start.getErrorStream()));

        thread.setDaemon(true);
        thread.setName(String.format("stdout reader"));
        thread.start();

        thread2.setDaemon(true);
        thread2.setName(String.format("stdout reader"));
        thread2.start();
        Main.processes.add(new TomcatProcess(tomcat.getId(), start));
      } else {
        TomcatProcess tomcatProcess = CollectionsHelper
            .firstOrNull(Main.processes, x -> x.getId().equals(tomcat.getId()));
        if (tomcatProcess != null) {
          List<ProcessHandle> collect = tomcatProcess.getProcess().children().collect(Collectors.toList());
          collect.forEach(x -> {
            try {
              String cmd = "taskkill /F /PID " + x.pid();
              Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
          String cmd = "taskkill /F /PID " + tomcatProcess.getProcess().pid();
          Runtime.getRuntime().exec(cmd);
          Main.processes.remove(tomcatProcess);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
