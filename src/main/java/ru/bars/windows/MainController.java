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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import ru.bars.Main;
import ru.bars.entities.Status;
import ru.bars.entities.TomThread;
import ru.bars.entities.Tomcat;
import ru.bars.entities.TomcatProcess;
import ru.bars.windows.AddWindow.AddWindow;

public class MainController {

  public static ArrayList<TomcatProcess> processes = new ArrayList<>();

  public BorderPane content;
  public TableView<Tomcat> table;

  public void initialize() {
    try {
      loadData();
      updateData();
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
      Main.TOMCATS_DATA.clear();
      StringBuilder dataStringBuilder = new StringBuilder();
      Files.lines(Paths.get(Main.TOMCATS_DATA_FILENAME), StandardCharsets.UTF_8)
          .forEach(line -> dataStringBuilder.append(line).append("\n"));
      Tomcat[] tomcats = gson.fromJson(dataStringBuilder.toString(), Tomcat[].class);
      Main.TOMCATS_DATA.addAll(Arrays.asList(tomcats));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateData() {
    table.getItems().clear();
    table.setItems(FXCollections.observableArrayList(Main.TOMCATS_DATA));
    table.refresh();
  }

  private void saveData() {
    try {
      Gson gson = new GsonBuilder().create();
      String s = gson.toJson(Main.TOMCATS_DATA, new TypeToken<List<Tomcat>>() {
      }.getType());
      Files.write(Paths.get(Main.TOMCATS_DATA_FILENAME), s.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startupService(ActionEvent event) {

    try {
      TomThread tt = new TomThread(getSelectedTomcat(), true);
      tt.start();
      getSelectedTomcat().setStatus(Status.CHARGING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void shutdownService(ActionEvent event) {
    try {
      TomThread tt = new TomThread(getSelectedTomcat(), false);
      tt.setPriority(Thread.MAX_PRIORITY);
      tt.start();
      getSelectedTomcat().setStatus(Status.DISABLED);
      table.refresh();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteTomcat(ActionEvent event) {
    Main.TOMCATS_DATA.remove(table.getSelectionModel().getSelectedItem());
    updateData();
    saveData();
  }


  private Tomcat getSelectedTomcat() {
    return table.getSelectionModel().getSelectedItem();
  }
}
