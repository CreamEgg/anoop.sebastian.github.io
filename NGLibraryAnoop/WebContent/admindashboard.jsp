<%@ page
	import="com.dcu.ee417.model.User,com.dcu.ee417.model.Transaction,
    com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.Set,java.util.List"%>

<%@ include file="header.jsp"%>


<!-- The fixed title bar on top -->
<%@ include file="fixedTitlebar.jsp"%>
<%
	Librarian user = (Librarian) session.getAttribute("user");
	List<Transaction> records = (List<Transaction>) request.getAttribute("records");
	List<User> users = (List<User>) request.getAttribute("users");
	List<Book> books = (List<Book>) request.getAttribute("rented");
%>

<div class="container">
	<div class="row spacer">
		<div class="col-md-2">
			<img class="img-responsive img-rounded"
				src="http://rolltechbowling.com/Content/images/rt-icon-user-b.jpg">
		</div>
		<div class="col-md-6">
			<h3 class="text-left"><small>Name: </small><strong><%=user.getFirstName()%>
                <%=user.getLastName()%></strong>
			</h3>
			
			<h3 class="text-left"><small>Email: </small><strong><%=user.getEmail()%></strong>
            </h3>
            
            <h3 class="text-left"><small>Librarian ID: </small><strong><%=user.getLibrarianID()%></strong>
            </h3>
            
		</div>
	</div>
	<div class="row spacer">
		<div class="col-lg-12">
			<h4>Library Dashboard</h4>
			<ul class="nav nav-tabs nav-justified">
				<li class="active"><a href="#records" data-toggle="tab"
					data-url="#">Transaction Records</a></li>
				<li class=""><a href="#books" data-toggle="tab" data-url="#">Currently
						Rented Books</a></li>
				<li class=""><a href="#users" data-toggle="tab" data-url="#">Users</a></li>
				<li class=""><a href="#assign" data-toggle="tab" data-url="#">Assign
						a book</a></li>
			</ul>
			<div class="tab-content">
				<div id="records" class="tab-pane active">
					<%
						if (records == null || records.isEmpty()) {
					%>
					<div class="alert alert-info">No transactions yet man, We
						just opened up..</div>

					<%
						} else {
					%>
					<div class="row spacer">
						<div class="col-md-12">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<td>Transaction ID</td>
											<td>Date</td>
											<td>Transaction Type</td>
											<td>Book ISBN</td>
											<td>User ID</td>
										</tr>
									</thead>
									<tbody>
										<%
											for (Transaction t : records) {
													if (t.getTransactionType().equals("rent")) {
										%>
										<tr class="success">
											<td><%=t.getTransactionID()%></td>
											<td><%=t.getTransactionDate()%></td>
											<td><%=t.getTransactionType()%></td>
											<td><%=t.getBookID()%></td>
											<td><%=t.getUserID()%></td>
										</tr>
										<%
											} else if (t.getTransactionType().equals("return")) {
										%>
										<tr class="info">
											<td><%=t.getTransactionID()%></td>
											<td><%=t.getTransactionDate()%></td>
											<td><%=t.getTransactionType()%></td>
											<td><%=t.getBookID()%></td>
											<td><%=t.getUserID()%></td>
										</tr>
										<%
											}
												}
										%>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<%
						}
					%>

				</div>

				<div id="books" class="tab-pane">
					<%
						if (books == null || books.isEmpty()) {
					%>
					<div class="alert alert-info">Nobody has rented a book yet.</div>
					<%
						} else {
							for (Book b : books) {
					%>
					<div class="row spacer">
						<div class="col-md-2">
							<a href="view?action=view&isbn=<%=b.getISBN()%>"><img
								class="img-responsive img-thumbnail" src="<%=b.getCoverPath()%>"></a>
						</div>
						<div class="col-md-10">
							<p class="text-left">
								<strong><%=b.getTitle()%></strong>
							</p>
							<p class="text-left"><%=b.getAuthor()%></p>
							<p class="text-left"><%=new String(b.getDescription())%></p>
						</div>
					</div>
					<%
						}
						}
					%>
				</div>

				<div id="users" class="tab-pane">
					<%
						if (users == null || users.isEmpty()) {
					%>
					<div class="alert alert-info">Nobody users registered yet.</div>
					<%
						} else {
							for (User u : users) {
					%>
					<div class="row spacer">
						<div class="col-md-2">
							<img class="img-responsive img-thumbnail"
								src="http://korea.fas.harvard.edu/sites/korea.fas.harvard.edu/files/imagecache/profile_page/imagefield_default_images/default_profile_icon_0.png">
						</div>
						<div class="col-md-6">
							<p class="text-left">
								<strong>ID </strong><%=u.getId()%></p>
							<p class="text-left">
								<strong>Name </strong><%=u.getFirstName()%>
								<%=u.getLastName()%></p>
							<p class="text-left">
								<strong>Email </strong><%=u.getEmail()%></p>
						</div>
						<div class="col-md-4">
							<p>
								<a href="profile?action=updateprofile&userid=<%=u.getId()%>"
									type="button" class="btn btn-info btn-sm"><small>Update
										Expiry Dates for</small> <%=u.getFirstName()%>'s books.</a>
							</p>
						</div>
						<hr>
					</div>
					<%
						}
						}
					%>

				</div>

				<div id="assign" class="tab-pane">
					<h1>Assign a book to a user.</h1>
					<hr>
					<div class="col-md-4">
						<div class="input-group input-group">
							<input id="assignid" type="text" class="form-control"
								placeholder="User ID"> <input id="assignisbn"
								type="text" class="form-control" placeholder="BOOK ISBN">
						</div>
						<hr>
						<p>
							<button id="assign" type="button" class="btn btn-success btn-lg">Assign</button>
						</p>
					</div>
					<div class="col-md-6">
						<div id="assignstatus"></div>
					</div>
				</div>
			</div>
		</div>


	</div>
</div>

</body>
</html>