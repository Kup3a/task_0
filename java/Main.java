/**
 * Created by user on 20.10.2015.
 */
public class Main {

    private static final String EXIT = "q|exit";

    public static void main(String[] args) {

        //тут клиент-серверный подход
       /* Runnable server = new ChatServer();
        Thread t = new Thread(server);
        t.start();*/
        ChatServer server = new ChatServer();
        server.initServer();
        ChatClient client = new ChatClient();
        client.connectToServer();
        System.out.println("main is over");
    }

}
