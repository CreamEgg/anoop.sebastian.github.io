package com.dcu.ee417.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dcu.ee417.model.Librarian;
import com.dcu.ee417.model.User;
import com.dcu.ee417.util.HibernateUtil;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/report.jsp");
		//dispatcher.forward(request, response);
		response.getWriter().print("Login Servlet Working");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if ("register".equals(request.getParameter("action"))) {

			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			User user = new User(firstName,lastName,email,password);
			String userRegisterStatus = HibernateUtil.addUser(user);
			
			if("success".equals(userRegisterStatus)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", user);
				System.out.println("New user session set: "+new Date(session.getCreationTime()));
			}
			
			response.getWriter().print(userRegisterStatus);
			return;
		}
		
		if ("registeradmin".equals(request.getParameter("action"))) {
			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			int librarianID = -1;
			
			try {
				librarianID = Integer.parseInt(request.getParameter("adminid"));
			} catch(NumberFormatException e) {
				response.getWriter().print("badid");
			}
			Librarian admin = new Librarian(firstName,lastName,email,password,librarianID); 
			String adminRegisterStatus = HibernateUtil.addUser(admin);
			
			if(adminRegisterStatus.equals("success")) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", admin);
			}			
			response.getWriter().print(adminRegisterStatus);
			return;
		}

		if ("signin".equals(request.getParameter("action"))) {			
			User user = HibernateUtil.validateUser(request.getParameter("email"),
					request.getParameter("password"));
			
			String status = (user == null) ? "fail": "success";
			
			if(status.equals("success")) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", user);
				System.out.println(user);
				System.out.println("Session created at: "+ session.getCreationTime());
			}
			response.getWriter().print(status);
			return;
		}
		
		if ("signinadmin".equals(request.getParameter("action"))) {			
			Librarian admin = HibernateUtil.validateAdmin(request.getParameter("email"),
					request.getParameter("password"));
			
			String status = (admin == null) ? "fail": "successadmin";
			
			if(status.equals("successadmin")) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user", admin);
				System.out.println(admin);
				System.out.println("Session created at: "+ session.getCreationTime());
			}
			response.getWriter().print(status);
			return;
		}
		if("logout".equals(request.getParameter("action"))) {
			System.out.println("Logout called");
			//To avoid IE caching
			response.setHeader("Cache-Control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");

			HttpSession session = request.getSession(false);
			
//			System.out.println("Logging out session: "+session.getId());
//			System.out.println("Session created at: "+ new Date(session.getCreationTime()));
//			System.out.println("Session servlet context: "+ session.getServletContext());
			
			if(session != null) session.invalidate();
			response.sendRedirect(request.getContextPath());
		}
		System.out.println("just post called");
	}
}

