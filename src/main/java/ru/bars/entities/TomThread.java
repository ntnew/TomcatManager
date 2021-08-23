package ru.bars.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        } else {
          ProcessSaver processSaver = CollectionsHelper
              .firstOrNull(Main.savedProcesses, x -> x.getTomId().equals(tomcat.getId()));
          if (processSaver != null) {
            for (int i = 0; i < processSaver.getPids().size(); i++) {
              try {
                String cmd = "taskkill /F /PID " + processSaver.getPids().get(i);
                Runtime.getRuntime().exec(cmd);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            Main.savedProcesses.remove(processSaver);
            Gson gson = new GsonBuilder().create();
            List<ProcessSaver> ps = new ArrayList<>(Main.savedProcesses);
            Main.processes.forEach(x-> {
              List<String> pids = new ArrayList<>();
              pids.add(x.getProcess().pid()+"");
              x.getProcess().children().forEach(y ->  pids.add(y.pid()+""));
              ps.add(new ProcessSaver(x.getId(), pids));
            });
            String s = gson.toJson(ps, new TypeToken<List<ProcessSaver>>() {
            }.getType());
            Files.write(Paths.get("./processes"), s.getBytes());
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
