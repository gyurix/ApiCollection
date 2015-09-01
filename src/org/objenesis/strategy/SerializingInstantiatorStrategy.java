package org.objenesis.strategy;

import java.io.NotSerializableException;
import java.io.Serializable;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.basic.ObjectStreamClassInstantiator;

public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy
{
  public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type)
  {
    if (!Serializable.class.isAssignableFrom(type)) {
      throw new ObjenesisException(new NotSerializableException(type + " not serializable"));
    }
    return new ObjectStreamClassInstantiator(type);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.strategy.SerializingInstantiatorStrategy
 * JD-Core Version:    0.6.2
 */