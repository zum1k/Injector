package com.elinext.provider;

public interface Provider<T> {
  /**
   * creating instance of
   * @return {code T} object
   */
  T getInstance();
}
