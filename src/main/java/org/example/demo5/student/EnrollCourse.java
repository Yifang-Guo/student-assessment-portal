package org.example.demo5.student;

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
 * This class is used when student click "enroll for this course" button.
 * We will save the enroll and enrollment information to our database.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/EnrollCourse")
public class EnrollCourse extends HttpServlet {

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
			String course_name = request.getParameter("courseName");
			String course_teacher = request.getParameter("courseTeacher");

			//insert enroll information to database
			String sql = "INSERT INTO enroll VALUES(?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, course_id);
			ps.setString(2, course_name);
			ps.setString(3, course_teacher);
			ps.setString(4, user_name);
			
			ps.executeUpdate();

			//insert enrollment information to database
			String sql1 = "INSERT INTO enrollments (st_id, st_name, course_id) VALUES(?, ?, ?)";
			PreparedStatement ps1 = con.prepareStatement(sql1);
			
			ps1.setString(1, user_id);
			ps1.setString(2, full_name);
			ps1.setString(3, course_id);
			ps1.executeUpdate();

			ps.close();
			ps1.close();
			con.close();

			//send redirect information to Student servlet, student can check the updated enroll information
			response.sendRedirect("Student");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
