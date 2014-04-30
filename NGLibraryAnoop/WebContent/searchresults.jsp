<%@ page
    import="com.dcu.ee417.model.User,com.dcu.ee417.model.Transaction,
    com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.Set,java.util.List"%>
<%@ include file="header.jsp"%>

<body>

    <!-- The fixed title bar on top -->
    <%@ include file="fixedTitlebar.jsp"%>
    <%
        String query = "" + request.getAttribute("query");
        List<Book> booksByTitle = (List<Book>) request.getAttribute("booksbytitle");
        List<Book> booksByAuthor = (List<Book>) request.getAttribute("booksbyauthor");
    %>

    <div class="container">
        <div class="row spacer">
            <%
                if (query != null && !query.isEmpty()) {
            %>
            <h1 class="page-header">
                Search results for <strong><%=query%></strong>
            </h1>
            <%
                }
            %>
        </div>
        <%@ include file="searchbar.jsp"%>

        <%
            if (booksByTitle == null || booksByTitle.isEmpty()) {
        %>
        <div class="alert alert-info">
            Your search term does not match items in the library. <a href="books"
                class="alert-link">Check out the library to see the books we
                offer.</a>
        </div>
        <%
            }
            else {
        %>
        <div class="row spacer">
            <div class="col-lg-12">
                <h4>Search Results</h4>
                <ul class="nav nav-tabs nav-justified">
                    <li class="active">
                    <a href="#title" data-toggle="tab" data-url="#"><small>Results matched by</small> <strong>Title</strong> <span class="badge"><%=booksByTitle.size()%></span></a></li>
                    <li class=""><a href="#author" data-toggle="tab" data-url="#"><small>Results matched by</small> <strong>Author</strong> <span class="badge"><%=booksByAuthor.size()%></span></a></li>
                </ul>
                <div class="tab-content">
                    <div id="title" class="tab-pane active">
                        <%
                            for (Book b : booksByTitle) {
                        %>
                        <div class="row spacer">
                            <div class="col-md-2">
                                <a href="view?action=view&isbn=<%=b.getISBN()%>"><img height="300" width="200"
                                    class="img-responsive img-thumbnail"
                                    src="<%=b.getCoverPath()%>"></a>
                            </div>
                            <div class="col-md-10">
                                <p class="text-left">
                                    <strong><%=b.getTitle()%></strong>
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
                        </div>
                        <%
                            }
                            
                        %>
                    </div>
                    <div id="author" class="tab-pane">
                    <%
                            for (Book b : booksByAuthor) {
                        %>
                        <div class="row spacer">
                            <div class="col-md-2">
                                <a href="view?action=view&isbn=<%=b.getISBN()%>"><img height="300" width="200"
                                    class="img-responsive img-thumbnail"
                                    src="<%=b.getCoverPath()%>"></a>
                            </div>
                            <div class="col-md-10">
                                <p class="text-left">
                                    <strong><%=b.getTitle()%></strong>
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
                        </div>
                        <%
                            }
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
        </div>
</body>
</html>