package org.objenesis.instantiator.basic;

import org.objenesis.instantiator.ObjectInstantiator;

public class NullInstantiator<T>
  implements ObjectInstantiator<T>
{
  public NullInstantiator(Class<T> type)
  {
  }

  public T newInstance()
  {
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.NullInstantiator
 * JD-Core Version:    0.6.2
 */