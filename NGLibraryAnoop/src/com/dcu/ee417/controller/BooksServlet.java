package com.dcu.ee417.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dcu.ee417.util.HibernateUtil;

@WebServlet("/books")
public class BooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.getWriter().print("Books Servlet Working");
		
		if ("search".equals(request.getParameter("action"))) {
			String query = request.getParameter("query");

			request.setAttribute("booksbytitle", HibernateUtil.getBooksByTitle(query));
			request.setAttribute("booksbyauthor", HibernateUtil.getBooksByAuthor(query));
			request.setAttribute("query", query);

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/searchresults.jsp");
			dispatcher.forward(request, response);
			return;
		}
		
		request.setAttribute("fictionbooks", HibernateUtil.getBooksByCategory("Fiction"));
		request.setAttribute("biobooks", HibernateUtil.getBooksByCategory("Biography"));
		request.setAttribute("business", HibernateUtil.getBooksByCategory("Business"));
		request.setAttribute("popular", HibernateUtil.getBooksByCategory("Popular"));
		request.setAttribute("tech", HibernateUtil.getBooksByCategory("Technology"));
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/books.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
