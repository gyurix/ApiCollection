package gyurix.invapi;

import PluginReference.MC_Command;
import PluginReference.MC_Container;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import com.google.common.collect.Lists;
import gyurix.konfigfajl.KFA;
import gyurix.permissions.PermApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class TestInventoryCommand
  implements MC_Command
{
  HashMap<String, Inventory> inv = new HashMap();

  public String getCommandName() {
    return "ti";
  }

  public List<String> getAliases()
  {
    return null;
  }

  public String getHelpLine(MC_Player paramMC_Player)
  {
    return "/ti new <name> <type> [displayname]\n/ti remove <name>\n/ti open <name>\n/ti set <name> <x> <y> [id] [amount] [data-value]\n/ti name <name> <newdisplayname>\n/ti data <name> <newrownumber>\n/ti list\n/ti amount <newamount>";
  }

  public void handleCommand(MC_Player plr, String[] args)
  {
    if (args[0].equals("new")) {
      this.inv.put(args[1], new Inventory(InventoryType.valueOf(args[2]), 
        args.length > 3 ? args[3].replaceAll("(?i)&([a-f0-9k-or])", "§$1") : null, new InventoryHandler(), null));
      plr.sendMessage("[InvAPI] Created.");
    }
    else if (args[0].equals("remove")) {
      if (this.inv.remove(args[1]) != null)
        plr.sendMessage("[InvAPI] Removed.");
      else
        plr.sendMessage("[InvAPI] Not found.");
    }
    else if (args[0].equals("open")) {
      InvAPI.openInventory(args.length > 2 ? KFA.srv.getOnlinePlayerByName(args[2]) : plr, (Inventory)this.inv.get(args[1]));
      plr.sendMessage("[InvAPI] Opened.");
    }
    else if (args[0].equals("set")) {
      ((Inventory)this.inv.get(args[1])).setItem(Integer.valueOf(args[2]).intValue(), Integer.valueOf(args[3]).intValue(), 
        args.length > 4 ? KFA.srv.createItemStack(Integer.valueOf(args[4]).intValue(), 
        args.length > 5 ? Integer.valueOf(args[5]).intValue() : 1, 
        args.length > 6 ? Integer.valueOf(args[6]).intValue() : 0) : plr.getItemInHand());
      plr.sendMessage("[InvAPI] Set.");
    }
    else if (args[0].equals("name")) {
      ((Inventory)this.inv.get(args[1])).name = StringUtils.join(args, " ", 2, args.length).replaceAll("(?i)&([a-f0-9k-or])", "§$1");
      plr.sendMessage("[InvAPI] Renamed.");
    }
    else if (args[0].equals("data")) {
      Inventory iv = (Inventory)this.inv.get(args[1]);
      if (iv.type == InventoryType.Chest) {
        iv.type.data = Integer.valueOf(args[2]).intValue();
        plr.sendMessage("[InvAPI] Set row number to " + iv.type.data);
      }
      else if (iv.type == InventoryType.Horse) {
        iv.type.data = Integer.valueOf(args[2]).intValue();
        plr.sendMessage("[InvAPI] Set horse id to " + iv.type.data);
      }
      else {
        plr.sendMessage("[InvAPI] This feature is only available for chests and horse inventories.");
      }
    }
    else if (args[0].equals("list")) {
      plr.sendMessage("[InvAPI] Inventories: " + StringUtils.join(this.inv.keySet(), ", "));
    }
    else if (args[0].equals("amount")) {
      plr.getItemInHand().setCount(Integer.valueOf(args[1]).intValue());
    }
    else if (args[0].equals("put")) {
      MC_Location loc = plr.getLocation();
      MC_ItemStack is = plr.getItemInHand();
      int count = is.getCount();
      KFA.srv.getWorld(loc.dimension).getContainerAt(new MC_Location(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ(), loc.dimension))
        .setItemAtIdx(Integer.valueOf(args[1]).intValue(), is);
      is.setCount(count);
    }
  }

  public boolean hasPermissionToUse(MC_Player plr)
  {
    return PermApi.has(plr, "invapi.test");
  }

  public List<String> getTabCompletionList(MC_Player plr, String[] args)
  {
    if (args.length == 1)
      return KFA.tabFilter(args[0], Lists.newArrayList(new String[] { "new", "remove", "open", "set", "name", "list" }));
    if (args.length == 2)
      return KFA.tabFilter(args[1], new ArrayList(this.inv.keySet()));
    if ((args.length == 3) && (args[0].equals("new")))
      return KFA.tabFilter(args[2], Lists.newArrayList(InventoryType.valueStrings()));
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.TestInventoryCommand
 * JD-Core Version:    0.6.2
 */