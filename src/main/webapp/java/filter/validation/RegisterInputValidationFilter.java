package filter.validation;

import entity.User;
import utils.fieldchecker.AuthFieldChecker;
import utils.fieldchecker.error.ErrorCode;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterInputValidationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        String userLogin = request.getParameter("login").trim();
        String userPassword = request.getParameter("password").trim();
        String userPasswordRepeat = request.getParameter("cpassword").trim();
        String userEmail = request.getParameter("email").trim();

        User newUser = new User(0, userLogin);
        newUser.setEmail(userEmail);
        AuthFieldChecker checker = new AuthFieldChecker();

        boolean hasErrors = false;
        ErrorCode loginError = checker.checkLogin(userLogin);
        if (loginError.isError()) {
            hasErrors = true;
            session.setAttribute("loginError", loginError.getMessage());
        }
        ErrorCode emailError = checker.checkEmail(userEmail);
        if (emailError.isError()) {
            hasErrors = true;
            session.setAttribute("emailError", emailError.getMessage());
        }
        ErrorCode passwordError = checker.checkPassword(userPassword, userPasswordRepeat);
        if (passwordError.isError()) {
            hasErrors = true;
            session.setAttribute("passwordError", passwordError.getMessage());
        }

        if (hasErrors) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/register");
            session.setAttribute("incorrectUserData", newUser);
        }
        else {
            request.setAttribute("login", userLogin);
            request.setAttribute("password", userPassword);
            request.setAttribute("email", userEmail);
            filterChain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {

    }
}
