package gyurix.konfigfajl;

import PluginReference.MC_Location;
import com.google.common.primitives.Primitives;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.objenesis.ObjenesisSerializer;

public class ConfigFile
{
  public String charset = "UTF-8";
  private TreeMap<String, String> data = new TreeMap();
  private TreeMap<String, String> comment = new TreeMap();
  public String filename;
  public String get_empty = "\r";
  public String get_na = "\r\r";
  public String list_adress = "<adress>";
  public String list_value = "<value>";
  public String list_format = "<adress>\t<value>";
  public String list_emptyformat = "<adress>\t(empty)";
  public String[] getBoolean_filter = { "\\+" };
  public long getLong_notnumber = -1L;
  public long getLong_empty = -1L;
  public long getLong_na = -1L;
  public int getInt_notnumber = -1;
  public int getInt_empty = -1;
  public int getInt_na = -1;
  public double getDouble_notnumber = (0.0D / 0.0D);
  public double getDouble_empty = (0.0D / 0.0D);
  public double getDouble_na = (0.0D / 0.0D);
  public int getNum_na = -1;
  public int getNum_empty = -1;
  public int getNum_notfound = -1;
  public boolean getBoolean_empty = false;
  public boolean getBoolean_na = false;
  public boolean repair_missing = false;
  public boolean update_parent = false;
  public boolean update_child = false;
  public boolean debug = false;
  public ObjenesisSerializer obj_creator = new ObjenesisSerializer();

  public ConfigFile()
  {
    clear();
  }

  public ConfigFile(String fn)
  {
    this.debug = true;
    load(fn);
  }

  public ConfigFile(InputStream input)
  {
    load(input);
  }

  public ConfigFile(List<String> l1, List<String> l2)
  {
    clear();
    int size = l1.size();
    for (int i = 0; i < size; i++)
      this.data.put((String)l1.get(i), (String)l2.get(i));
  }

  public boolean contains(String adress) {
    return this.data.containsKey(adress);
  }
  public boolean isEmpty() {
    return this.data.isEmpty();
  }

  public ConfigFile childconfig(String adress) {
    return new ConfigFile(adressList(adress), datalist(adress));
  }

  public void insertchild(String adress, ConfigFile kf)
  {
    insertchild(adress, kf, true);
  }

  public void insertchild(String adress, ConfigFile kf, boolean override)
  {
    if (override) {
      remove(adress);
    }
    for (Map.Entry e : kf.data.entrySet())
      set(adress + "." + (String)e.getKey(), e.getValue());
  }

  public boolean load(String fn)
  {
    this.filename = fn;
    return load();
  }

  public boolean load(InputStream input) {
    try {
      byte[] in = new byte[input.available()];
      return mainload(in);
    }
    catch (Throwable e) {
      clear();
    }return false;
  }

  public boolean load()
  {
    clear();
    if (this.filename == null) {
      this.filename = "";
    }
    if (this.filename.isEmpty())
    {
      if (this.debug) {
        System.out.println("[ConfigFile] Error, filename is not given!");
      }
      return false;
    }
    File f = new File(this.filename);
    if (!f.exists())
    {
      if (this.debug) {
        System.out.println("[ConfigFile] Error, the given file (" + this.filename + ") not exists!");
      }
      return false;
    }
    try
    {
      return mainload(Files.readAllBytes(Paths.get(this.filename, new String[0])));
    }
    catch (Throwable h)
    {
      if (this.debug)
      {
        System.out.println("[ConfigFile] Error the give file (" + this.filename + ") is not readable!");
      }
      clear();
    }return false;
  }

  public boolean mainload(byte[] loaddata) throws Throwable {
    String[] sorok = new String(loaddata, Charset.forName(this.charset)).replace("\r", "").split("\n");
    int sh = sorok.length;
    int lvl = -2;
    String adress = "";
    boolean commentmode = true;
    String u = "";
    clear();
    for (int i = 0; i < sh; i++)
    {
      String s = sorok[i];
      int newlvl = 0;
      while (s.startsWith(" "))
      {
        s = s.substring(1);
        newlvl++;
      }
      if (s.startsWith("#"))
      {
        if (!commentmode) {
          this.data.put(adress, u.replace(".:.", ":").replace("\\n", "\n").replace("\\r", "\r"));
          commentmode = true;
          u = s.substring(1);
        }
        else
        {
          u = u + "\n" + s.substring(1);
        }
      }
      else if ((s.contains(": ")) || (s.endsWith(":")))
      {
        if (commentmode)
        {
          this.comment.put(adress, u.replace(".:.", ":").replace("\\n", "\n").replace("\\r", "\r"));
          commentmode = false;
        }
        else
        {
          this.data.put(adress, u.replace(".:.", ":").replace("\\n", "\n").replace("\\r", "\r"));
        }
        String[] newrow = s.split("\\:\\ ", 2);
        if (newrow[0].contains("."))
        {
          if (this.debug) {
            System.out.println("[ConfigFile] Error, the given file (" + this.filename + ") contains adressing error at row " + (i + 1) + "!\ngiven adress: " + newrow[0] + ", rule: any adress in ConfigFile mustn't contain dot!\n" + "Because this error, can break the whole adressing system, the loading was canceled!");
          }
          return false;
        }
        if (newrow.length == 1)
        {
          u = "\r";
          newrow[0] = newrow[0].substring(0, newrow[0].length() - 1);
        }
        else {
          u = newrow[1];
        }
        if (newlvl == lvl)
        {
          if (adress.contains("."))
            adress = adress.substring(0, adress.lastIndexOf(".") + 1) + newrow[0];
          else {
            adress = newrow[0];
          }
        }
        else if (newlvl == lvl + 2)
        {
          if (!adress.isEmpty())
            adress = adress + "." + newrow[0];
          else {
            adress = newrow[0];
          }
        }
        else
        {
          if (newlvl > lvl + 2)
          {
            if (this.debug) {
              System.out.println(
                "[ConfigFile] Error the given file (" + this.filename + ") contains blockleveling error at line " + (i + 1) + "\nprevious block level: " + lvl + ", new block level: " + newlvl + ", rule: between each block must be 2 level difference!\n" + "Because, this is a critical error, which can break the block leveling system, the ConfigFile loading was canceled!");
            }
            return false;
          }
          if (newlvl < lvl) {
            if (newlvl % 2 == 0)
            {
              if (newlvl == 0)
                adress = newrow[0];
              else {
                adress = StringUtils.join(adress.split("\\."), ".", 0, newlvl / 2) + "." + newrow[0];
              }
            }
            else
            {
              if (this.debug)
                System.out.println(
                  "[ConfigFile] Error the given file (" + this.filename + ") contains blockleveling error at line " + (i + 1) + "\nprevious block level: " + lvl + ", new block level: " + newlvl + ", rule: every block level must be pair number!\n" + "Because, this is a critical error, which can break the block leveling system, the ConfigFile loading was canceled!");
              return false;
            }
          }
        }
        lvl = newlvl;
      }
      else
      {
        u = u + "\n" + s;
      }
    }
    if (commentmode)
    {
      this.comment.put(adress, u.replace(".:.", ":").replace("\\n", "\n"));
      commentmode = false;
    }
    else
    {
      this.data.put(adress, u.replace(".:.", ":").replace("\\n", "\n").replace("\\r", "\r"));
    }
    return true;
  }

  public boolean save(String fn) {
    this.filename = fn;
    return save();
  }

  public boolean save()
  {
    if (this.filename == null) {
      this.filename = "";
    }
    if (this.filename.isEmpty())
    {
      if (this.debug) {
        System.out.println("[ConfigFile] Error, the filename isn't given!");
      }
      return false;
    }
    File f = new File(this.filename);
    try
    {
      f.createNewFile();
      PrintWriter fi = new PrintWriter(f, this.charset);
      if (!((String)this.comment.get("")).isEmpty()) {
        fi.println("#" + ((String)this.comment.get("")).replace(":", ".:.").replace("\n ", "\\n ").substring(1));
      }
      for (Map.Entry e : this.data.entrySet())
      {
        String[] c = ((String)e.getKey()).split("\\.");
        String mainAdress = c[(c.length - 1)];
        if (((String)e.getValue()).equals("\r"))
          fi.println(StringUtils.leftPad(mainAdress, (c.length - 1) * 2 + mainAdress.length(), " ") + ":");
        else {
          fi.println(StringUtils.leftPad(mainAdress, (c.length - 1) * 2 + mainAdress.length(), " ") + ": " + 
            ((String)e.getValue()).replace(":", ".:.").replace("\n ", "\\n ").replace("\r", "\\r"));
        }
        if (this.comment.containsKey(e.getKey())) {
          fi.println("#" + ((String)this.comment.get(e.getKey())).replace(":", ".:.").replace("\n ", "\\n ").replace("\r", "\\r"));
        }
      }
      fi.close();
    }
    catch (Throwable h)
    {
      if (this.debug) {
        System.out.println("[ConfigFile] Error, creating the given file (" + this.filename + ") was unsuccesful!");
        h.printStackTrace();
      }
      return false;
    }
    return true;
  }

  public int set(String adress, Object ertek) {
    this.data.put(adress, String.valueOf(ertek));
    int out = 0;
    while (adress.contains(".")) {
      adress = adress.substring(0, adress.lastIndexOf("."));
      if (!this.data.containsKey(adress)) {
        this.data.put(adress, "\r");
        out++;
      }
    }
    return out;
  }

  public int setlocation(String adress, MC_Location ertek)
  {
    return set(adress, ertek.x + " " + ertek.y + " " + ertek.z + " " + ertek.dimension + " " + ertek.yaw + " " + ertek.pitch);
  }

  public boolean modify(String adress, Object value)
  {
    if (this.data.containsKey(adress)) {
      this.data.put(adress, String.valueOf(value));
      return true;
    }
    return false;
  }

  public void clear() {
    this.data = new TreeMap();
    this.comment = new TreeMap();
    this.comment.put("", "");
  }

  public boolean remove(String adress)
  {
    if ((adress == null) || (adress.isEmpty())) {
      clear();
      return true;
    }
    if (!this.data.containsKey(adress)) {
      return false;
    }
    this.data.remove(adress);
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      this.data.remove(e.getKey());
      e = this.data.higherEntry(adress);
    }
    return true;
  }

  public String get(String adress)
  {
    return get(adress, this.get_empty, this.get_na);
  }

  public String get(String adress, String emptynotfound)
  {
    return get(adress, emptynotfound, emptynotfound);
  }

  public String get(String adress, String empty, String notfound) {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if (this.repair_missing)
        set(adress, notfound);
      return notfound;
    }
    if (s.equals("\r"))
      return empty;
    return s;
  }

  public long getLong(String adress) {
    return getLong(adress, this.getLong_notnumber, this.getLong_empty, this.getLong_na);
  }

  public long getLong(String adress, long bad) {
    return getLong(adress, bad, bad, bad);
  }

  public long getLong(String adress, long notlong, long emptyna) {
    return getLong(adress, notlong, emptyna, emptyna);
  }

  public long getLong(String adress, long notlong, long empty, long notfound) {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if (this.repair_missing)
        set(adress, Long.valueOf(notfound));
      return notfound;
    }
    if (s.equals("\r"))
      return empty;
    try {
      return Long.valueOf(s).longValue();
    } catch (Throwable e) {
    }
    return notlong;
  }

  private int getInt(String adress) {
    return getInt(adress, this.getInt_notnumber, this.getInt_empty, this.getInt_na);
  }
  public int getInt(String adress, int bad) {
    return getInt(adress, bad, bad, bad);
  }
  public int getInt(String adress, int notlong, int emptyna) {
    return getInt(adress, notlong, emptyna, emptyna);
  }
  public int getInt(String adress, int notlong, int empty, int notint) {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if (this.repair_missing)
        set(adress, Integer.valueOf(notint));
      return notint;
    }
    if (s.equals("\r"))
      return empty;
    try {
      return Integer.valueOf(s).intValue();
    } catch (Throwable e) {
    }
    return notlong;
  }

  public void setObject(String adress, Object obj) {
    remove(adress);
    try {
      if (obj == null) {
        set(adress + ".$class", "null");
        return;
      }
      Class c = obj.getClass();
      c = Primitives.unwrap(c);
      set(adress + ".$class", c.getName());
      if ((c == Boolean.class) || (c == Boolean.TYPE)) {
        set(adress, ((Boolean)obj).booleanValue() ? "+" : "-");
      }
      else if (c.isPrimitive()) {
        set(adress, obj);
      }
      else if (c.isArray()) {
        Object a = obj;
        int l = Array.getLength(a);
        for (int i = 0; i < l; i++)
          setObject(adress + "." + i, Array.get(a, i));
      }
      else
      {
        Object o;
        if (Collection.class.isAssignableFrom(c)) {
          int i = 0;
          for (Iterator it = ((Collection)obj).iterator(); it.hasNext(); ) {
            o = it.next();
            if ((o == null) && (!it.hasNext()))
              return;
            setObject(adress + "." + i, o);
            i++;
          }
        }
        else if (Map.class.isAssignableFrom(c)) {
          int i = 0;
          set(adress, Integer.valueOf(((Map)obj).size()));
          for (Map.Entry e : ((Map)obj).entrySet()) {
            setObject(adress + "." + i, e.getValue());
            setObject(adress + "." + i + ".$key", e.getKey());
            i++;
          }
        }
        else
        {
          StringBuilder out;
          long l;
          if (BitSet.class.isAssignableFrom(c)) {
            BitSet bs = (BitSet)obj;
            out = new StringBuilder();
            for (l : bs.toLongArray()) {
              out.append(" " + l);
            }
            set(adress, out.substring(1));
          }
          else if (c.getName().startsWith("java.")) {
            set(adress, obj.toString());
          }
          else
          {
            Field[] arrayOfField;
            l = (arrayOfField = obj.getClass().getDeclaredFields()).length; for (out = 0; out < l; out++) { Field f = arrayOfField[out];
              f.setAccessible(true);
              setObject(adress + "." + f.getName(), f.get(obj)); }
          }
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public Object getObject(String adress) { return getObject(adress, null); }

  public Object getObject(String adress, Object obj) {
    try {
      String cn = get(adress + ".$class", "null");
      if (cn.equals("null"))
        return null;
      if (ArrayUtils.contains(KFA.primitives, cn))
        return KFA.stringToPrimitive(get(adress, "0"), cn);
      Class c = Class.forName(get(adress + ".$class"));
      if (obj == null)
        if (c.getName().startsWith("java.")) {
          try {
            obj = c.newInstance();
          }
          catch (Throwable e) {
            obj = this.obj_creator.newInstance(c);
          }
        }
        else
          obj = this.obj_creator.newInstance(c);
      int i;
      if (c.isArray()) {
        Class ac = c.getComponentType();
        int l = mainAdressList(adress).size();
        Object ar = Array.newInstance(ac, l);
        for (i = 0; i < l; i++) {
          Array.set(ar, i, getObject(adress + "." + i));
        }
      }
      else if (Collection.class.isAssignableFrom(c)) {
        c.getMethod("clear", new Class[0]).invoke(obj, new Object[0]);
        Method m = c.getMethod("add", new Class[] { Object.class });
        int l = mainAdressList(adress).size();
        for (int i = 0; i < l; i++) {
          m.invoke(obj, new Object[] { getObject(adress + "." + i) });
        }
      }
      else if (Map.class.isAssignableFrom(c)) {
        int l = getInt(adress);
        c.getMethod("clear", new Class[0]).invoke(obj, new Object[0]);
        Method m = c.getMethod("put", new Class[] { Object.class, Object.class });
        for (int i = 0; i < l; i++)
          m.invoke(obj, new Object[] { getObject(adress + "." + i + ".$key"), getObject(adress + "." + i) });
      }
      else {
        if (BitSet.class.isAssignableFrom(c)) {
          String[] sl = get(adress).split(" ");
          l = new long[sl.length];
          for (i = 0; i < sl.length; i++) {
            l[i] = Long.valueOf(sl[i]).longValue();
          }
          return BitSet.valueOf(l);
        }
        if (c.getName().startsWith("java.")) {
          return c.getConstructor(new Class[] { String.class }).newInstance(new Object[] { get(adress) });
        }

        int i = (i = c.getDeclaredFields()).length; for (long[] l = 0; l < i; l++) { Field f = i[l];
          try {
            f.setAccessible(true);
            Object obj2 = f.get(obj);
            f.set(obj, getObject(adress + "." + f.getName(), obj2));
          }
          catch (Throwable e) {
            e.printStackTrace();
          } }
      }
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
    return obj;
  }

  public double getDouble(String adress)
  {
    return getDouble(adress, this.getDouble_notnumber, this.getDouble_empty, this.getDouble_na);
  }

  public double getDouble(String adress, double bad)
  {
    return getDouble(adress, bad, bad, bad);
  }

  public double getDouble(String adress, double notlong, double emptyna)
  {
    return getDouble(adress, notlong, emptyna, emptyna);
  }

  public double getDouble(String adress, double notlong, double empty, double notfound) {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if (this.repair_missing)
        set(adress, Double.valueOf(notfound));
      return notfound;
    }
    if (s.equals("\r"))
      return empty;
    try {
      return Double.valueOf(s).doubleValue();
    } catch (Throwable e) {
    }
    return notlong;
  }

  public boolean getBoolean(String adress)
  {
    return getBoolean(adress, this.getBoolean_filter, this.getBoolean_empty, this.getBoolean_na);
  }

  public boolean getBoolean(String adress, boolean emptyna)
  {
    return getBoolean(adress, this.getBoolean_filter, emptyna, emptyna);
  }

  public boolean getBoolean(String adress, boolean empty, boolean na)
  {
    return getBoolean(adress, this.getBoolean_filter, empty, na);
  }

  public boolean getBoolean(String adress, String[] filter)
  {
    return getBoolean(adress, filter, this.getBoolean_empty, this.getBoolean_na);
  }

  public boolean getBoolean(String adress, String[] filter, boolean emptyna)
  {
    return getBoolean(adress, filter, emptyna, emptyna);
  }

  public boolean getBoolean(String adress, String[] filter, boolean empty, boolean na)
  {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if (this.repair_missing)
        set(adress, na ? "+" : "-");
      return na;
    }
    if (s.equals("\r"))
      return empty;
    for (String sz : filter) {
      if (s.matches(sz)) {
        return true;
      }
    }
    return false;
  }

  public MC_Location getLocation(String adress) {
    return getLocation(adress, null, null, null);
  }

  public MC_Location getLocation(String adress, MC_Location bad)
  {
    return getLocation(adress, bad, bad, bad);
  }

  public MC_Location getLocation(String adress, MC_Location notlocation, MC_Location emptyna)
  {
    return getLocation(adress, notlocation, emptyna, emptyna);
  }

  public MC_Location getLocation(String adress, MC_Location notlocation, MC_Location empty, MC_Location na)
  {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if ((this.repair_missing) && (na != null))
        setlocation(adress, na);
      return na;
    }
    if (s.equals("\r"))
      return empty;
    String[] a = s.split("\\ ");
    try {
      return new MC_Location(Double.valueOf(a[0]).doubleValue(), 
        Double.valueOf(a[1]).doubleValue(), 
        Double.valueOf(a[2]).doubleValue(), 
        a.length >= 4 ? Integer.valueOf(a[3]).intValue() : 0, 
        a.length >= 5 ? Float.valueOf(a[4]).floatValue() : 0.0F, 
        a.length >= 6 ? Float.valueOf(a[5]).floatValue() : 0.0F);
    } catch (Throwable e) {
    }
    return notlocation;
  }

  public int getNum(String adress, String[] lh)
  {
    return getNum(adress, this.getNum_notfound, this.getNum_empty, this.getNum_na, lh);
  }

  public int getNum(String adress, int bad, String[] lh)
  {
    return getNum(adress, bad, bad, bad, lh);
  }

  public int getNum(String adress, int notfound, int emptyna, String[] lh)
  {
    return getNum(adress, notfound, emptyna, emptyna, lh);
  }

  public int getNum(String adress, int notfound, int empty, int na, String[] lh)
  {
    String s = (String)this.data.get(adress);
    if (s == null) {
      if (this.repair_missing)
        set(adress, Integer.valueOf(na));
      return na;
    }
    if (s.equals("\r"))
      return empty;
    int id = KFA.search(lh, s);
    return id == -1 ? notfound : id;
  }

  public List<String> adressList()
  {
    return new ArrayList(this.data.keySet());
  }

  public List<String> adressList(String adress) {
    if ((adress == null) || (adress.isEmpty())) {
      return adressList();
    }
    List l = new ArrayList();
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      l.add(((String)e.getKey()).substring(adress.length()));
      e = this.data.higherEntry((String)e.getKey());
    }
    return l;
  }

  public List<String> datalist()
  {
    return new ArrayList(this.data.values());
  }

  public List<String> datalist(String adress)
  {
    if (adress == null) {
      adress = "";
    }
    if (adress.isEmpty()) {
      return adressList();
    }
    List l = new ArrayList();
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      l.add((String)e.getValue());
      e = this.data.higherEntry((String)e.getKey());
    }
    return l;
  }

  public List<String> list() {
    List l = new ArrayList();
    for (Map.Entry e : this.data.entrySet()) {
      l.add(((String)e.getValue()).equals("\r") ? 
        this.list_emptyformat.replace(this.list_adress, (CharSequence)e.getKey()).replace(this.list_value, this.get_empty) : 
        this.list_format.replace(this.list_adress, (CharSequence)e.getKey()).replace(this.list_value, (CharSequence)e.getValue()));
    }
    return l;
  }

  public List<String> list(String adress) {
    return list(adress, this.list_format, this.list_emptyformat);
  }

  public List<String> list(String adress, String format)
  {
    return list(adress, format, format);
  }

  public List<String> list(String adress, String format, String emptyformat)
  {
    if ((adress == null) || (adress.isEmpty())) {
      return adressList();
    }
    List l = new ArrayList();
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      l.add(((String)e.getValue()).equals("\r") ? 
        this.list_emptyformat.replace(this.list_adress, ((String)e.getKey()).substring(adress.length())).replace(this.list_value, this.get_empty) : 
        this.list_format.replace(this.list_adress, ((String)e.getKey()).substring(adress.length())).replace(this.list_value, (CharSequence)e.getValue()));
      e = this.data.higherEntry((String)e.getKey());
    }
    return l;
  }

  public List<String> mainAdressList() {
    List l = new ArrayList();
    for (String s : this.data.keySet()) {
      if (s.indexOf(".") == -1) {
        l.add(s);
      }
    }
    return l;
  }

  public List<String> mainAdressList(String adress)
  {
    if ((adress == null) || (adress.isEmpty())) {
      return mainAdressList();
    }
    List l = new ArrayList();
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      String newadr = ((String)e.getKey()).substring(adress.length());
      if (!newadr.contains("."))
        l.add(newadr);
      e = this.data.higherEntry((String)e.getKey());
    }
    return l;
  }

  public List<String> mainDataList()
  {
    List l = new ArrayList();
    for (Map.Entry e : this.data.entrySet()) {
      if (!((String)e.getKey()).contains(".")) {
        l.add((String)e.getValue());
      }
    }
    return l;
  }

  public List<String> mainDataList(String adress)
  {
    if ((adress == null) || (adress.isEmpty())) {
      return mainDataList();
    }
    List l = new ArrayList();
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      String newadr = ((String)e.getKey()).substring(adress.length());
      if (!newadr.contains("."))
        l.add((String)e.getValue());
      e = this.data.higherEntry((String)e.getKey());
    }
    return l;
  }

  public List<String> mainList()
  {
    List l = new ArrayList();
    for (Map.Entry e : this.data.entrySet()) {
      if (!((String)e.getKey()).contains(".")) {
        l.add(((String)e.getValue()).equals("\r") ? 
          this.list_emptyformat.replace(this.list_adress, (CharSequence)e.getKey()).replace(this.list_value, this.get_empty) : 
          this.list_format.replace(this.list_adress, (CharSequence)e.getKey()).replace(this.list_value, (CharSequence)e.getValue()));
      }
    }
    return l;
  }

  public List<String> mainList(String adress)
  {
    return mainList(adress, this.list_format, this.list_emptyformat);
  }

  public List<String> mainList(String adress, String format)
  {
    return mainList(adress, format, format);
  }

  public List<String> mainList(String adress, String format, String emptyformat)
  {
    if ((adress == null) || (adress.isEmpty())) {
      return mainList();
    }
    List l = new ArrayList();
    adress = adress + ".";
    Map.Entry e = this.data.higherEntry(adress);
    while ((e != null) && (((String)e.getKey()).startsWith(adress))) {
      String newadr = ((String)e.getKey()).substring(adress.length());
      if (!newadr.contains("."))
        l.add(((String)e.getValue()).equals("\r") ? 
          this.list_emptyformat.replace(this.list_adress, newadr).replace(this.list_value, this.get_empty) : 
          this.list_format.replace(this.list_adress, newadr).replace(this.list_value, (CharSequence)e.getValue()));
      e = this.data.higherEntry((String)e.getKey());
    }
    return l;
  }

  public void deleteconfig() {
    this.comment = null;
    this.data = null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.konfigfajl.ConfigFile
 * JD-Core Version:    0.6.2
 */