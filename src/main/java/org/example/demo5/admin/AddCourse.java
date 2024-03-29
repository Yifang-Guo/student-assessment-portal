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
 * This class is used for adding courses if an admin has logged in.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/AddCourse")
public class AddCourse extends HttpServlet {

	private DBConfig dbConfig;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	//handle post request from home-admin.jsp
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			//database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);
			
			// Getting course informations from parameters
			String course_id = request.getParameter("courseId");
			String course_name = request.getParameter("courseName");
			String course_teacher = request.getParameter("courseTeacher");
			String course_semester = request.getParameter("courseSemester");

			//insert newly added course information into database
			String sql = "INSERT INTO courses VALUES(?, ?, ? , ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, course_id);
			ps.setString(2, course_name);
			ps.setString(3, course_semester);
			ps.setString(4, course_teacher);

			//execute update query, close data base connection, and send redirect to Admin servlet
			ps.executeUpdate();
			con.close();
			response.sendRedirect("Admin");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
