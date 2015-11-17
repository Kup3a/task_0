import org.codehaus.jackson.map.ObjectMapper;
import session.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by user on 27.10.2015.
 */
public class ChatClient implements Runnable {

    private Socket s;
    private Message m = new Message();
    private boolean listener = true;

    /**
     * ћетода подключени€ к серверу.
     * ƒл€ обеих нитей используетс€ один и тот же сокет, что позвол€ет отправл€ть сообщени€ серверу и получать сообщени€
     * от сервера направленно, т.е. по connectionId из Message
     */
    private void initClient() {
        try {
            s = new Socket("localhost", 3129);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ётот метод вызываетс€ сразу после создани€ нового клиента.
     */
    public void connectToServer() {
        // это говорит серверу о том, что подключение ещЄ не добавлено в пул подключений
        m.setConnectionId(0);

        initClient();
        // запускаем нить, котора€ будет получать сообщени€ от сервера
        Thread listener = new Thread(this);
        listener.start();
        // запускаем нить, котора€ будет отправл€ть сообщени€ серверу, считыва€ их с клавиатуры
        Thread producer = new Thread(this);
        producer.start();
    }

    @Override
    public void run() {
        if (listener) {
            listener = false;
            System.out.println("i'm new thread-listener");
            try {
                byte buf[] = new byte[64 * 1024];
                int length;
                String mes;
                while (true) {
                    length = s.getInputStream().read(buf);
                    mes = new String(buf, 0, length);
                    //таким сообщением сервер выставл€ет очередному клиенту connectionId
                    if (mes.startsWith("connectionAm")) {
                        String[] pars = mes.split(" ");
                        m.setConnectionId(Integer.parseInt(pars[1]));
                        System.out.println("my id now is " + m.getConnectionId());
                    }
                    System.out.println("message from server: " + mes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("i'm new thread-producer");
            Scanner scanner = new Scanner(System.in);
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString;
            while (true) {
                //считываем сообщение
                m.setBody(scanner.nextLine());
                m.setTime(Calendar.getInstance().getTime());
                //преобразуем сообщение-объект в json-строку
                try {
                    jsonInString = mapper.writeValueAsString(m);
                    //строку преобразуем в массив битов и передаЄм его через сокет
                    s.getOutputStream().write(jsonInString.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
