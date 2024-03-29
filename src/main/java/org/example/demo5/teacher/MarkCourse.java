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
 * This class is used when teacher click "grade" button to set the grade for students.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/MarkCourse")
public class MarkCourse extends HttpServlet {

    // JDBC driver name and database URL
    private DBConfig dbConfig;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

            // Getting course informations from parameters
            String st_id = request.getParameter("st_id");
            String course_id = request.getParameter("courseId");
            String quiz = request.getParameter("quiz");
            String assignment = request.getParameter("assignment");
            String final_exam = request.getParameter("final_exam");

            //update quiz, assignment and final score in database
            String sql = "UPDATE enrollments SET quiz = ?, assignment = ?, final = ? WHERE st_id = ? AND course_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, quiz);
            ps.setString(2, assignment);
            ps.setString(3, final_exam);
            ps.setString(4, st_id);
            ps.setString(5, course_id);

            ps.executeUpdate();
            con.close();

            //send redirect to teacher home page
            response.sendRedirect("Teacher");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }
}
