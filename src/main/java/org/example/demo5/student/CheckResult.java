package org.example.demo5.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo5.DBConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used when student click "check result" button to check their grade in each course.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/CheckResult")
public class CheckResult extends HttpServlet {

    // JDBC driver name and database URL
    private DBConfig dbConfig;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

            //get student id and course id from request parameters
            String st_id = request.getParameter("userid");
            String course_id = request.getParameter("course_id");

            //get course grades based on student id and course id and store them to a list, namely resultData
            String sql = "SELECT * from enrollments where st_id = ? and course_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, st_id);
            ps.setString(2, course_id);

            ResultSet rs = ps.executeQuery();
            List<String[]> resultData = new ArrayList<String []>();

            while (rs.next()) {
                String arr[] = {rs.getString(4), rs.getString(5), rs.getString(6)};
                resultData.add(arr);
            }
            rs.close();
            con.close();

            //send request dispatcher to result.jsp to show the result information
            request.setAttribute("results", resultData);
            request.getRequestDispatcher("result.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
