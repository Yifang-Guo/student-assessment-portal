package org.example.demo5.teacher;

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
 * This class is used when teacher click "Register for this course" button.
 * We will update course information in our database.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/RegisterCourse")
public class RegisterCourse extends HttpServlet {
    // JDBC driver name and database URL
    private DBConfig dbConfig;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Getting user's credentials from session
        String user_name = (String)request.getSession(false).getAttribute("user_name");
        String full_name = (String)request.getSession(false).getAttribute("full_name");
        String user_id = (String)request.getSession(false).getAttribute("user_id");

        try {
            //database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

            // Getting enrolled course's informations
            String course_id = request.getParameter("courseId");

            //update course teacher based on course id
            String sql = "UPDATE courses SET course_teacher = ? where course_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user_name);
            ps.setString(2, course_id);

            ps.executeUpdate();
            con.close();

            //send redirect to Teacher servlet
            response.sendRedirect("Teacher");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
