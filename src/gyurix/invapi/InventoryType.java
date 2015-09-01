package gyurix.invapi;

import java.util.ArrayList;
import java.util.List;

public enum InventoryType
{
  PlayerInventory(-1), Chest(0), Crafting_Table(1), Furnace(2), Dispenser(3), Enchanting_Table(4), Brewing_Stand(5), Villager(6), Beacon(7), Anvil(8), 
  Hopper(9), Dropper(10), Horse(11);

  private int id;
  public int data = 3;

  private InventoryType(int i) { this.id = i; }

  public int getId() {
    return this.id;
  }
  public String getOfficialName() {
    if (this == Horse) {
      return "EntityHorse";
    }
    return "minecraft:" + name().toLowerCase();
  }
  public static InventoryType fromName(String in) {
    String str = in; switch (in.hashCode()) { case -1879003021:
      if (str.equals("minecraft:villager"));
      break;
    case -1719356277:
      if (str.equals("minecraft:furnace"));
      break;
    case -1366784614:
      if (str.equals("EntityHorse"));
      break;
    case -1293651279:
      if (str.equals("minecraft:beacon"));
      break;
    case -1150744385:
      if (str.equals("minecraft:anvil"));
      break;
    case -1124126594:
      if (str.equals("minecraft:crafting_table")) break; break;
    case -1112182111:
      if (str.equals("minecraft:hopper"));
      break;
    case 319164197:
      if (str.equals("minecraft:enchanting_table"));
      break;
    case 712019713:
      if (str.equals("minecraft:dropper"));
      break;
    case 1649065834:
      if (str.equals("minecraft:brewing_stand"));
      break;
    case 2090881320:
      if (!str.equals("minecraft:dispenser")) { break label280;

        return Crafting_Table;

        return Furnace;
      } else {
        return Dispenser;

        return Enchanting_Table;

        return Brewing_Stand;

        return Villager;

        return Beacon;

        return Anvil;

        return Hopper;

        return Dropper;

        return Horse;
      }break; }
    label280: return Chest;
  }

  public int getRealSlotNumber() {
    if (this == Beacon)
      return 1;
    if (this == Horse)
      return 2;
    if ((this == Villager) || (this == Furnace))
      return 3;
    if (this == Brewing_Stand)
      return 4;
    if (this == Hopper)
      return 5;
    if ((this == Dispenser) || (this == Dropper))
      return 9;
    if (this == Chest)
      return this.data * 9;
    if (this == PlayerInventory)
      return 45;
    return 0;
  }
  public int getSlotNumber() {
    if (this == Beacon)
      return 1;
    if ((this == Enchanting_Table) || (this == Horse))
      return 2;
    if ((this == Villager) || (this == Furnace) || (this == Anvil))
      return 3;
    if (this == Brewing_Stand)
      return 4;
    if (this == Hopper)
      return 5;
    if ((this == Dispenser) || (this == Dropper))
      return 9;
    if (this == Crafting_Table)
      return 10;
    if (this == Chest)
      return this.data * 9;
    if (this == PlayerInventory)
      return 45;
    return 0;
  }
  public static List<String> valueStrings() {
    List out = new ArrayList();
    for (InventoryType v : values()) {
      out.add(v.name());
    }
    return out;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.InventoryType
 * JD-Core Version:    0.6.2
 */