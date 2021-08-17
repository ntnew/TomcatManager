package ru.bars.windows.AddWindow;

import java.io.File;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ru.bars.Main;
import ru.bars.commonDirs.BarsimDirectory;
import ru.bars.entities.Status;
import ru.bars.entities.Tomcat;

public class AddWindowController {


  public Button addBtn;

  public TextField path;
  public TextField name;

  public void initialize(){

  }

  public void cancel(ActionEvent event) {
    ((Stage) path.getScene().getWindow()).close();
  }

  public void addServer(ActionEvent event) {
    Tomcat tomcat = new Tomcat(UUID.randomUUID(), name.getText(), path.getText(), Status.DISABLED,
        new BarsimDirectory(new File(path.getText())));
    Main.data.add(tomcat);

    ((Stage) path.getScene().getWindow()).close();
  }

  @FXML
  private void chooseTomcatDir(MouseEvent event){
    File dir = new DirectoryChooser().showDialog(path.getScene().getWindow());
    if (dir != null) {
      path.setText(dir.getAbsolutePath());
    }
  }
}
