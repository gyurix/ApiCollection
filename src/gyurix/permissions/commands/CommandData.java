package gyurix.permissions.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import gyurix.permissions.Group;
import gyurix.permissions.PermApi;
import gyurix.permissions.Player;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CommandData
  implements MC_Command
{
  public String getCommandName()
  {
    return "data";
  }

  public List<String> getAliases()
  {
    return Arrays.asList(KFA.lf.get("permapi.command.data").split("\\ "));
  }

  public String getHelpLine(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.data")) ? "/data <player|g:group> [adress] [newvalue(\\r:empty value, \\r\\:remove adress)]" : null;
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
          ChatAPI.msg(plr, "permapi.group.notfound", new String[] { "<group>", args[0] });
        } else if (args.length == 1) {
          g.data.list_format = KFA.l(plr, "permapi.group.dataformat");
          g.data.list_emptyformat = KFA.l(plr, "permapi.group.dataformat.empty");
          ChatAPI.msg(plr, "permapi.group.data.all", new String[] { "<group>", args[0], "<data>", 
            StringUtils.join(g.data.mainList(), "\n") });
        }
        else if (args.length == 2) {
          if (!g.data.contains(args[1])) {
            ChatAPI.msg(plr, "permapi.group.data.notfound", new String[] { "<group>", args[0], "<adress>", args[1] });
            return;
          }
          g.data.list_format = KFA.l(plr, "permapi.group.dataformat");
          g.data.list_emptyformat = KFA.l(plr, "permapi.group.dataformat.empty");
          ChatAPI.msg(plr, "permapi.group.data.starting", new String[] { "<group>", args[0], "<adress>", args[1], "<data>", 
            StringUtils.join(g.data.mainList(args[1]), "\n") });
        }
        else {
          args[2] = StringUtils.join(args, " ", 2, args.length).replaceAll("(?i)&([a-f0-9k-or])", "§$1");
          if (args[2].equals("\\r\\")) {
            if (g.data.remove(args[1])) {
              ChatAPI.msg(plr, "permapi.group.data.remove", new String[] { "<group>", args[0], "<adress>", args[1] });
            }
            else
              ChatAPI.msg(plr, "permapi.group.data.notfound", new String[] { "<group>", args[0], "<adress>", args[1] });
          }
          else
          {
            g.data.set(args[1], args[2]);
            ChatAPI.msg(plr, "permapi.group.data.set" + (args[2].equals("\\r") ? ".empty" : ""), new String[] { 
              "<group>", args[0], "<adress>", args[1], "<data>", args[2] });
          }
        }
      }
      else {
        args[0] = args[0].toLowerCase();
        Player p = (Player)PermApi.pls.get(args[0]);
        if (p == null) {
          ChatAPI.msg(plr, "permapi.player.notfound", new String[] { "<player>", args[0] });
        } else if (args.length == 1) {
          p.data.list_format = KFA.l(plr, "permapi.player.dataformat");
          p.data.list_emptyformat = KFA.l(plr, "permapi.player.dataformat.empty");
          ChatAPI.msg(plr, "permapi.player.data.all", new String[] { "<player>", args[0], "<data>", 
            StringUtils.join(p.data.mainList(), "\n") });
        }
        else if (args.length == 2) {
          if (!p.data.contains(args[1])) {
            ChatAPI.msg(plr, "permapi.player.data.notfound", new String[] { "<player>", args[0], "<adress>", args[1] });
            return;
          }
          p.data.list_format = KFA.l(plr, "permapi.player.dataformat");
          p.data.list_emptyformat = KFA.l(plr, "permapi.player.dataformat.empty");
          ChatAPI.msg(plr, "permapi.player.data.starting", new String[] { "<player>", args[0], "<adress>", args[1], "<data>", 
            StringUtils.join(p.data.mainList(args[1]), "\n") });
        }
        else {
          args[2] = StringUtils.join(args, " ", 2, args.length).replaceAll("(?i)&([a-f0-9k-or])", "§$1");
          if (args[2].equals("\\r\\")) {
            if (p.data.remove(args[1])) {
              ChatAPI.msg(plr, "permapi.player.data.remove", new String[] { "<player>", args[0], "<adress>", args[1] });
            }
            else
              ChatAPI.msg(plr, "permapi.player.data.notfound", new String[] { "<player>", args[0], "<adress>", args[1] });
          }
          else
          {
            p.data.set(args[1], args[2]);
            ChatAPI.msg(plr, "permapi.player.data.set" + (args[2].equals("\\r") ? ".empty" : ""), new String[] { 
              "<player>", args[0], "<adress>", args[1], "<data>", args[2] });
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
 * Qualified Name:     gyurix.permissions.commands.CommandData
 * JD-Core Version:    0.6.2
 */