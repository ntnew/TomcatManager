package ru.bars.utils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс-помощник для работы с коллекциями.
 */
public class CollectionsHelper {

  /**
   * Преобразование коллекции к типизированному списку.
   *
   * @param clazz Класс типа данных коллекции.
   * @param c     Коллекция.
   * @param <T>   Тип данных коллекции.
   * @return Список записей.
   */
  public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
    List<T> r = new ArrayList<>(c.size());
    for (Object o : c) {
      r.add(clazz.cast(o));
    }
    return r;
  }

  /**
   * Получение первого элемента коллекции, удовлетворяющего условию
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return Первый элемент, удовлетворяющий условию, или null
   */
  public static <T> T firstOrNull(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.stream().filter(predicate).findFirst().orElse(null);
  }

  /**
   * Возвращает единственный элемент последовательности, удовлетворяющий заданному условию, или null, если такого
   * элемента не существует; если условию удовлетворяет более одного элемента, генерируется исключение.
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return Единственный элемент входной последовательности, удовлетворяющий условию, или null, если такой элемент не
   * найден
   */
  public static <T> T singleOrNull(Collection<T> c, UncheckedPredicate<T> predicate) {
    List<T> results = c.stream().filter(predicate).collect(Collectors.toList());
    if (results.size() > 1) {
      throw new IllegalStateException();
    }
    return !results.isEmpty() ? results.get(0) : null;
  }

  /**
   * Получение первого элемента коллекции, удовлетворящего условию
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return Первый элемент, удовлетворяющий условию
   */
  public static <T> T first(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.stream().filter(predicate).findFirst().orElseThrow(NoSuchElementException::new);
  }

  /**
   * Получение списка элементов коллекции, удовлетворяющих условию
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return Список записей, удовлетворяющих условию
   */
  public static <T> List<T> findAll(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Получение списка элементов коллекции, удовлетворяющих условию (многопоточный)
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return Список записей, удовлетворяющих условию
   */
  public static <T> List<T> findAllParallel(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.parallelStream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Проверка на то что хотябы один элемент коллекции удовлетворяет условию
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return true, если есть удовлетворяющий условию элемент, иначе - false
   */
  public static <T> boolean any(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.stream().anyMatch(predicate);
  }

  /**
   * Проверка на то что все элементы коллекции удовлетворяют условию
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return true, если все элементы удовлетворяют условию, иначе - false
   */
  public static <T> boolean all(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.stream().allMatch(predicate);
  }

  /**
   * Получение списка записей с преобразованием с помощью функции
   *
   * @param c       Коллекция
   * @param mapFunc Функция преобразования
   * @param <T1>    Тип данных коллекции
   * @param <T2>    Тип данных результирующего списка
   * @return Список преобразованных записей
   */
  public static <T1, T2> List<T2> select(Collection<T1> c, UncheckedFunction<T1, T2> mapFunc) {
    return c.stream().map(mapFunc).collect(Collectors.toList());
  }

  /**
   * Получение списка записей с преобразованием с помощью функции
   *
   * @param c       поток
   * @param mapFunc Функция преобразования
   * @param <T1>    Тип данных коллекции
   * @param <T2>    Тип данных результирующего списка
   * @return Список преобразованных записей
   */
  public static <T1, T2> List<T2> select(Stream<T1> c, UncheckedFunction<T1, T2> mapFunc) {
    return c.map(mapFunc).collect(Collectors.toList());
  }

  /**
   * Получение количества элементов коллекции, удовлетворяющих условию
   *
   * @param c         Коллекция
   * @param predicate Условие
   * @param <T>       Тип данных коллекции
   * @return Количество записей, удовлетворяющих условию
   */
  public static <T> long count(Collection<T> c, UncheckedPredicate<T> predicate) {
    return c.stream().filter(predicate).count();
  }

  /**
   * Получение сгруппированного списка
   *
   * @param c         Коллекция
   * @param groupFunc Функция группировки
   * @param <T1>      Тип данных коллекции
   * @param <T2>      Тип данных ключа группировки
   * @return Сгруппированная коллеция
   */
  public static <T1, T2> Map<T2, List<T1>> groupBy(Collection<T1> c,
      UncheckedFunction<T1, T2> groupFunc) {
    return c.stream().collect(Collectors.groupingBy(groupFunc));
  }

  /**
   * Выбрать уникальные элементы из коллекции (Object.equals(Object)))
   *
   * @param c   Коллекция
   * @param <T> Тип данных коллекции
   * @return Коллекция с уникальными элементами
   */
  public static <T> List<T> distinct(Collection<T> c) {
    return c.stream().distinct().collect(Collectors.toList());
  }

  /**
   * Находит разность множеств, представленных двумя последовательностями, используя для сравнения значений компаратор
   * проверки на равенство по умолчанию.
   *
   * @param c1  Коллекция 1, из которой требуется извлечь элементы, отсутствующие в коллекции 2.
   * @param c2  Коллекция 2,элементы которой, входящие также в первую последовательность, должны быть исключены из
   *            возвращаемой последовательности.
   * @param <T> Тип элементов входных последовательностей.
   * @return Последовательность, представляющая собой разность двух последовательностей как множеств.
   */
  public static <T> List<T> except(Collection<T> c1, Collection<T> c2) {
    return c1.stream().filter(((Predicate<T>) c2::contains).negate()).collect(Collectors.toList());
  }


  /**
   * Проссумировать значения в коллекции
   *
   * @param collection коллекция
   * @param mapper     какие значения из объектов суммировать
   * @param <T>        тип объекта исходный
   * @return сумма
   */
  public static <T> BigDecimal sum(Collection<T> collection, Function<T, BigDecimal> mapper) {
    return collection.stream().map(mapper).reduce(BigDecimal.ZERO, BigDecimal::add);
  }




  /**
   * Выбрать все элементы по указанным индексам из списка, в новый список
   *
   * @param list    список
   * @param indexes индексы
   * @param <T>     тип элементов
   * @return список, содержащий элементы из исходного списка, по указанным индексам
   */
  public static <T> List<T> select(List<T> list, int[] indexes) {
    ArrayList<T> ret = new ArrayList<>();
    for (Integer num : indexes) {
      ret.add(list.get(num));
    }
    return ret;
  }





  /**
   * Вырезать, удалить элементы из исходного списка по индексам
   *
   * @param list    список
   * @param indexes индексы
   * @param <T>
   * @return список элементов по индексам из исходного списка
   */
  public static <T> List<T> cutElements(List<T> list, int[] indexes) {
    List<T> select = select(list, indexes);
    list.removeAll(select);
    return select;
  }

  /**
   * Получить подсписок по условию
   *
   * @param list      исходный список
   * @param predicate условие фильтрации
   * @param <T>       тип элементов
   * @return новый подсписок
   */
  public static <T> List<T> sublist(List<T> list, Predicate<T> predicate) {
    return list.stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * Получить последний элемент в списке
   *
   * @param list список
   * @param <T>  тип элемента
   * @return последний элемент
   */
  public static <T> T lastElement(List<T> list) {
    return list.get(list.size() - 1);
  }

  /**
   * Проверка списка на null и на наличие хотя бы одного элемента
   *
   * @param list список
   * @param <T>  тип элемента
   * @return true если список не null и не пустой
   */
  public static <T> boolean notNullOrEmpty(List<T> list) {
    return list != null && !list.isEmpty();
  }

  public static <T> Optional<T> findAny(List<T> list, Predicate<T> predicate) {
    List<T> subList = sublist(list, predicate);
    return notNullOrEmpty(subList)
        ? Optional.of(subList.get(0))
        : Optional.empty();
  }
}

