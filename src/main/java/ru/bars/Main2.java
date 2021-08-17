package ru.bars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.bars.commonDirs.WinLinuxUtils;
import ru.bars.entities.Tomcat;

public class Main2 extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("Томкат Манагер");
    primaryStage.setScene(new Scene(new VBox(new Label("РАБОТАЙ"))));
    primaryStage.setResizable(true);
    primaryStage.show();


    new Thread(){{
    try {
      //прочитать
      Gson gson = new GsonBuilder().create();
      StringBuilder dataStringBuilder = new StringBuilder();
      Files.lines(Paths.get(Main.FILE_NAME), StandardCharsets.UTF_8)
          .forEach(line -> dataStringBuilder.append(line).append("\n"));
      Tomcat[] tomcats = gson.fromJson(dataStringBuilder.toString(), Tomcat[].class);
      //наш томкат, который запускаю
      Tomcat tom = tomcats[1];
      //запускаем процесс
      File startupFile = new File(tom.getBarsimDirectory().tomcat + "/bin/" + WinLinuxUtils.startup());
      ProcessBuilder pb = new ProcessBuilder(startupFile.getAbsolutePath());
      pb.directory(tom.getBarsimDirectory().tomcat);
      Process start = pb.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    }}.start();
  }
}
