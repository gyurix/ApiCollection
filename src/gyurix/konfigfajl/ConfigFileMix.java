package gyurix.konfigfajl;

import com.google.common.collect.Lists;
import java.util.ArrayList;

public class ConfigFileMix
{
  public ArrayList<ConfigFile> kf;

  public ConfigFileMix(ConfigFile[] elements)
  {
    this.kf = Lists.newArrayList(elements);
  }
  public String getFirst(String key) {
    return getFirst(key, "\r\r");
  }
  public String getFirst(String key, String not_found) {
    for (ConfigFile kf : this.kf) {
      String out = kf.get(key);
      if (!out.equals(kf.get_na))
        return out;
    }
    return not_found;
  }
  public ArrayList<String> getAll(String key) {
    return getAll(key, false, false);
  }
  public ArrayList<String> getAll(String key, boolean containEmpty, boolean containNa) {
    ArrayList out = new ArrayList();
    for (ConfigFile kf : this.kf) {
      String e = kf.get(key);
      if (((!e.equals(kf.get_empty)) || (containEmpty)) && ((!e.equals(kf.get_na)) || (containNa)))
      {
        out.add(e);
      }
    }
    return out;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.konfigfajl.ConfigFileMix
 * JD-Core Version:    0.6.2
 */