package org.example.demo5.teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo5.DBConfig;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used when teacher click "Enrollments" button to check the student enrollment information in each course.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/Enrollments")
public class Enrollments extends HttpServlet {

	// JDBC driver name and database URL
	private DBConfig dbConfig;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			//database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbConfig.DB_URL, dbConfig.USER, dbConfig.PASSWORD);

			// Getting parameter
			String course_id = request.getParameter("courseId");

			//find enrollment information based on a given course id
			String sql = "SELECT * FROM enrollments WHERE course_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, course_id);
			
			ResultSet rs = ps.executeQuery();
			List<String[]> courseData = new ArrayList<String []>();

			//store enrollment information to a list, namely courseData
			while (rs.next()) {
				String arr[] = {rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6)};
				courseData.add(arr);
			}
			rs.close();
			con.close();

			//send request dispatcher to enrollments.jsp to display information
			request.setAttribute("courseId", course_id);
			request.setAttribute("courses", courseData);
			request.getRequestDispatcher("enrollments.jsp").forward(request, response);
			
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
