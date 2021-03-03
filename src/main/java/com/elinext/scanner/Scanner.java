package com.elinext.scanner;

import java.io.IOException;
import java.util.List;

public interface Scanner {
  /** scanning default package for annotated {@code Bean} Classes
   *  or Classes with annotated {@code Inject} constructor
   *
   * @return List of Classes {@code List<Class}
   * @throws IOException
   * @throws ClassNotFoundException
   */
  List<Class> scan() throws IOException, ClassNotFoundException;
}
