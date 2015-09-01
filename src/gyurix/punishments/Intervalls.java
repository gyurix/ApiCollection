package gyurix.punishments;

import java.util.ArrayList;

public class Intervalls
{
  public final ArrayList<Integer> starts = new ArrayList();
  public final ArrayList<Integer> ends = new ArrayList();

  public Intervalls(String input) { for (String s : input.replace(" ", "").split("\\,")) {
      String[] d = s.split("\\-", 2);
      if (d[0].endsWith("+")) {
        this.starts.add(Integer.valueOf(d[0].substring(0, d[0].length() - 1)));
        this.ends.add(Integer.valueOf(2147483647));
      }
      else {
        int st = Integer.valueOf(d[0]).intValue();
        this.starts.add(Integer.valueOf(st));
        this.ends.add(Integer.valueOf(d.length == 2 ? Integer.valueOf(d[1]).intValue() : st));
      }
    } }

  public boolean contains(int id) {
    for (int i = 0; i < this.starts.size(); i++) {
      if ((((Integer)this.starts.get(i)).intValue() <= id) && (((Integer)this.ends.get(i)).intValue() >= id))
        return true;
    }
    return false;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.punishments.Intervalls
 * JD-Core Version:    0.6.2
 */