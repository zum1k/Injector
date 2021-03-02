package com.elinext.scanner;

import com.elinext.InjectorImpl;
import com.elinext.annotation.Bean;
import com.elinext.annotation.Inject;
import com.elinext.exception.BeanClassesNotFoundException;
import com.elinext.exception.ConstructorNotFountException;
import com.elinext.exception.TooManyConstructorsException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ScannerImpl implements Scanner {
  private static Scanner instance;

  private ScannerImpl() {
  }

  public static Scanner getInstance() {
    if (instance == null) {
      instance = new ScannerImpl();
    }
    return instance;
  }

  @Override
  public List<Class> scan() throws IOException, ClassNotFoundException {
    String defaultPackage = InjectorImpl.class.getPackage().getName();
    return getInjectedClasses(defaultPackage);
  }

  private static List<Class> getInjectedClasses(String packageName) throws IOException, ClassNotFoundException {
    List<Class> classes = getBeanClasses(packageName);
    for (Class<?> type : classes) {
      if (type.getConstructors().length == 0) {
        throw new ConstructorNotFountException("Constructors not found");
      }
      List<Constructor<?>> filteredConstructors =
          Arrays.stream(type.getConstructors()).filter(constructor -> constructor.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
      if (filteredConstructors.size() > 1) {
        throw new TooManyConstructorsException("Too many Injected constructors");
      }
      if (filteredConstructors.isEmpty()) {
        List<Constructor<?>> defaultConstructors =
            Arrays.stream(type.getConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .collect(Collectors.toList());
        if (defaultConstructors.isEmpty()) {
          throw new ConstructorNotFountException("Constructors not found");
        }
      }
    }
    return classes;
  }

  private static List<Class> getBeanClasses(String packageName) throws IOException, ClassNotFoundException {
    List<Class> classList = getClasses(packageName);
    List<Class> beanClasses = classList.stream().filter(c -> c.isAnnotationPresent(Bean.class))
        .collect(Collectors.toList());
    List<Class> injectedClasses = new ArrayList<>();
    for (Class type : classList) {
      List<Constructor> filteredConstructors = Arrays.stream(type.getConstructors())
          .filter(c -> c.isAnnotationPresent(Inject.class))
          .collect(Collectors.toList());
      if (filteredConstructors.isEmpty()) {
        continue;
      }
      injectedClasses.add(type);
    }
    Set<Class> resultSet = new HashSet<>();
    resultSet.addAll(beanClasses);
    resultSet.addAll(injectedClasses);
    if (resultSet.isEmpty()) {
      throw new BeanClassesNotFoundException("No bean classes found");
    }
    return new ArrayList<>(resultSet);
  }


  private static List<Class> getClasses(String packageName)
      throws ClassNotFoundException, IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }
    ArrayList<Class> classes = new ArrayList<>();
    for (File directory : dirs) {
      classes.addAll(findClasses(directory, packageName));
    }
    return classes;
  }

  private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    assert files != null;
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }
}
