package gyurix.tabapi;

import PluginReference.MC_Player;
import PluginReference.MC_Server;
import WrapperObjects.Entities.PlayerWrapper;
import gyurix.konfigfajl.KFA;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import joebkt.EntityPlayer;
import joebkt.EnumMultiplayStatusChange;
import joebkt.JSonConverter;
import joebkt.PacketBase;
import joebkt.Packet_PlayerListHeaderFooter;
import joebkt.Packet_PlayerListItem;
import joebkt.PlayerConnection;
import org.apache.commons.lang3.StringEscapeUtils;

public class Tab
{
  private List<TabComponent> tabs = new ArrayList();
  String header = "§e§lTabAPI, by Gyurix\n§bHeader,\nwith multiline support";
  String footer = "§b§lTest-footer\n§e--► unlimited lines ◄--\n--► UTF8 characters ☻☺♫ ◄--";
  private static int[] minys = { 0, 11, 14, 16 };
  private int size = 42;

  public Tab() { for (int i = 0; i < 80; i++) {
      this.tabs.add(new TabComponent(this, i, "", TabComponent.Ping.Ping5, false, "MHF_ArrowRight"));
    }
    for (int i = 0; i < 80; i++) {
      TabComponent tc = (TabComponent)this.tabs.get(i);
      tc.setText(i);
      tc.update();
    } }

  public void setHeader(String newheader) {
    this.header = newheader;
  }
  public void setFooter(String newfooter) {
    this.footer = newfooter;
  }
  public void sendInit(MC_Player plr) {
    PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
    Packet_PlayerListItem p = new Packet_PlayerListItem();
    p.a = EnumMultiplayStatusChange.ADD_PLAYER;
    for (int i = 0; i < 80; i++) {
      if (isVisible(i)) {
        getTabComponent(i).add(p);
      }
    }
    c.sendPacket(p);
    p = new Packet_PlayerListItem();
    p.a = EnumMultiplayStatusChange.UPDATE_DISPLAY_NAME;
    for (int i = 0; i < 80; i++) {
      if (isVisible(i)) {
        getTabComponent(i).add(p);
      }
    }
    c.sendPacket(p);
    sendHeaderFooter(c);
  }
  void changeTab(MC_Player plr, Tab previous) {
    PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
    Packet_PlayerListItem p1 = new Packet_PlayerListItem();
    p1.a = EnumMultiplayStatusChange.ADD_PLAYER;
    if ((previous instanceof VanillaTab)) {
      VanillaTab.sendRemovePacket(c);
      for (int i = 0; i < 80; i++) {
        if (isVisible(i)) {
          TabComponent tc = getTabComponent(i);
          tc.add(p1);
        }
      }
      c.sendPacket(p1);
      TabAPI.plt.put(plr.getName(), this);
      sendHeaderFooter(c);
      return;
    }
    Packet_PlayerListItem p2 = new Packet_PlayerListItem();
    p2.a = EnumMultiplayStatusChange.REMOVE_PLAYER;
    for (int i = 0; i < 80; i++) {
      TabComponent t1 = previous.getTabComponent(i);
      TabComponent t2 = getTabComponent(i);
      boolean v1 = previous.isVisible(i);
      boolean v2 = isVisible(i);
      boolean eq = t1.equals(t2);
      if (((!v1) && (v2)) || ((v2) && (!eq))) {
        t2.add(p1);
      }
      if (((v1) && (!v2)) || (!eq)) {
        t1.add(p2);
      }
    }
    if (!p2.b.isEmpty())
      c.sendPacket(p2);
    if (!p1.b.isEmpty())
      c.sendPacket(p1);
    sendHeaderFooter(c);
    TabAPI.plt.put(plr.getName(), this);
  }
  public void updateHeaderFooter() {
    for (Map.Entry e : TabAPI.plt.entrySet())
      if (e.getValue() == this)
        sendHeaderFooter(((PlayerWrapper)KFA.srv.getOnlinePlayerByName((String)e.getKey())).plr.plrConnection); 
  }

  void sendHeaderFooter(PlayerConnection c) { Packet_PlayerListHeaderFooter p = new Packet_PlayerListHeaderFooter();
    p.a = (this.header != null ? JSonConverter.getTextObjectFromString("\"" + StringEscapeUtils.escapeJava(this.header) + "\"") : null);
    p.b = (this.footer != null ? JSonConverter.getTextObjectFromString("\"" + StringEscapeUtils.escapeJava(this.footer) + "\"") : null);
    c.sendPacket(p); }

  public int getMaxx(int size) {
    return (size - 1) / 20 + 1;
  }
  public int getMaxy(int size) {
    return (size - 1) / getMaxx() + 1;
  }
  public int getMaxx() {
    return (this.size - 1) / 20 + 1;
  }
  public int getMaxy() {
    return (this.size - 1) / getMaxx() + 1;
  }
  public int getId(int x, int y) {
    return (x - 1) * 20 + y - 1;
  }
  public TabComponent getTabComponent(int id) {
    return (TabComponent)this.tabs.get(id);
  }
  public boolean wasVisible(int size, int id) {
    if (size == 0)
      return false;
    int x = id / 20 + 1;
    int y = id % 20 + 1;
    return (x <= getMaxx(size)) && (y <= getMaxy(size));
  }
  public boolean isVisible(int id) {
    int x = id / 20 + 1;
    int y = id % 20 + 1;
    return (x <= getMaxx()) && (y <= getMaxy());
  }
  public boolean setSize(int x, int y) {
    if ((x < 1) || (y < 1)) {
      if (this.size != 0) {
        setSize(0);
        return true;
      }
      return false;
    }
    if (x > 4) {
      x = 4;
    }
    else if (y > 20) {
      y = 20;
    }
    if (y < minys[(x - 1)]) {
      y = minys[(x - 1)];
    }
    if (this.size != x * y) {
      setSize(x * y);
      return true;
    }
    return false;
  }
  private void setSize(int newsize) {
    int oldsize = this.size;
    this.size = newsize;
    Packet_PlayerListItem p1 = new Packet_PlayerListItem();
    Packet_PlayerListItem p2 = new Packet_PlayerListItem();
    p1.a = EnumMultiplayStatusChange.REMOVE_PLAYER;
    p2.a = EnumMultiplayStatusChange.ADD_PLAYER;
    boolean is;
    for (int i = 0; i < 80; i++) {
      boolean was = wasVisible(oldsize, i);
      is = isVisible(i);
      if ((was) && (!is)) {
        getTabComponent(i).add(p1);
      }
      else if ((!was) && (is)) {
        getTabComponent(i).add(p2);
      }
    }
    List send = new ArrayList();
    if (p1.b.size() > 0)
      send.add(p1);
    if (p2.b.size() > 0)
      send.add(p2);
    for (Map.Entry e : TabAPI.plt.entrySet())
      if (e.getValue() == this) {
        PlayerConnection c = ((PlayerWrapper)KFA.srv.getOnlinePlayerByName((String)e.getKey())).plr.plrConnection;
        for (PacketBase p : send)
          c.sendPacket(p);
      }
  }

  public TabComponent get(int x, int y) {
    int id = getId(x, y);
    if ((id > -1) && (id < 80)) {
      return (TabComponent)this.tabs.get(id);
    }
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.tabapi.Tab
 * JD-Core Version:    0.6.2
 */