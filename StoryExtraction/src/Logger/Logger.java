package Logger;

/**
 * Created by Near on 15-05-05.
 */
public class Logger {
  private static boolean debug = false;
  public static void setDebugFlag(boolean flag) {
    debug = flag;
  }
  public static void log(String str) {
    if(debug) {
      System.err.println(str);
    }
  }
}
