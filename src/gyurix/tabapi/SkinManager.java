package gyurix.tabapi;

import com.google.gson.Gson;
import com.mojang.authlib.properties.Property;
import gyurix.konfigfajl.MojangAPI;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinManager
{
  private static final Gson gson = new Gson();

  static final Map<String, Property> cache = new HashMap();
  static final Map<String, List<TabComponent>> tabs = new HashMap();

  public static Property getSkin(String pln, TabComponent tc)
  {
    if (!cache.containsKey(pln)) {
      synchronized (tabs) {
        if (tabs.containsKey(pln)) {
          ((List)tabs.get(pln)).add(tc);
        }
        else {
          List tcl = new ArrayList();
          tcl.add(tc);
          tabs.put(pln, tcl);
          new Thread(new SkinLoader(pln)).start();
        }
      }
      return null;
    }
    return (Property)cache.get(pln);
  }

  public static Property fetchSkin(String uuid) {
    try {
      HttpURLConnection connection = (HttpURLConnection)new URL(
        "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false")
        .openConnection();
      BufferedReader reader = new BufferedReader(new InputStreamReader(
        connection.getInputStream()));
      return (Property)((SkinProfile)gson.fromJson(reader, SkinProfile.class)).properties.get(0);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  private static class SkinLoader implements Runnable
  {
    String pln;

    public SkinLoader(String plnin) {
      this.pln = plnin;
    }

    public void run() {
      Property p = SkinManager.fetchSkin(MojangAPI.getUUID(this.pln));
      SkinManager.cache.put(this.pln, p);
      if (p != null) {
        synchronized (SkinManager.tabs) {
          for (TabComponent c : (List)SkinManager.tabs.get(this.pln)) {
            c.skin = p;
            if (c.autoupdateskin) {
              c.updateAll();
            }
          }
          SkinManager.tabs.remove(this.pln);
        }
      }
      System.out.println("Loaded " + this.pln + "s skin.");
    }
  }

  private static class SkinProfile
  {
    List<Property> properties = new ArrayList();
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.tabapi.SkinManager
 * JD-Core Version:    0.6.2
 */