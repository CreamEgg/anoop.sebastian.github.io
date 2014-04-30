<%@ page
	import="com.dcu.ee417.model.User,com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.List"%>
<%@ include file="header.jsp"%>

<body>

	<!-- The fixed title bar on top -->
	<%@ include file="fixedTitlebar.jsp"%>
	<div class="container">
		<div class="jumbotron">
			<h1>404 Oops Something went wrong ...</h1>
			<p>The page you're looking for does not exist or has been moved to
				a different location. Please use the search bar below to find what you are looking for.</p>
		</div>
		<%@ include file="searchbar.jsp"%>
		<div class="row">
		<p style="text-align: center; margin-bottom: -25px;">
                <img alt="Img Error" height="428" width="416"
                    src="http://web.thisorth.at/assets/0.014/images/main/img-error.png">
            </p>
		</div>
	</div>

</body>
</html>
