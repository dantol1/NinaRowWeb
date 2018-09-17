//package Services;
//
//import Exceptions.GameServiceException;
//import Game.Game;
//import GameLogic.Game;
//import GameXmlParser.GameBoardXmlParser;
//import GameXmlParser.GameDefinitionsXmlParserException;
//import WebLogic.GameManager;
//import logic.GameManager;
//import logic.ServiceException;
//import utils.ServletUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import static constants.Constants.USERNAME;
//
//@WebServlet(name = "UploadGameService", urlPatterns = {"/uploadGame"})
//@MultipartConfig
//public class UploadGameService extends JsonHttpServlet {
//
//    @Override
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        Part filePart = request.getPart("file");
//        try {
//            GameBoardXmlParser gameBoardXmlParser = new GameBoardXmlParser(filePart.getInputStream());
//            Game game = new Game(gameBoardXmlParser);
//            GameManager gameManager = ServletUtils.getGameManager(getServletContext());
//            gameManager.addGame(game, request.getParameter(USERNAME));
//        } catch (GameDefinitionsXmlParserException | GameServiceException e) {
//            response.setStatus(400);
//            try (PrintWriter out = response.getWriter()) {
//                sendErrorJsonResponse(out, e.getMessage());
//            }
//        }
//    }
//}