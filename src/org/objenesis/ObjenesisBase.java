package org.objenesis;

import java.util.concurrent.ConcurrentHashMap;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

public class ObjenesisBase
  implements Objenesis
{
  protected final InstantiatorStrategy strategy;
  protected ConcurrentHashMap<String, ObjectInstantiator<?>> cache;

  public ObjenesisBase(InstantiatorStrategy strategy)
  {
    this(strategy, true);
  }

  public ObjenesisBase(InstantiatorStrategy strategy, boolean useCache)
  {
    if (strategy == null) {
      throw new IllegalArgumentException("A strategy can't be null");
    }
    this.strategy = strategy;
    this.cache = (useCache ? new ConcurrentHashMap() : null);
  }

  public String toString()
  {
    return getClass().getName() + " using " + this.strategy.getClass().getName() + (
      this.cache == null ? " without" : " with") + " caching";
  }

  public <T> T newInstance(Class<T> clazz)
  {
    return getInstantiatorOf(clazz).newInstance();
  }

  public <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz)
  {
    if (this.cache == null) {
      return this.strategy.newInstantiatorOf(clazz);
    }
    ObjectInstantiator instantiator = (ObjectInstantiator)this.cache.get(clazz.getName());
    if (instantiator == null) {
      ObjectInstantiator newInstantiator = this.strategy.newInstantiatorOf(clazz);
      instantiator = (ObjectInstantiator)this.cache.putIfAbsent(clazz.getName(), newInstantiator);
      if (instantiator == null) {
        instantiator = newInstantiator;
      }
    }
    return instantiator;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.ObjenesisBase
 * JD-Core Version:    0.6.2
 */