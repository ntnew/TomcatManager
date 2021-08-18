package ru.bars.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

public class StreamSink implements Runnable {

  InputStream is;

  StreamSink(InputStream in) {
    is = in;
  }

  @Override
  public void run() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
      for (String line; ((line = reader.readLine()) != null); ) {
        System.out.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
