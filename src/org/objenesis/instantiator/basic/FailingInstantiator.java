package org.objenesis.instantiator.basic;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class FailingInstantiator<T>
  implements ObjectInstantiator<T>
{
  public FailingInstantiator(Class<T> type)
  {
  }

  public T newInstance()
  {
    throw new ObjenesisException("Always failing");
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.FailingInstantiator
 * JD-Core Version:    0.6.2
 */