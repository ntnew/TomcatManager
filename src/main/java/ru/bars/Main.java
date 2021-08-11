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

  public static final String FILE_NAME = "./data";

  public static ArrayList<Tomcat> data = new ArrayList<>();

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
      loadAndShowMainStage(primaryStage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadAndShowMainStage(Stage primaryStage) throws IOException {
    URL fxmlURL = getClass().getClassLoader().getResource("MainPage.fxml");
    FXMLLoader loader = new FXMLLoader(fxmlURL);
    BorderPane loginBox = loader.load();
    primaryStage.setTitle("Томкат Манагер");
    primaryStage.setScene(new Scene(loginBox));
    primaryStage.setResizable(true);
    primaryStage.show();
  }
}
