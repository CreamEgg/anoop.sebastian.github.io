<%@ page
	import="com.dcu.ee417.model.User,com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book"%>
<%@ include file="header.jsp"%>

<body>

	<!-- The fixed title bar on top -->
	<%@ include file="fixedTitlebar.jsp"%>
	<%
		Object object = session.getAttribute("user");

		Book book = (Book) request.getAttribute("book");
		if (book != null) {
	%>
	<div class="container">
		<div class="row spacer">
			<div id="rentstatus" class="col-md-8">
				<h1 class="page-header"><%=book.getTitle()%></h1>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<img class="img-responsive" src="<%=book.getCoverPath()%>">
			</div>
			<div class="col-md-8">
				<%
					if (book.getQuantity() > 0) {
							if (object == null) {
				%>
				<p>
					<button id="loginbeforerent" type="button"
						class="btn btn-success btn-lg">
						<small>Sign in to rent</small>
						<%
							if (book.getTitle().length() > 20) {
						%>
						<%=book.getTitle().substring(0, 20)%>
						<%
							} else {
						%>
						<%=book.getTitle()%>
						<%
							}
						%>
					</button>
				</p>
				<%
					//TODO use ajax to handle renting, reload the page after renting
							} else if (object.getClass().equals(Librarian.class)) {
				%>
				<p>
					<button id="rentButton" type="button" disabled="disabled"
						class="btn btn-success btn-lg">
						Rent
						<%=book.getTitle()%></button>
				</p>

				<%
					} else {
								User user = (User) object;
				%>
				<p>
					<button id="rentButton" type="button"
						data-userid="<%=user.getId()%>" data-isbn="<%=book.getISBN()%>"
						class="btn btn-success btn-lg">
						Rent
						<%=book.getTitle()%></button>
				</p>
				<%
					}
				%>
				<%
					} else {
				%>
				<p>
					<button type="button" disabled="disabled"
						class="btn btn-warning btn-lg">
						Rent
						<%=book.getTitle()%></button>
				</p>
				<%
					}
				%>
				<h3 class="text-left">ISBN</h3>
				<p class="text-left"><%=book.getISBN()%></p>
				<hr>
				<h3 class="text-left">Author</h3>
				<p class="text-left"><%=book.getAuthor()%></p>
				<hr>
				<h3 class="text-left">Description</h3>
				<p class="text-left"><%=new String(book.getDescription())%>...
				</p>
				<hr>
				<h3 class="text-left">Category</h3>
				<p class="text-left"><%=book.getCategory()%></p>
				<hr>
				<h3 class="text-left">Copies left in the library</h3>
				<p id="quantity" class="text-left"><%=book.getQuantity()%></p>
			</div>
		</div>
	</div>
	<%
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	%>

</body>
</html>