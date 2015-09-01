package gyurix.invapi;

import PluginReference.MC_ItemStack;
import java.io.PrintStream;

public class InventoryHandler
{
  public Inventory inv;

  public boolean onClick(int x, int y, InventoryClickType ct, MC_ItemStack item)
  {
    System.out.println("CLICK INVENTORY: " + x + " " + y + " >>> " + ct.name() + (
      item == null ? "" : new StringBuilder(String.valueOf(item.getCount())).append("x ").append(item.getId()).append(":").append(item.getDamage()).toString()));
    return false;
  }
  public boolean onEvent(InventoryEventType et) {
    System.out.println("INVENTORY EVENT: " + et.name());
    return false;
  }
  public boolean onOpen() {
    System.out.println(this.inv.name + " opened.");
    return true;
  }
  public void onClose() {
    System.out.println(this.inv.name + " closed.");
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.InventoryHandler
 * JD-Core Version:    0.6.2
 */