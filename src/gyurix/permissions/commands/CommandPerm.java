package gyurix.permissions.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import gyurix.permissions.Group;
import gyurix.permissions.Perm;
import gyurix.permissions.PermApi;
import gyurix.permissions.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CommandPerm
  implements MC_Command
{
  public String getCommandName()
  {
    return "perm";
  }

  public List<String> getAliases()
  {
    return Arrays.asList(KFA.lf.get("permapi.command.perm").split("\\ "));
  }

  public String getHelpLine(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.perm")) ? "/perm <player|g:group> [perm] [parameters]" : null;
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (args.length == 0) {
      plr.sendMessage(getHelpLine(plr));
    }
    else {
      boolean group = args[0].startsWith("g:");
      if (group) {
        args[0] = args[0].substring(2);
        Group g = (Group)PermApi.groups.get(args[0]);
        if (g == null) {
          PermApi.groups.put(args[0], new Group(args[0]));
          ChatAPI.msg(plr, "permapi.group.created", new String[] { "<group>", args[0] });
        }
        else if (args.length == 1) {
          PermApi.groups.remove(g.name);
          for (Group g2 : PermApi.groups.values()) {
            g2.subgroups.remove(args[0]);
          }
          for (Player p : PermApi.pls.values()) {
            p.groups.remove(args[0]);
            if (p.groups.size() == 0)
              p.groups.put(PermApi.dfgroup.name, PermApi.dfgroup);
          }
          ChatAPI.msg(plr, "permapi.group.removed", new String[] { "<group>", args[0] });
        }
        else {
          args[1] = StringUtils.join(args, " ", 1, args.length);
          Perm p = new Perm(args[1]);
          if (g.perms.remove(p)) {
            ChatAPI.msg(plr, "permapi.group.perm.removed", new String[] { "<group>", args[0], "<perm>", args[1] });
          }
          else {
            g.perms.add(0, p);
            ChatAPI.msg(plr, "permapi.group.perm.added", new String[] { "<group>", args[0], "<perm>", args[1] });
          }
        }
      }
      else {
        args[0] = args[0].toLowerCase();
        Player p = (Player)PermApi.pls.get(args[0]);
        if (p == null) {
          PermApi.pls.put(args[0], new Player(args[0]));
          ChatAPI.msg(plr, "permapi.player.created", new String[] { "<player>", args[0] });
        }
        else if (args.length == 1) {
          PermApi.pls.remove(args[0]);
          if (KFA.srv.getOnlinePlayerByName(args[0]) != null) {
            Player p2 = new Player(args[0]);
            p2.groups.put(PermApi.dfgroup.name, PermApi.dfgroup);
            PermApi.pls.put(args[0], p2);
          }
          ChatAPI.msg(plr, "permapi.player.removed", new String[] { "<player>", args[0] });
        }
        else {
          args[1] = StringUtils.join(args, " ", 1, args.length);
          Perm perm = new Perm(args[1]);
          if (p.perms.remove(perm)) {
            ChatAPI.msg(plr, "permapi.player.perm.removed", new String[] { "<player>", args[0], "<perm>", args[1] });
          }
          else {
            p.perms.add(0, perm);
            ChatAPI.msg(plr, "permapi.player.perm.added", new String[] { "<player>", args[0], "<perm>", args[1] });
          }
        }
      }
    }
  }

  public boolean hasPermissionToUse(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.perm"));
  }

  public List<String> getTabCompletionList(MC_Player paramMC_Player, String[] paramArrayOfString)
  {
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.commands.CommandPerm
 * JD-Core Version:    0.6.2
 */