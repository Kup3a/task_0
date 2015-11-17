import authorization.AuthorizationService;
import authorization.DBUserStore;
import authorization.UserStore;
import comands.*;
import org.codehaus.jackson.map.ObjectMapper;
import session.Message;
import session.Session;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

;

/**
 * Created by user on 27.10.2015.
 */
public class ChatServer implements Runnable {

    private int connectionAmount = 1;
    //пробую перейти к методу initServer
    final String EXIT = "q|exit";
    Map<String, Command> commands;
    UserStore userStore;
    AuthorizationService authService;
    InputHandler handler;
    ServerSocket serverSocket;

    public void initServer() {
        commands = new HashMap<>();
        userStore = new DBUserStore();
        authService = new AuthorizationService(userStore);

        Command loginCommand = new LoginCommand(authService);
        Command helpCommand = new HelpCommand(commands);
        Command historyCommand = new HistoryCommand();
        Command findCommand = new FindCommand();
        Command nickCommand = new NickCommand();

        commands.put("\\login", loginCommand);
        commands.put("\\help", helpCommand);
        commands.put("\\history", historyCommand);
        commands.put("\\find", findCommand);
        commands.put("\\nick", nickCommand);

        handler = new InputHandler(commands);

        try {
            serverSocket = new ServerSocket(3129, 0, InetAddress.getByName("localhost"));
            Thread connection = new Thread(this);
            connection.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //ServerSocket serverSocket = new ServerSocket(3129, 0, InetAddress.getByName("localhost"));
            System.out.println("Server inited successfully.");
            Socket socket = serverSocket.accept();
            int threadId = connectionAmount;
            connectionAmount++;
            Thread connection = new Thread(this);
            connection.start();
            System.out.println("Server got new connection.");
            InputStream is = socket.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            Session session = new Session();

            while (true) {
                // буффер данных в 64 килобайта
                byte buf[] = new byte[64*1024];
                // читаем 64кб от клиента, результат - кол-во реально принятых данных
                int r = is.read(buf);
                // создаём строку, содержащую полученную от клиента информацию
                String data = new String(buf, 0, r);
                // строка - это json, который надо преобразовать в объект в Message
                Message m = mapper.readValue(data, Message.class);
                // дальше на обработку отдаём тело сообщения-объекта
                if (m.getBody() != null && m.getBody().equals(EXIT)) {
                    break;
                }
                //на обработку мы отправляем сообщение только после того, как клиенту присвоили connectionId
                if (m.getConnectionId() != 0) {
                    System.out.println("handling message from clientId = " + m.getConnectionId());
                    //System.out.println(connections.get(m.getConnectionId()));
                    //handler.handle(m.getBody(), connections.get(m.getConnectionId()));
                    String answer = handler.handle(m.getBody(), session);
                    socket.getOutputStream().write(answer.getBytes());
                } else {
                    System.out.println("sending mes from server");
                    String ans = "connectionAmount " + String.valueOf(threadId);
                    socket.getOutputStream().write(ans.getBytes());
                    //initNewConnection();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
