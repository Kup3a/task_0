package razakov.authorization;

import session.Session;
import session.User;
import tools.HashClass;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

/**
 * Created by user on 20.10.2015.
 */
public class AuthorizationService {

    private UserStore userStore;

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
    }

    public User login(String login, String password, Session session) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (userStore.isUserExist(login)) {
            User checkingUser = userStore.getUser(login);
            String checkingSalt = checkingUser.getSalt();
            if (checkingUser.getHashedPassword().equals(HashClass.createHash(password.toCharArray(), HashClass.fromHex(checkingSalt)))) {
                System.out.println("you have authorized");
                session.setSessionUser(checkingUser);
            } else {
                System.out.println("fail authorization");
            }
        }
//            1. Ask for name
//            2. Ask for password
//            3. Ask UserStore for user:  userStore.getUser(name, pass)


        return null;
    }

    public User createUser() throws InvalidKeySpecException, NoSuchAlgorithmException, FileNotFoundException {
        System.out.println("Please enter your login:");
        Scanner in = new Scanner(System.in);
        User user = new User();
        user.setLogin(in.next());
        System.out.println("Please enter your password:");
        user.setPassword(in.next());
        userStore.addUser(user);
        // 1. Ask for name
        // 2. Ask for pass
        // 3. Add user to UserStore: userStore.addUser(user)

        return null;
    }

}

