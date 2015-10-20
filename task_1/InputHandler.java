import comands.Command;
import session.MessageFileStorage;
import session.MessagesStorage;
import session.Session;

import java.util.Map;

/**
 * Created by user on 20.10.2015.
 */
public class InputHandler {

    private Session session;

    private Map<String, Command> commandMap;

    private MessagesStorage messagesStorage = new MessageFileStorage();

    public InputHandler(Session session, Map<String, Command> commandMap) {
        this.session = session;
        this.commandMap = commandMap;
    }

    public void handle(String data) {
        // проверяем на спецсимвол команды
        // Это пример!
        if (data.startsWith("\\")) {
            String[] tokens = data.split(" ");

            // Получим конкретную команду, но нам не важно что за команда,
            // у нее есть метод execute()
            Command cmd = commandMap.get(tokens[0]);
            if (cmd == null) {
                System.out.println("There's no such command. Please, enter '\\help' for getting list of available commands");
            } else {
                cmd.execute(session, tokens);
            }
        } else if (session.getSessionUser() == null){
            System.out.println(">" + data);
        } else if (session.getSessionUser().getNick() == null){
            messagesStorage.storeMesage(data);
            System.out.println("(message is stored)>" + data);
        } else {
            messagesStorage.storeMesage(data);
            System.out.println("(message is stored)<" + session.getSessionUser().getNick() + ">" + data);
        }
    }
}
