package Servlets;
import Exceptions.PlayersAmountException;
import GameLogic.GameController;
import Utils.ServletUtils;
import WebLogic.GameManager;
import WebLogic.LoadGameStatus;
import WebLogic.LoginStatus;
import WebLogic.UserManager;
import com.google.gson.Gson;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JoinGameServlet extends HttpServlet {

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
        String userName = request.getParameter("user");
        boolean isComputer = false;
        if (request.getParameter("isComputer") != null)
        {
            isComputer = true;
        }
        //TODO: maybe get the game title from the user.
        String gameTitle = request.getParameter("gameTitle");
        String realGameTitle = gameTitle.substring(12);
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        GameController gameController = gameManager.getGameInfo(realGameTitle);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        response.setContentType("application/json");
        if (userManager.canUserJoinTheGame(userName) && gameController.getStatus() == GameController.GameStatus.WaitingForPlayers) {
            userManager.JoinUserToTheGame(userName,realGameTitle);
            try {
                gameController.registerPlayer(userManager.getUser(userName));
                out.println(gson.toJson(new LoadGameStatus(true,"")));
            }
            catch (PlayersAmountException e)
            {
                System.out.println("too many players");
                out.println(gson.toJson(new LoadGameStatus(false,"Error in joining the game")));
            }
        }
        else {
            System.out.println("the game status is " + gameController.getStatus().toString() + " and user can join game is " + userManager.canUserJoinTheGame(userName));
            out.println(gson.toJson(new LoadGameStatus(false,"Error in joining the game")));
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
