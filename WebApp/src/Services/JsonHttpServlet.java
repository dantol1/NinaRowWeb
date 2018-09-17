package Services;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.APPLICATION_JSON;

/**
 * Created by ido on 17/10/2016.
 */
public abstract class JsonHttpServlet extends HttpServlet {
    protected Gson gson = new Gson();

    protected class ErrorJsonResponse {
        public boolean error = true;
        public String message;
    }

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON);
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(APPLICATION_JSON);
        processRequest(request, response);
    }

    protected void sendErrorJsonResponse(PrintWriter out, String message) {
        ErrorJsonResponse errorJsonResponse = new ErrorJsonResponse();
        errorJsonResponse.message = message;
        String jsonResponse = gson.toJson(errorJsonResponse);
        out.print(jsonResponse);
        out.flush();

    }

    protected class SuccessJsonResponse {
        public boolean success = true;
    }
}
