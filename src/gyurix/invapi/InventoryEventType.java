package gyurix.invapi;

import java.io.PrintStream;

public enum InventoryEventType
{
  LeftClickOutside(4, 0), RightClickOutside(4, 1), StartLeftDrag(5, 0), StartRightDrag(5, 4), EndLeftDrag(5, 2), EndRightDrag(5, 6), 
  LeftClickOutsideWithItem(0, 0), RightClickOutsideWithItem(1, 0), MiddleClickOutside(2, 3);

  private int mode;
  private int id;

  private InventoryEventType(int m, int i) { this.mode = m;
    this.id = i; }

  public int getMode() {
    return this.mode;
  }
  public int getId() {
    return this.id;
  }
  public static InventoryEventType fromData(int i, int m) {
    switch (m) {
    case 0:
      if (i == 0)
        return LeftClickOutsideWithItem;
      if (i == 4)
        return LeftClickOutside;
      if (i == 5)
        return StartLeftDrag;
      break;
    case 1:
      if (i == 0)
        return RightClickOutsideWithItem;
      if (i == 4)
        return RightClickOutside;
      break;
    case 2:
      if (i == 3)
        return MiddleClickOutside;
      if (i == 5)
        return EndLeftDrag;
      if (i == 5)
        break;
    case 4:
      if (i == 5)
        return StartRightDrag;
      break;
    case 6:
      if (i == 5)
        return EndRightDrag; break;
    case 3:
    case 5:
    }System.out.println("[InventoryAPI] Error on handling getting inventory event type from data: i=" + i + ", m=" + m);
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.InventoryEventType
 * JD-Core Version:    0.6.2
 */