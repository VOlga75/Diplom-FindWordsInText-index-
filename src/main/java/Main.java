import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //  System.out.println(engine.search("бизнес"));
        int port = 8989;
        // стартуем сервер один(!) раз
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try (
                Socket clientSocket = serverSocket.accept(); // ждем подключения
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                System.out.println("Start " + clientSocket.getPort());
                final String str = in.readLine();
                System.out.println("Ищем слово " + str);// консоль сервера
                List<PageEntry> listRez = engine.search(str);
                if (listRez == null) {
                    System.out.println("Слово " + str + " не найдено. Закрываем подключение");
                    out.println("Слово " + str + " не найдено.");
                    // out.flush();
                } else {
                    out.println(listRez.toString());
                    out.flush();
                    System.out.println("Результат поиска слова " + str + " отправлен клиенту");
                }
            } catch (IOException e) {
                System.out.println("Не могу стартовать сервер");
                e.printStackTrace();
            }
        }
    }
}