package ru.bars.windows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import ru.bars.Main;
import ru.bars.entities.TomThread;
import ru.bars.entities.Tomcat;
import ru.bars.entities.TomcatProcess;
import ru.bars.windows.AddWindow.AddWindow;
import ru.bars.windows.progressIndicator.ProgressIndicatorWindow;

public class MainController {
  public static ArrayList<TomcatProcess> processes = new ArrayList<>();

  public BorderPane content;
  public TableView<Tomcat> table;

  public void initialize() {
    try {
      loadData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void goBack(ActionEvent event) {

  }

  public void checkServer(ActionEvent event) {

  }

  public void addTomcat(ActionEvent event) {
    try {
      AddWindow addWindow = new AddWindow();
      addWindow.showAndWait();

      saveData();
      updateData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void loadData() throws IOException {
    try {
      Gson gson = new GsonBuilder().create();

      StringBuilder dataStringBuilder = new StringBuilder();
      Files.lines(Paths.get(Main.FILE_NAME), StandardCharsets.UTF_8)
          .forEach(line -> dataStringBuilder.append(line).append("\n"));
      Tomcat[] tomcats = gson.fromJson(dataStringBuilder.toString(), Tomcat[].class);
      Main.data.addAll(Arrays.asList(tomcats));
    } catch (Exception e) {
      e.printStackTrace();
    }
    updateData();
  }

  private void updateData() {
    table.getItems().clear();
    table.setItems(FXCollections.observableArrayList(Main.data));
    table.refresh();
  }

  private void saveData() {
    try {
      Gson gson = new GsonBuilder().create();
      String s = gson.toJson(Main.data, new TypeToken<List<Tomcat>>() {
      }.getType());
      Files.write(Paths.get(Main.FILE_NAME), s.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startupService(ActionEvent event) {
    TomThread tomThread = new TomThread(table.getSelectionModel().getSelectedItem(), true);
    tomThread.start();
    try {
      updateData();
      Thread.sleep(2000);
      updateData();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void shutdownService(ActionEvent event) {
    Tomcat selectedItem = table.getSelectionModel().getSelectedItem();
    TomThread tomThread = new TomThread(selectedItem, false);
    tomThread.start();
    try {
      Thread.sleep(10000);
      new Thread(){{
        List<TomcatProcess> collect = processes.stream().filter(x -> x.getId().equals(selectedItem.getId()))
            .collect(Collectors.toList());
        if (!collect.isEmpty()){
          Process process = collect.get(0).getProcess();
          System.out.println(process.isAlive());
          process.destroy();
          System.out.println(process.isAlive());
        }
      }}.start();
      updateData();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void deleteTomcat(ActionEvent event) {
    Main.data.remove(table.getSelectionModel().getSelectedItem());
    updateData();
    saveData();
  }

}
