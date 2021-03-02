package com.elinext.provider;

public class ProviderImpl<T> implements Provider<T> {

  private final Class<T> type;

  public ProviderImpl(Class<T> type) {
    this.type = type;
  }

  @Override
  public T getInstance() {

    type.getConstructors();
    return null;
  }
}
