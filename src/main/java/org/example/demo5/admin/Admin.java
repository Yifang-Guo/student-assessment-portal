package org.example.demo5.admin;

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
 * This class is used when an admin logged in, and related information should be retrieved from database
 * and pass them to home-admin.jsp
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/Admin")
public class Admin extends HttpServlet {

	// JDBC driver configuration
	private DBConfig dbConfig;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Getting user's information from session
		String user_name = (String)request.getSession(false).getAttribute("user_name");
		String full_name = (String)request.getSession(false).getAttribute("full_name");

		try {
			//database connection
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

			// Fetching course information
			String sql = "SELECT * FROM courses";
			PreparedStatement ps = con.prepareStatement(sql);

			// Fetching user information
			String sql3 = "SELECT * FROM users";
			PreparedStatement ps3 = con.prepareStatement(sql3);

			// Storing course information
			List<String[]> courseData = new ArrayList<>();
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				//get course information from database
				String course_id = rs.getString(1);
				String course_name = rs.getString(2);
				String course_semester = rs.getString(3);
				String course_teacher_user_name = rs.getString(4);

				// Getting teacher's full name
				String sql2 = "SELECT full_name FROM users WHERE user_name = ?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, course_teacher_user_name);

				ResultSet rs2 = ps2.executeQuery();
				String course_teacher_full_name = course_teacher_user_name;

				if (rs2.next()) {
					course_teacher_full_name = rs2.getString(1);
				}
				rs2.close();

				String arr[] = {course_id, course_name, course_semester, course_teacher_full_name};
				courseData.add(arr);
			}

			// Storing users information
			List<String[]> userData = new ArrayList<>();
			ResultSet rs3 = ps3.executeQuery();

			while (rs3.next()){
				//get user information from database
				String user_id = rs3.getString(1);
				String userName = rs3.getString(2);
				String password = rs3.getString(3);
				String user_type = rs3.getString(4);
				String fullName = rs3.getString(5);

				String arr2[] = {user_id, userName, password, user_type, fullName};
				userData.add(arr2);
			}

			rs.close();
			rs3.close();
			con.close();

			// passing data to home-admin.jsp page
			request.setAttribute("user_name", user_name);
			request.setAttribute("full_name", full_name);
			request.setAttribute("courses", courseData);
			request.setAttribute("users", userData);
			request.getRequestDispatcher("home-admin.jsp").forward(request, response);


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
