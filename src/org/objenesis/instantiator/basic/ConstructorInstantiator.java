package org.objenesis.instantiator.basic;

import java.lang.reflect.Constructor;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ConstructorInstantiator<T>
  implements ObjectInstantiator<T>
{
  protected Constructor<T> constructor;

  public ConstructorInstantiator(Class<T> type)
  {
    try
    {
      this.constructor = type.getDeclaredConstructor(null);
    }
    catch (Exception e) {
      throw new ObjenesisException(e);
    }
  }

  public T newInstance() {
    try {
      return this.constructor.newInstance(null);
    }
    catch (Exception e) {
      throw new ObjenesisException(e);
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.ConstructorInstantiator
 * JD-Core Version:    0.6.2
 */