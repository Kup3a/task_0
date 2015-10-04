package kazakov.hash;


import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * Это класс для демонстрации решения задачи.
 */
public class Test {
    private String login;
    private String salt;
    private String password;
    private String hashedPassword;

    public Test() {}

    public Test(String login, String s, String password, String hashedPassword) {
        this.login = login;
        this.salt = s;
        this.password = password;
        this.hashedPassword = hashedPassword;
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //Программа учебная, поэтому сначала смотрим, что есть в хранилище пользователей, которое устроено следующим
        //образом: в каждой строке разделенные пробелом хранятся последовательно логин соль пароль пароль_под_хешем.
        System.out.println("Now we have following in our storage:");
        FileWork fw = new FileWork();
        try {
            String storage = fw.readFile("D:\\Projects\\IdeaProjects\\MailHashTask\\src\\storage.txt");
            //В этой мапе в на протяжении всей работы хранятся данные по пользователям.
            HashMap<String, Test> map = new HashMap();
            String[] s = storage.split("\\s");
            for (int i = 0; i < s.length / 4; i++) {
                Test t = new Test(s[4*i], s[4*i + 1], s[4*i + 2], s[4*i + 3]);
                map.put(t.login, t);
            }
            Set<String> set = map.keySet();
            for (String str : set) {
                Test t = map.get(str);
                System.out.println(str + " " + t.salt + " " + t.password + " " + t.hashedPassword);
            }
            //Тут начинается общение с пользователем.
            System.out.println("Enter login:");
            Scanner in = new Scanner(System.in);
            Test t  = new Test();
            t.login = in.next();
            //В зависимости от того, есть в нашей мапе ключ, совпадающий с логином, пользователю будет предложено
            //залогиниться или зарегистрироваться.
            if (map.get(t.login) == null) { //условие соответсвтует новому юзеру
                System.out.println("Enter password for making accaunt:");
                t.password = in.next();
                fw.writeToFile(t.login + " " + HashClass.createHash(t.password), "D:\\Projects\\IdeaProjects\\MailHashTask\\src\\storage.txt");
            } else {
                System.out.println("Enter password for sign in:"); //а тут логин совпал
                t.password = in.next();
                Test test = map.get(t.login);
                String salt = test.salt;
                System.out.println("salt = " + salt);
                System.out.println("hashedPassword = " + test.hashedPassword);
                if (test.hashedPassword.equals(HashClass.createHash(t.password.toCharArray(), HashClass.fromHex(salt)))) { //если пароль соответсвует введенному логину.
                    System.out.println("Correct!");                                                                         //отмечу, что сравзнение по хешированному паролю, т.е. пароль в хранилище нам не нужен (как и доложно быть)
                } else {
                    System.out.println("Failed");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
