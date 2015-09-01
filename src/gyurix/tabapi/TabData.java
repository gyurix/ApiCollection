package gyurix.tabapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import joebkt.GameMode;
import joebkt.JSonConverter;
import joebkt.Packet_PlayerListItem;
import joebkt.TextObject;
import joebkt.kk_PacketListItemRelated;
import org.apache.commons.lang3.StringEscapeUtils;

public class TabData
{
  public UUID id;
  public String name;
  public int ping;
  public TextObject displayname;
  public GameMode gamemode;
  public Property skin = null;

  public TabData(kk_PacketListItemRelated in) { this.id = in.a().getId();
    this.name = in.a().getName();
    this.ping = in.b();
    this.displayname = in.d();
    this.gamemode = in.c();
    for (Property p : in.a().getProperties().get("textures"))
      this.skin = p; }

  public TabData(TabComponent in)
  {
    this.id = UUID.fromString("7777-0-0-0-" + in.id);
    this.name = in.id;
    this.ping = in.ping.value;
    this.displayname = JSonConverter.getTextObjectFromString("\"" + StringEscapeUtils.escapeJava(in.text) + "\"");
    this.gamemode = (in.translucent ? GameMode.SPECTATOR : GameMode.CREATIVE);
    this.skin = in.skin;
  }
  public kk_PacketListItemRelated getkk(Packet_PlayerListItem p) {
    GameProfile prof = new GameProfile(this.id, this.name);
    prof.getProperties().clear();
    if (this.skin != null) {
      prof.getProperties().put("textures", this.skin);
    }
    return new kk_PacketListItemRelated(p, 
      prof, this.ping, this.gamemode, this.displayname);
  }

  public String toString() {
    String d = "NAME:" + this.skin.getName() + "\n\nVALUE:" + this.skin.getValue() + "\n\nSIGNATURE:" + this.skin.getSignature();
    return "UUID: " + this.id.toString() + 
      "\nName: " + this.name + 
      "\nPing: " + this.ping + 
      "\nDisplayName: " + JSonConverter.getStringFromTextObject(this.displayname) + 
      "\nGameMode: " + this.gamemode.name() + 
      "\nSkin:\n " + d;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.tabapi.TabData
 * JD-Core Version:    0.6.2
 */