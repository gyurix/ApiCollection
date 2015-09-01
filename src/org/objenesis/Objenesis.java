package org.objenesis;

import org.objenesis.instantiator.ObjectInstantiator;

public abstract interface Objenesis
{
  public abstract <T> T newInstance(Class<T> paramClass);

  public abstract <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> paramClass);
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.Objenesis
 * JD-Core Version:    0.6.2
 */