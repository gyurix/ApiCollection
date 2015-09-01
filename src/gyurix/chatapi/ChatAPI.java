package gyurix.chatapi;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import com.google.gson.Gson;
import gyurix.chatapi.modules.Channel;
import gyurix.chatapi.modules.Replacement;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import joebkt.Packet_IncomingChatMessage;
import org.apache.commons.lang3.StringUtils;

public class ChatAPI
{
  public static Gson gs = new Gson();
  public static boolean isEnabled = false;
  public static ConfigFile kf;
  public static final HashMap<String, Channel> chs = new HashMap();
  public static final HashMap<String, Channel> plc = new HashMap();
  public static final HashMap<String, String> longmsgs = new HashMap();
  public static final HashMap<String, Replacement> replacements = new HashMap();

  public static String removeColors(String msg) {
    return msg.replaceAll("(?i)§([a-f0-9])", "");
  }
  public ChatAPI() {
    load();
    KFA.srv.registerCommand(new MC_Command()
    {
      public boolean hasPermissionToUse(MC_Player plr)
      {
        return (plr == null) || (plr.hasPermission("chatapi.test"));
      }

      public void handleCommand(MC_Player plr, String[] args)
      {
        MessageBuilder mb = new MessageBuilder();
        mb.addAll(StringUtils.join(args, " ").replace("\\r", "\r").replace("\\n", "\n").replace("&", "§"));
        for (MC_Player p : KFA.srv.getPlayers())
          KFA.srv.executeCommand("tellraw " + p.getName() + " " + mb);
      }

      public List<String> getTabCompletionList(MC_Player paramMC_Player, String[] paramArrayOfString)
      {
        return null;
      }

      public String getHelpLine(MC_Player paramMC_Player)
      {
        return "Chat testing...";
      }

      public String getCommandName()
      {
        return "ct";
      }

      public List<String> getAliases()
      {
        return null;
      }
    });
    isEnabled = true;
  }

  public static boolean handlePlayerPacket(Packet_IncomingChatMessage p) {
    return false;
  }
  public static void load() {
    KFA.fileCopy(KFA.pl, "ChatAPI.yml", false);
    kf = new ConfigFile(KFA.dir + "/ChatAPI.yml");
    int j;
    int i;
    for (Iterator localIterator1 = kf.mainAdressList("channels").iterator(); localIterator1.hasNext(); 
      i < j)
    {
      String adr = (String)localIterator1.next();
      Channel ch = new Channel(adr);
      chs.put(adr, ch);
      adr = "channels." + adr + ".";
      ch.save = true;
      ch.fastswitch = kf.get(adr + "fastswitch");
      for (String r : kf.mainAdressList(adr + "radius")) {
        ch.radius.put(Double.valueOf(kf.getDouble(adr + "radius." + r, 0.0D)), r);
      }
      for (String tf : kf.mainAdressList(adr + "timeformats"))
        ch.timeformats.put(tf, kf.get(adr + "timeformats." + tf, "HH:mm:ss"));
      String[] arrayOfString;
      j = (arrayOfString = kf.get(adr + "chatformats", "").split(" ")).length; i = 0; continue; String cf = arrayOfString[i];
      ch.chatformats.put(cf, kf.get(adr + "chatformats." + cf));

      i++;
    }
  }

  public static void msg(String prefix, String suffix, MC_Player plr, String adress, String[] repl)
  {
    String msg = KFA.l(plr, adress);
    msg = prefix + msg + suffix;
    for (int i = 1; i < repl.length; i += 2)
      msg = msg.replace(repl[(i - 1)], repl[i]);
    if (plr == null)
      KFA.srv.log(removeColors(msg));
    else
      plr.sendMessage(msg); 
  }

  public static void playerJoin(String pln) { plc.put(pln, (Channel)chs.get("global")); }

  public static void playerLeave(String pln) {
    plc.remove(pln);
  }
  public static boolean playerInput(MC_Player plr, String msg) {
    if (msg.startsWith("/"))
      return false;
    String pln = plr.getName();
    Channel pc = (Channel)plc.get(pln);
    for (Channel ch : chs.values()) {
      if ((msg.startsWith(ch.fastswitch)) && (plr.hasPermission("chatapi.fastswitch." + ch.name))) {
        pc = ch;
        break;
      }
    }
    pc.sendMessage(plr, msg);
    return true;
  }
  public static void msg(MC_Player plr, String adress, String[] repl) {
    String msg = KFA.l(plr, adress);
    for (int i = 1; i < repl.length; i += 2)
      msg = msg.replace(repl[(i - 1)], repl[i]);
    if (plr == null)
      KFA.srv.log(removeColors(msg));
    else
      plr.sendMessage(msg);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.chatapi.ChatAPI
 * JD-Core Version:    0.6.2
 */