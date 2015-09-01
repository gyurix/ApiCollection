package gyurix.barapi;

import PluginReference.MC_EventInfo;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import WrapperObjects.Entities.PlayerWrapper;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import joebkt.EntityLiving;
import joebkt.EntityPlayer;
import joebkt.Packet_DestroyEntities;
import joebkt.PlayerConnection;
import joebkt.WitherBoss;
import org.apache.commons.lang3.ArrayUtils;

public class BarAPI
{
  static EntityLiving ent;
  static ConfigFile kf;
  static int intervall = 5;
  static List<FakeWither> wl;
  static int[] ids;
  static List<String> pls = new ArrayList();
  static List<Bar> bars = new ArrayList();

  public BarAPI() { ent = new WitherBoss(null);
    ent.setInvisible(true);
    String dir = KFA.fileCopy(KFA.pl, "BarAPI.yml", false);
    kf = new ConfigFile(dir + File.separator + "BarAPI.yml");
    wl = new ArrayList();
    for (String s : kf.get("fake-withers").split("\\n")) {
      FakeWither w = new FakeWither(s);
      wl.add(w);
      ids = ArrayUtils.add(ids, w.id);
    }
    intervall = (int)kf.getLong("update-intervall", 5L); }

  public static boolean hasBar(String pln) {
    return pls.contains(pln);
  }
  public static boolean hasBar(MC_Player plr) {
    return pls.contains(plr.getName());
  }
  public static Bar getBar(String pln) {
    int id = pls.indexOf(pln);
    if (id == -1)
      return null;
    return (Bar)bars.get(id);
  }
  public static Bar getBar(MC_Player plr) {
    int id = pls.indexOf(plr.getName());
    if (id == -1)
      return null;
    return (Bar)bars.get(id);
  }
  public static void setBar(String pln, Bar b) {
    setBar(KFA.srv.getOnlinePlayerByName(pln), b);
  }
  public static void setBar(MC_Player plr, Bar b) {
    ent.setEntityName(b.acttext);
    ent.setHealth((float)b.acthealth.doubleValue());
    if (hasBar(plr)) {
      for (FakeWither w : wl) {
        w.sendData(plr);
      }
    }
    else {
      for (FakeWither w : wl) {
        w.sendSpawn(plr);
      }
    }
    pls.add(plr.getName());
    bars.add(b);
  }
  public static void removeBar(MC_Player plr) {
    if (hasBar(plr)) {
      int id = pls.indexOf(plr.getName());
      pls.remove(id);
      bars.remove(id);
      ((PlayerWrapper)plr).plr.plrConnection.sendPacket(new Packet_DestroyEntities(ids));
    }
  }

  public static void removeBar(String pln) { if (hasBar(pln)) {
      int id = pls.indexOf(pln);
      pls.remove(id);
      bars.remove(id);
      ((PlayerWrapper)KFA.srv.getOnlinePlayerByName(pln)).plr.plrConnection.sendPacket(new Packet_DestroyEntities(ids));
    } }

  static void updateBar(Bar b) {
    ent.setEntityName(b.acttext);
    ent.setHealth((float)b.acthealth.doubleValue());
    for (int id = 0; id < pls.size(); id++)
      if (((Bar)bars.get(id)).equals(b))
        for (FakeWither w : wl) {
          ent.setEntityName(b.acttext);
          ent.setHealth((float)b.acthealth.doubleValue());
          w.sendData(KFA.srv.getOnlinePlayerByName((String)pls.get(id)));
        }
  }

  public static void onTick(int tickNumber)
  {
    if (tickNumber % intervall == 0)
    {
      Iterator localIterator2;
      for (Iterator localIterator1 = pls.iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
      {
        String pl = (String)localIterator1.next();
        MC_Player plr = KFA.srv.getOnlinePlayerByName(pl);
        PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
        MC_Location loc = plr.getLocation();
        localIterator2 = wl.iterator(); continue; FakeWither w = (FakeWither)localIterator2.next();
        w.sendMove(c, loc);
      }
    }
  }

  public static void onPlayerLogout(String playerName) {
    for (int i = 0; i < pls.size(); i++)
      if (((String)pls.get(i)).equals(playerName)) {
        pls.remove(i);
        bars.remove(i);
        return;
      }
  }

  public static void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
    if (ei.isCancelled)
      return;
    if (plr.hasPermission("barapi.test")) {
      ei.isCancelled = true;
      if (msg.equals("/bar")) {
        if (hasBar(plr)) {
          removeBar(plr);
        }
        else {
          setBar(plr, new Bar());
        }
      }
      else if (msg.startsWith("/barm ")) {
        Bar b = getBar(plr);
        b.setStaticMessage(msg.substring(6).replace("&", "§"));
      }
      else if (msg.startsWith("/barh ")) {
        Bar b = getBar(plr);
        b.setStaticHealth(Double.valueOf(msg.substring(6)).doubleValue());
      }
      else if (msg.startsWith("/bartm ")) {
        String[] args = msg.split("\\ ", 3);
        Bar b = getBar(plr);
        b.setTemporaryMessage(args[2].replace("&", "§"), Long.valueOf(args[1]).longValue());
      }
      else if (msg.startsWith("/barth ")) {
        String[] args = msg.split("\\ ", 3);
        Bar b = getBar(plr);
        b.setTemporaryHealth(Double.valueOf(args[2]).doubleValue(), Long.valueOf(args[1]).longValue());
      }
      else if (msg.startsWith("/barp ")) {
        String[] args = msg.split("\\ ");
        if (hasBar(args[1])) {
          removeBar(args[1]);
        }
        else {
          setBar(args[1], new Bar());
        }
      }
      else if (msg.startsWith("/barpm ")) {
        String[] args = msg.split("\\ ", 3);
        Bar b = getBar(args[1]);
        b.setStaticMessage(args[2].replace("&", "§"));
      }
      else if (msg.startsWith("/barph ")) {
        String[] args = msg.split("\\ ");
        Bar b = getBar(args[1]);
        b.setStaticHealth(Double.valueOf(args[2]).doubleValue());
      }
      else if (msg.startsWith("/barptm ")) {
        String[] args = msg.split("\\ ", 4);
        Bar b = getBar(args[1]);
        b.setTemporaryMessage(args[3].replace("&", "§"), Long.valueOf(args[2]).longValue());
      }
      else
      {
        Bar b;
        if (msg.startsWith("/barpth ")) {
          String[] args = msg.split("\\ ");
          b = getBar(args[1]);
          b.setTemporaryHealth(Double.valueOf(args[3]).doubleValue(), Long.valueOf(args[2]).longValue());
        }
        else if (msg.equals("/bara")) {
          for (MC_Player plr2 : KFA.srv.getPlayers()) {
            if (hasBar(plr2)) {
              removeBar(plr2);
            }
            else {
              setBar(plr2, new Bar());
            }
          }
        }
        else if (msg.startsWith("/baram ")) {
          for (MC_Player plr2 : KFA.srv.getPlayers()) {
            Bar b = getBar(plr2);
            b.setStaticMessage(msg.substring(6).replace("&", "§"));
          }
        }
        else
        {
          Bar b;
          if (msg.startsWith("/barah ")) {
            for (MC_Player plr2 : KFA.srv.getPlayers()) {
              b = getBar(plr2);
              b.setStaticHealth(Double.valueOf(msg.substring(6)).doubleValue());
            }
          }
          else if (msg.startsWith("/baratm ")) {
            String[] args = msg.split("\\ ", 3);
            for (MC_Player plr2 : KFA.srv.getPlayers()) {
              Bar b = getBar(plr2);
              b.setTemporaryMessage(args[2].replace("&", "§"), Long.valueOf(args[1]).longValue());
            }
          }
          else if (msg.startsWith("/barath ")) {
            String[] args = msg.split("\\ ", 3);
            for (MC_Player plr2 : KFA.srv.getPlayers()) {
              Bar b = getBar(plr2);
              b.setTemporaryHealth(Double.valueOf(args[2]).doubleValue(), Long.valueOf(args[1]).longValue());
            }
          }
          else {
            ei.isCancelled = false;
            return;
          }
        }
      }
      plr.sendMessage("§b§l[BarAPI] §aCommand executed successfully.");
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.barapi.BarAPI
 * JD-Core Version:    0.6.2
 */