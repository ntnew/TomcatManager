package ru.bars.entities;

import java.util.UUID;
import lombok.Data;

@Data
public class Tomcat {

  private UUID id;
  private String name;
  private String path;
  private Status status;

}
