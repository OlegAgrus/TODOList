package listener;

import com.mysql.jdbc.Driver;
import dao.DAO;
import dao.GroupDAO;
import dao.UserDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.*;

public class ServletContextListenerClass implements ServletContextListener {

    private String login;
    private String password;
    private String url = "jdbc:mysql://localhost:3306/todolist_database?useUnicode=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private Connection connection;

    public void contextInitialized(ServletContextEvent event) {

        ServletContext context = event.getServletContext();

        login = context.getInitParameter("dbLogin");
        password = context.getInitParameter("dbPassword");

        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(url, login, password);
            if (!connection.isClosed()) {
                System.out.println("Connected");
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }

        context.setAttribute("dataBaseConnection", connection);

        DAO.setConnection(connection);
    }

    public void contextDestroyed(ServletContextEvent event) {
        try {
            connection.close();
            if (connection.isClosed()) {
                System.out.println("Closed");
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}