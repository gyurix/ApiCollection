package gyurix.chatapi.modules;

import PluginReference.MC_Player;
import PluginReference.MC_Server;
import WrapperObjects.Entities.PlayerWrapper;
import com.google.common.collect.Sets;
import gyurix.chatapi.MessageBuilder;
import gyurix.konfigfajl.ConfigFileMix;
import gyurix.konfigfajl.KFA;
import gyurix.permissions.PermApi;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import joebkt.EntityPlayer;
import joebkt.PacketBase;
import joebkt.Packet_ChatMessage;
import joebkt.PlayerConnection;
import org.apache.commons.lang3.ArrayUtils;

public class Channel
{
  public String fastswitch;
  public String name;
  public final TreeMap<Double, String> radius = new TreeMap();
  public final TreeMap<Integer, String> longerChat = new TreeMap();
  public final ConcurrentHashMap<String, String> chatformats = new ConcurrentHashMap();
  public final HashMap<String, String> timeformats = new HashMap();
  public final ArrayList<VariableHandler> vh = new ArrayList();
  public boolean save;

  public Channel(String n)
  {
    this.name = n;
    this.vh.add(new VariableHandler(this));
  }
  public String fillVariables(MC_Player plr, String input) {
    String[] in = (">" + input + "<").split(">+[^<]*<");
    HashSet variables = Sets.newHashSet((String[])ArrayUtils.subarray(in, 1, in.length - 1));
    List removable = new ArrayList();
    for (VariableHandler h : this.vh) {
      for (String s : variables) {
        String out = h.handle(plr, s);
        if ((out != null) && (!out.isEmpty())) {
          input = input.replace("<" + s + ">", out);
          removable.add(s);
        }
      }
      if (!removable.isEmpty()) {
        for (String s : removable) {
          variables.remove(s);
        }
        removable.clear();
      }
    }
    for (String s : variables) {
      System.out.println("[ChatAPI] Error on handling <" + s + "> variable!");
      input = input.replace("<" + s + ">", "");
    }
    return input;
  }
  public void sendMessage(MC_Player sender, String msg) {
    HashMap neededFormats = new HashMap();
    HashMap playerFormats = new HashMap();
    for (MC_Player pl : KFA.srv.getPlayers()) {
      String f = PermApi.getAllPlayerData(pl.getName()).getFirst("chat.format", "player");
      playerFormats.put(pl.getName(), f);
      if (!neededFormats.containsKey(f)) {
        neededFormats.put(f, new Packet_ChatMessage(new MessageBuilder(fillVariables(sender, (String)this.chatformats.get(f) + "\\\\|" + msg)).toTextObject()));
      }
    }
    for (MC_Player pl : KFA.srv.getPlayers())
      ((PlayerWrapper)pl).plr.plrConnection.sendPacket((PacketBase)neededFormats.get(playerFormats.get(pl.getName())));
  }

  public double getRadius(MC_Player plr) {
    for (Map.Entry e : this.radius.descendingMap().entrySet()) {
      if (plr.hasPermission("chatapi.radius." + (String)e.getValue()))
        return ((Double)e.getKey()).doubleValue();
    }
    return 0.0D;
  }
  public double getMaxMessageLength(MC_Player plr) {
    for (Map.Entry e : this.longerChat.descendingMap().entrySet()) {
      if (plr.hasPermission("chatapi.longerchat." + (String)e.getValue()))
        return ((Integer)e.getKey()).intValue();
    }
    return 0.0D;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.chatapi.modules.Channel
 * JD-Core Version:    0.6.2
 */