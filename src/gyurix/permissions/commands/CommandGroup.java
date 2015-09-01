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
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CommandGroup
  implements MC_Command
{
  public String getCommandName()
  {
    return "group";
  }

  public List<String> getAliases()
  {
    return Arrays.asList(KFA.lf.get("permapi.command.group").split("\\ "));
  }

  public String getHelpLine(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.group")) ? "/group [player|g:group] [(sub)group]" : null;
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (args.length == 0) {
      ChatAPI.msg(plr, "permapi.group.list", new String[] { "<groups>", StringUtils.join(PermApi.groups.keySet(), ", ") });
    }
    else {
      boolean group = args[0].startsWith("g:");
      if (group) {
        args[0] = args[0].substring(2);
        Group g = (Group)PermApi.groups.get(args[0]);
        if (g == null) {
          ChatAPI.msg(plr, "permapi.group.notfound", new String[] { "<group>", args[0] });
        } else if (args.length == 1) {
          ChatAPI.msg(plr, "permapi.group.subgroup.list", new String[] { 
            "<group>", args[0], "<groups>", StringUtils.join(g.subgroups.keySet(), "\n") });
        } else {
          boolean deleteold = args[1].startsWith("-");
          if (deleteold)
            args[1] = args[1].substring(1);
          Group g2 = (Group)PermApi.groups.get(args[1]);
          if (g2 == null) {
            ChatAPI.msg(plr, "permapi.group.notfound", new String[] { "<group>", args[1] });
          } else if (g.subgroups.remove(g2.name) != null) {
            ChatAPI.msg(plr, "permapi.group.subgroup.removed", new String[] { "<group>", args[0], "<subgroup>", args[1] });
          }
          else {
            if (deleteold) {
              g.subgroups.clear();
              ChatAPI.msg(plr, "permapi.group.subgroup.set", new String[] { "<group>", args[0], "<subgroup>", args[1] });
            }
            else {
              ChatAPI.msg(plr, "permapi.group.subgroup.added", new String[] { "<group>", args[0], "<subgroup>", args[1] });
            }
            g.subgroups.put(g2.name, g2);
          }
        }
      }
      else {
        args[0] = args[0].toLowerCase();
        Player p = (Player)PermApi.pls.get(args[0]);
        if (p == null) {
          ChatAPI.msg(plr, "permapi.player.notfound", new String[] { "<player>", args[0] });
        } else if (args.length == 1) {
          ChatAPI.msg(plr, "permapi.player.group.list", new String[] { 
            "<player>", args[0], "<groups>", StringUtils.join(p.groups.keySet(), "\n") });
        } else {
          boolean deleteold = args[1].startsWith("-");
          if (deleteold)
            args[1] = args[1].substring(1);
          Group g2 = (Group)PermApi.groups.get(args[1]);
          if (g2 == null) {
            ChatAPI.msg(plr, "permapi.group.notfound", new String[] { "<group>", args[1] });
          } else if (p.groups.remove(args[1]) != null) {
            ChatAPI.msg(plr, "permapi.player.group.removed", new String[] { "<player>", args[0], "<group>", args[1] });
          }
          else {
            if (deleteold) {
              p.groups.clear();
              ChatAPI.msg(plr, "permapi.player.group.set", new String[] { "<player>", args[0], "<group>", args[1] });
            }
            else {
              ChatAPI.msg(plr, "permapi.player.group.added", new String[] { "<player>", args[0], "<group>", args[1] });
            }
            LinkedHashMap newm = (LinkedHashMap)p.groups.clone();
            p.groups.clear();
            p.groups.put(g2.name, g2);
            p.groups.putAll(newm);
          }
        }
      }
    }
  }

  public boolean hasPermissionToUse(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.group"));
  }

  public List<String> getTabCompletionList(MC_Player paramMC_Player, String[] paramArrayOfString)
  {
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.permissions.commands.CommandGroup
 * JD-Core Version:    0.6.2
 */