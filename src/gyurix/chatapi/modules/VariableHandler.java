package gyurix.chatapi.modules;

import PluginReference.MC_Player;
import com.google.common.collect.Lists;
import gyurix.konfigfajl.ConfigFileMix;
import gyurix.permissions.PermApi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class VariableHandler
{
  public Channel c;

  public VariableHandler(Channel ch)
  {
    this.c = ch;
  }
  public String handle(MC_Player plr, String var) {
    String tf = (String)this.c.timeformats.get(var);
    if (tf != null) {
      return new SimpleDateFormat(tf).format(new Date());
    }
    String pln = plr.getName();
    String str1;
    switch ((str1 = var).hashCode()) { case -985752863:
      if (str1.equals("player"));
      break;
    case -257500528:
      if (str1.equals("fastswitch")) break; break;
    case 98629247:
      if (str1.equals("group"));
      break;
    case 2096610540:
      if (!str1.equals("playername")) { break label243;

        return this.c.fastswitch;

        ConfigFileMix pld = PermApi.getAllPlayerData(pln);
        return StringUtils.join(new String[] { StringUtils.join(Lists.reverse(pld.getAll("chat.prefix")), "") }) + 
          pln + StringUtils.join(new String[] { StringUtils.join(Lists.reverse(pld.getAll("chat.suffix")), "") });
      } else {
        return pln;

        return PermApi.getGroupName(plr);
      }break; }
    label243: return "";
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.chatapi.modules.VariableHandler
 * JD-Core Version:    0.6.2
 */