package org.objenesis.strategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class SingleInstantiatorStrategy
  implements InstantiatorStrategy
{
  private Constructor<?> constructor;

  public <T extends ObjectInstantiator<?>> SingleInstantiatorStrategy(Class<T> instantiator)
  {
    try
    {
      this.constructor = instantiator.getConstructor(new Class[] { Class.class });
    }
    catch (NoSuchMethodException e) {
      throw new ObjenesisException(e);
    }
  }

  public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type)
  {
    try {
      return (ObjectInstantiator)this.constructor.newInstance(new Object[] { type });
    } catch (InstantiationException e) {
      throw new ObjenesisException(e);
    } catch (IllegalAccessException e) {
      throw new ObjenesisException(e);
    } catch (InvocationTargetException e) {
      throw new ObjenesisException(e);
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.strategy.SingleInstantiatorStrategy
 * JD-Core Version:    0.6.2
 */