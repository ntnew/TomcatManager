package ru.bars.windows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.bars.Main;
import ru.bars.Step;
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
      Main.data.clear();
      StringBuilder dataStringBuilder = new StringBuilder();
      Files.lines(Paths.get(Main.FILE_NAME), StandardCharsets.UTF_8)
          .forEach(line -> dataStringBuilder.append(line).append("\n"));
      Tomcat[] tomcats = gson.fromJson(dataStringBuilder.toString(), Tomcat[].class);
      Main.data.addAll(Arrays.asList(tomcats));
    } catch (Exception e) {
      e.printStackTrace();
    }
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

    try {
//      Step step = () -> table.getSelectionModel().getSelectedItem().getBarsimDirectory().tomcat.start();
      TomThread tt = new TomThread(table.getSelectionModel().getSelectedItem(), true);
      tt.start();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void shutdownService(ActionEvent event) {
    try {
      TomThread tt = new TomThread(table.getSelectionModel().getSelectedItem(), false);
      tt.setPriority(Thread.MAX_PRIORITY);
      tt.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteTomcat(ActionEvent event) {
    Main.data.remove(table.getSelectionModel().getSelectedItem());
    updateData();
    saveData();
  }

}
