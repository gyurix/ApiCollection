package org.objenesis.instantiator.basic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjectInputStreamInstantiator<T>
  implements ObjectInstantiator<T>
{
  private ObjectInputStream inputStream;

  public ObjectInputStreamInstantiator(Class<T> clazz)
  {
    if (Serializable.class.isAssignableFrom(clazz)) {
      try {
        this.inputStream = new ObjectInputStream(new MockStream(clazz));
      }
      catch (IOException e) {
        throw new Error("IOException: " + e.getMessage());
      }
    }
    else
      throw new ObjenesisException(new NotSerializableException(clazz + " not serializable"));
  }

  public T newInstance()
  {
    try
    {
      return this.inputStream.readObject();
    }
    catch (ClassNotFoundException e) {
      throw new Error("ClassNotFoundException: " + e.getMessage());
    }
    catch (Exception e) {
      throw new ObjenesisException(e);
    }
  }

  private static class MockStream extends InputStream
  {
    private int pointer;
    private byte[] data;
    private int sequence;
    private static final int[] NEXT = { 1, 2, 2 };
    private byte[][] buffers;
    private final byte[] FIRST_DATA;
    private static byte[] HEADER;
    private static byte[] REPEATING_DATA;

    static
    {
      initialize();
    }

    private static void initialize() {
      try {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(byteOut);
        dout.writeShort(-21267);
        dout.writeShort(5);
        HEADER = byteOut.toByteArray();

        byteOut = new ByteArrayOutputStream();
        dout = new DataOutputStream(byteOut);

        dout.writeByte(115);
        dout.writeByte(113);
        dout.writeInt(8257536);
        REPEATING_DATA = byteOut.toByteArray();
      }
      catch (IOException e) {
        throw new Error("IOException: " + e.getMessage());
      }
    }

    public MockStream(Class<?> clazz)
    {
      this.pointer = 0;
      this.sequence = 0;
      this.data = HEADER;

      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      DataOutputStream dout = new DataOutputStream(byteOut);
      try {
        dout.writeByte(115);
        dout.writeByte(114);
        dout.writeUTF(clazz.getName());
        dout.writeLong(ObjectStreamClass.lookup(clazz).getSerialVersionUID());
        dout.writeByte(2);
        dout.writeShort(0);
        dout.writeByte(120);
        dout.writeByte(112);
      }
      catch (IOException e) {
        throw new Error("IOException: " + e.getMessage());
      }
      this.FIRST_DATA = byteOut.toByteArray();
      this.buffers = new byte[][] { HEADER, this.FIRST_DATA, REPEATING_DATA };
    }

    private void advanceBuffer() {
      this.pointer = 0;
      this.sequence = NEXT[this.sequence];
      this.data = this.buffers[this.sequence];
    }

    public int read() throws IOException
    {
      int result = this.data[(this.pointer++)];
      if (this.pointer >= this.data.length) {
        advanceBuffer();
      }

      return result;
    }

    public int available() throws IOException
    {
      return 2147483647;
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
      int left = len;
      int remaining = this.data.length - this.pointer;

      while (remaining <= left) {
        System.arraycopy(this.data, this.pointer, b, off, remaining);
        off += remaining;
        left -= remaining;
        advanceBuffer();
        remaining = this.data.length - this.pointer;
      }
      if (left > 0) {
        System.arraycopy(this.data, this.pointer, b, off, left);
        this.pointer += left;
      }

      return len;
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     org.objenesis.instantiator.basic.ObjectInputStreamInstantiator
 * JD-Core Version:    0.6.2
 */