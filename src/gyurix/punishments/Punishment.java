package gyurix.punishments;

import PluginReference.MC_Player;

public class Punishment
{
  PunishmentType type;
  String value;

  public Punishment(String[] s)
  {
    this.type = PunishmentType.valueOf(s[0]);
    if (s.length == 2)
      this.value = s[1];
  }

  public void execute(PunishmentManager mgr, MC_Player plr, int id)
  {
  }

  public static enum PunishmentType
  {
    Msg, MsgAdmin, Log, SetExpire, SetExpired, Kick, TempBan, Ban, Cmd;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.punishments.Punishment
 * JD-Core Version:    0.6.2
 */