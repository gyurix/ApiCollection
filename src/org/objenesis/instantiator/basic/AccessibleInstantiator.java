package org.objenesis.instantiator.basic;

import java.lang.reflect.Constructor;

public class AccessibleInstantiator<T> extends ConstructorInstantiator<T>
{
  public AccessibleInstantiator(Class<T> type)
  {
    super(type);
    if (this.constructor != null)
      this.constructor.setAccessible(true);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.AccessibleInstantiator
 * JD-Core Version:    0.6.2
 */