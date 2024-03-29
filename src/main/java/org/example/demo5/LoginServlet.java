package org.example.demo5;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * This class is used when user click "login" button. It will determine the type of user and display different dashboard.
 * @author Yifang
 * @version 1.0
 **/
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			//if the login failed, we will simply display a message
			PrintWriter out = response.getWriter();

			//database connection
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursesdb3?autoReconnect=true&useSSL=false", "root", "Xw2583317361-");
			
			String name = request.getParameter("name");
			String pwd = request.getParameter("pwd");
			PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user_name = ? AND pass_word = ?");
			
			ps.setString(1, name);
			ps.setString(2, pwd);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				// Getting user's informations
				String full_name = rs.getString("full_name");
				String utype = rs.getString("utype");
				String user_name = rs.getString("user_name");
				String user_id = rs.getString("user_id");
				
				// Storing user credentials into session
				HttpSession session = request.getSession(true);
				session.setAttribute("full_name", full_name);
				session.setAttribute("user_name", user_name);
				session.setAttribute("user_id", user_id);
				
				// Rendering pages based on user type
				switch(rs.getString("utype")) {
					case "admin": {
						response.sendRedirect("Admin");
						break;
					}
					case "teacher": {
						response.sendRedirect("Teacher");
						break;
					}
					case "student": {
						response.sendRedirect("Student");
						break;
					}
				}

			} else {
				out.println("Login failed!");
				out.println("Try again!!");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
