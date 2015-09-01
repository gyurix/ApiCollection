package gyurix.barapi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bar
{
  ScheduledExecutorService sch = Executors.newScheduledThreadPool(1);
  String acttext;
  Double acthealth;
  List<String> texts = new ArrayList();
  List<Double> healths = new ArrayList();
  Bar ins;
  public boolean autoupdate = true;

  public Bar() { this.texts.add("§cNo static text set");
    this.acttext = "§cNo static text set";
    this.healths.add(Double.valueOf(300.0D));
    this.acthealth = Double.valueOf(300.0D);
    this.ins = this; }

  public Bar(String staticMessage) {
    this.texts.add(staticMessage);
    this.acttext = staticMessage;
    this.healths.add(Double.valueOf(300.0D));
    this.acthealth = Double.valueOf(300.0D);
    this.ins = this;
  }
  public Bar(double staticHealth) {
    this.texts.add("§cNo static text set");
    this.acttext = "§cNo static text set";
    this.healths.add(Double.valueOf(staticHealth));
    this.acthealth = Double.valueOf(staticHealth);
    this.ins = this;
  }
  public Bar(String staticMessage, double staticHealth) {
    this.texts.add(staticMessage);
    this.acttext = staticMessage;
    this.healths.add(Double.valueOf(staticHealth));
    this.acthealth = Double.valueOf(staticHealth);
    this.ins = this;
  }
  public void setStaticMessage(String msg) {
    if (((String)this.texts.get(0)).equals(this.acttext))
      this.acttext = msg;
    BarAPI.updateBar(this);
    this.texts.set(0, msg);
  }
  public void setStaticHealth(double health) {
    if ((((Double)this.healths.get(0)).equals(this.acthealth)) && (((Double)this.healths.get(0)).doubleValue() != health)) {
      this.acthealth = Double.valueOf(health);
      if (this.autoupdate)
        BarAPI.updateBar(this);
    }
    this.healths.set(0, Double.valueOf(health));
  }
  public void showStaticText() {
    this.acttext = ((String)this.texts.get(0));
    if (this.autoupdate)
      BarAPI.updateBar(this); 
  }

  public void showStaticHealth() { this.acthealth = ((Double)this.healths.get(0));
    if (this.autoupdate)
      BarAPI.updateBar(this); }

  public void setTemporaryMessage(String msg, long milis) {
    this.texts.add(msg);
    this.sch.schedule(new Runnable() {
      String m = (String)Bar.this.texts.get(Bar.this.texts.size() - 1);

      public void run() {
        Bar.this.texts.remove(this.m);
        if (Bar.this.acttext.equals(this.m)) {
          Bar.this.acttext = ((String)Bar.this.texts.get(Bar.this.texts.size() - 1));
          if (Bar.this.autoupdate)
            BarAPI.updateBar(Bar.this.ins);
        }
      }
    }
    , milis, TimeUnit.MILLISECONDS);
    this.acttext = msg;
    if (this.autoupdate)
      BarAPI.updateBar(this);
  }

  public void setTemporaryHealth(double health, long milis) {
    this.healths.add(Double.valueOf(health));
    this.sch.schedule(new Runnable() {
      double h = ((Double)Bar.this.healths.get(Bar.this.healths.size() - 1)).doubleValue();

      public void run() {
        Bar.this.texts.remove(Double.valueOf(this.h));
        if (Bar.this.acttext.equals(Double.valueOf(this.h))) {
          Bar.this.acttext = ((String)Bar.this.texts.get(Bar.this.texts.size() - 1));
          if (Bar.this.autoupdate)
            BarAPI.updateBar(Bar.this.ins);
        }
      }
    }
    , milis, TimeUnit.MILLISECONDS);
    this.acthealth = Double.valueOf(health);
    if (this.autoupdate)
      BarAPI.updateBar(this);
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.barapi.Bar
 * JD-Core Version:    0.6.2
 */