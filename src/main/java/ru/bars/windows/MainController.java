package ru.bars.windows;

import com.google.common.util.concurrent.Runnables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import ru.bars.Main;
import ru.bars.entities.Status;
import ru.bars.entities.TomThread;
import ru.bars.entities.Tomcat;
import ru.bars.utils.CollectionsHelper;
import ru.bars.utils.InstallStep;
import ru.bars.windows.AddWindow.AddWindow;
import ru.bars.windows.progressIndicator.ProgressIndicatorWindow;

public class MainController {

  public BorderPane content;
  public TableView<TomcatView> table;

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
      List<Tomcat> tomcats = Arrays.asList(gson.fromJson(dataStringBuilder.toString(), Tomcat[].class));
      tomcats.forEach(x -> x.setPort(getPort(x)));
      Main.TOMCATS_DATA.addAll(tomcats);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getPort(Tomcat tomcat) {
    String pattern = "<Connector port=\"";
    StringBuilder port = new StringBuilder();
    String absolutePath = tomcat.getBarsimDirectory().tomcat.file() + "/conf/server.xml";
    try {
      AtomicBoolean flag = new AtomicBoolean(true);
      Files.lines(Paths.get(absolutePath), StandardCharsets.UTF_8).forEach(x -> {
        if (x.contains(pattern) && flag.get()) {
          port.append(x, x.lastIndexOf(pattern) + 17, x.lastIndexOf(pattern) + 21);
          flag.set(false);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return port.toString();
  }

  private void updateData() {
    table.getItems().clear();
    List<TomcatView> kbkOnYears =
        CollectionsHelper.select(Main.TOMCATS_DATA, TomcatView::new);
    table.setItems(FXCollections.observableArrayList(kbkOnYears));
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
      Platform.runLater(() -> getSelectedTomcatView().setStatus(Status.CHARGING));
      new ProgressIndicatorWindow().show(() -> validate(10, 3000, getSelectedTomcat()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void shutdownService(ActionEvent event) {
    try {
      TomThread tt = new TomThread(getSelectedTomcat(), false);
      tt.start();
      getSelectedTomcatView().setStatus(Status.DISABLED);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteTomcat(ActionEvent event) {
    Main.TOMCATS_DATA.remove(getSelectedTomcat());
    updateData();
    saveData();
  }

  /**
   * Провести валидацию запущенного приложения, путём выполнения запроса. Точный момент запуска не известен, но в
   * среднем занимает от 10 до 30 секунд.
   *
   * @param trys    количество попыток
   * @param timeout время между попытками
   */
  public void validate(int trys, int timeout, Tomcat tomcat) throws Exception {
    InstallStep step = tomcat::validateGetRequest;
    Thread.sleep(10000);

    for (int i = 1; i <= trys; i++) {
      try {
        step.perform();
        getSelectedTomcatView().setStatus(Status.ENABLED);
        return;
      } catch (Exception e) {
        if (i == trys) {
          throw e;
        }
        Thread.sleep(timeout);
      }
    }
    getSelectedTomcatView().setStatus(Status.DISABLED);
  }

  private Tomcat getSelectedTomcat() {
    return getSelectedTomcatView().getTomcat();
  }

  private TomcatView getSelectedTomcatView() {
    return table.getSelectionModel().getSelectedItem();
  }
}
