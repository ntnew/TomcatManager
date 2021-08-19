package ru.bars.entities;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import ru.bars.commonDirs.BarsimDirectory;

@Data
public class Tomcat implements Serializable, Cloneable {

  private UUID id;
  private String name;
  private String path;
  private Status status;
  private BarsimDirectory barsimDirectory;
  private String port;

  public Tomcat() {
  }

  public Tomcat(UUID id, String name, String path, Status status, BarsimDirectory barsimDirectory) {
    this.id = id;
    this.name = name;
    this.path = path;
    this.status = status;
    this.barsimDirectory = barsimDirectory;
  }

  /**
   * Произвести контрольный GET-запрос к приложению, для проверки, что оно запущено
   */
  public void validateGetRequest() throws IOException {
    try {
      URI validateURI =  new URIBuilder().setScheme("http").setHost("localhost").setPort(Integer.parseInt(port)).build();
      try (CloseableHttpResponse execute = HttpClients.createDefault().execute(new HttpGet(validateURI.resolve("/upd/client.zip")))) {
        if (execute.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
          throw new RuntimeException("Приложение не отвечает, скорее всего не запущено");
        }
      }
    } catch (URISyntaxException ignored) {
    }
  }
}
