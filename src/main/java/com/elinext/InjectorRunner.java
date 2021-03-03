package com.elinext;

import com.elinext.annotation.Inject;
import com.elinext.provider.Provider;
import com.elinext.scanner.Scanner;
import com.elinext.scanner.ScannerImpl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InjectorRunner {

  private final Injector injector = new InjectorImpl();
  private static final Scanner scanner = ScannerImpl.getInstance();

  public void run() {
    try {
      List<Class> classes = scanner.scan();
      InjectorRunner runner = new InjectorRunner();
      runner.bind(classes);
      classes.forEach(injector::getProvider);

    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

  }

  private void bind(List<Class> classes) {
    List<Class> defaultClasses = findClassesWithDefaultConstructor(classes);
    List<Class> differences = classes.stream()
        .filter(element -> !defaultClasses.contains(element))
        .collect(Collectors.toList());
    List<Class> allParameters = new ArrayList<>();
    for (Class type : differences) {
      List<Constructor> annotatedConstructors = annotatedConstructors(type);
      List<Class> parameters = Arrays.asList(annotatedConstructors.get(0).getParameterTypes());
      allParameters.addAll(parameters);
    }
    createDefaultsBinding(defaultClasses, allParameters);
    bindAll(allParameters, differences);
  }

  private void bindAll(List<Class> parameters, List<Class> classes) {
    for (Class parameter : parameters) {
      for (Class type : classes)
        if (parameter.isAssignableFrom(type)) {
          injector.bind(parameter, type);
        }
    }
  }

  private void createDefaultsBinding(List<Class> classesWithDefaultConstructors, List<Class> allParameters) {
    for (Class parameter : allParameters) {
      for (Class type : classesWithDefaultConstructors)
        if (parameter.isAssignableFrom(type)) {
          injector.bind(parameter, type);
        }
    }
  }

  private List<Class> findClassesWithDefaultConstructor(List<Class> classes) {
    List<Class> filteredClasses = new ArrayList<>();
    for (Class<?> type : classes) {
      List<Constructor> filteredConstructors = annotatedConstructors(type);
      if (filteredConstructors.isEmpty()) {
        List<Constructor<?>> defaultConstructors =
            Arrays.stream(type.getConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .collect(Collectors.toList());
        if (defaultConstructors.size() == 1) {
          filteredClasses.add(type);
        }
      }
    }
    return filteredClasses;
  }

  private List<Constructor> annotatedConstructors(Class type) {
    return Arrays.stream(type.getConstructors())
        .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
        .collect(Collectors.toList());
  }

}
