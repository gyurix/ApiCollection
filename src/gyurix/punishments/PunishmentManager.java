package gyurix.punishments;

import PluginReference.MC_Player;
import PluginReference.MC_Server;
import gyurix.konfigfajl.KFA;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class PunishmentManager
{
  public HashMap<Intervalls, ArrayList<Punishment>> map = new HashMap();
  public long expiration;
  public int id;
  public String pln;

  public PunishmentManager()
  {
  }

  public PunishmentManager(HashMap<Intervalls, ArrayList<Punishment>> m, String p)
  {
    this.pln = p;
    this.map = m;
  }
  public void setExpiration(long exp) {
    this.expiration = (System.currentTimeMillis() + exp);
  }
  public long getExpiration() {
    long time = System.currentTimeMillis();
    return time > this.expiration ? 0L : this.expiration - time;
  }
  public void setExpired() {
    this.expiration = 0L;
  }
  public boolean isExpired() {
    return this.expiration <= System.currentTimeMillis();
  }
  public void executeNextPunishment() {
    this.id += 1;
    MC_Player plr = KFA.srv.getOnlinePlayerByName(this.pln);
    for (Map.Entry e : this.map.entrySet())
      if (((Intervalls)e.getKey()).contains(this.id))
        for (Punishment p : (ArrayList)e.getValue())
          p.execute(this, plr, this.id);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.punishments.PunishmentManager
 * JD-Core Version:    0.6.2
 */