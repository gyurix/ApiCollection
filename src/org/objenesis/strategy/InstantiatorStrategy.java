package org.objenesis.strategy;

import org.objenesis.instantiator.ObjectInstantiator;

public abstract interface InstantiatorStrategy
{
  public abstract <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> paramClass);
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.strategy.InstantiatorStrategy
 * JD-Core Version:    0.6.2
 */