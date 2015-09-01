package org.objenesis;

import org.objenesis.strategy.SerializingInstantiatorStrategy;

public class ObjenesisSerializer extends ObjenesisBase
{
  public ObjenesisSerializer()
  {
    super(new SerializingInstantiatorStrategy());
  }

  public ObjenesisSerializer(boolean useCache)
  {
    super(new SerializingInstantiatorStrategy(), useCache);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.ObjenesisSerializer
 * JD-Core Version:    0.6.2
 */