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
 * This class is used when student click "enroll for courses" button. It will get enrolled courses information.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/Enroll")
public class Enroll extends HttpServlet {

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
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

			//find the course information which is not enrolled by student
			String sql3 = "SELECT * FROM courses WHERE course_id NOT IN(SELECT course_id FROM enroll WHERE user_name= ?)";
			PreparedStatement ps3 = con.prepareStatement(sql3);
			ps3.setString(1, user_name);
			
			ResultSet rs3 = ps3.executeQuery();
			List<String[]> courseData = new ArrayList<String[]>();

			//store the course information to list, namely courseData
			while (rs3.next()) {
				String course_id = rs3.getString(1);
				String course_name = rs3.getString(2);
				String course_teacher_user_name = rs3.getString(3);
				
				// Getting teacher's full name
				String sql2 = "SELECT full_name FROM users WHERE user_name = ?";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, course_teacher_user_name);
		
				ResultSet rs2 = ps2.executeQuery();
				String course_teacher_full_name = "";
				
				if (rs2.next()) {
					course_teacher_full_name = rs2.getString(1);
				}
				rs2.close();
				
				String arr[] = {course_id, course_name, course_teacher_full_name};
				courseData.add(arr);
			}
			rs3.close();
			con.close();
			
			request.setAttribute("fullname", full_name);
			request.setAttribute("userid", user_id);
			request.setAttribute("username", user_name);
			request.setAttribute("courses", courseData);
			
			request.getRequestDispatcher("enroll.jsp").forward(request, response);
			
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
