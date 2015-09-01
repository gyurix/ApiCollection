package gyurix.invapi;

import PluginReference.MC_Player;
import PluginReference.MC_Server;
import WrapperObjects.Entities.PlayerWrapper;
import WrapperObjects.ItemStackWrapper;
import gyurix.konfigfajl.KFA;
import java.io.PrintStream;
import java.util.HashMap;
import joebkt.EntityPlayer;
import joebkt.JSonConverter;
import joebkt.PacketBase;
import joebkt.Packet_CloseWindow;
import joebkt.Packet_IncomingClickWindow;
import joebkt.Packet_IncomingCloseWindow;
import joebkt.Packet_IncomingConfirmTransaction;
import joebkt.Packet_IncomingCreateInventoryAction;
import joebkt.Packet_OpenWindow;
import joebkt.Packet_SetSlot;
import joebkt.Packet_WindowItems;
import joebkt.PlayerConnection;
import joebkt.TextObject;
import org.apache.commons.lang3.StringEscapeUtils;

public class InvAPI
{
  public static HashMap<String, HashMap<Integer, Inventory>> invs = new HashMap();
  public boolean donthandle;

  public InvAPI()
  {
    KFA.srv.registerCommand(new TestInventoryCommand());
  }

  public static boolean handleIncommingPacket(MC_Player plr, PacketBase packet) {
    if ((packet instanceof Packet_IncomingClickWindow)) {
      Packet_IncomingClickWindow p = (Packet_IncomingClickWindow)packet;

      Inventory inv = (Inventory)((HashMap)invs.get(plr.getName())).get(Integer.valueOf(p.a));
      Inventory plinv = (Inventory)((HashMap)invs.get(plr.getName())).get(Integer.valueOf(0));
      int slotid = p.b;
      boolean allow;
      boolean allow;
      if (p.b == -999) {
        allow = inv.handleEvent(InventoryEventType.fromData(p.f, p.c));
      }
      else
      {
        boolean allow;
        if (p.b >= inv.type.getRealSlotNumber()) {
          slotid = p.b - inv.type.getRealSlotNumber() + 9;
          allow = plinv.handleClick(inv.getX(slotid), inv.getY(slotid), InventoryClickType.fromData(p.f, p.c), new ItemStackWrapper(p.e));
        }
        else {
          allow = inv.handleClick(inv.getX(slotid), inv.getY(slotid), InventoryClickType.fromData(p.f, p.c), new ItemStackWrapper(p.e));
        }
      }
      if (!allow) {
        PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
        c.sendPacket(new Packet_SetSlot(0, 0, null));
        c.sendPacket(new Packet_WindowItems(0, plinv.getItems()));
        c.sendPacket(new Packet_SetSlot(p.a, 0, null));
        c.sendPacket(new Packet_WindowItems(p.a, inv.getItems()));
      }
      return !allow;
    }
    if ((packet instanceof Packet_IncomingConfirmTransaction)) {
      Packet_IncomingConfirmTransaction p = (Packet_IncomingConfirmTransaction)packet;
      System.out.println("INCOMING TRANSACTION " + (p.c ? "ALLOWED" : "DENIED") + " window:" + p.a + " action:" + p.b);
    }
    else if ((packet instanceof Packet_IncomingCloseWindow)) {
      Packet_IncomingCloseWindow p = (Packet_IncomingCloseWindow)packet;
      Inventory inv = (Inventory)((HashMap)invs.get(plr.getName())).get(Integer.valueOf(p.a));
      inv.handleClose();
      if (p.a != 0)
        ((HashMap)invs.get(plr.getName())).remove(Integer.valueOf(p.a));
    } else {
      (packet instanceof Packet_IncomingCreateInventoryAction);
    }

    return false;
  }

  public static boolean handleOutcommingPacket(MC_Player plr, PacketBase packet) {
    if ((packet instanceof Packet_OpenWindow)) {
      Packet_OpenWindow p = (Packet_OpenWindow)packet;

      Inventory inv = (Inventory)((HashMap)invs.get(plr.getName())).get(Integer.valueOf(p.a));
      if (inv == null) {
        inv = new Inventory(InventoryType.fromName(p.b), p.c.getText1(), null, null);
        ((HashMap)invs.get(plr.getName())).put(Integer.valueOf(p.a), inv);
      }
      return !inv.handleOpen();
    }
    if ((packet instanceof Packet_CloseWindow)) {
      Packet_CloseWindow p = (Packet_CloseWindow)packet;
      Inventory inv = (Inventory)((HashMap)invs.get(plr.getName())).get(Integer.valueOf(p.a));
      inv.handleClose();
      if (p.a != 0)
        ((HashMap)invs.get(plr.getName())).remove(Integer.valueOf(p.a));
    }
    return false;
  }
  public static Inventory getPlayerInventory(MC_Player plr) {
    return (Inventory)((HashMap)invs.get(plr.getName())).get(Integer.valueOf(0));
  }
  public static void openInventory(MC_Player plr, Inventory inv) {
    PlayerConnection c = ((PlayerWrapper)plr).plr.plrConnection;
    ((HashMap)invs.get(plr.getName())).put(Integer.valueOf(-1), inv);
    c.sendPacket(
      new Packet_OpenWindow(-1, inv.type.getOfficialName(), 
      JSonConverter.getTextObjectFromString("\"" + StringEscapeUtils.escapeJava(inv.name) + "\""), 
      inv.type.getRealSlotNumber(), 
      inv.type == InventoryType.Horse ? inv.type.data : 0));
    c.sendPacket(new Packet_WindowItems(-1, inv.getItems()));
  }
  public static void onPlayerJoin(MC_Player plr) {
    HashMap m = new HashMap();
    m.put(Integer.valueOf(0), new Inventory(InventoryType.PlayerInventory, "Inventory", null, plr.getInventory()));
    invs.put(plr.getName(), m);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.invapi.InvAPI
 * JD-Core Version:    0.6.2
 */