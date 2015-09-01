package gyurix.invapi;

import PluginReference.MC_ItemStack;
import PluginReference.MC_Server;
import WrapperObjects.ItemStackWrapper;
import gyurix.konfigfajl.KFA;
import java.util.ArrayList;
import java.util.List;
import joebkt.InventoryBase;
import joebkt.ItemStack;

public class Inventory extends InventoryBase
{
  final MC_ItemStack noItem = new ItemStackWrapper(null);
  final MC_ItemStack testItem = KFA.srv.createItemStack(20, 4, 0);
  private InventoryHandler handler;
  private List<MC_ItemStack> slots = new ArrayList();
  public InventoryType type;
  public String name;

  public Inventory(InventoryType Type, String Name, InventoryHandler Handler, List<MC_ItemStack> Slots)
  {
    super(Name, Name != null, Type.getSlotNumber());
    this.type = Type;
    this.name = Name;
    if (Handler != null) {
      this.handler = Handler;
      this.handler.inv = this;
    }
    if ((this.type == InventoryType.PlayerInventory) && (Slots.size() == 36)) {
      for (int i = 0; i < 9; i++)
        this.slots.add(this.noItem);
      this.slots.addAll(Slots);
    }
    else if (Slots == null) {
      int slotnum = Type.getSlotNumber();
      for (int i = 0; i < slotnum; i++)
        this.slots.add(this.testItem); 
    }
  }

  public boolean handleOpen() { return (this.handler == null) || (this.handler.onOpen()); }

  public void handleClose() {
    if (this.handler != null)
      this.handler.onClose(); 
  }

  public boolean handleEvent(InventoryEventType t) { return (this.handler == null) || (this.handler.onEvent(t)); }

  public boolean handleClick(int x, int y, InventoryClickType ct, MC_ItemStack item) {
    return (this.handler == null) || (this.handler.onClick(x, y, ct, item));
  }
  public int getSlotId(int x, int y) {
    switch ($SWITCH_TABLE$gyurix$invapi$InventoryType()[this.type.ordinal()]) {
    case 10:
      if ((y != 0) || (x > 2) || (x < 0)) {
        return -1;
      }
      return x;
    case 9:
      return 0;
    case 7:
      if (y == 0)
        return 3;
      if ((y == 1) && (x > -1) && (x < 3))
        return x;
    case 2:
      if ((y < 0) || (y >= this.type.data) || (x < 0) || (x > 8)) {
        return -1;
      }
      return y * 9 + x;
    case 5:
    case 12:
      if ((y < 0) || (y > 2) || (x < 0) || (x > 2)) {
        return -1;
      }
      return y * 3 + x;
    case 6:
      if ((y == 0) && ((x == 0) || (x == 1))) {
        return x;
      }
      return -1;
    case 4:
      if ((y == 0) && (x == 0))
        return 0;
      if (((y == 0) || (y == 1)) && (x == 1))
        return 2;
      if ((y == 1) && (x == 0)) {
        return 1;
      }
      return -1;
    case 11:
      if ((y != 0) || (x < 0) || (x > 4)) {
        return -1;
      }
      return x;
    case 13:
      if ((x == 0) && ((y == 0) || (y == 1))) {
        return y;
      }
      return -1;
    case 8:
      if (y == 0) {
        return x;
      }
      return -1;
    case 1:
      if ((y < 0) || (y > 3) || (x < 0) || (x > 8)) {
        return y * 9 + x;
      }
      return -1;
    case 3:
      if (x == 3)
        return 0;
      if ((y > -1) && (y < 3) && (x > -1) && (x < 3)) {
        return y * 3 + x + 1;
      }
      return -1;
    }
    return -1;
  }

  public int getY(int id) {
    switch ($SWITCH_TABLE$gyurix$invapi$InventoryType()[this.type.ordinal()]) {
    case 9:
    case 10:
      return 0;
    case 7:
      if (id == 3) {
        return 0;
      }
      return 1;
    case 2:
      return id / 9;
    case 5:
    case 12:
      return id / 3;
    case 6:
    case 11:
      return 0;
    case 4:
      if ((id == 0) || (id == 2)) {
        return 0;
      }
      return 1;
    case 13:
      return id;
    case 8:
      return 0;
    case 1:
      return id / 9;
    case 3:
      if (id == 0) {
        return 1;
      }
      return (id - 1) / 3;
    }
    return 0;
  }

  public int getX(int id) {
    switch ($SWITCH_TABLE$gyurix$invapi$InventoryType()[this.type.ordinal()]) {
    case 6:
    case 8:
    case 9:
    case 10:
    case 11:
      return id;
    case 7:
      if (id == 3) {
        return 0;
      }
      return id;
    case 2:
      return id % 9;
    case 5:
      return id % 3;
    case 12:
      return id % 3;
    case 4:
      if (id == 2) {
        return 1;
      }
      return 0;
    case 13:
      return 0;
    case 1:
      return id % 9;
    case 3:
      if (id == 0) {
        return 3;
      }
      return (id - 1) % 3;
    }
    return 0;
  }

  public MC_ItemStack getItem(int x, int y) {
    return (MC_ItemStack)this.slots.get(getSlotId(x, y));
  }
  public MC_ItemStack getItem(int slotid) {
    return (MC_ItemStack)this.slots.get(slotid);
  }
  public ItemStack getRealItem(int slotid) {
    return ((ItemStackWrapper)this.slots.get(slotid)).is;
  }
  public List<ItemStack> getItems() {
    List out = new ArrayList();
    for (MC_ItemStack s : this.slots) {
      out.add(((ItemStackWrapper)s).is);
    }
    return out;
  }
  public void setItem(int x, int y, MC_ItemStack item) {
    this.slots.set(getSlotId(x, y), item);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.Inventory
 * JD-Core Version:    0.6.2
 */