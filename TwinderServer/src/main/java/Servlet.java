import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Servlet", value = "/Servlet")
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    final int requestValue1 = 1;
    final int requestValue2 = 5000;
    final int requestValue3 = 1000000;
    final int characterValue = 256;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("bbbbbbbbbb");
        response.setContentType("application/json");
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        String s;

        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }

        Swipe req = (Swipe) gson.fromJson(sb.toString(), Swipe.class);
//        System.out.println("aaaaaaaaa");

        if (!isValid(req)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Valid Url!");
        }
    }

    private boolean isValid(Swipe req){
        if (Integer.valueOf(req.getSwiper()) < requestValue1 || Integer.valueOf(req.getSwiper()) > requestValue2) {
            return false;
        }

        if (Integer.valueOf(req.getSwipee()) < requestValue1 || Integer.valueOf(req.getSwipee()) > requestValue3) {
            return false;
        }

        if (req.getComment().length() > characterValue) {
            return false;
        }

        return true;
    }
}
