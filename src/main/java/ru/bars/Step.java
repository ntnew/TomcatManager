package ru.bars;

/**
 * Интерфейс шага установки. Уровень БЛ, но не GUI.
 */
@FunctionalInterface
public interface Step {

  /**
   * Выполнить шаг установки
   */
  void perform() throws Exception;
}
