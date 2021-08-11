package ru.bars.entities;

import java.util.UUID;
import lombok.Data;

@Data
public class TomcatProcess {

  private UUID id;
  private Process process;

  public TomcatProcess(UUID id, Process process) {
    this.id = id;
    this.process = process;
  }
}
