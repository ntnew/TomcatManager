package ru.bars.commonDirs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

/**
 * Класс директории
 */
public class Directory extends File {

  /**
   * конст
   * @param pathname оборачиваемая директория
   */
  public Directory(String pathname) {
    super(pathname);
  }

  /**
   * конст
   * @param dir оборачиваемая директория
   */
  public Directory(File dir) {
    super(dir.getAbsolutePath());
  }

  /**
   * конст
   * @param parent родительский каталог
   * @param child дочерний путь
   */
  public Directory(File parent, String child) {
    super(parent, child);
  }

  /**
   * Получить файл относительно этой директории
   * @param names относительный путь
   * @return файл
   */
  public File file(String... names) {
    return FileUtils.getFile(this, names);
  }

  /**
   * Положить, копировать файл по относительному пути
   * @param file файл для копирования
   * @param names относительный путь назначения, если не указан то копируется в корень с сохранением имени,
   *              иначе берется последний элемент из пути назначения в качестве имени
   * @return файл по пути назначения
   */
  public File put(File file, String... names) throws IOException {
    File dest = file(names.length == 0 ? new String[]{file.getName()} : names);
    if (file.isDirectory()) {
      FileUtils.copyDirectory(file, dest);
    } else {
      FileUtils.copyFile(file, dest);
    }
    return dest;
  }

  /**
   * Распаковать ZIP архив в директорию
   * @param zipFile архив
   */
  public void extract(ZipFile zipFile) throws ZipException {
    zipFile.extractAll(this.getAbsolutePath());
  }

  /**
   * Доступ на запись в директорию
   * @return true если запись возможна
   */
  public boolean writeAccess() {
    try {
      File test = file("test");
      FileUtils.write(test, "test", StandardCharsets.UTF_8);
      test.delete();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Сколько доступно памяти в этой директории, в Мб
   *
   * @return объем доступной к использованию памяти в Мб
   */
  public int usableSpaceMb() {
    return new BigDecimal(getUsableSpace()).divide(new BigDecimal(1024 * 1024)).intValue();
  }

  /**
   * Переименовать рекурсивно все файлы в директории
   *
   * @param target целевая директория
   */
  public void moveToDirExistIgnored(Directory target) throws IOException {
    target.mkdirs();
    for (File file : listFiles()) {
      if (file.isFile()) {
        file.renameTo(new File(target, file.getName()));
      } else {
        new Directory(file).moveToDirExistIgnored(new Directory(target, file.getName()));
      }
    }
    deleteDirectory();
  }

  /**
   * Удалить эту директорию
   */
  public void deleteDirectory() throws IOException {
    FileUtils.deleteDirectory(this);
  }

  /**
   * Переместить в директорию. Перемещается туда. a/b/c -> a/b/d
   *
   * @param target целевая
   */
  public void moveToDir(Directory target) throws IOException {
    FileUtils.moveDirectory(this, target);
  }
}
