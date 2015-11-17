package tools; /**
 * Created by r.kildiev on 02.11.2015.
 */

import authorization.DBUserStore;
import authorization.UserStore;
import session.MessageDBStorage;
import session.MessagesStorage;
import session.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class JDBCExample {


    public User getUserFromDB(String login) throws SQLException, ClassNotFoundException {
        User u = new User();

        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/Kup3a",
                "senthil", "ubuntu");

        Statement stmt = c.createStatement();
        String sql = "SELECT * FROM USERS WHERE u_login =" + "'" + login + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            u.setId(rs.getInt("U_ID"));
            u.setLogin(login);
            u.setHashedPassword(rs.getString("HASHED_PASSWORD"));
            u.setSalt(rs.getString("SALT"));
            return u;
        } else {
            return null;
        }
    }

    public void addUserToDB(User u) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/Kup3a",
                "senthil", "ubuntu");
        Statement stmt = c.createStatement();

        String s = null;
        try {
            s = HashClass.getSaltHP(u.getPassword().toCharArray());
            String[] ss = s.split(" ");
            u.setSalt(ss[0]);
            u.setHashedPassword(ss[1]);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO USERS (U_LOGIN,SALT,HASHED_PASSWORD) "
                + "VALUES (" + "'" + u.getLogin() + "','" + u.getSalt() + "','" + u.getHashedPassword() + "'" + ");";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public static void main(String[] argv) throws SQLException, ClassNotFoundException {
        //дальше начинаются мои изыскания
        ArrayList<String> requiredTables = new ArrayList<>();
        requiredTables.add("messages");
        requiredTables.add("users");
        requiredTables.add("chats");
        requiredTables.add("user_chat");
        int flag = 0;
        Class.forName("org.postgresql.Driver");

        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/Kup3a",
                "senthil", "ubuntu");

        DatabaseMetaData dbData = c.getMetaData();
        ResultSet rs = dbData.getTables(null, "public", "", null);
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            if (requiredTables.contains(name)) {
                flag++;
            }
        }
        rs.close();

        Statement stmt;
        String sql;

        if (flag != requiredTables.size()) {
            System.out.println("Wrong db");

            stmt = c.createStatement();
            sql = "CREATE TABLE USERS " +
                    "(U_ID            SERIAL PRIMARY KEY     NOT NULL," +
                    " U_LOGIN         TEXT    NOT NULL UNIQUE , " +
                    " U_NICK          TEXT    UNIQUE , " +
                    " SALT        TEXT, " +
                    " HASHED_PASSWORD         TEXT)";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE CHATS " +
                    "(CH_ID            SERIAL PRIMARY KEY     NOT NULL," +
                    " CH_NAME         TEXT    NOT NULL UNIQUE , " +
                    " CH_DATE          TEXT     )";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE MESSAGES " +
                    "(M_ID             SERIAL PRIMARY KEY     NOT NULL," +
                    " M_TEXT         TEXT    NOT NULL, " +
                    " SENDER_ID INT     NOT NULL, " +
                    " CHAT_ID        INT, " +
                    " CHAT_DATE        TIMESTAMP, " +
                    " CONSTRAINT MES_FK_U FOREIGN KEY (SENDER_ID) REFERENCES USERS (U_ID) ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    " CONSTRAINT MES_FK_CH FOREIGN KEY (CHAT_ID) REFERENCES CHATS (CH_ID) ON UPDATE NO ACTION ON DELETE NO ACTION)";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE USER_CHAT " +
                    "(CH_ID            INT     NOT NULL," +
                    " U_ID             INT     NOT NULL," +
                    " PRIMARY KEY (CH_ID, U_ID)," +
                    " CONSTRAINT U_CH_FK_U FOREIGN KEY (U_ID) REFERENCES USERS (U_ID) ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    " CONSTRAINT U_CH_FK_CH FOREIGN KEY (CH_ID) REFERENCES CHATS (CH_ID) ON UPDATE NO ACTION ON DELETE NO ACTION)";
            stmt.executeUpdate(sql);
            stmt.close();

            c.setAutoCommit(false);

            stmt = c.createStatement();
            sql = "INSERT INTO USERS (U_LOGIN) "
                    + "VALUES ('alex');";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
        } else {
            System.out.println("OK db");
        }

        stmt = c.createStatement();
        rs = stmt.executeQuery("SELECT * FROM MESSAGES;");

        while (rs.next()) {
            String text = rs.getString("m_text");
            System.out.println(" M_TEXT  = " + text);
        }
        rs.close();


//        stmt = c.createStatement();
//        sql = "INSERT INTO CHATS (CH_NAME) "
//                + "VALUES ('myChat');";
//        stmt.executeUpdate(sql);
        //ТЕСТИРОВАНИЕ КЛАССА DBUserStore
        //МЕТОДА isUserExist
        UserStore userStore = new DBUserStore();
        System.out.println("is user exist: " + userStore.isUserExist("alex"));
        //МЕТОДА addUser
        User u = new User();
        u.setLogin("alexey");
        if (!userStore.isUserExist("alexey")) {
            try {
                String s = HashClass.getSaltHP("114499".toCharArray());
                String[] ss = s.split(" ");
                u.setSalt(ss[0]);
                u.setHashedPassword(ss[1]);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            userStore.addUser(u);
        }
        //МЕТОДА getUser
        u = userStore.getUser("alexey");
        System.out.println(u.getHashedPassword());
        //ТЕСТИРОВАНИЕ КЛАССА
        //тестирование метода storeMesage
        MessagesStorage messagesStorage = new MessageDBStorage(2, 2);
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        while (!str.equals("exit")) {
            messagesStorage.storeMesage(str);
            str = scanner.nextLine();
        }
        //тестирование метода
        String[] list =  messagesStorage.getAllMessages();
        System.out.println("list.length: " + list.length);
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }

        stmt.close();
        c.close();

    }

}