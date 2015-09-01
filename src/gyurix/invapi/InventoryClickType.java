package gyurix.invapi;

import java.io.PrintStream;

public enum InventoryClickType
{
  LeftMouse(0, 0), RightMouse(0, 1), ShiftLeftMouse(1, 0), ShiftRightMouse(1, 1), Number(2, 0), MiddleClick(3, 2), DropKey(4, 0), CtrlDropKey(4, 1), 
  LeftDrag(5, 1), RightDrag(5, 5), DoubleClick(6, 0);

  private int id;
  private int mode;

  private InventoryClickType(int m, int i) { this.mode = m;
    this.id = i; }

  public int getMode() {
    return this.mode;
  }
  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    if ((this == Number) && (id < 9) && (id > 0))
      this.id = id; 
  }

  public static InventoryClickType fromData(int i, int m) { switch (i) {
    case 0:
      if (m == 0)
        return LeftMouse;
      if (m == 1)
        return RightMouse;
      break;
    case 1:
      if (m == 0)
        return ShiftLeftMouse;
      if (m == 1)
        return ShiftRightMouse;
      break;
    case 2:
      if ((m > -1) && (m < 9)) {
        InventoryClickType out = Number;
        out.mode = m;
        return out;
      }
      break;
    case 3:
      if (m == 2)
        return MiddleClick;
      break;
    case 4:
      if (m == 0)
        return DropKey;
      if (m == 1)
        return CtrlDropKey;
      break;
    case 5:
      if (m == 1)
        return LeftDrag;
      if (m == 5)
        return RightDrag;
      break;
    case 6:
      if (m == 0)
        return DoubleClick;
      break;
    }
    System.out.println("[InventoryAPI] Error on handling getting inventory click type from data: i=" + i + ", m=" + m);
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.InventoryClickType
 * JD-Core Version:    0.6.2
 */