package comands;

import razakov.authorization.AuthorizationService;
import session.Session;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by user on 20.10.2015.
 */
public class LoginCommand implements Command {

    private AuthorizationService service;

    public LoginCommand(AuthorizationService service) {
        this.service = service;
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session.getSessionUser() != null) {
            System.out.println("You are already authorizated");
        } else if (args.length == 3) {
            //тут авторизуем
            try {
                service.login(args[1], args[2], session);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1) {
            //тут заводим нового юзера
            try {
                service.createUser();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong arguments for \\login command.");
        }
        /*
        А эта часть у нас уже реализована
        1 проверим, есть ли у нас уже юзер сессии
        2 посмотрим на аргументы команды
        3 пойдем в authorizationService и попробуем получить юзера
         */
    }
}
