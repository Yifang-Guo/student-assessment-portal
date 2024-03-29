package org.example.demo5.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo5.DBConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is used for adding users if an admin has logged in.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/AddUser")
public class AddUser extends HttpServlet {

    private DBConfig dbConfig;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

            // Getting user informations from parameters
            String user_id = request.getParameter("userId");
            String user_name = request.getParameter("userName");
            String password = request.getParameter("password");
            String userType = request.getParameter("userType");
            String fullName = request.getParameter("fullName");

            //insert newly added user information into database
            String sql = "INSERT INTO users VALUES(?, ?, ? , ? , ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user_id);
            ps.setString(2, user_name);
            ps.setString(3, password);
            ps.setString(4, userType);
            ps.setString(5, fullName);

            //execute update query, close data base connection, and send redirect to Admin servlet
            ps.executeUpdate();
            con.close();
            response.sendRedirect("Admin");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
