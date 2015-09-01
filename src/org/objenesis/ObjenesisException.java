package org.objenesis;

public class ObjenesisException extends RuntimeException
{
  private static final long serialVersionUID = -2677230016262426968L;

  public ObjenesisException(String msg)
  {
    super(msg);
  }

  public ObjenesisException(Throwable cause)
  {
    super(cause);
  }

  public ObjenesisException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.ObjenesisException
 * JD-Core Version:    0.6.2
 */