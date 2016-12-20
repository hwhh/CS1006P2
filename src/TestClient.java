import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",12345);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("Please respond:");
            String response = stdIn.readLine();
            output.println(response);
            System.out.println(input.readLine());
        }
    }
}
