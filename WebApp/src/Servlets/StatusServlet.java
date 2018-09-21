package Servlets;

import Utils.ServletUtils;
import Utils.SessionUtils;
import WebLogic.LoginStatus;
import WebLogic.User;
import WebLogic.UserManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        if (SessionUtils.hasSession(request) && SessionUtils.isLoggedIn(request.getSession())) {
            String userName = SessionUtils.getUsername(request.getSession(false));
            boolean isComputer = SessionUtils.isComputer(request.getSession(false));
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            User user = userManager.getUser(userName);
            out.println(gson.toJson(new LoginStatus(true, (String)null, userName, isComputer, user.getId())));
        } else {
            out.println(gson.toJson(new LoginStatus(false)));
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
