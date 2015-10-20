package session;

import tools.FileWork;

import java.io.FileNotFoundException;
import java.util.Calendar;

/**
 * Created by user on 20.10.2015.
 */
public class MessageFileStorage implements MessagesStorage {
    FileWork fw = new FileWork();

    @Override
    public void storeMesage(String mes) {
        try {
            Calendar ca = Calendar.getInstance();
            fw.writeToFile("[" + ca.getTime() + "] " + mes, "D:\\Projects\\IdeaProjects\\technotrackChat\\src\\session\\messages.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getAllMessages() {
        String[] messages = new String[0];
        try {
            messages = fw.readFile("D:\\Projects\\IdeaProjects\\technotrackChat\\src\\session\\messages.txt").split("\\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public String[] getNMessages(int N) {
        String[] messages = new String[0];
        try {
            messages = fw.readFile("D:\\Projects\\IdeaProjects\\technotrackChat\\src\\session\\messages.txt").split("\\n");
            if (messages.length > N) {
                String[] lastMes = new String[N];
                for (int i = messages.length - N, j = 0; i < messages.length; i++, j++) {
                    lastMes[j] = messages[i];
                }
                return lastMes;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
