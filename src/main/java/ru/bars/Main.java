package ru.bars;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.bars.entities.Tomcat;
import ru.bars.entities.TomcatProcess;

/**
 * Приложение
 */

public class Main extends Application {

  public static final String TOMCATS_DATA_FILENAME = "./data";

  public static ArrayList<Tomcat> TOMCATS_DATA = new ArrayList<>();

  public static ArrayList<TomcatProcess> processes = new ArrayList<>();

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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
