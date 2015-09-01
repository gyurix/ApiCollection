package gyurix.konfigfajl;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MojangAPI
{
  public static String getUUID(String player)
  {
    try
    {
      HttpURLConnection connection = (HttpURLConnection)new URL(
        "https://api.mojang.com/profiles/minecraft")
        .openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      Object localObject1 = null; Object localObject4 = null;
      Object localObject3;
      label142: 
      try { out = new DataOutputStream(connection
          .getOutputStream());
      }
      finally
      {
        DataOutputStream out;
        localObject3 = localThrowable1; break label142; if (localObject3 != localThrowable1) localObject3.addSuppressed(localThrowable1); 
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(
        connection.getInputStream()));
      String id = reader.readLine().substring(8);
      return id.substring(0, id.indexOf("\""));
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ArrayList<String> getPlayerNames(String uuid) {
    try {
      HttpURLConnection connection = (HttpURLConnection)new URL(
        "https://api.mojang.com/user/profiles/" + uuid + "/names")
        .openConnection();
      return (ArrayList)KFA.gs.fromJson(new BufferedReader(new InputStreamReader(
        connection.getInputStream())), ArrayList.class);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }
}

/* Location:           D:\GitHub\_ApiCollection.jar
 * Qualified Name:     gyurix.konfigfajl.MojangAPI
 * JD-Core Version:    0.6.2
 */