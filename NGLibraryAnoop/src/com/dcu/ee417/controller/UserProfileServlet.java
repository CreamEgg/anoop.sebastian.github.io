package com.dcu.ee417.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dcu.ee417.model.Book;
import com.dcu.ee417.model.Librarian;
import com.dcu.ee417.model.Transaction;
import com.dcu.ee417.model.User;
import com.dcu.ee417.util.HibernateUtil;
import com.dcu.ee417.util.Utils;
@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().print("User Profile Servlet Working");
		
		// user profile books
		if ("viewbooks".equals(request.getParameter("action"))) {
			
			if(request.getSession(false) == null || request.getSession().getAttribute("user")==null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			
			String id = request.getParameter("userid");
			Integer userID = Utils.parseIntFromString(id);
			
			Object object = request.getSession(false).getAttribute("user");
			if(! object.getClass().equals(User.class)) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			User user = (User) object;
			
			// if the parameter userid and the session userid matches
			if(userID!=null && userID == user.getId()) {
				
				Set<Book> usersBooks = HibernateUtil.getUsersBooks(userID);
				List<Transaction> transactions = HibernateUtil.getUsersTransactions(userID);
				
				//TODO do validation in jso and display danger alerts
				request.setAttribute("books", usersBooks);
				request.setAttribute("records", transactions);
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/userbooksprofile.jsp");
				dispatcher.forward(request, response);
				return;
			}
			response.sendRedirect(request.getContextPath());
		}
		
	/*if ("viewbooks".equals(request.getParameter("action"))) {
			
			if(request.getSession(false) == null) {
				response.sendRedirect(request.getContextPath());
			}
			
			String id = request.getParameter("userid");
			Integer userID = Utils.parseIntFromString(id);
			
			// assume the type of "user" will always be User, as librarians cannot access this page
			User user = (User) request.getSession(false).getAttribute("user");
			
			// if the parameter userid and the session userid matches
			if(userID!=null && userID == user.getId()) {
				
				Set<Book> usersBooks = HibernateUtil.getUsersBooks(userID);
				List<Transaction> transactions = HibernateUtil.getUsersTransactions(userID);
				
				//TODO do validation in jso and display danger alerts
				request.setAttribute("books", usersBooks);
				request.setAttribute("records", transactions);
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/userbooksprofile.jsp");
				dispatcher.forward(request, response);
				return;
			}
			response.sendRedirect(request.getContextPath());
		}*/
		
		//admin login
		if ("viewrecords".equals(request.getParameter("action"))) {
			
			if(request.getSession(false) == null) {
				response.sendRedirect(request.getContextPath());
			}
			
			String userid = request.getParameter("userid");
			String adminid = request.getParameter("adminid");
			
			Integer userID = Utils.parseIntFromString(userid);
			Integer adminID = Utils.parseIntFromString(adminid);
			
			// assume the type of "user" will always be User, as librarians cannot access this page
			Object object = request.getSession(false).getAttribute("user");
			if(!object.getClass().equals(Librarian.class)) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			Librarian admin = (Librarian) request.getSession(false).getAttribute("user");
			
			if(admin==null) response.sendRedirect(request.getContextPath());
			
			// if the parameter userid and the session userid matches
			if((userID!=null && userID == admin.getId()) && (adminID != null && adminID == admin.getLibrarianID())) {
				
				List<User> users = HibernateUtil.getUsers();
				List<Transaction> transactions = HibernateUtil.getAllTransactions();
				List<Book> rentedBooks = HibernateUtil.getRentedBooks();
				//TODO do validation in jso and display danger alerts
				request.setAttribute("users", users);
				request.setAttribute("records", transactions);
				request.setAttribute("rented",rentedBooks );
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admindashboard.jsp");
				dispatcher.forward(request, response);
				return;
			}
			response.sendRedirect(request.getContextPath());
		}
		
		if("updateprofile".equals(request.getParameter("action"))) {
			
			Integer userID = Utils.parseIntFromString(request.getParameter("userid"));
			
			if(request.getSession(false) == null || userID == null || 
					request.getSession().getAttribute("user")==null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			
			request.setAttribute("usertoupdate", HibernateUtil.getUser(userID));
			request.setAttribute("books", HibernateUtil.getUsersBooks(userID));
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/updateuser.jsp");
			dispatcher.forward(request, response);
			return;
		}
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if("updatedate".equals(request.getParameter("action"))) {
			
			Integer userID = Utils.parseIntFromString(request.getParameter("userid"));
			Integer isbn = Utils.parseIntFromString(request.getParameter("bookid"));
			Integer days = Utils.parseIntFromString(request.getParameter("days"));
			
			if(null == request.getSession(false) || null == request.getSession().getAttribute("user")) {
				response.sendRedirect(request.getContextPath());
			} 
			else if(userID == null || isbn == null || days == null) {
				//response.getWriter().print("fail");
				response.sendRedirect(request.getHeader("referer"));
				return;
			}			
		
			System.out.println("id; "+ userID+" isbn"+isbn+" days: "+ days);
			String updateStatus = "fail";
			try {
				updateStatus = HibernateUtil.updateDateForUser(userID, isbn, days);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			System.out.println("Book date update status: "+ updateStatus);
			//response.getWriter().print(updateStatus);
			response.sendRedirect(request.getHeader("referer"));
			return;
		}
	}

}
