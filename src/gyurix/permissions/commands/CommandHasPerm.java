package gyurix.permissions.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import gyurix.permissions.Group;
import gyurix.permissions.PermApi;
import gyurix.permissions.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CommandHasPerm
  implements MC_Command
{
  public String getCommandName()
  {
    return "hasperm";
  }

  public List<String> getAliases()
  {
    return Arrays.asList(KFA.lf.get("permapi.command.hasperm").split("\\ "));
  }

  public String getHelpLine(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.hasperm")) ? "/hasperm [player|g:group] [perm]" : null;
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (args.length == 0) {
      ChatAPI.msg(plr, "permapi.player.list", new String[] { "<players>", StringUtils.join(PermApi.pls.keySet(), ", ") });
    }
    else {
      boolean group = args[0].startsWith("g:");
      if (group) {
        args[0] = args[0].substring(2);
        Group g = (Group)PermApi.groups.get(args[0]);
        if (g == null) {
          ChatAPI.msg(plr, "permapi.group.notfound", new String[] { "<group>", args[0] });
        } else if (args.length == 1) {
          ChatAPI.msg(plr, "permapi.group.perm.list", new String[] { 
            "<group>", args[0], "<perms>", g.getPermissions() });
        } else {
          Boolean b = g.hasPerm(System.currentTimeMillis(), args[1]);
          if (b == null)
            ChatAPI.msg(plr, "permapi.group.perm.notdefined", new String[] { "<group>", args[0], "<perm>", args[1] });
          else if (!b.booleanValue())
            ChatAPI.msg(plr, "permapi.group.perm.false", new String[] { "<group>", args[0], "<perm>", args[1] });
          else if (b.booleanValue())
            ChatAPI.msg(plr, "permapi.group.perm.true", new String[] { "<group>", args[0], "<perm>", args[1] });
        }
      }
      else {
        args[0] = args[0].toLowerCase();
        Player p = (Player)PermApi.pls.get(args[0]);
        if (p == null) {
          ChatAPI.msg(plr, "permapi.player.notfound", new String[] { "<player>", args[0] });
        } else if (args.length == 1) {
          ChatAPI.msg(plr, "permapi.player.perm.list", new String[] { 
            "<player>", args[0], "<perms>", p.getPermissions() });
        } else {
          boolean b = p.hasPerm(args[1]);
          if (b)
            ChatAPI.msg(plr, "permapi.player.perm.true", new String[] { "<player>", args[0], "<perm>", args[1] });
          else
            ChatAPI.msg(plr, "permapi.player.perm.false", new String[] { "<player>", args[0], "<perm>", args[1] });
        }
      }
    }
  }

  public boolean hasPermissionToUse(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.hasperm"));
  }

  public List<String> getTabCompletionList(MC_Player paramMC_Player, String[] paramArrayOfString)
  {
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.commands.CommandHasPerm
 * JD-Core Version:    0.6.2
 */