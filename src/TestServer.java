import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alex on 3/16/16.
 */
public class TestServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(12345);

        Socket connection = socket.accept();

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        PrintWriter output = new PrintWriter(connection.getOutputStream(),true);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println(input.readLine());
            System.out.println("Please respond:");
            String response = stdIn.readLine();
            output.println(response);
        }
    }
}
