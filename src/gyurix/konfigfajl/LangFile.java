package gyurix.konfigfajl;

import PluginReference.MC_Player;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import gyurix.permissions.PermApi;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ArrayUtils;

public class LangFile
{
  public String filename;
  public String charset = "UTF-8";
  public boolean debug = false;
  public boolean autosave = false;
  public List<String> langs = new ArrayList();
  public List<String[]> list1 = new ArrayList();
  public List<String[]> list2 = new ArrayList();
  ScheduledExecutorService sch = Executors.newScheduledThreadPool(1);

  public LangFile(String fn)
  {
    this.filename = fn;
    load();
  }

  public LangFile(InputStream stream)
  {
    try {
      byte[] data = new byte[stream.available()];
      stream.read(data);
      mainload(data);
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public boolean load()
  {
    if (this.filename == null) {
      this.filename = "";
    }
    if (this.filename.isEmpty())
    {
      if (this.debug) System.out.println("[LangFile] Error, the filename isn't given!");
      return false;
    }
    File f = new File(this.filename);
    if (!f.exists())
    {
      if (this.debug) System.out.println("[LangFile] Error the given file (§f" + this.filename + ") not exists!");
      return false;
    }
    try
    {
      mainload(Files.toByteArray(f));
    }
    catch (Throwable h)
    {
      if (this.debug)
      {
        System.out.println("[LangFile] Error the given file (§f" + this.filename + ") isn't readable!");
        h.printStackTrace();
      }
      return false;
    }
    return true;
  }
  public void mainload(byte[] data) throws Throwable {
    String[] lines = (new String(data, this.charset).replace("\r", "") + "\nvege").split("\\n");
    List l1 = new ArrayList(); List l2 = new ArrayList();
    String lng = "default";
    for (String s : lines)
      if (!s.startsWith("#"))
      {
        String[] sa = s.split("\\:", 2);
        if (sa.length == 1)
        {
          int size = l1.size();
          if (size > 0)
          {
            String[] t1 = new String[size];
            l1.toArray(t1);
            String[] t2 = new String[size];
            l2.toArray(t2);
            this.langs.add(lng);
            this.list1.add(t1);
            this.list2.add(t2);
            l1.clear();
            l2.clear();
          }
          lng = s;
        }
        else if (sa.length == 2)
        {
          l1.add(sa[0]);
          l2.add(sa[1].replace("\\n", "\n"));
        }
      }
  }

  public boolean save() {
    this.autosave = false;
    if ((this.filename == null) || (this.filename.isEmpty())) {
      if (this.debug) System.out.println("[LangFile] Error, the filename isn't given!");
      return false;
    }
    File f = new File(this.filename);
    try {
      f.createNewFile();
      int langnum = this.langs.size();
      PrintWriter pw = new PrintWriter(f, this.charset);
      for (int id = 0; id < langnum; id++) {
        pw.println((String)this.langs.get(id));
        String[] l1 = (String[])this.list1.get(id);
        String[] l2 = (String[])this.list2.get(id);
        int maxl = l1.length;
        for (int i = 0; i < maxl; i++) {
          pw.println(l1[i] + ":" + l2[i].replace("\n", "\\n"));
        }
      }
      pw.close();
    }
    catch (Throwable h) {
      if (this.debug)
      {
        System.out.println("[LangFile] Error the given file (§f" + this.filename + ") isn't writeable!");
        h.printStackTrace();
      }
      return false;
    }
    return true;
  }
  public void autosave(int secs) {
    if (!this.autosave) {
      this.sch.schedule(new Runnable()
      {
        public void run() {
          if (LangFile.this.autosave)
            LangFile.this.save();
        }
      }
      , secs, TimeUnit.SECONDS);
      this.autosave = true;
    }
  }

  public void insert(LangFile lf) { autosave(5);
    insert(lf, false); }

  public void insert(LangFile lf, boolean forcesave) {
    int langnum = lf.langs.size();
    for (int lid = 0; lid < langnum; lid++) {
      String lng = (String)lf.langs.get(lid);
      int id = this.langs.indexOf(lng);
      if (id != -1) {
        String[] a = (String[])this.list1.get(id);
        String[] b = (String[])this.list2.get(id);
        String[] c = (String[])lf.list1.get(lid);
        String[] d = (String[])lf.list2.get(lid);
        for (int i = 0; i < c.length; i++) {
          int sid = Arrays.binarySearch(a, c[i]);
          if (sid > -1) {
            b[sid] = d[i];
            c = (String[])ArrayUtils.remove(c, i);
            d = (String[])ArrayUtils.remove(d, i);
            i--;
          }
        }
        String[] t = (String[])ArrayUtils.addAll(a, c);
        this.list1.set(id, t);
        t = (String[])ArrayUtils.addAll(b, d);
        this.list2.set(id, t);
        sort(id);
      }
      else {
        this.langs.add(lng);
        this.list1.add((String[])lf.list1.get(lid));
        this.list2.add((String[])lf.list2.get(lid));
      }
    }
    if (forcesave) save();
  }

  public String get(String adress)
  {
    int id = Arrays.binarySearch((Object[])this.list1.get(0), adress);
    if (id > -1) {
      return ((String[])this.list2.get(0))[id];
    }
    if (this.debug) {
      System.out.println("[LangFile] Error, the given adress (" + adress + ") isn't found in " + this.filename + " language file's default language!");
    }
    return adress;
  }

  public String get(String adress, String lang)
  {
    int langid = this.langs.indexOf(lang);
    if (langid == -1) {
      if (this.debug) {
        System.out.println("[LangFile] Error, the given language (" + lang + ") isn't found in " + this.filename + " language file, that's why the default language will be used instead of it!");
      }
      langid = 0;
    }
    int id = Arrays.binarySearch((Object[])this.list1.get(langid), adress);
    if (id > -1) {
      return ((String[])this.list2.get(langid))[id];
    }

    if (this.debug) {
      System.out.println("[LangFile] Error, the given adress (" + adress + ") isn't found in " + this.filename + " language files " + lang + " language!");
    }
    for (langid = 0; langid < this.langs.size(); langid++) {
      id = Arrays.binarySearch((Object[])this.list1.get(langid), adress);
      if (id > -1) {
        if (this.debug) {
          System.out.println("[LangFile] Returned string from " + (String)this.langs.get(langid) + " language!");
        }
        return ((String[])this.list2.get(langid))[id];
      }
    }

    if (this.debug) {
      System.out.println("[LangFile] Error, the given adress (" + adress + ") isn't found in " + this.filename + " language file!");
    }
    return adress;
  }

  public String get(MC_Player plr, String adress)
  {
    String pll = PermApi.getPlayerData(plr == null ? null : plr.getName(), "lang");
    if (pll == null) {
      pll = (String)this.langs.get(0);
    }
    return get(adress, pll);
  }
  public void remove(String prefix) {
    for (int lid = 0; lid < this.langs.size(); lid++) {
      List old1 = Lists.newArrayList((String[])this.list1.get(lid));
      List old2 = Lists.newArrayList((String[])this.list2.get(lid));
      for (int i = 0; i < old1.size(); i++) {
        if (((String)old1.get(i)).startsWith(prefix)) {
          old1.remove(i);
          old2.remove(i);
          i--;
        }
      }
      String[] new1 = new String[old1.size()];
      String[] new2 = new String[old2.size()];
      this.list1.set(lid, (String[])old1.toArray(new1));
      this.list2.set(lid, (String[])old2.toArray(new2));
    }
  }

  public void delete() {
    this.list1.clear();
    this.list2.clear();
    this.langs.clear();
    this.filename = null;
  }

  public void sort()
  {
    for (int i = 0; i < this.langs.size(); i++) sort(i); 
  }

  public void sort(int i) { String[] l1 = (String[])this.list1.get(i); String[] l2 = (String[])this.list2.get(i);
    String[] lr1 = (String[])l1.clone(); String[] lr2 = new String[l2.length];
    Arrays.sort(lr1);
    for (int id = 0; id < lr1.length; id++)
    {
      int id2 = KFA.search(l1, lr1[id]);
      lr2[id] = l2[id2];
    }
    this.list1.set(i, lr1);
    this.list2.set(i, lr2);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.konfigfajl.LangFile
 * JD-Core Version:    0.6.2
 */