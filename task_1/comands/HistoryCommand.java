package comands;

import session.MessageFileStorage;
import session.MessagesStorage;
import session.Session;

/**
 * Created by user on 20.10.2015.
 */
public class HistoryCommand implements Command {
    @Override
    public void execute(Session session, String[] args) {
        MessagesStorage messagesStorage = new MessageFileStorage();
        if (session.getSessionUser() == null) {
            System.out.println("You are not authorized");
        } else if (args.length == 1) {
            String[] s = messagesStorage.getAllMessages();
            for (int i = 0; i < s.length; i++) {
                System.out.println(s[i]);
            }
        } else if (args.length == 2) {
            Integer N = Integer.valueOf(args[1]);
            String[] s = messagesStorage.getNMessages(N);
            for (int i = 0; i < s.length; i++) {
                System.out.println(s[i]);
            }
        } else {
            System.out.println("Wrong arguments for \\history command.");
        }
    }
}
