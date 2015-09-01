package gyurix.chatapi;

import com.google.gson.Gson;
import gyurix.konfigfajl.ConfigFile;
import java.util.ArrayList;
import java.util.List;
import joebkt.JSonConverter;
import joebkt.TextObject;

public class MessageBuilder
{
  public List<Object> comps = new ArrayList();

  public MessageBuilder() {
  }
  public MessageBuilder(String input) { addAll(input); }

  public static ChatComponent.Event getClickEvent(String in) {
    if (in == null)
      return null;
    if (in.startsWith("\\s\\"))
      return new ChatComponent.Event(ChatComponent.ClickAction.suggest_command, in.substring(3));
    if (in.startsWith("\\e\\"))
      return new ChatComponent.Event(ChatComponent.ClickAction.run_command, in.substring(3));
    if (in.startsWith("\\p\\"))
      return new ChatComponent.Event(ChatComponent.ClickAction.change_page, in.substring(3));
    if (in.startsWith("\\u\\"))
      return new ChatComponent.Event(ChatComponent.ClickAction.open_url, in.substring(3));
    if (in.startsWith("\\f\\"))
      return new ChatComponent.Event(ChatComponent.ClickAction.open_file, in.substring(3));
    return new ChatComponent.Event(ChatComponent.ClickAction.suggest_command, in);
  }
  public static ChatComponent.Event getHoverEvent(String in) {
    if (in == null)
      return null;
    if (in.startsWith("\\t\\"))
      return new ChatComponent.Event(ChatComponent.HoverAction.show_text, in.substring(3));
    if (in.startsWith("\\i\\"))
      return new ChatComponent.Event(ChatComponent.HoverAction.show_item, in.substring(3));
    if (in.startsWith("\\e\\"))
      return new ChatComponent.Event(ChatComponent.HoverAction.show_entity, in.substring(3));
    if (in.startsWith("\\a\\"))
      return new ChatComponent.Event(ChatComponent.HoverAction.show_achievement, in.substring(3));
    return new ChatComponent.Event(ChatComponent.HoverAction.show_text, in);
  }
  public void add(String str) {
    String txt = null;
    ChatComponent.Event click = null; ChatComponent.Event hover = null;
    String ins = null;
    for (String s : ("T" + str).replaceAll("[\\\\]([HCI])", "\r$1").split("\r")) {
      if (s.startsWith("T")) {
        txt = s.substring(1);
      }
      else if (s.startsWith("H")) {
        hover = getHoverEvent(s.substring(1));
      }
      else if (s.startsWith("C")) {
        click = getClickEvent(s.substring(1));
      }
      else if (s.startsWith("I")) {
        ins = s.substring(1);
      }
    }
    this.comps.add(new ChatComponent(txt, hover, click, ins, ""));
  }
  public void addAll(String str) {
    for (String s : str.split("\\\\\\|"))
      add(s);
  }

  public void addAll(MessageBuilder builder) {
    this.comps.addAll(builder.comps);
  }
  public void addConfig(ConfigFile kf, String adress) {
    MessageBuilder mb = new MessageBuilder();
    mb.comps.add(new ChatComponent(kf.get(adress, ""), 
      getHoverEvent(kf.get(adress + ".hover", null)), 
      getClickEvent(kf.get(adress + ".click", null)), 
      kf.get(adress + ".insertion", null), 
      kf.get(adress + ".color", "")));
    String msg = kf.get(adress + ".1");
    int i = 1;
    while (!msg.equals(kf.get_na)) {
      mb.comps.add(new ChatComponent(kf.get(adress, ""), 
        getHoverEvent(kf.get(adress + "." + i + ".hover", null)), 
        getClickEvent(kf.get(adress + "." + i + ".click", null)), 
        kf.get(adress + "." + i + ".insertion", null), 
        kf.get(adress + "." + i + ".color", "")));
      i++;
      msg = kf.get(adress + "." + i);
    }
  }

  public String toString() {
    return ChatAPI.gs.toJson(this.comps);
  }
  public TextObject toTextObject() {
    return JSonConverter.getTextObjectFromString(toString());
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.chatapi.MessageBuilder
 * JD-Core Version:    0.6.2
 */