package org.objenesis.instantiator.sun;

import java.lang.reflect.Constructor;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class SunReflectionFactoryInstantiator<T>
  implements ObjectInstantiator<T>
{
  private final Constructor<T> mungedConstructor;

  public SunReflectionFactoryInstantiator(Class<T> type)
  {
    Constructor javaLangObjectConstructor = getJavaLangObjectConstructor();
    this.mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(
      type, javaLangObjectConstructor);
    this.mungedConstructor.setAccessible(true);
  }

  public T newInstance() {
    try {
      return this.mungedConstructor.newInstance(null);
    }
    catch (Exception e) {
      throw new ObjenesisException(e);
    }
  }

  private static Constructor<Object> getJavaLangObjectConstructor() {
    try {
      return Object.class.getConstructor(null);
    }
    catch (NoSuchMethodException e) {
      throw new ObjenesisException(e);
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.sun.SunReflectionFactoryInstantiator
 * JD-Core Version:    0.6.2
 */