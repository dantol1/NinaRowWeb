package Servlets;

import GameLogic.Game;
import GameLogic.GameController;
import GameLogic.GamePlayer;
import Utils.ServletUtils;
import Utils.SessionUtils;
import WebLogic.GameManager;
import WebLogic.GameStatus;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameStatusServlet extends HttpServlet {

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
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        String userName = SessionUtils.getUsername(request.getSession());
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        GameController game = gameManager.getGameByUserName(userName);
        String status = game.getStatus().toString();
        String message = "";

        if (game.getStatus() == GameController.GameStatus.WaitingForPlayers) {
            message = "Waiting For Players";
        }
        else if (game.getStatus() == GameController.GameStatus.Finished) {

            if (game.getState() == Game.GameState.GameWin) {

                String name = "";
                for (GamePlayer player : game.getWinningPlayers()) {
                    name = player.getName();
                }
                message = "Game finished! " + name + " Has Won!";
            } else if (game.getState() == Game.GameState.SeveralPlayersWonTie) {

                ArrayList<String> winnerNames = new ArrayList<>();

                for (GamePlayer player : game.getWinningPlayers()) {
                    winnerNames.add(player.getName());
                }

                String opening = "Game Finished with Several Players Won Tie! ";

                StringBuilder builder = new StringBuilder();
                builder.append(opening);

                for (String name : winnerNames) {

                    builder.append(name).append(" ");
                }
                message = builder.toString();
            } else if (game.getState() == Game.GameState.GameTie) {

                message = "Game Ended with Tie!";
            }
        }

        out.println(gson.toJson(new GameStatus(status,message)));
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
