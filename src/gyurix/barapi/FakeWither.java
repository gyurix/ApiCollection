package gyurix.barapi;

import PluginReference.MC_Location;
import PluginReference.MC_Player;
import WrapperObjects.Entities.PlayerWrapper;
import joebkt.EntityLiving;
import joebkt.EntityPlayer;
import joebkt.Packet_EntityMetadata;
import joebkt.Packet_EntityTeleport;
import joebkt.Packet_SpawnMob;
import joebkt.PlayerConnection;

public class FakeWither
{
  int id;
  int x;
  int y;
  int z;

  public FakeWither(String s)
  {
    String[] d = s.split("\\ ");
    this.id = Integer.valueOf(d[0]).intValue();
    this.x = Integer.valueOf(d[1]).intValue();
    this.y = Integer.valueOf(d[2]).intValue();
    this.z = Integer.valueOf(d[3]).intValue();
  }
  void sendMove(PlayerConnection con, MC_Location loc) {
    con.sendPacket(new Packet_EntityTeleport(this.id, (int)(loc.x + this.x) * 32, (int)(loc.y + this.y) * 32, (int)(loc.z + this.z) * 32, (byte)0, (byte)0, true));
  }
  void sendSpawn(MC_Player plr) {
    BarAPI.ent.EntGen_setEntityID(this.id);
    MC_Location loc = plr.getLocation();
    BarAPI.ent.teleportWrapper(loc.x + this.x, loc.y + this.y, loc.z + this.z);
    ((PlayerWrapper)plr).plr.plrConnection.sendPacket(new Packet_SpawnMob(BarAPI.ent));
  }
  void sendData(MC_Player plr) {
    ((PlayerWrapper)plr).plr.plrConnection.sendPacket(new Packet_EntityMetadata(this.id, BarAPI.ent.getDataWatcher(), true));
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.barapi.FakeWither
 * JD-Core Version:    0.6.2
 */