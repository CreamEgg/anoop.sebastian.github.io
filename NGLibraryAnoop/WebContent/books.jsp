<%@ page
	import="com.dcu.ee417.model.User,com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Next Generation Library System">
<meta name="author" content="Anoop Sebastian">
<link rel="shortcut icon" href="img/books-icon.png">

<title>NG Library System</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/navbar-fixed-top.css" rel="stylesheet">

<script src="js/jquery-1.11.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>

<style type="text/css">
.page-header {
	margin-top: 60px;
}

.spacer {
	margin-top: 3%;
}

.fixme {
	position: fixed;
}
/* Landscape phone to portrait tablet */
@media ( max-width : 767px) {
	.fixme {
		width: 100%;
		position: static;
	}
}

.img-responsive,.thumbnail>img,.thumbnail a>img,.carousel-inner>.item>img,.carousel-inner>.item>a>img
	{
	max-height: 300px;
}
</style>
</head>

<body>

	<!-- The fixed title bar on top -->
	<%@ include file="fixedTitlebar.jsp"%>

	<!-- /container -->
	<div class="container">
		<div class="row">
			<h1 class="page-header">Books</h1>
		</div>
		<%@ include file="searchbar.jsp"%>
	</div>
	<div class="container-fluid">
		<div class="row spacer">
			<div class="col-lg-12">
				<div class="col-lg-2 fixme">
					<h4>Categories</h4>
					<ul class="nav nav-pills nav-stacked" id="categories">
						<li class="active"><a href="#popular" data-toggle="tab">Popular
								Books</a></li>
						<li class=""><a href="#fiction" data-toggle="tab">Fiction</a></li>
						<li class=""><a href="#biography" data-toggle="tab">Biographies</a></li>
						<li class=""><a href="#business" data-toggle="tab">Business</a></li>
						<li class=""><a href="#technology" data-toggle="tab">Technology</a></li>
					</ul>
				</div>
				<div class="col-lg-10 pull-right">
					<div class="tab-content">
						<!-- Fiction Books -->
						<div id="fiction" class="tab-pane">
							<div class="row">
								<%
									List<Book> books = (List<Book>) request
											.getAttribute("fictionbooks");
									for (Book b : books) {
										//out.println(b);
								%>
								<div class="col-lg-3 col-md-4 col-xs-6 thumb">
									<div class="thumbnail">
										<a href="view?action=view&isbn=<%=b.getISBN()%>"> <img
											height="3" width="100%" src="<%=b.getCoverPath()%>" /></a>
										<%
											if (b.getTitle().length() > 30) {
										%>
										<h5><%=b.getTitle().substring(0, 30)%>...
										</h5>
										<%
											} else {
										%>
										<h5><%=b.getTitle()%></h5>
										<%
											}
										%>
									</div>
								</div>
								<%
									}
								%>
							</div>
						</div>

						<!-- Biography Books -->
						<div id="biography" class="tab-pane">
							<div class="row">
								<%
									List<Book> bioBooks = (List<Book>) request.getAttribute("biobooks");
									for (Book b : bioBooks) {
										//out.println(b);
								%>
								<div class="col-lg-3 col-md-4 col-xs-6 thumb">
									<div class="thumbnail">
										<a href="view?action=view&isbn=<%=b.getISBN()%>"> <img
											height="300px" width="100%" src="<%=b.getCoverPath()%>" /></a>
										<%
											if (b.getTitle().length() > 30) {
										%>
										<h5><%=b.getTitle().substring(0, 30)%>...
										</h5>
										<%
											} else {
										%>
										<h5><%=b.getTitle()%></h5>
										<%
											}
										%>
									</div>
								</div>
								<%
									}
								%>
							</div>
						</div>

						<!-- Business Books -->
						<div id="business" class="tab-pane">
							<div class="row">
								<%
									List<Book> businessBooks = (List<Book>) request
											.getAttribute("business");
									for (Book b : businessBooks) {
										//out.println(b);
								%>
								<div class="col-lg-3 col-md-4 col-xs-6 thumb">
									<div class="thumbnail">
										<a href="view?action=view&isbn=<%=b.getISBN()%>"> <img
											height="300px" width="100%" src="<%=b.getCoverPath()%>" /></a>
										<%
											if (b.getTitle().length() > 30) {
										%>
										<h5><%=b.getTitle().substring(0, 30)%>...
										</h5>
										<%
											} else {
										%>
										<h5><%=b.getTitle()%></h5>
										<%
											}
										%>
									</div>
								</div>
								<%
									}
								%>
							</div>
						</div>

						<!-- Popular Books -->
						<div id="popular" class="tab-pane active">
							<div class="row">
								<%
									List<Book> popularBooks = (List<Book>) request
											.getAttribute("popular");
									for (Book b : popularBooks) {
										//out.println(b);
								%>
								<div class="col-lg-3 col-md-4 col-xs-6 thumb">
									<div class="thumbnail">
										<a href="view?action=view&isbn=<%=b.getISBN()%>"> <img
											height="300px" width="100%" src="<%=b.getCoverPath()%>" /></a>
										<%
											if (b.getTitle().length() > 30) {
										%>
										<h5><%=b.getTitle().substring(0, 30)%>...
										</h5>
										<%
											} else {
										%>
										<h5><%=b.getTitle()%></h5>
										<%
											}
										%>
									</div>
								</div>
								<%
									}
								%>
							</div>
						</div>

						<!-- Popular Books -->
						<div id="technology" class="tab-pane">
							<div class="row">
								<%
									List<Book> techBooks = (List<Book>) request.getAttribute("tech");
									for (Book b : techBooks) {
										//out.println(b);
								%>
								<div class="col-lg-3 col-md-4 col-xs-6 thumb">
									<div class="thumbnail">
										<a href="view?action=view&isbn=<%=b.getISBN()%>"> <img
											height="300px" width="100%" src="<%=b.getCoverPath()%>" /></a>
										<%
											if (b.getTitle().length() > 30) {
										%>
										<h5><%=b.getTitle().substring(0, 30)%>...
										</h5>
										<%
											} else {
										%>
										<h5><%=b.getTitle()%></h5>
										<%
											}
										%>
									</div>
								</div>
								<%
									}
								%>
							</div>
						</div>

					</div>
				</div>
			</div>
			<!-- tab content -->
		</div>
	</div>
</body>
</html>
