package org.objenesis.instantiator.sun;

import java.lang.reflect.Field;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import sun.misc.Unsafe;

public class UnsafeFactoryInstantiator<T>
  implements ObjectInstantiator<T>
{
  private static Unsafe unsafe;
  private final Class<T> type;

  public UnsafeFactoryInstantiator(Class<T> type)
  {
    if (unsafe == null)
    {
      try {
        f = Unsafe.class.getDeclaredField("theUnsafe");
      }
      catch (NoSuchFieldException e)
      {
        Field f;
        throw new ObjenesisException(e);
      }
      Field f;
      f.setAccessible(true);
      try {
        unsafe = (Unsafe)f.get(null);
      } catch (IllegalAccessException e) {
        throw new ObjenesisException(e);
      }
    }
    this.type = type;
  }

  public T newInstance() {
    try {
      return this.type.cast(unsafe.allocateInstance(this.type));
    } catch (InstantiationException e) {
      throw new ObjenesisException(e);
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.sun.UnsafeFactoryInstantiator
 * JD-Core Version:    0.6.2
 */