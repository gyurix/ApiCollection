package gyurix.konfigfajl;

import PluginReference.MC_Command;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import gyurix.chatapi.ChatAPI;
import gyurix.permissions.PermApi;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class LangChange
  implements MC_Command
{
  public List<String> getAliases()
  {
    return Arrays.asList(KFA.lf.get("kf.langchange.commands").split("\\ "));
  }

  public String getCommandName()
  {
    return "§";
  }

  public String getHelpLine(MC_Player plr)
  {
    return KFA.l(plr, "kf.langchange.helpline");
  }

  public List<String> getTabCompletionList(MC_Player plr, String[] args)
  {
    if (args.length == 1) {
      return KFA.tabFilter(args[0], KFA.lf.langs);
    }
    if (args.length == 2) {
      List pll = KFA.srv.getMatchingOnlinePlayerNames("");
      pll.add("CONSOLE");
      return KFA.tabFilter(args[1], pll);
    }
    return null;
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (args.length == 0) {
      ChatAPI.msg("", StringUtils.join(KFA.lf.langs, KFA.l(plr, "kf.langchange.list.separator")), 
        plr, "kf.langchange.list.prefix", new String[0]);
      return;
    }
    if (!KFA.lf.langs.contains(args[0])) {
      ChatAPI.msg(plr, "kf.langchange.notfound", new String[] { "<0>", args[0] });
      return;
    }
    if (args.length == 1) {
      PermApi.setPlayerData(plr == null ? null : plr.getName(), "lang", args[0]);
      ChatAPI.msg(plr, "kf.langchange.success", new String[] { "<0>", args[0] });
    }
    else if (args[1].equals("CONSOLE")) {
      if (PermApi.has(plr, "kf.langchange.console")) {
        PermApi.setPlayerData(null, "lang", args[0]);
        ChatAPI.msg(plr, "kf.langchange.success.console", new String[] { "<0>", args[0] });
      }
      else {
        ChatAPI.msg(plr, "kf.langchange.noperm.console", new String[0]);
      }
    }
    else if (PermApi.has(plr, "kf.langchange.player")) {
      MC_Player target = KFA.srv.getOnlinePlayerByName(args[1]);
      if (target != null) {
        PermApi.setPlayerData(target.getName(), "lang", args[0]);
        ChatAPI.msg(plr, "kf.langchange.success.player", new String[] { "<lang>", args[0], "<player>", target.getName() });
      }
      else {
        ChatAPI.msg(plr, "kf.langchange.playernotfound", new String[] { "<0>", args[1] });
      }
    } else {
      ChatAPI.msg(plr, "kf.langchange.noperm.player", new String[0]);
    }
  }

  public boolean hasPermissionToUse(MC_Player plr)
  {
    return PermApi.has(plr, "kf.langchange");
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.konfigfajl.LangChange
 * JD-Core Version:    0.6.2
 */