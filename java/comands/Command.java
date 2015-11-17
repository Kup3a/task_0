package comands;

import session.Session;

/**
 * Created by user on 20.10.2015.
 */
public interface Command {


    /**
     * Здесь можно возвращать результат, подумайте как лучше сделать
     * результат желательно инкапсулировать в неком объекте Result
     *
     * В качестве пример оставлю void
     */
    String execute(Session session, String[] args);
}
