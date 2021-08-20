package ru.bars.entities;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessSaver {
  private UUID tomId;
  private List<String> pids;
}
