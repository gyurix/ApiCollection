package gyurix.chatapi;

import java.util.List;
import joebkt.ChatColor;

public class ChatComponent
{
  public String text;
  public String translate;
  public String insertion;
  public String selector;
  public String color;
  public Event clickEvent = new Event(); public Event hoverEvent = new Event();
  public List<?> extra;
  public List<?> with;
  public boolean bold;
  public boolean italic;
  public boolean underlined;
  public boolean strikethrough;
  public boolean obfuscated;

  public ChatComponent()
  {
  }

  public ChatComponent(String txt)
  {
    this.text = txt;
  }
  public void setFormat(String format) {
    if ((format == null) || (format.isEmpty())) {
      this.obfuscated = (this.bold = this.strikethrough = this.underlined = this.italic = 0);
      this.color = null;
      return;
    }
    this.color = ChatColor.getByChar(format.charAt(0)).name().toLowerCase();
    this.obfuscated = format.contains("k");
    this.bold = format.contains("l");
    this.strikethrough = format.contains("m");
    this.underlined = format.contains("n");
    this.italic = format.contains("o");
  }
  public ChatComponent(String txt, Event hover, Event click, String ins, String format) {
    this.insertion = ins;
    this.text = txt;
    this.hoverEvent = hover;
    this.clickEvent = click;
    setFormat(format);
  }

  public static enum ClickAction
  {
    open_url, open_file, run_command, suggest_command, change_page;
  }

  public static class Event
  {
    public String action;
    public String value;

    public Event()
    {
    }

    public Event(ChatComponent.HoverAction act, String val)
    {
      this.action = act.name();
      this.value = val;
    }
    public Event(ChatComponent.ClickAction act, String val) {
      this.action = act.name();
      this.value = val;
    }
  }

  public static enum HoverAction
  {
    show_text, show_achievement, show_item, show_entity;
  }

  public static class Score
  {
    public String name;
    public String objective;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.chatapi.ChatComponent
 * JD-Core Version:    0.6.2
 */