package ru.bars.windows;

import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ru.bars.entities.Status;
import ru.bars.entities.Tomcat;

public class TomcatView {

  /**
   * Изначальный элемент
   */
  private Tomcat originalTomcat;
  /**
   * Идентифкатор справочника
   */
  private UUID id;
  /**
   * Наименвоангие
   */
  private final StringProperty name = new SimpleStringProperty();
  /**
   * Текст запроса
   */
  private final StringProperty path = new SimpleStringProperty();
  /**
   * Автор запроса
   */
  private final ObjectProperty<Status>  status = new SimpleObjectProperty<Status>();

  /**
   * Конструктор на основе справочника
   *
   * @param tomcat томкат
   */
  public TomcatView(Tomcat tomcat) {
    originalTomcat = tomcat;
    fillFromOriginalUserQuery();
  }

  /**
   * Пустой конструктор
   */
  public TomcatView() {

  }

//  /**
//   * Получение элемента
//   *
//   * @return Элемент
//   */
//  public Tomcat getUserQuery() {
//    if (originalUserQuery == null) {
//      originalUserQuery = new UserQuery();
//    }
//    originalUserQuery.setName(getName());
//    originalUserQuery.setQuery(getQuery());
//    originalUserQuery.setUserName(getUserName());
//    originalUserQuery.setId(getId());
//    return originalUserQuery;
//  }

  /**
   * Заполнение из первоначального
   */
  public void fillFromOriginalUserQuery() {
    setName(originalTomcat.getName());
    setId(originalTomcat.getId());
    setPath(originalTomcat.getPath());
    setStatus(originalTomcat.getStatus());
  }


  public Tomcat getOriginalTomcat() {
    return originalTomcat;
  }

  public void setOriginalTomcat(Tomcat originalTomcat) {
    this.originalTomcat = originalTomcat;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getPath() {
    return path.get();
  }

  public StringProperty pathProperty() {
    return path;
  }

  public void setPath(String path) {
    this.path.set(path);
  }

  public Status getStatus() {
    return status.get();
  }

  public ObjectProperty<Status> statusProperty() {
    return status;
  }

  public void setStatus(Status status) {
    this.status.set(status);
  }
}
