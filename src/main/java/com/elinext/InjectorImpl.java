package com.elinext;

import com.elinext.provider.Provider;
import com.elinext.provider.ProviderImpl;

import java.util.HashMap;
import java.util.Map;

public class InjectorImpl implements Injector {
  private final Map<Class, Class> bindings = new HashMap<>();
  private final Map<Class, Class> singleTons = new HashMap<>();

  @Override
  public <T> Provider<T> getProvider(Class<T> type) {
    return new ProviderImpl<>(type, bindings);
  }

  @Override
  public <T> void bind(Class<T> type, Class<? extends T> implType) {
    bindings.put(type, implType);
  }

  @Override
  public <T> void bindSingleton(Class<T> type, Class<? extends T> implType) {
    singleTons.put(type, implType);
  }
}
