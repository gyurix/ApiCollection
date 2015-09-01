package gyurix.permissions;

import gyurix.konfigfajl.ConfigFile;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Group
{
  public String name;
  public LinkedHashMap<String, Group> subgroups = new LinkedHashMap();
  public final ConfigFile data;
  public List<Perm> perms = new ArrayList();

  public Group(String n) { this.data = PermApi.gf.childconfig(n + ".data");
    this.name = n; }

  public void load() {
    for (String adr : PermApi.gf.mainAdressList(this.name))
      if (adr.equals("permissions")) {
        for (String perm : PermApi.gf.get(this.name + "." + adr).split("\\n")) {
          this.perms.add(new Perm(perm));
        }
      }
      else if (adr.equals("subgroups"))
        for (String group : PermApi.gf.get(this.name + "." + adr).split("\\n")) {
          Group g = (Group)PermApi.groups.get(group);
          if (g == null) {
            System.out.println("Error, the given subgroup (" + group + ") for group " + this.name + " doesn't exists.");
          }
          else
            this.subgroups.put(g.name, g);
        }
  }

  public void addSubgroups(List<Group> gr)
  {
    if (!gr.contains(this)) {
      for (Group g : this.subgroups.values()) {
        g.addSubgroups(gr);
      }
      gr.add(this);
    }
  }

  public Boolean hasPerm(long time, String perm) { if (PermApi.debug)
      System.out.print("PERMCHECK-GROUP: " + this.name + " " + perm + " --> ");
    for (Perm p : this.perms) {
      if (!p.isValid(time, null)) {
        this.perms.remove(p);
      }
      Boolean out = p.matches(perm);
      if (out != null) {
        if ((PermApi.debug) && 
          (PermApi.debug))
          System.out.println(out);
        return out;
      }
    }
    if (PermApi.debug)
      System.out.println("not defined");
    return null; }

  public String getPermissions() {
    String out = "";
    for (Perm p : this.perms) {
      out = out + p + "\n";
    }
    return out.length() > 1 ? out.substring(0, out.length() - 1) : "";
  }

  public String toString() {
    return this.name;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.Group
 * JD-Core Version:    0.6.2
 */