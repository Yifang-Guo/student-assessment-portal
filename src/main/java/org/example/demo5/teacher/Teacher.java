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
 * This class is used when a teacher logged in, and related information should be retrieved from database
 * and pass them to home-teacher.jsp
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/Teacher")
public class Teacher extends HttpServlet {

	// JDBC driver name and database URL
	private DBConfig dbConfig;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Getting user's credentials from session
		String user_name = (String)request.getSession(false).getAttribute("user_name");
		String full_name = (String)request.getSession(false).getAttribute("full_name");
		
		try {
			//database connection
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);
			
			// Fetching course informations for teacher
			String sql = "SELECT * FROM courses WHERE course_teacher = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user_name);
			ResultSet rs = ps.executeQuery();
			
			// Storing course informations
			List<String[]> courseData = new ArrayList<String[]>();
			while (rs.next()) {
				String course_id = rs.getString(1);
				String course_name = rs.getString(2);
				
				String arr[] = {course_id, course_name, full_name};
				courseData.add(arr);
			}
			rs.close();
			con.close();
			
			// Passing data to home-teacher.jsp page
			request.setAttribute("username", user_name);
			request.setAttribute("fullname", full_name);
			request.setAttribute("courses", courseData);
			request.getRequestDispatcher("home-teacher.jsp").forward(request, response);
			
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
