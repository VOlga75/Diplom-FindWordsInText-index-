import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8989;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите искомое слово");
            String str = scanner.nextLine();
            if (str.split("\\P{IsAlphabetic}+").length == 1) {
                try (Socket clientSocket = new Socket(host, port);
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    out.println(str);
                    out.flush();
                    String s;
                    while ((s = in.readLine()) != null) {
                        System.out.println(s);
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Не слово. Клиент закончил работу");
                break;
            }
        }
    }
}
