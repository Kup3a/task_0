package session;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 30.10.2015.
 */
public class Message {
    private String body;
    private Date time;
    private int connectionId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public Message(){
    }

    public Message(String message) {
        body = message;
        Calendar ca = Calendar.getInstance();
        time = ca.getTime();
    }
}
