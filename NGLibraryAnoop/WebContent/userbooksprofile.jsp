<%@ page
    import="com.dcu.ee417.model.User,com.dcu.ee417.model.Transaction,
    com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.Set,java.util.List"%>
<%@ include file="header.jsp"%>

<body>

    <!-- The fixed title bar on top -->
    <%@ include file="fixedTitlebar.jsp"%>
    <%
        User user = (User) session.getAttribute("user");
        Set<Book> books = (Set<Book>) request.getAttribute("books");
        List<Transaction> transactions = (List<Transaction>) request.getAttribute("records");
    %>

    <div class="container">
        <div class="row spacer">
            <div class="col-md-2">
                <img class="img-responsive img-rounded"
                    src="http://forums.foxitsoftware.com/core/images/default/default_avatar_large.png">
            </div>
            <div class="col-md-6">
                <h1 class="text-left"><%=user.getFirstName()%>
                    <%=user.getLastName()%>
                    <%
                        if (books == null || books.isEmpty()) {
                    %>
                    <span class="badge">0 Books</span>
                    <%
                        }
                        else {
                    %>
                    <span class="badge"><%=books.size()%> Books</span>
                    <%
                        }
                    %>
                </h1>
                <p class="text-left"><small>User Email </small><strong><%=user.getEmail()%></strong> </p>
                 <p class="text-left"><small>User ID </small><strong><%=user.getId()%></strong> </p>
            </div>
        </div>
        <div class="row spacer">
            <div class="col-lg-10">
                <h4><%=user.getFirstName()%>'s Books
                </h4>
                <ul class="nav nav-tabs nav-justified">
                    <li class="active"><a href="#rented" data-toggle="tab"
                        data-url="#">Rented Books</a></li>
                    <li class=""><a href="#history" data-toggle="tab" data-url="#">Returned</a></li>
                    <li class=""><a href="#duedates" data-toggle="tab"
                        data-url="#">Due Dates</a></li>
                </ul>
                <div class="tab-content">
                    <div id="rented" class="tab-pane active">
                        <%
                            if (books == null || books.isEmpty()) {
                        %>
                        <div class="alert alert-info">
                            You don't have any books rented at the moment. <a href="books"
                                class="alert-link">Check out the library</a>
                        </div>
                        <%
                            }
                            else {
                                for (Book b : books) {
                        %>
                        <div class="row spacer">
                            <div class="col-md-2">
                                <a href="view?action=view&isbn=<%=b.getISBN()%>"><img
                                    class="img-responsive img-thumbnail"
                                    src="<%=b.getCoverPath()%>"></a>
                            </div>
                            <div class="col-md-4">
                                <p class="text-left">
                                    <strong><%=b.getTitle()%></strong><br> <small>Rented
                                        on </small><span class="badge"><%=b.getRentDate()%></span>
                                </p>
                                <p class="text-left"><%=b.getAuthor()%></p>
                                <%
                                    if (b.getDescription().length > 100) {
                                %>
                                <p class="text-left"><%=new String(b.getDescription()).substring(0, 96)%>...
                                </p>
                                <%
                                    }
                                            else {
                                %>
                                <p class="text-left"><%=new String(b.getDescription())%></p>
                                <%
                                    }
                                %>
                            </div>
                            <div class="col-md-4" id="returnstatus">
                                <form action="view" method="post">
                                <input type="hidden" name="action" value="return">
                                <input type="hidden" name="isbn" value="<%=b.getISBN()%>">
                                <input type="hidden" name="userid" value="<%=user.getId()%>">
                                    <p>
                                    <button id="returnbook" data-userid="<%=user.getId()%>"
                                        data-isbn="<%=b.getISBN()%>" type="submit"
                                        class="btn btn-info btn-sm">
                                        <small>Return</small>
                                        <%=b.getTitle()%></button>
                                </p>
                                </form>
                            </div>
                        </div>
                        <hr>
                        <%
                            }
                            }
                        %>
                    </div>

                    <div id="history" class="tab-pane">
                        <%
                            if (transactions == null || transactions.isEmpty()) {
                        %>
                        <div class="alert alert-info">
                            After returning a book, the details will appear here. <a
                                href="books" class="alert-link">Check out the library to see
                                what you like.</a>
                        </div>
                        <%
                            }
                            else {
                                for (Transaction t : transactions) {
                        %>
                        <div class="row spacer">
                            <div class="col-md-2">
                                <a href=""><img class="img-responsive img-thumbnail"
                                    src="http://placehold.it/200x200"></a>
                            </div>
                            <div class="col-md-4">
                                <hr>
                                <p class="text-left">
                                    <strong>ISBN </strong><%=t.getBookID()%></p>
                                <p class="text-left">
                                    <strong>Date </strong><%=t.getTransactionDate()%></p>
                                <p class="text-left">
                                    <strong>Transaction </strong><%=t.getTransactionType()%></p>
                            </div>
                            <div class="col-md-4">
                                <p>
                                    <a type="button"
                                        href="view?action=view&isbn=<%=t.getBookID()%>"
                                        class="btn btn-warning btn-sm">View Book</a>
                                </p>
                            </div>
                        </div>
                        <hr>
                        <%
                            }
                            }
                        %>
                    </div>

                    <div id="duedates" class="tab-pane">
                        <div class="row spacer">
                            <div class="col-md-12">
                                <%
                                    if (books == null || books.isEmpty()) {
                                %>
                                <div class="alert alert-info">
                                    You don't have any records at the moment. <a href="books"
                                        class="alert-link">Check out the library to rent a book.</a>
                                </div>
                                <%
                                    }
                                    else {
                                %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <td>Book ISBN</td>
                                                <td>Book Title</td>
                                                <td>Rent Date</td>
                                                <td>Return Date</td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                for (Book b : books) {
                                            %>
                                            <tr class="info">
                                                <td><%=b.getISBN()%></td>
                                                <td><%=b.getTitle()%></td>
                                                <td><%=b.getRentDate()%></td>
                                                <td><%=b.getExpiryDate()%></td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                </div>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


        </div>
    </div>

</body>
</html>