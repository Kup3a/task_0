package comands;

import session.Session;

import java.util.Map;

/**
 * Created by user on 20.10.2015.
 */
public class HelpCommand implements Command {

    private Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(Session session, String[] args) {
        System.out.println("Available list of commands:");
        System.out.println("\\login <user_login> <password> - for authorization");
        System.out.println("\\login - for register new user");
        System.out.println("\\history - for getting history of messages");
        System.out.println("\\find <key_word> - for searching through the history");
    }
}
