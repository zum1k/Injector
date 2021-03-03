package com.elinext;

import com.elinext.provider.Provider;

public interface Injector {
  /** get instance with all injections from implemented Class
   *
   * @param type {@code Class} injected interface
   * @param <T> (@code T} of injected interface
   * @return {@code Provider} if create instance or null
   */
  <T> Provider<T> getProvider(Class<T> type);

  /** register binding by implemented Class and his realisation
   *
   * @param type {@code Class}  injected interface
   * @param implType {@code Class}  implemented realisation of injected interface
   * @param <T> {@code Type} of injected interface
   */
  <T> void bind(Class<T> type, Class<? extends T> implType);

  /** register Singleton class
   *
   *  @param type {@code Class}  injected interface
   *  @param implType {@code Class}  implemented realisation of injected interface
   *  @param <T> {@code Type} of injected interface
   */
  <T> void bindSingleton(Class<T> type, Class<? extends T> implType);
}
