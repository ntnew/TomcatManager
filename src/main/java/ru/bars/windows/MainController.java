package ru.bars.windows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import ru.bars.Main;
import ru.bars.entities.Status;
import ru.bars.entities.Tomcat;

public class MainController {

  public BorderPane content;
  public TableView<Tomcat> table;

  public void initialize(){
    try {
      loadData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void goBack(ActionEvent event) {

  }

  public void checkServer(ActionEvent event) {

  }

  public void addTomcat(ActionEvent event) {
    Main.data.add(new Tomcat(){{
      setId(UUID.randomUUID());
      setName("hui");
      setPath("path");
      setStatus(Status.DISABLED);
    }});
  }

  private void loadData() throws IOException {
    Gson gson = new GsonBuilder().create();
//
//    Tomcat tomcat = new Tomcat() {{
//      setId(UUID.randomUUID());
//      setName("hui");
//      setPath("path");
//      setStatus(Status.DISABLED);
//    }};
//
//    String s = gson.toJson(tomcat, Tomcat.class);
//    Files.write(Paths.get(Main.FILE_NAME), s.getBytes());

    StringBuilder dataStringBuilder = new StringBuilder();
    Files.lines(Paths.get(Main.FILE_NAME), StandardCharsets.UTF_8).forEach(line -> dataStringBuilder.append(line).append("\n"));
    Tomcat[] tomcats = gson.fromJson(dataStringBuilder.toString(), Tomcat[].class);
    Main.data.addAll(Arrays.asList(tomcats));
    table.setItems(FXCollections.observableArrayList(Main.data));
  }

}
