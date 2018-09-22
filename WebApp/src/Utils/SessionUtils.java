package Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static constants.Constants.PLAYERTYPE;
import static constants.Constants.USERNAME;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getUsername(HttpSession session) {
        return (String)session.getAttribute("UserNameInput");
    }


    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static boolean getUserType(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(PLAYERTYPE) : null;
        return sessionAttribute != null;
    }

    public static boolean hasSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    public static boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("UserNameInput") != null;
    }

    public static boolean isComputer(HttpSession session) {
        return (boolean)session.getAttribute("ComputerCheckBox");
    }

}