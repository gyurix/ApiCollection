package gyurix.permissions;

import PluginReference.MC_Server;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Player
{
  public String name;
  public LinkedHashMap<String, Group> groups = new LinkedHashMap();
  public List<Perm> perms = new ArrayList();
  public final ConfigFile data;

  public Player(String n)
  {
    this.data = PermApi.plf.childconfig(n + ".data");
    this.name = n;
    load();
  }
  public void load() {
    for (String adr : PermApi.plf.mainAdressList(this.name))
      if (adr.equals("permissions")) {
        for (String perm : PermApi.plf.get(this.name + "." + adr).split("\\n")) {
          this.perms.add(new Perm(perm));
        }
      }
      else if (adr.equals("groups"))
        for (String group : PermApi.plf.get(this.name + "." + adr).split("\\n")) {
          Group g = (Group)PermApi.groups.get(group);
          if (g == null) {
            System.out.println("Error, the given group (" + group + ") for player " + this.name + " doesn't exists.");
          }
          else
            this.groups.put(g.name, g);
        }
  }

  public boolean hasPerm(String perm)
  {
    if (PermApi.debug)
      System.out.print("PERMCHECK-PLAYER: " + this.name + " " + perm + " --> ");
    long time = System.currentTimeMillis();
    Boolean out;
    for (Perm p : this.perms) {
      if (!p.isValid(time, KFA.srv.getOnlinePlayerByName(this.name))) {
        this.perms.remove(p);
      }
      out = p.matches(perm);
      if (out != null) {
        if (PermApi.debug)
          System.out.println(out);
        return out.booleanValue();
      }
    }
    if (PermApi.debug)
      System.out.println("not defined, checking groups...");
    List gr = new ArrayList();
    for (Group g : this.groups.values()) {
      g.addSubgroups(gr);
    }
    for (Group g : gr) {
      Boolean b = g.hasPerm(time, perm);
      if (b != null) {
        if (PermApi.debug)
          System.out.println("PERMCHECK-PLAYER: " + this.name + " " + perm + " --> found in " + g.name + " group: " + b);
        return b.booleanValue();
      }
    }
    if (PermApi.debug)
      System.out.println("PERMCHECK-PLAYER: " + this.name + " " + perm + " --> not defined, returned false");
    return false;
  }
  public String getPermissions() {
    String out = "";
    for (Perm p : this.perms) {
      out = out + p + "\n";
    }
    return out.length() > 1 ? out.substring(0, out.length() - 1) : "";
  }
  public LinkedHashMap<String, Group> getAllGroups() {
    LinkedHashMap out = new LinkedHashMap();
    for (Group g : this.groups.values()) {
      out.put(g.name, g);
      out.putAll(g.subgroups);
    }
    return out;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.Player
 * JD-Core Version:    0.6.2
 */