package Function;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by vera on 15-3-16.
 */
public class splitFunction {
    public splitFunction(){
        return;
    }
    public String splitStringByM160(String input, boolean extend) {
        try {
            Socket server = null;
            server = new Socket("m160.cs.uwaterloo.ca", 5134);

            PrintWriter out = new PrintWriter(new
                    OutputStreamWriter(server.getOutputStream(), "utf-8"));
            BufferedReader in =
                    new BufferedReader(new
                            InputStreamReader(server.getInputStream(), "utf-8"));
            out.println(extend);
            out.println(input);
            out.println("#END#");
            out.flush();
            StringBuilder sb = new StringBuilder();
            String line;
            while (true) {
                line = in.readLine();
                if (line == null || line.equals("#END#"))
                    break;
                sb.append(line);
                sb.append('\n');
            }
            server.close();
            return sb.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
