package gyurix.tabapi;

import PluginReference.MC_Player;
import WrapperObjects.Entities.PlayerWrapper;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import joebkt.EntityPlayer;
import joebkt.EnumMultiplayStatusChange;
import joebkt.Packet_PlayerListItem;
import joebkt.PlayerConnection;
import joebkt.kk_PacketListItemRelated;

public final class VanillaTab extends Tab
{
  public static HashMap<UUID, TabData> data = new HashMap();

  public VanillaTab() {
    this.footer = "§e§lTabAPI     by: §6§lGyurix\n§bVanilla tab";
    this.header = "§b>>> Vanilla TAB <<<";
  }
  public static List<kk_PacketListItemRelated> getData(Packet_PlayerListItem p) {
    List out = new ArrayList();
    for (TabData d : data.values()) {
      out.add(d.getkk(p));
    }
    return out;
  }

  void changeTab(MC_Player plr, Tab previous) {
    TabAPI.plt.put(plr.getName(), this);
    Packet_PlayerListItem p = new Packet_PlayerListItem();
    p.a = EnumMultiplayStatusChange.REMOVE_PLAYER;
    for (int i = 0; i < 80; i++) {
      if (previous.isVisible(i)) {
        previous.getTabComponent(i).add(p);
      }
    }
    PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
    c.sendPacket(p);
    super.sendHeaderFooter(c);
    sendInit(plr);
  }

  public void sendInit(MC_Player plr)
  {
    Packet_PlayerListItem p = new Packet_PlayerListItem();
    p.a = EnumMultiplayStatusChange.ADD_PLAYER;
    p.b.addAll(getData(p));
    TabAPI.donthandle = true;
    TabAPI.dontcancel = true;
    ((PlayerWrapper)plr).plr.plrConnection.sendPacket(p);
    TabAPI.donthandle = false;
    TabAPI.dontcancel = false;
  }

  static void sendRemovePacket(PlayerConnection c) {
    Packet_PlayerListItem p = new Packet_PlayerListItem();
    p.a = EnumMultiplayStatusChange.REMOVE_PLAYER;
    p.b.addAll(getData(p));
    TabAPI.donthandle = true;
    TabAPI.dontcancel = true;
    c.sendPacket(p);
    TabAPI.donthandle = false;
    TabAPI.dontcancel = false;
  }

  static void handlePacket(Packet_PlayerListItem p)
  {
    Iterator localIterator;
    if (p.a == EnumMultiplayStatusChange.ADD_PLAYER) {
      for (localIterator = p.b.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        kk_PacketListItemRelated k = (kk_PacketListItemRelated)obj;
        data.put(k.a().getId(), new TabData(k));
      }
    }
    else if (p.a == EnumMultiplayStatusChange.UPDATE_LATENCY) {
      for (localIterator = p.b.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        kk_PacketListItemRelated k = (kk_PacketListItemRelated)obj;
        ((TabData)data.get(k.a().getId())).ping = k.b();
      }

    }
    else if (p.a == EnumMultiplayStatusChange.REMOVE_PLAYER)
      for (localIterator = p.b.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        kk_PacketListItemRelated k = (kk_PacketListItemRelated)obj;
        data.remove(k.a().getId());
      }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.tabapi.VanillaTab
 * JD-Core Version:    0.6.2
 */