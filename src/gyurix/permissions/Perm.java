package gyurix.permissions;

import PluginReference.MC_Player;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Perm
{
  String name;
  boolean negate;
  boolean regex;
  boolean onlystart;
  long expire;
  public final Map<String, String> flags = new TreeMap();

  public Perm(String n) { String[] data = n.split(" ");
    this.name = data[0];
    if (this.name.startsWith("!")) {
      this.regex = true;
      this.name = this.name.substring(1);
    }
    if (this.name.startsWith("-")) {
      this.negate = true;
      this.name = this.name.substring(1);
    }
    if (this.name.endsWith("*")) {
      this.onlystart = true;
      this.name = this.name.substring(0, this.name.length() - 1);
    }
    for (int i = 1; i < data.length; i++) {
      String[] d = data[i].split(":", 2);
      this.flags.put(d[0], d[1].replace("\\_", "\r").replace("_", " ").replace("\r", "_"));
    }
    try {
      this.expire = Long.valueOf((String)this.flags.get("expire")).longValue();
    }
    catch (Throwable e) {
      this.expire = 9223372036854775807L;
    }
  }

  public String toString()
  {
    String out = (this.regex ? "!" : "") + (this.negate ? "-" : "") + this.name + (this.onlystart ? "*" : "");
    for (Map.Entry f : this.flags.entrySet()) {
      out = out + " " + (String)f.getKey() + ":" + ((String)f.getValue()).replace("_", "\\_").replace(" ", "_");
    }
    return out;
  }

  public boolean equals(Object arg0) {
    return arg0.toString().equals(toString());
  }

  public boolean isValid(long time, MC_Player plr)
  {
    Iterator localIterator2;
    for (Iterator localIterator1 = PermApi.fh.iterator(); localIterator1.hasNext(); 
      localIterator2.hasNext())
    {
      FlagHandler fh = (FlagHandler)localIterator1.next();
      localIterator2 = this.flags.entrySet().iterator(); continue; Map.Entry f = (Map.Entry)localIterator2.next();
      if (!fh.HandleFlag(this, (String)f.getKey(), (String)f.getValue(), time, plr)) {
        return false;
      }
    }

    return this.expire > time;
  }
  public Boolean matches(String test) {
    if (!this.regex) {
      if (this.onlystart) {
        if (test.startsWith(this.name)) {
          return Boolean.valueOf(!this.negate);
        }
      }
      else if (test.equals(this.name)) {
        return Boolean.valueOf(!this.negate);
      }

    }
    else if (test.matches(this.name)) {
      return Boolean.valueOf(!this.negate);
    }
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.Perm
 * JD-Core Version:    0.6.2
 */