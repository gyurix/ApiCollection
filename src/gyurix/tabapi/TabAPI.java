package gyurix.tabapi;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import WrapperObjects.Entities.PlayerWrapper;
import com.mojang.authlib.GameProfile;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import joebkt.EntityPlayer;
import joebkt.EnumMultiplayStatusChange;
import joebkt.PacketBase;
import joebkt.Packet_PlayerListItem;
import joebkt.Packet_SpawnPlayer;
import joebkt.PlayerConnection;
import joebkt.kk_PacketListItemRelated;
import org.apache.commons.lang3.StringUtils;

public class TabAPI
{
  static Map<String, Tab> plt = new HashMap();
  static Tab tab1 = new Tab();
  static Tab tab2 = new Tab();
  static VanillaTab vtab = new VanillaTab();
  public static final ScheduledExecutorService sch = Executors.newScheduledThreadPool(1);
  static Tab dftab = vtab;
  static ConfigFile tf;
  static SkinManager sm;
  public static boolean isenabled;
  public static boolean donthandle;
  public static boolean dontcancel;
  public static long removetime = 100L; public static long firstremovetime = 400L; public static long jointime = 200L;

  public TabAPI() { sm = new SkinManager();
    KFA.fileCopy(KFA.pl, "TabAPI.yml", false);
    tf = new ConfigFile(KFA.dir + "/TabAPI.yml");
    isenabled = tf.getBoolean("enable");
    removetime = tf.getLong("removetime", 100L);
    firstremovetime = tf.getLong("firstremovetime", 200L);
    jointime = tf.getLong("jointime", 300L); }

  public static boolean handleOutcommingPacket(MC_Player plr, PacketBase packet)
  {
    if (((packet instanceof Packet_SpawnPlayer)) && (!(getTab(plr) instanceof VanillaTab))) {
      Packet_SpawnPlayer p = (Packet_SpawnPlayer)packet;
      Packet_PlayerListItem p2 = new Packet_PlayerListItem();
      p2.a = EnumMultiplayStatusChange.ADD_PLAYER;
      p2.b.add(((TabData)VanillaTab.data.get(p.uuid)).getkk(p2));
      PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
      dontcancel = true;
      donthandle = true;
      c.sendPacket(p2);
      dontcancel = false;
      donthandle = false;
      sch.schedule(new RemovePacketSender(c, p2), removetime, TimeUnit.MILLISECONDS);
    }
    else if ((packet instanceof Packet_PlayerListItem)) {
      Packet_PlayerListItem p = (Packet_PlayerListItem)packet;
      List l = p.b;
      Tab t = getTab(plr);
      if (!((kk_PacketListItemRelated)l.get(0)).a().getId().toString().startsWith("00007777-0000-0000-0000-000000000")) {
        if (!donthandle)
          VanillaTab.handlePacket(p);
        if ((dontcancel) || ((t != null) && ((t instanceof VanillaTab)))) {
          return false;
        }
        return true;
      }
    }
    return false;
  }
  public static void onPlayerJoin(MC_Player plr) {
    sch.schedule(new PlayerJoin(plr), jointime, TimeUnit.MILLISECONDS);
  }
  public static void onPlayerLogout(String playerName) {
    plt.remove(playerName);
    if (KFA.srv.getPlayers().size() == 1)
      VanillaTab.data.clear();
  }

  public static Tab getTab(MC_Player plr) {
    return (Tab)plt.get(plr.getName());
  }
  public static Tab getTab(String pln) {
    return (Tab)plt.get(pln);
  }
  public static void setTab(String pln, Tab tab) {
    setTab(KFA.srv.getOnlinePlayerByName(pln), tab);
  }
  public static void setTab(MC_Player plr, Tab tab) {
    tab.changeTab(plr, getTab(plr));
  }
  public static void fill(Tab tab, String text, int minx, int miny, int maxx, int maxy) {
    for (int x = minx < 1 ? 1 : minx; x <= (maxx > 4 ? 4 : maxx); x++)
      for (int y = miny < 1 ? 1 : miny; y <= (maxy > 20 ? 20 : maxy); y++) {
        TabComponent c = tab.get(x, y);
        c.setText(text);
        c.update();
      }
  }

  public static void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
    if (ei.isCancelled) {
      return;
    }
    if (plr.hasPermission("tabapi.test")) {
      String[] args = msg.split("\\ ");
      ei.isCancelled = true;
      if (args[0].equals("/chname")) {
        TabComponent t = getTab(plr).get(Integer.valueOf(args[1]).intValue(), Integer.valueOf(args[2]).intValue());
        t.setText(args[3].replace("&", "§"));
        t.update();
        plr.sendMessage("§aName changed.");
      }
      else if (args[0].equals("/loadskin")) {
        vtab.sendInit(plr);
        if (args.length == 1) {
          VanillaTab.sendRemovePacket(((PlayerWrapper)plr).plr.plrConnection);
        }
        else {
          sch.schedule(new JoinPacket(plr), Long.valueOf(args[1]).longValue(), TimeUnit.MILLISECONDS);
        }
      }
      else if (args[0].equals("/chskin")) {
        TabComponent t = getTab(plr).get(Integer.valueOf(args[1]).intValue(), Integer.valueOf(args[2]).intValue());
        t.setSkin(args[3]);
        plr.sendMessage("§aChanging skin...");
      }
      else if (args[0].equals("/fill")) {
        fill(getTab(plr), args[5].replace("&", "§"), Integer.valueOf(args[1]).intValue(), Integer.valueOf(args[2]).intValue(), 
          Integer.valueOf(args[3]).intValue(), Integer.valueOf(args[4]).intValue());
      }
      else if (args[0].equals("/chping")) {
        TabComponent t = getTab(plr).get(Integer.valueOf(args[1]).intValue(), Integer.valueOf(args[2]).intValue());
        t.setPing(TabComponent.Ping.valueOf("Ping" + args[3]));
        t.update();
        plr.sendMessage("§aPing changed.");
      }
      else if (args[0].equals("/chtr")) {
        TabComponent t = getTab(plr).get(Integer.valueOf(args[1]).intValue(), Integer.valueOf(args[2]).intValue());
        t.setTranslucent(Boolean.valueOf(args[3]).booleanValue());
        t.update();
        plr.sendMessage("§aTranslucent changed.");
      }
      else if (args[0].equals("/size")) {
        Tab t = getTab(plr);
        t.setSize(Integer.valueOf(args[1]).intValue(), Integer.valueOf(args[2]).intValue());
        plr.sendMessage("§aSize set.");
      }
      else if (args[0].equals("/header")) {
        Tab t = getTab(plr);
        t.setHeader(StringUtils.join(args, " ", 1, args.length).replace("&", "§").replace("\\n", "\n"));
        t.updateHeaderFooter();
      }
      else if (args[0].equals("/footer")) {
        Tab t = getTab(plr);
        t.setFooter(StringUtils.join(args, " ", 1, args.length).replace("&", "§").replace("\\n", "\n"));
        t.updateHeaderFooter();
      }
      else if (args[0].equals("/vtab")) {
        setTab(plr, vtab);
        plr.sendMessage("§aVanilla tab set.");
      }
      else if (args[0].equals("/dftab")) {
        setTab(plr, tab1);
        plr.sendMessage("§aDefault tab set.");
      }
      else if (args[0].equals("/tab2")) {
        setTab(plr, tab2);
        plr.sendMessage("§aTab2 set.");
      }
      else {
        ei.isCancelled = false;
      }
    }
  }

  private static class JoinPacket
    implements Runnable
  {
    MC_Player plr;

    public JoinPacket(MC_Player p)
    {
      this.plr = p;
    }

    public void run() {
      if (TabAPI.getTab(this.plr) != TabAPI.vtab)
        VanillaTab.sendRemovePacket(((PlayerWrapper)this.plr).plr.plrConnection);
    }
  }

  private static class PlayerJoin
    implements Runnable
  {
    MC_Player plr;

    public PlayerJoin(MC_Player p)
    {
      this.plr = p;
    }

    public void run() {
      if (!TabAPI.plt.containsKey(this.plr.getName())) {
        TabAPI.plt.put(this.plr.getName(), TabAPI.dftab);
        TabAPI.dftab.sendInit(this.plr);
        if (!(TabAPI.dftab instanceof VanillaTab))
          TabAPI.vtab.sendInit(this.plr);
        TabAPI.sch.schedule(new TabAPI.JoinPacket(this.plr), TabAPI.firstremovetime, TimeUnit.MILLISECONDS);
      }
    }
  }

  private static class RemovePacketSender
    implements Runnable
  {
    Packet_PlayerListItem p;
    PlayerConnection c;

    public RemovePacketSender(PlayerConnection con, Packet_PlayerListItem addPacket)
    {
      this.p = new Packet_PlayerListItem();
      this.p.a = EnumMultiplayStatusChange.REMOVE_PLAYER;
      this.p.b.addAll(addPacket.b);
      this.c = con;
    }

    public void run() {
      TabAPI.donthandle = true;
      TabAPI.dontcancel = true;
      this.c.sendPacket(this.p);
      TabAPI.donthandle = false;
      TabAPI.dontcancel = false;
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.tabapi.TabAPI
 * JD-Core Version:    0.6.2
 */