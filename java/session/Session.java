package session;

/**
 * Created by user on 20.10.2015.
 */
public class Session {

    private User sessionUser;
    //дополнительно к базовому набору будем хранить тут еще и объект MessageStorage авторизованного пользователя
    private MessagesStorage messagesStorage;

    public MessagesStorage getMessagesStorage() {
        return messagesStorage;
    }

    public void setMessagesStorage(MessagesStorage messagesStorage) {
        this.messagesStorage = messagesStorage;
    }


    public Session() {    }

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
    }
}
