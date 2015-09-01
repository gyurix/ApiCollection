package _ApiCollection;

import PluginReference.MC_EventInfo;
import PluginReference.MC_EventInfoPacket;
import PluginReference.MC_Player;
import PluginReference.MC_PlayerPacketListener;
import PluginReference.MC_Server;
import PluginReference.MC_ServerPacketListener;
import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import WrapperObjects.Entities.PlayerWrapper;
import gyurix.barapi.BarAPI;
import gyurix.chatapi.ChatAPI;
import gyurix.invapi.InvAPI;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangChange;
import gyurix.konfigfajl.LangFile;
import gyurix.permissions.PermApi;
import gyurix.tabapi.TabAPI;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.UUID;
import joebkt.EntityPlayer;
import joebkt.PacketBase;
import joebkt.PlayerConnection;

public class MyPlugin extends PluginBase
{
  HashMap<SocketAddress, String> pls = new HashMap();

  public void onStartup(MC_Server srv) {
    KFA.srv = srv;
    KFA.pl = this;
    srv.registerPlayerPacketListener(new MC_PlayerPacketListener()
    {
      public byte[] handleRawPacket(MC_Player plr, int arg1, byte[] arg2, String arg3, MC_EventInfo inf)
      {
        PacketBase packet = (PacketBase)((MC_EventInfoPacket)inf).packetObject;
        if ((TabAPI.isenabled) && 
          (TabAPI.handleOutcommingPacket(plr, packet))) {
          inf.isCancelled = true;
          return arg2;
        }
        if (InvAPI.handleOutcommingPacket(plr, packet)) {
          inf.isCancelled = true;
          return arg2;
        }
        return arg2;
      }
    });
    srv.registerServerPacketListener(new MC_ServerPacketListener()
    {
      public byte[] handleRawPacket(SocketAddress adr, int paramInt, byte[] byteData, String packetName, MC_EventInfo inf)
      {
        PacketBase packet = (PacketBase)((MC_EventInfoPacket)inf).packetObject;
        if (InvAPI.handleIncommingPacket(KFA.srv.getOnlinePlayerByName((String)MyPlugin.this.pls.get(adr)), packet)) {
          inf.isCancelled = true;
          return byteData;
        }
        return byteData;
      }
    });
    initConfigFile();
    new PermApi();
    new BarAPI();
    new TabAPI();
    new InvAPI();
  }

  public void initConfigFile() {
    KFA.dir = KFA.fileCopy(this, "lang.lng", false);
    KFA.lf = new LangFile(KFA.dir + "/lang.lng");
    KFA.lf.insert(new LangFile(KFA.getFileStream(KFA.pl, "lang.lng")));
    KFA.srv.registerCommand(new LangChange());
  }

  public PluginInfo getPluginInfo() {
    PluginInfo inf = new PluginInfo();
    inf.name = "Api-Collection";
    inf.description = "The plugin, which provides APIs for everything. by: Gyurix";
    inf.eventSortOrder = 10.0D;
    inf.version = "2.0.2";
    inf.ref = this;
    return inf;
  }

  public void onShutdown() {
    PermApi.save();
  }

  public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
    if (ei.isCancelled)
      return;
    msg = msg.replace("&", "§");
    BarAPI.onPlayerInput(plr, msg, ei);
    if (TabAPI.isenabled)
      TabAPI.onPlayerInput(plr, msg, ei);
    ei.isCancelled = (ChatAPI.isEnabled ? ChatAPI.playerInput(plr, msg) : ei.isCancelled);
  }

  public void onPlayerJoin(MC_Player plr) {
    this.pls.put(((PlayerWrapper)plr).plr.plrConnection.m_sockAddr, plr.getName());
    if (TabAPI.isenabled)
      TabAPI.onPlayerJoin(plr);
    InvAPI.onPlayerJoin(plr);
    if (ChatAPI.isEnabled)
      ChatAPI.playerJoin(plr.getName());
  }

  public Boolean onRequestPermission(String plr, String perm) {
    if (!PermApi.enabled)
      return null;
    if ((plr.equals("*")) || (plr.length() > 16))
      return Boolean.valueOf(false);
    return Boolean.valueOf(PermApi.has(plr, perm));
  }

  public void onPlayerLogin(String playerName, UUID uuid, String ip) {
    PermApi.registerPlayer(playerName.toLowerCase());
  }

  public void onPlayerLogout(String playerName, UUID uuid) {
    BarAPI.onPlayerLogout(playerName);
    if (TabAPI.isenabled)
      TabAPI.onPlayerLogout(playerName);
    PermApi.unregisterPlayer(playerName.toLowerCase());
    if (ChatAPI.isEnabled)
      ChatAPI.playerLeave(playerName);
  }

  public void onTick(int tickNumber) {
    BarAPI.onTick(tickNumber);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     _ApiCollection.MyPlugin
 * JD-Core Version:    0.6.2
 */