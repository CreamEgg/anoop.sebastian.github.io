package com.dcu.ee417.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dcu.ee417.model.Book;
import com.dcu.ee417.model.Transaction;
import com.dcu.ee417.util.HibernateUtil;
import com.dcu.ee417.util.Utils;

@WebServlet("/view")
public class BookViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().print("BookView Working");
		if ("view".equals(request.getParameter("action"))) {
			String isbn = request.getParameter("isbn");
			Book book = HibernateUtil.getBook(isbn);
			request.setAttribute("book", book);
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/detailedview.jsp");
		dispatcher.forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		if ("rent".equals(request.getParameter("action"))) {
			String isbn = request.getParameter("isbn");
			String userId = request.getParameter("userid");
			
			Transaction transaction = new Transaction(Utils.formatDate(new Date()), userId, isbn, "rent");
			System.out.println("Rented book"+transaction.getBookID()+" on "+ transaction.getTransactionDate());
			boolean rentStatus = HibernateUtil.rentBook(transaction);
			
			if (rentStatus) {
				response.getWriter().print("success"); 
				System.out.println("Book rented successfully");
				return;
			} else if(!rentStatus) {
				response.getWriter().print("fail");
				System.out.println("Book rented failed");
			}

		}
		
		if ("return".equals(request.getParameter("action"))) {
			String isbn = request.getParameter("isbn");
			String userId = request.getParameter("userid");
			
			Transaction transaction = new Transaction(Utils.formatDate(new Date()), userId, isbn, "return");
			HibernateUtil.returnBook(transaction);
			
			response.sendRedirect(request.getHeader("referer"));
		}
	}

}
