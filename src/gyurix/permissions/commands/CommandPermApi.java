package gyurix.permissions.commands;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import gyurix.chatapi.ChatAPI;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import gyurix.konfigfajl.LangFile;
import gyurix.permissions.PermApi;
import java.util.Arrays;
import java.util.List;

public class CommandPermApi
  implements MC_Command
{
  public String getCommandName()
  {
    return "permapi";
  }

  public List<String> getAliases()
  {
    return Arrays.asList(KFA.lf.get("permapi.command.permapi").split("\\ "));
  }

  public String getHelpLine(MC_Player plr)
  {
    return (plr == null) || (plr.isOp()) || (PermApi.has(plr, "permapi.command.permapi")) ? "/permapi [debug|save|reload|toggle]" : null;
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (args.length == 0) {
      plr.sendMessage("§b§lPermissionAPI, part of the big Api collection plugin - INFO\n§e§lversion: §f1.0.0\n§e§lmaker: §fgyurix\n§e§ltranslator: §f" + KFA.l(plr, "permapi.translator"));
    }
    else if (args[0].equalsIgnoreCase("reload")) {
      PermApi.reload();
      ChatAPI.msg(plr, "permapi.reload", new String[0]);
    }
    else if (args[0].equalsIgnoreCase("save")) {
      PermApi.save();
      ChatAPI.msg(plr, "permapi.saved", new String[0]);
    }
    else if (args[0].equalsIgnoreCase("debug")) {
      PermApi.debug = !PermApi.debug;
      PermApi.kf.set("debug", PermApi.debug ? "+" : "-");
      ChatAPI.msg(plr, "permapi.debug." + (PermApi.debug ? "on" : "off"), new String[0]);
    }
    else if (args[0].equalsIgnoreCase("toggle")) {
      PermApi.enabled = !PermApi.enabled;
      PermApi.kf.set("enabled", PermApi.enabled ? "+" : "-");
      ChatAPI.msg(plr, "permapi.toggled." + (PermApi.enabled ? "on" : "off"), new String[0]);
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
 * Qualified Name:     gyurix.permissions.commands.CommandPermApi
 * JD-Core Version:    0.6.2
 */