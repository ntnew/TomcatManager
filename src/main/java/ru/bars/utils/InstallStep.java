package ru.bars.utils;

/**
 * Интерфейс шага установки. Уровень БЛ, но не GUI.
 */
@FunctionalInterface
public interface InstallStep {

  /**
   * Выполнить шаг установки
   */
  void perform() throws Exception;
}
