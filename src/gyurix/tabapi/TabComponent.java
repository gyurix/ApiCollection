package gyurix.tabapi;

import PluginReference.MC_Server;
import WrapperObjects.Entities.PlayerWrapper;
import com.mojang.authlib.properties.Property;
import gyurix.konfigfajl.KFA;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import joebkt.EntityPlayer;
import joebkt.EnumMultiplayStatusChange;
import joebkt.GameMode;
import joebkt.JSonConverter;
import joebkt.Packet_PlayerListItem;
import joebkt.PlayerConnection;
import org.apache.commons.lang3.StringEscapeUtils;

public class TabComponent
{
  protected Tab tab;
  protected int id;
  protected Ping ping;
  protected String text;
  protected String skinname = "MHF_ArrowUp";
  protected Property skin;
  protected TabData data;
  protected boolean translucent;
  protected boolean pingchanged = false; protected boolean textchanged = false; protected boolean translucentchanged = false; protected boolean skinchanged = false; protected boolean autoupdateskin = true;

  public TabComponent() {
  }
  protected TabComponent(Tab t, int i, String s, Ping p, boolean sh, String skinn) { this.tab = t;
    this.id = i;
    this.text = ("§" + (char)(1000 + this.id) + s);
    this.ping = p;
    this.translucent = sh;
    this.skinname = skinn;
    this.skin = SkinManager.getSkin(skinn, this);
    this.data = new TabData(this); }

  public boolean getTranslucent() {
    return this.translucent;
  }
  public Ping getPing() {
    return this.ping;
  }
  public String getText() {
    return this.text.substring(2);
  }
  public String getSkin() {
    return this.skinname;
  }
  public boolean setPing(Ping newping) {
    if (this.ping != newping) {
      this.ping = newping;
      this.pingchanged = true;
      return true;
    }
    return false;
  }
  public boolean setText(String newtext) {
    newtext = "§" + (char)(1000 + this.id) + newtext;
    if (!this.text.equals(newtext)) {
      this.text = newtext;
      this.textchanged = true;
      return true;
    }
    return false;
  }
  public boolean setSkin(String newskin) {
    if (!this.skinname.equals(newskin)) {
      this.skinname = newskin;
      this.skinchanged = true;
      this.skin = SkinManager.getSkin(this.skinname, this);
      if ((this.skin != null) && (this.autoupdateskin))
        updateAll();
      return true;
    }
    return false;
  }
  public boolean setTranslucent(boolean newtranslucent) {
    if (this.translucent != newtranslucent) {
      this.translucent = newtranslucent;
      this.translucentchanged = true;
      return true;
    }
    return false;
  }

  public boolean updateAll() {
    Packet_PlayerListItem p = new Packet_PlayerListItem();
    this.data = new TabData(this);
    p.a = EnumMultiplayStatusChange.ADD_PLAYER;
    p.b.add(this.data.getkk(p));
    Packet_PlayerListItem p2 = new Packet_PlayerListItem();
    p2.a = EnumMultiplayStatusChange.UPDATE_DISPLAY_NAME;
    p2.b.add(p.b.get(0));
    for (Map.Entry e : TabAPI.plt.entrySet()) {
      if (e.getValue() == this.tab) {
        PlayerConnection c = ((PlayerWrapper)KFA.srv.getOnlinePlayerByName((String)e.getKey())).plr.plrConnection;
        c.sendPacket(p);
        c.sendPacket(p2);
      }
    }
    this.textchanged = (this.pingchanged = this.translucentchanged = this.skinchanged = 0);
    return true;
  }

  public boolean update() {
    if ((!this.tab.isVisible(this.id)) || ((!this.pingchanged) && (!this.textchanged) && (!this.translucentchanged) && (!this.skinchanged))) {
      return false;
    }
    Packet_PlayerListItem p = new Packet_PlayerListItem();
    boolean updatedall = false;
    if (!this.skinchanged) {
      if ((this.textchanged) && (!this.pingchanged) && (!this.translucentchanged)) {
        this.data.displayname = JSonConverter.getTextObjectFromString("\"" + StringEscapeUtils.escapeJava(this.text) + "\"");
        p.a = EnumMultiplayStatusChange.UPDATE_DISPLAY_NAME;
        p.b.add(this.data.getkk(p));
        this.textchanged = false;
      }
      else if ((this.pingchanged) && (!this.textchanged) && (!this.translucentchanged)) {
        this.data.ping = this.ping.value;
        p.a = EnumMultiplayStatusChange.UPDATE_LATENCY;
        p.b.add(this.data.getkk(p));
        this.pingchanged = false;
      }
      else if ((this.translucentchanged) && (!this.textchanged) && (!this.pingchanged)) {
        this.data.gamemode = (this.translucent ? GameMode.SPECTATOR : GameMode.CREATIVE);
        p.a = EnumMultiplayStatusChange.UPDATE_GAME_MODE;
        p.b.add(this.data.getkk(p));
        this.translucentchanged = false;
      } else {
        updatedall = updateAll();
      }
    } else updatedall = updateAll();
    if (!updatedall) {
      for (Map.Entry e : TabAPI.plt.entrySet()) {
        if (e.getValue() == this.tab) {
          PlayerConnection c = ((PlayerWrapper)KFA.srv.getOnlinePlayerByName((String)e.getKey())).plr.plrConnection;
          c.sendPacket(p);
        }
      }
    }
    return true;
  }

  void add(Packet_PlayerListItem p) {
    p.b.add(this.data.getkk(p));
  }

  public boolean equals(Object obj) {
    TabComponent t = (TabComponent)obj;
    return (t.getText().equals(this.text)) && (t.getPing().equals(this.ping)) && (t.getTranslucent() == this.translucent) && 
      (t.pingchanged == this.pingchanged) && (t.textchanged == this.textchanged) && (t.translucentchanged == this.translucentchanged);
  }

  public static enum Ping
  {
    Ping0(-1), Ping1(1000), Ping2(750), Ping3(500), Ping4(250), Ping5(0);

    int value;

    private Ping(int p) { this.value = p; }

    public int getValue() {
      return this.value;
    }
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.tabapi.TabComponent
 * JD-Core Version:    0.6.2
 */