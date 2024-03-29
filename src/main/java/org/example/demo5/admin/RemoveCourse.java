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
 * This class is used when admin click "remove" button in admin desk.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/RemoveCourse")
public class RemoveCourse extends HttpServlet {

	// JDBC driver name and database URL
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
			String course_id = request.getParameter("id");

			//delete course information in courses table
			String sql = "DELETE FROM courses WHERE course_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, course_id);
			ps.executeUpdate();

			//delete course information in enroll table
			String sql1 = "DELETE FROM enroll WHERE course_id = ?";
			PreparedStatement ps1 = con.prepareStatement(sql1);
			ps1.setString(1, course_id);
			ps1.executeUpdate();

			//delete course information in enrollments table
			String sql2 = "DELETE FROM enrollments WHERE course_id = ?";
			PreparedStatement ps2 = con.prepareStatement(sql2);
			ps2.setString(1, course_id);
			ps2.executeUpdate();
			
			con.close();

			//send redirect to Admin servlet
			response.sendRedirect("Admin");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
