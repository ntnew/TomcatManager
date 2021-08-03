package ru.bars.entities;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bars.commonDirs.BarsimDirectory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tomcat {

  private UUID id;
  private String name;
  private String path;
  private Status status;
  private BarsimDirectory barsimDirectory;

}
