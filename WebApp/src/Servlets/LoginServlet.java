package Servlets;
import Utils.ServletUtils;
import Utils.SessionUtils;
import WebLogic.UserManager;
import constants.Constants;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static constants.Constants.PLAYERTYPE;
import static constants.Constants.USERNAME;

public class LoginServlet extends HttpServlet {


    private final String GAME_ROOM_URL = "../GamesHubPage/GamesHubPage.html";
    private final String SIGN_UP_URL = "../LoginPage/Login.html";
    private final String LOGIN_ERROR_URL = "/Pages/LoginPage/ErrorLogin.html";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        boolean userTypeFromSession = SessionUtils.getUserType(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);
            String isComputer = request.getParameter(PLAYERTYPE);
            boolean isComputerBool = false;
            if (isComputer != null)
            {
                isComputerBool = true;
            }
            if (usernameFromParameter.isEmpty()) {
                response.sendRedirect(SIGN_UP_URL);
            }
            else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                        getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    } else {
                        userManager.addUser(usernameFromParameter, userTypeFromSession);
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession(true).setAttribute(Constants.PLAYERTYPE, isComputerBool);

                        response.sendRedirect(GAME_ROOM_URL);
                    }
                }
            }
        } else {
            //user is already logged in
            response.sendRedirect(GAME_ROOM_URL);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
