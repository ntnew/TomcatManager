package ru.bars;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

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
      VBox loginBox = loader.load();
      primaryStage.setTitle("Вход в систему");
      primaryStage.setScene(new Scene(loginBox));
      primaryStage.setResizable(false);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
