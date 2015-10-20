package comands;

import session.MessageFileStorage;
import session.MessagesStorage;
import session.Session;

/**
 * Created by user on 20.10.2015.
 */
public class FindCommand implements Command {
    @Override
    public void execute(Session session, String[] args) {
        if (session.getSessionUser() == null) {
            System.out.println("you are not authorized");
        } else if (args.length == 2) {
            MessagesStorage messagesStorage = new MessageFileStorage();
            String[] allMessages = messagesStorage.getAllMessages();
            for (int i = 0; i < allMessages.length; i++) {
                if (allMessages[i].contains(args[1])) {
                    System.out.println(allMessages[i]);
                }
            }
        } else {
            System.out.println("This command require 1 argument");
        }
    }
}
