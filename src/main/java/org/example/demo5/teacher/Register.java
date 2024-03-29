package org.example.demo5.teacher;

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
 * This class is used when teacher click "register courses" button. It will display unassigned courses to be registered.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/Register")
public class Register extends HttpServlet {
    private DBConfig dbConfig;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get user information from session
        String user_name = (String)request.getSession(false).getAttribute("user_name");
        String full_name = (String)request.getSession(false).getAttribute("full_name");
        String user_id = (String)request.getSession(false).getAttribute("user_id");

        try {
            //database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

            //find courses where no course teacher
            String sql = "SELECT * FROM courses WHERE LENGTH(course_teacher) = 0";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            List<String[]> courseData = new ArrayList<String[]>();

            //store course information to a list, namely courseData
            while (rs.next()){
                String course_id = rs.getString(1);
                String course_name = rs.getString(2);
                String course_semester = rs.getString(3);

                String arr[] = {course_id, course_name, course_semester};
                courseData.add(arr);
            }

            rs.close();
            con.close();

            //send request dispatcher to register.jsp
            request.setAttribute("fullname", full_name);
            request.setAttribute("userid", user_id);
            request.setAttribute("username", user_name);
            request.setAttribute("courses", courseData);
            request.getRequestDispatcher("register.jsp").forward(request, response);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
