package session;

/**
 * Created by user on 20.10.2015.
 */
public interface MessagesStorage {

    void storeMesage(String mes);

    String[] getAllMessages();

    String[] getNMessages(int N);
}
