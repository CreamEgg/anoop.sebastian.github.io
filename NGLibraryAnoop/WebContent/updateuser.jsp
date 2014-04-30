<%@ page
	import="com.dcu.ee417.model.User,com.dcu.ee417.model.Transaction,
    com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.Set,java.util.List"%>
<%@ include file="header.jsp"%>

<body>

	<!-- The fixed title bar on top -->
	<%@ include file="fixedTitlebar.jsp"%>
	<%
		Librarian admin = (Librarian) session.getAttribute("user");

		User user = (User) request.getAttribute("usertoupdate");
		Set<Book> books = (Set<Book>) request.getAttribute("books");
		if (user != null && books != null && !books.isEmpty()) {
	%>
	<div class="container">
		<div class="row spacer">
			<h1 class="page-header"><%=user.getFirstName()%>'s Books
			</h1>
			<div id="updateStatus"></div>
		</div>
		<%
			for (Book b : books) {
		%>
		<div class="row">
		 <div class="col-md-2">
                                <a href="view?action=view&isbn=<%=b.getISBN()%>"><img height="300" width="200"
                                    class="img-responsive img-thumbnail"
                                    src="<%=b.getCoverPath()%>"></a>
                            </div>
			<div class="col-lg-8">
			<p>
                    <small>ISBN</small>
                    <strong><%=b.getISBN()%></strong></p>
                <p>
                <p>
                    <small>Rent Date</small>
                    <strong><%=b.getRentDate()%></strong></p>
                <p>
				<p>
					<small>Title</small>
					<strong><%=b.getTitle()%></strong></p>
				<p>
					<small>Expiry Date</small>
					<strong><%=b.getExpiryDate()%></strong></p>
					<form method="post" action="profile">
					<input type="hidden" name="action" value="updatedate">
					<input type="hidden" name="bookid" value="<%=b.getISBN()%>">
					<input type="hidden" name="userid" value="<%=user.getId()%>">
				<div class="input-group">
					<span class="input-group-btn">
						<button id="updateDate" class="btn btn-default" type="submit" data-userid="<%=user.getId()%>" data-isbn="<%=b.getISBN()%>">
						Days to increment</button>
					</span> <input id="days" type="number" name="days" class="form-control" required="required" placeholder="e.g enter +5 to increment or -5 to decrement">
				</div>
				</form>
			</div>
		</div>
		<hr>
		<%
			}
		%>

	</div>
	<%
		} else {
			response.setStatus(response.SC_NO_CONTENT);
			response.setHeader("Location",
					"profile?action=viewrecords&userid=" + admin.getId()
							+ "&adminid=" + admin.getLibrarianID());
		}
	%>
</body>
</html>