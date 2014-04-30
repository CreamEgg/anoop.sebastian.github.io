<%@ page
	import="com.dcu.ee417.model.User,com.dcu.ee417.model.Librarian,com.dcu.ee417.model.Book,java.util.List"%>
<%@ include file="header.jsp"%>

<body>

	<!-- The fixed title bar on top -->
	<%@ include file="fixedTitlebar.jsp"%>
	<div class="container">
		<div class="jumbotron">
			<h1>500 Oops Something went wrong ...</h1>
			<p>A group of chimps are currently working on this mess.  Please stare at this while they fix it.</p>
			<p>Enjoy this video while they fix it.</p>
		</div>
		<%@ include file="searchbar.jsp"%>
		<div class="row">
		<p class="text-center" style="text-align: center; margin-bottom: -25px;">
			<img class="img-responsive" alt="Img Error" height="428" width="416"
				src="img/simpsons.gif">
		</p>
		</div>
	</div>

</body>
</html>
