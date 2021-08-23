package ru.bars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.bars.entities.ProcessSaver;
import ru.bars.entities.Tomcat;
import ru.bars.entities.TomcatProcess;

/**
 * Приложение
 */

public class Main extends Application {
  //файл куда сохраняется инфа по томкатам
  public static final String TOMCATS_DATA_FILENAME = "./data";
  //Томкаты
  public static ArrayList<Tomcat> TOMCATS_DATA = new ArrayList<>();
  //Процессы
  public static ArrayList<TomcatProcess> processes = new ArrayList<>();

  public static ArrayList<ProcessSaver> savedProcesses = new ArrayList<>();

  /**
   * Точка входа в приложение
   *
   * @param args Аргументы запуска
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Запуск приложения
   *
   * @param primaryStage Окно
   */
  @Override
  public void start(Stage primaryStage) {
    try {
      URL fxmlURL = getClass().getClassLoader().getResource("MainPage.fxml");
      FXMLLoader loader = new FXMLLoader(fxmlURL);
      BorderPane loginBox = loader.load();
      primaryStage.setTitle("Томкат Манагер");
      primaryStage.setScene(new Scene(loginBox));
      primaryStage.setResizable(true);
      primaryStage.show();
      primaryStage.setOnCloseRequest(this::saveProcessesPids);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void saveProcessesPids(WindowEvent windowEvent) {
    try {
      Gson gson = new GsonBuilder().create();
      List<ProcessSaver> ps = new ArrayList<>(savedProcesses);
      processes.forEach(x-> {
        List<String> pids = new ArrayList<>();
        pids.add(x.getProcess().pid()+"");
        x.getProcess().children().forEach(y ->  pids.add(y.pid()+""));
        ps.add(new ProcessSaver(x.getId(), pids));
      });
      String s = gson.toJson(ps, new TypeToken<List<ProcessSaver>>() {
      }.getType());
      Files.write(Paths.get("./processes"), s.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
