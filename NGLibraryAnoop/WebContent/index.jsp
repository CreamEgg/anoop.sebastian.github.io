<%@ page
	import="com.dcu.ee417.model.User, com.dcu.ee417.model.Librarian"%>
<%@ include file="header.jsp"%>

<body>
	<!-- The fixed title bar on top -->
	<%@ include file="fixedTitlebar.jsp"%>

	<div class="container">

		<div class="jumbotron">
			<h2>Next Generation Library System</h2>
			<p>This project is developed for the DCU DME 4th year module EE417 and it features
				a library system. Any body is able to browse through the available
				books in the library. In order to rent a book a user must be
				registered and should log in to the system.</p>
			<p>There is good collections of books available for rental and are categorised in to different sections</p>
			<p>
				<a class="btn btn-lg btn-success" href="books" role="button">View
					Books</a>
			</p>
		</div>
		<%@ include file="searchbar.jsp"%>
		<div class="row spacer">
			<div class="col-md-10">
				<h1>Most popular books</h1>
				<hr>
			</div>
		</div>

	</div>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div id="myCarousel" class="carousel slide">

					<ol class="carousel-indicators">
						<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
						<li data-target="#myCarousel" data-slide-to="1"></li>
						<li data-target="#myCarousel" data-slide-to="2"></li>
					</ol>

					<!-- Carousel items -->
					<div class="carousel-inner">

						<div class="item active">
							<div class="row">
								<div class="col-md-3">
									<a href="view?action=view&isbn=20" class="thumbnail"><img
										src="img/popular/A Clash of Honor.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=10" class="thumbnail"><img
										src="img/popular/A Feast of Dragons.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=21" class="thumbnail"><img
										src="img/popular/A March of Kings.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=22" class="thumbnail"><img
										src="img/popular/Divergent.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
							</div>
						</div>

						<div class="item">
							<div class="row">
								<div class="col-md-3">
									<a href="view?action=view&isbn=9" class="thumbnail"><img
										src="img/popular/Philomena.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=18" class="thumbnail"><img
										src="img/popular/The Book Thief.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=23" class="thumbnail"><img
										src="img/fiction/A Kings Ransom.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=4" class="thumbnail"><img
										src="img/fiction/Banquet for the Damned.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
							</div>
						</div>

						<div class="item">
							<div class="row">
								<div class="col-md-3">
									<a href="view?action=view&isbn=17" class="thumbnail"><img
										src="img/fiction/Be Careful What You Wish For.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=2" class="thumbnail"><img
										src="img/fiction/BETRAYED.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=27" class="thumbnail"><img
										src="img/fiction/Daughter of the Blood.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
								<div class="col-md-3">
									<a href="view?action=view&isbn=14" class="thumbnail"><img
										src="img/fiction/Natchez Burning.jpg" alt="Image"
										style="max-width: 100%;" /></a>
								</div>
							</div>
						</div>
					</div>
					<a class="left carousel-control" href="#myCarousel"
						data-slide="prev"><strong>Previous</strong></a> <a
						class="right carousel-control" href="#myCarousel"
						data-slide="next"><strong>Next</strong></a>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<hr>
		<footer>
			<p>&copy; EE417 Web Development Project <a
                        href="http://ie.linkedin.com/in/anoopsebastian">Anoop
                        Sebastian</a> 10323753
				2014</p>
		</footer>
	</div>
</body>
</html>
