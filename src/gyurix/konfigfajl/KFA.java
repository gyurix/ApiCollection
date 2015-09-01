package gyurix.konfigfajl;

import PluginReference.MC_DirectionNESWUD;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.PluginBase;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Hex;

public class KFA
{
  public static LangFile lf;
  public static MC_Server srv;
  public static String dir;
  public static PluginBase pl;
  public static Gson gs = new Gson();
  public static final String[] primitives = { "byte", "short", "int", "long", "float", "double", "char", "boolean" };

  public static void log(Object msg) { srv.log(msg); }

  public static String itemtostr(MC_ItemStack it) {
    try {
      StringBuffer result = new StringBuffer();
      for (byte b : it.serialize()) {
        result.append(String.format("%02X", new Object[] { Byte.valueOf(b) }));
      }
      return result.toString();
    }
    catch (Throwable e) {
      it = srv.createItemStack(0, 0, 0);
      StringBuffer result = new StringBuffer();
      for (byte b : it.serialize()) {
        result.append(String.format("%02X", new Object[] { Byte.valueOf(b) }));
      }
      return result.toString();
    }
  }

  public static Object stringToPrimitive(String str, String prn)
  {
    String str;
    switch ((str = prn).hashCode()) { case -1325958191:
      if (str.equals("double"));
      break;
    case 104431:
      if (str.equals("int"));
      break;
    case 3039496:
      if (str.equals("byte")) break; break;
    case 3052374:
      if (str.equals("char"));
      break;
    case 3327612:
      if (str.equals("long"));
      break;
    case 64711720:
      if (str.equals("boolean"));
      break;
    case 97526364:
      if (str.equals("float"));
      break;
    case 109413500:
      if (!str.equals("short")) { break label235;

        return Byte.valueOf(str);
      } else {
        return Short.valueOf(str);

        return Integer.valueOf(str);

        return Long.valueOf(str);

        return Float.valueOf(str);

        return Double.valueOf(str);

        return Character.valueOf(str.charAt(0));

        if (str.charAt(0) == '+') return Boolean.valueOf(true); return Boolean.valueOf(false);
      }break; }
    label235: return null;
  }

  public static MC_ItemStack itemfromstr(String in) {
    try {
      return srv.createItemStack(Hex.decodeHex(in.toCharArray()));
    }
    catch (Throwable e) {
      e.printStackTrace();
    }return null;
  }

  public static String invtostr(List<MC_ItemStack> its)
  {
    String out = "";
    for (MC_ItemStack it : its) {
      out = out + "\n" + itemtostr(it);
    }
    return out.substring(1);
  }
  public static List<MC_ItemStack> invfromstr(String strs) {
    String[] st = strs.split("\n");
    List its = new ArrayList();
    for (String s : st) {
      its.add(itemfromstr(s));
    }
    return its;
  }
  public static int search(Object[] obja, Object obj) {
    int objal = obja.length;
    for (int id = 0; id < objal; id++) {
      if ((obja[id] != null) && (obja[id].equals(obj))) {
        return id;
      }
    }
    return -1;
  }
  public static InputStream getFileStream(PluginBase pl, String filename) {
    return pl.getClass().getResourceAsStream("/" + filename);
  }
  public static String getPluginDir(PluginBase pl) {
    String dir = pl.getClass().getName();
    dir = "plugins_mod" + File.separator + dir.substring(0, dir.indexOf("."));
    try {
      Files.createDirectories(new File(dir).toPath(), new FileAttribute[0]);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return dir;
  }

  public static String fileCopy(PluginBase pl, String fileName, boolean overwrite) {
    String dir = getPluginDir(pl);
    File file = new File(dir + "/" + fileName);
    if ((!overwrite) && (file.exists())) return dir;
    InputStream stream = pl.getClass().getResourceAsStream("/" + fileName);
    if (stream == null) return dir;
    OutputStream resStreamOut = null;
    byte[] buffer = new byte[4096];
    try
    {
      file.createNewFile();
      resStreamOut = new FileOutputStream(file);
      int readBytes;
      while ((readBytes = stream.read(buffer)) > 0)
      {
        int readBytes;
        resStreamOut.write(buffer, 0, readBytes);
      }
      stream.close();
      resStreamOut.close();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
    return dir;
  }
  public static String l(MC_Player plr, String adr) {
    return lf.get(plr, adr);
  }
  public static Object getField(Object obj, String fieldName) {
    try {
      Field f = obj.getClass().getDeclaredField(fieldName);
      f.setAccessible(true);
      Object o = f.get(obj);
      f.setAccessible(false);
      return o;
    }
    catch (Throwable h) {
      h.printStackTrace();
    }return null;
  }

  public static Object getStaticField(Class<?> cls, String fieldName) {
    try {
      Field f = cls.getDeclaredField(fieldName);
      f.setAccessible(true);
      Object o = f.get(null);
      f.setAccessible(false);
      return o;
    } catch (Throwable h) {
    }
    return null;
  }

  public static boolean setField(Object obj, String fieldName, Object field) {
    try {
      Field f = obj.getClass().getDeclaredField(fieldName);
      f.setAccessible(true);
      f.set(obj, field);
      f.setAccessible(false);
      return true;
    } catch (Throwable h) {
    }
    return false;
  }

  public static List<String> tabFilter(String starting, List<String> list) {
    if (starting.isEmpty())
      return list;
    starting = starting.toLowerCase();
    List out = new ArrayList();
    for (String s : list) {
      if (s.toLowerCase().startsWith(starting)) {
        out.add(s);
      }
    }
    return out;
  }
  public static boolean setStaticField(Class<?> cls, String fieldName, Object field) {
    try {
      Field f = cls.getDeclaredField(fieldName);
      f.setAccessible(true);
      f.set(null, field);
      f.setAccessible(false);
      return true;
    } catch (Throwable h) {
    }
    return false;
  }

  public static MC_DirectionNESWUD getLocationDirection(MC_Location loc) {
    if (loc.yaw > 0.0F)
      loc.yaw -= 360.0F;
    if (loc.pitch > 60.0F) return MC_DirectionNESWUD.DOWN;
    if (loc.pitch < -60.0F) return MC_DirectionNESWUD.UP;
    if ((loc.yaw < -315.0F) && (loc.yaw >= -45.0F)) return MC_DirectionNESWUD.SOUTH;
    if ((loc.yaw < -45.0F) && (loc.yaw >= -135.0F)) return MC_DirectionNESWUD.EAST;
    if ((loc.yaw < -135.0F) && (loc.yaw >= -225.0F)) return MC_DirectionNESWUD.NORTH;
    if ((loc.yaw < -225.0F) && (loc.yaw >= -315.0F)) return MC_DirectionNESWUD.WEST;
    return MC_DirectionNESWUD.UNSPECIFIED;
  }
  public static MC_DirectionNESWUD negateDirection(MC_DirectionNESWUD dir) {
    if (dir == MC_DirectionNESWUD.UP) return MC_DirectionNESWUD.DOWN;
    if (dir == MC_DirectionNESWUD.DOWN) return MC_DirectionNESWUD.UP;
    if (dir == MC_DirectionNESWUD.NORTH) return MC_DirectionNESWUD.SOUTH;
    if (dir == MC_DirectionNESWUD.SOUTH) return MC_DirectionNESWUD.NORTH;
    if (dir == MC_DirectionNESWUD.EAST) return MC_DirectionNESWUD.WEST;
    if (dir == MC_DirectionNESWUD.WEST) return MC_DirectionNESWUD.EAST;
    return MC_DirectionNESWUD.UNSPECIFIED;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.konfigfajl.KFA
 * JD-Core Version:    0.6.2
 */