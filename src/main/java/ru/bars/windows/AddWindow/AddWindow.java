package ru.bars.windows.AddWindow;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;

public class AddWindow {

  @Getter
  private Stage stage = new Stage();


  /**
   * Путь к файлу шаблона
   */
  private static final String FXML_PATH =
      "AddWindow.fxml";

  /**
   * Контроллер
   */
  private final AddWindowController controller;

  /**
   * Конструктор
   */
  public AddWindow() throws IOException {
    URL resource = this.getClass().getClassLoader().getResource(FXML_PATH);
    FXMLLoader loader = new FXMLLoader(resource);
    BorderPane loginBox = loader.load();
    stage.setTitle("Добавление нового сервиса");
    stage.setScene(new Scene(loginBox));
    stage.setResizable(true);
    controller = loader.getController();
  }

  /**
   * Получить {@link #controller}
   *
   * @return {@link #controller}
   */
  public AddWindowController getController() {
    return controller;
  }

  public void showAndWait(){
    stage.showAndWait();
  }
}
