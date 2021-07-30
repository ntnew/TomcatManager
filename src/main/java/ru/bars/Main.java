package ru.bars;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Приложение
 */

public class Main extends Application {

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
