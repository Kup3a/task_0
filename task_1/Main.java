import comands.*;
import razakov.authorization.AuthorizationService;
import razakov.authorization.FileUserStore;
import razakov.authorization.UserStore;
import session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by user on 20.10.2015.
 */
public class Main {

    private static final String EXIT = "q|exit";

    public static void main(String[] args) {

        Map<String, Command> commands = new HashMap<>();


        // В этом объекте хранится инфа о сесии
        // то есть текущее состояние чата
        Session session = new Session();

        // Реализация интерфейса задается в одном месте
        UserStore userStore = new FileUserStore();
        AuthorizationService authService = new AuthorizationService(userStore);


        //Создаем команды
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

        InputHandler handler = new InputHandler(session, commands);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the chat! Enter '\\help' for getting list of available commands.");
        while (true) {
            String line = scanner.nextLine();
            if (line != null && line.equals(EXIT)) {
                break;
            }
            handler.handle(line);
        }


    }

}
