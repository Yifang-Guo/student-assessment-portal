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
 * This class is used when a student logged in, and related information should be retrieved from database
 * and pass them to home-student.jsp
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/Student")
public class Student extends HttpServlet {

	// JDBC driver name and database URL
	private DBConfig dbConfig;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Getting user's credentials from session
		String user_name = (String)request.getSession(false).getAttribute("user_name");
		String full_name = (String)request.getSession(false).getAttribute("full_name");
		String user_id = (String)request.getSession(false).getAttribute("user_id");
		
		try {
			//database connection
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);
			
			// Fetching course informations for student
			String sql = "SELECT * FROM enroll WHERE user_name = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user_name);
			
			ResultSet rs = ps.executeQuery();
			// Storing course informations
			List<String[]> courseData = new ArrayList<String[]>();
			
			while (rs.next()) {
				String course_id = rs.getString(1);
				String course_name = rs.getString(2);
				String course_teacher = rs.getString(3);

				// Getting teacher's full name
				String sql2 = "SELECT full_name FROM users WHERE user_name = ?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, course_teacher);

				ResultSet rs2 = ps2.executeQuery();
				String course_teacher_full_name = course_teacher;

				if (rs2.next()) {
					course_teacher_full_name = rs2.getString(1);
				}
				rs2.close();

				String arr[] = {course_id, course_name, course_teacher_full_name};
				courseData.add(arr);
			}
			rs.close();
			con.close();
			
			// Passing data to home-student.jsp page
			request.setAttribute("userid", user_id);
			request.setAttribute("username", user_name);
			request.setAttribute("fullname", full_name);
			request.setAttribute("courses", courseData);
			
			request.getRequestDispatcher("home-student.jsp").forward(request, response);
			
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
