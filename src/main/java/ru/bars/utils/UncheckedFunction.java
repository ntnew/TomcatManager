package ru.bars.utils;


import java.util.Objects;
import java.util.function.Function;
import lombok.SneakyThrows;

/**
 * Интерфейс функции с непроверяемыми исключениями
 *
 * @param <T> Тип входных данных
 * @param <R> Тип результата функции
 */
@FunctionalInterface
public interface UncheckedFunction<T, R> extends Function<T, R> {

  @Override
  @SneakyThrows
  default R apply(T t) {
    return applyThrows(t);
  }

  R applyThrows(T t) throws Exception;

  /**
   * Returns a composed function that first applies this function to its input, and then applies the
   * {@code after} function to the result. If evaluation of either function throws an exception, it
   * is relayed to the caller of the composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after}
   * function
   * @throws NullPointerException if after is null
   * @see #compose(Function)
   */
  @SneakyThrows
  default <V> UncheckedFunction<T, V> andThen(UncheckedFunction<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
  }

  /**
   * Returns a composed function that first applies the {@code before} function to its input, and
   * then applies this function to the result. If evaluation of either function throws an exception,
   * it is relayed to the caller of the composed function.
   *
   * @param <V>    the type of input to the {@code before} function, and to the composed function
   * @param before the function to apply before this function is applied
   * @return a composed function that first applies the {@code before} function and then applies
   * this function
   * @throws NullPointerException if before is null
   * @see #andThen(Function)
   */
  @SneakyThrows
  default <V> UncheckedFunction<V, R> compose(UncheckedFunction<? super V, ? extends T> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }
}

