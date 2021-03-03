package com.elinext.provider;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderImpl<T> implements Provider<T> {
  private final Map<Class, Class> bindings;
  private final Class<T> type;
  private Map<Class, Object> instances;

  public ProviderImpl(Class<T> type, Map<Class, Class> bindings) {
    this.type = type;
    this.bindings = bindings;
  }

  @Override
  public T getInstance() {
    type.getConstructors();
    return null;
  }

  private List<Constructor> defaultConstructors(Class type){
    List<Constructor> constructors = Arrays.asList(type.getConstructors());
    constructors.stream().filter()
  }

}
