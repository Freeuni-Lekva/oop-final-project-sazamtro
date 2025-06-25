package Servlets;

@WebServlet("/SignupServlet")
public class SingupServlet {
}


package servlets;


@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        ServletContext context = request.getServletContext();
        UserDAO userDAO = (UserDAO)context.getAttribute("users");
        if(userDAO.userExists(username)) {
            response.sendRedirect("usedName.jsp");
        }
        else {
            User newUser = null;
            try {
                newUser = new User(username, password);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            userDAO.addUser(newUser);
            response.sendRedirect("homepage.jsp");
        }
    }
}