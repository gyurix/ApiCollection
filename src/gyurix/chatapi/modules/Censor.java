package gyurix.chatapi.modules;

import java.util.HashSet;

public class Censor
{
  public static String regex = ".*<word>.*";
  public static String perchar = "<char>+";
  public static final HashSet<String> blackl = new HashSet();
  public static final HashSet<String> whitel = new HashSet();
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.chatapi.modules.Censor
 * JD-Core Version:    0.6.2
 */