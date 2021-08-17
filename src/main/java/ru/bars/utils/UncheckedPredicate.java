package ru.bars.utils;

import java.util.function.Predicate;
import lombok.SneakyThrows;

/**
 * Интерфейс предиката с непроверяемыми исключениями
 *
 * @param <T> Тип входных данных
 */
@FunctionalInterface
public interface UncheckedPredicate<T> extends Predicate<T> {

  @Override
  @SneakyThrows
  default boolean test(T t) {
    return applyThrows(t);
  }

  boolean applyThrows(T t) throws Exception;
}
