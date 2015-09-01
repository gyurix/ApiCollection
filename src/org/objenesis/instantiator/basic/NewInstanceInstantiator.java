package org.objenesis.instantiator.basic;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class NewInstanceInstantiator<T>
  implements ObjectInstantiator<T>
{
  private final Class<T> type;

  public NewInstanceInstantiator(Class<T> type)
  {
    this.type = type;
  }

  public T newInstance() {
    try {
      return this.type.newInstance();
    }
    catch (Exception e) {
      throw new ObjenesisException(e);
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.NewInstanceInstantiator
 * JD-Core Version:    0.6.2
 */