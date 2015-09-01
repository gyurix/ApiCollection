package gyurix.permissions;

import PluginReference.MC_Player;
import PluginReference.MC_Server;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.ConfigFileMix;
import gyurix.konfigfajl.KFA;
import gyurix.permissions.commands.CommandData;
import gyurix.permissions.commands.CommandGroup;
import gyurix.permissions.commands.CommandHasPerm;
import gyurix.permissions.commands.CommandPerm;
import gyurix.permissions.commands.CommandPermApi;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PermApi
{
  public static boolean debug = true; public static boolean enabled = true;
  public static Group dfgroup;
  public static HashMap<String, Player> pls = new HashMap();
  public static HashMap<String, Group> groups = new HashMap();
  public static List<FlagHandler> fh = new ArrayList();
  public static ConfigFile gf;
  public static ConfigFile plf;
  public static ConfigFile kf;

  public PermApi()
  {
    load();
    KFA.srv.registerCommand(new CommandGroup());
    KFA.srv.registerCommand(new CommandHasPerm());
    KFA.srv.registerCommand(new CommandPerm());
    KFA.srv.registerCommand(new CommandData());
    KFA.srv.registerCommand(new CommandPermApi());
  }
  public static void reload() {
    pls.clear();
    groups.clear();
    load();
    for (MC_Player plr : KFA.srv.getPlayers())
      registerPlayer(plr.getName().toLowerCase());
  }

  public static void load() {
    KFA.fileCopy(KFA.pl, "PermissionAPI.yml", false);
    kf = new ConfigFile(KFA.dir + "/PermissionAPI.yml");
    enabled = kf.getBoolean("enabled");
    debug = kf.getBoolean("debug");
    KFA.fileCopy(KFA.pl, "groups.yml", false);
    gf = new ConfigFile(KFA.dir + "/groups.yml");
    for (String gn : gf.mainAdressList()) {
      groups.put(gn, new Group(gn));
    }
    for (Group g : groups.values())
      g.load();
    dfgroup = (Group)groups.get(kf.get("groups.default"));
    KFA.fileCopy(KFA.pl, "players.yml", false);
    plf = new ConfigFile(KFA.dir + "/players.yml");
    for (String pn : plf.mainAdressList())
      pls.put(pn, new Player(pn));
  }

  public static void save() {
    for (Group g : groups.values()) {
      gf.set(g.name + ".permissions", StringUtils.join(g.perms, "\n"));
      gf.set(g.name + ".subgroups", StringUtils.join(g.subgroups.keySet(), "\n"));
      gf.insertchild(g.name + ".data", g.data);
    }
    gf.save();
    for (Player p : pls.values()) {
      plf.set(p.name + ".permissions", StringUtils.join(p.perms, "\n"));
      plf.set(p.name + ".groups", StringUtils.join(p.groups.keySet(), "\n"));
      plf.insertchild(p.name + ".data", p.data);
    }
    plf.save();
  }
  public static ConfigFile playerData(String pln) {
    return ((Player)pls.get(pln == null ? "CONSOLE" : pln.toLowerCase())).data;
  }
  public static String getPlayerData(String pln, String adr) {
    return ((Player)pls.get(pln == null ? "CONSOLE" : pln.toLowerCase())).data.get(adr);
  }
  public static ConfigFileMix getAllPlayerData(String pln) {
    Player p = (Player)pls.get(pln == null ? "CONSOLE" : pln.toLowerCase());
    ConfigFileMix out = new ConfigFileMix(new ConfigFile[] { p.data });
    for (Group g : p.getAllGroups().values()) {
      out.kf.add(g.data);
    }
    return out;
  }
  public static void setPlayerData(String pln, String adr, String data) {
    ((Player)pls.get(pln == null ? "CONSOLE" : pln.toLowerCase())).data.set(adr, data);
  }
  public static ConfigFile groupData(String gn) {
    return ((Player)pls.get(gn)).data;
  }
  public static String getGroupData(String grn, String adr) {
    return ((Group)groups.get(grn)).data.get(adr);
  }
  public static void setGroupData(String grn, String adr, String data) {
    ((Group)groups.get(grn)).data.set(adr, data);
  }
  public static String getGroupName(MC_Player plr) {
    Iterator localIterator = ((Player)pls.get(plr.getName().toLowerCase())).groups.keySet().iterator(); if (localIterator.hasNext()) { String gn = (String)localIterator.next();
      return gn;
    }
    return "noGroup";
  }
  public static boolean has(MC_Player plr, String perm) {
    if (debug)
      System.out.println("PERM CHECK: " + plr.getName() + " " + perm);
    return 
      plr == null ? true : enabled ? ((Player)pls.get(plr.getName().toLowerCase())).hasPerm(perm) : 
      plr.hasPermission(perm);
  }
  public static boolean has(String pln, String perm) {
    if (debug)
      System.out.println("PERM CHECK: " + pln + " " + perm);
    return ((Player)pls.get(pln.toLowerCase())).hasPerm(perm);
  }
  public static boolean registerPlayer(String pln) {
    if (!pls.containsKey(pln)) {
      Player p = new Player(pln);
      p.groups.put(dfgroup.name, dfgroup);
      pls.put(pln, p);
      return true;
    }
    return false;
  }
  public static boolean unregisterPlayer(String pln) {
    Player p = (Player)pls.get(pln);
    if ((p.groups.size() == 1) && (p.groups.get(Integer.valueOf(0)) == dfgroup) && (p.perms.isEmpty()) && (p.data.isEmpty())) {
      pls.remove(pln);
      return true;
    }
    return false;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.PermApi
 * JD-Core Version:    0.6.2
 */