package com.elinext.scanner;

import java.io.IOException;
import java.util.List;

public interface Scanner {
  List<Class> scan() throws IOException, ClassNotFoundException;
}
