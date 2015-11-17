package authorization;

import session.User;

/**
 * Created by user on 20.10.2015.
 */
public interface UserStore {

    // проверить, есть ли пользователь с таким именем
    // если есть, вернуть true
    boolean isUserExist(String name);

    // Добавить пользователя в хранилище
    void addUser(User user);

    // Получить пользователя по имени и паролю
    User getUser(String name);
}
