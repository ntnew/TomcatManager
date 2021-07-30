package ru.bars.commonDirs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Конфиг файл клиента.
 */
public class BarsimClientConfig extends File {

  public static final String DEFAULT_FILENAME = "clientConfig.xml";

  /**
   * констр
   * @param dir директория в которой находится конфиг
   */
  public BarsimClientConfig(File dir) {
    super(dir, DEFAULT_FILENAME);
  }

  /**
   * Обновить\создать значение по конкретному ключ
   * @param key ключ
   * @param value значение
   */
  public void update(String key, String value) throws IOException {
    Properties properties = new Properties();
    properties.loadFromXML(new FileInputStream(this));
    properties.setProperty(key, value);
    properties.storeToXML(new FileOutputStream(this), DEFAULT_FILENAME);
  }
}
