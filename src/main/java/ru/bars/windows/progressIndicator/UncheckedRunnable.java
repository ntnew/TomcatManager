package ru.bars.windows.progressIndicator;

import lombok.SneakyThrows;

/**
 * Интерфейс с непроверяемой операцией
 */
@FunctionalInterface
public interface UncheckedRunnable extends Runnable {

  @Override
  @SneakyThrows
  default void run() {
    runThrows();
  }

  void runThrows() throws Throwable;
}

