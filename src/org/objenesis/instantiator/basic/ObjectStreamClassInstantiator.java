package org.objenesis.instantiator.basic;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjectStreamClassInstantiator<T>
  implements ObjectInstantiator<T>
{
  private static Method newInstanceMethod;
  private final ObjectStreamClass objStreamClass;

  private static void initialize()
  {
    if (newInstanceMethod == null)
      try {
        newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[0]);
        newInstanceMethod.setAccessible(true);
      }
      catch (RuntimeException e) {
        throw new ObjenesisException(e);
      }
      catch (NoSuchMethodException e) {
        throw new ObjenesisException(e);
      }
  }

  public ObjectStreamClassInstantiator(Class<T> type)
  {
    initialize();
    this.objStreamClass = ObjectStreamClass.lookup(type);
  }

  public T newInstance()
  {
    try
    {
      return newInstanceMethod.invoke(this.objStreamClass, new Object[0]);
    }
    catch (Exception e) {
      throw new ObjenesisException(e);
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.ObjectStreamClassInstantiator
 * JD-Core Version:    0.6.2
 */