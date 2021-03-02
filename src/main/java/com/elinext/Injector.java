package com.elinext;

import com.elinext.provider.Provider;

public interface Injector {
  // get instance with all injections from implemented Class
  <T> Provider<T> getProvider(Class<T> type);

  //register binding by implemented Class and his realisation
  <T> void bind(Class<T> type, Class<? extends T> implType);

  //register Singleton class
  <T> void bindSingleton(Class<T> type, Class<? extends T> implType);
}
