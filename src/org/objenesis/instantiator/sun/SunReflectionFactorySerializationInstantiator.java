package org.objenesis.instantiator.sun;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.SerializationInstantiatorHelper;

public class SunReflectionFactorySerializationInstantiator<T>
  implements ObjectInstantiator<T>
{
  private final Constructor<T> mungedConstructor;

  public SunReflectionFactorySerializationInstantiator(Class<T> type)
  {
    Class nonSerializableAncestor = 
      SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
    try
    {
      nonSerializableAncestorConstructor = nonSerializableAncestor
        .getConstructor(null);
    }
    catch (NoSuchMethodException e)
    {
      Constructor nonSerializableAncestorConstructor;
      throw new ObjenesisException(new NotSerializableException(type + " has no suitable superclass constructor"));
    }
    Constructor nonSerializableAncestorConstructor;
    this.mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(
      type, nonSerializableAncestorConstructor);
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
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator
 * JD-Core Version:    0.6.2
 */