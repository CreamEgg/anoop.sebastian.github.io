<!-- Fixed navigation bar -->
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/NGLibraryAnoop">Next Generation Library System</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class=""><a href="/NGLibraryAnoop">Home</a></li>
				<li><a href="books">Books</a></li>
				<li class="dropdown"><a href="report.html" class="dropdown-toggle"
					data-toggle="dropdown">Report<b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="report.html#data">Data Storage</a></li>
						<li><a href="report.html#desc">Description</a></li>
						<li><a href="report.html#features">Features</a></li>
						<li class="divider"></li>
						<li class="dropdown-header">Login Details</li>
						<li><a href="report.html#userlogin">User Logins</a></li>
						<li><a href="report.html#adminlogin">Librarian Logins</a></li>
					</ul></li>

			</ul>
			<ul id="loginbutton" class="nav navbar-nav navbar-right">
				<%
					HttpSession currentSession = request.getSession(false);
					if (currentSession != null) {
						Object object = currentSession.getAttribute("user");
						if (object == null) {
				%>
				<li><a href="#signup" data-toggle="modal"
					data-target=".bs-modal-sm">Sign In / Register</a></li>
				<noscript>
					<meta HTTP-EQUIV="REFRESH" content="2; url=ancient.html">
				</noscript>
				<%
					} else if (object.getClass().equals(User.class)) {
						User user = (User)object;
				%>
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown"><%=user.getFirstName()%>'s Account <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="profile?action=viewbooks&userid=<%=user.getId()%>">Books</a></li>
					</ul></li>
				<li><form action="login?action=logout" method="post">
						<button class="btn btn-info navbar-btn">Logout</button>
					</form></li>
				<%
					} else if (object.getClass().equals(Librarian.class)) {
						Librarian admin = (Librarian)object;
				%>
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown"><%=admin.getFirstName()%>'s Account <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="profile?action=viewrecords&userid=<%=admin.getId()%>&adminid=<%=admin.getLibrarianID()%>">View Dashboard</a></li>
					</ul></li>
				<li><form action="login?action=logout" method="post">
						<button class="btn btn-info navbar-btn">Logout</button>
					</form></li>
				<%
					}
					}
				%>
			</ul>
		</div>
	</div>
</div>

<!-- Modal Window for Login -->
<div class="modal fade bs-modal-sm" id="myModal" tabindex="-1"
	role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<br>
			<div class="bs-example bs-example-tabs">
				<ul id="myTab" class="nav nav-tabs">
					<li class="active"><a href="#login" data-toggle="tab">Sign
							In</a></li>
					<li class=""><a href="#signup" data-toggle="tab">Register</a></li>
					<li class=""><a href="#why" data-toggle="tab">Why?</a></li>
				</ul>
			</div>
			<div class="modal-body">
				<div id="myTabContent" class="tab-content">
					<div class="tab-pane fade in" id="why">
						<p>We need this information so that you can receive access to
							the site and its content. Rest assured your information will not
							be sold, traded, or given to anyone.</p>
						<p></p>
						<br> Please contact <a
							mailto:href="anoop.sebastian2@mail.dcu.ie"></a>anoop.sebastian2@mail.dcu.ie</a>
						for any other inquiries.
						</p>
					</div>
					<div class="tab-pane fade active in" id="login">
						<div class="form-horizontal">
							<fieldset>
								<!-- Sign In Form -->
								<!-- Text input-->
								<div class="control-group">
									<label class="control-label" for="userid">Email</label>
									<div class="controls">
										<input required="required" id="userid" name="userid" type="email"
											class="form-control" placeholder="email address"
											class="input-medium" autofocus="autofocus">
									</div>
								</div>

								<!-- Password input-->
								<div class="control-group">
									<label class="control-label" for="passwordinput">Password</label>
									<div class="controls">
										<input required="" id="passwordinput" name="passwordinput"
											class="form-control" type="password" placeholder="password"
											class="input-medium">
									</div>
								</div>

								<!-- Multiple Checkboxes (inline) -->
								<div class="control-group">
									<label class="control-label" for="isadmin"></label>
									<div class="controls">
										<label class="checkbox inline" for="isadmin"> <input
											type="checkbox" name="isamdin" id="isAdmin" value="isadmin ">I'm
											a Librarian
										</label>
									</div>
								</div>
								<div id="loginstatus"></div>

								<!-- Button -->
								<div class="control-group">
									<label class="control-label" for="signin"></label>
									<div class="controls">
										<button id="signin" name="signin" class="btn btn-success"
											data-complete-text="You're now signed In"
											data-loading-text="Signing You in ...">Sign In</button>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<div class="tab-pane fade" id="signup">
						<div class="form-horizontal">
							<fieldset>
								<!-- Sign Up Form -->
								<!-- Text input-->
								<div class="control-group">
									<label class="control-label" for="Email">First Name
										(required)</label>
									<div class="controls">
										<input id="firstname" name="Email" class="form-control"
											type="text" placeholder="First Name" class="input-large"
											required="required" autofocus="autofocus">
									</div>
								</div>

								<div class="control-group">
									<label class="control-label" for="Email">Last Name
										(required)</label>
									<div class="controls">
										<input id="lastname" name="Email" class="form-control"
											type="text" placeholder="Last Name" class="input-large"
											required="required">
									</div>
								</div>

								<div class="control-group">
									<label class="control-label" for="Email">Email
										(required)</label>
									<div class="controls">
										<input id="email" name="Email" class="form-control"
											type="email" placeholder="Your Email" class="input-large"
											required="required">
									</div>
								</div>
								<div id="emailstatus"></div>

								<!-- Empty Librarian ID-->
								<div id="libID" class="control-group"></div>
								<div id="idstatus"></div>
								<!-- Password input-->
								<div class="control-group">
									<label class="control-label" for="password">Password
										(required)</label>
									<div class="controls">
										<input id="password" name="password" class="form-control"
											type="password" placeholder="********" class="input-large"
											required=""> <em>1-8 Characters</em>
									</div>
								</div>

								<!-- Text input-->
								<div class="control-group">
									<label class="control-label" for="reenterpassword">Re-Enter
										Password (required)</label>
									<div class="controls">
										<input id="reenterpassword" class="form-control"
											name="reenterpassword" type="password" placeholder="********"
											class="input-large" required="">
									</div>
								</div>

								<!-- Multiple Radios (inline) -->
								<br>
								<div class="control-group">
									<label class="control-label" for="humancheck">Humanity
										Check:</label>
									<div class="controls">
										<label class="radio inline" for="humancheck-0"> <input
											id="standard" type="radio" name="humancheck"
											id="humancheck-0" value="normaluser" checked="checked">I'm
											a Normal User
										</label> <label class="radio inline" for="humancheck-1"> <input
											id="librarian" type="radio" name="humancheck"
											id="humancheck-1" value="librarian">I'm Librarian
										</label>
									</div>
								</div>

								<div id="registerstatus"></div>
								<!-- Button -->
								<div class="control-group">
									<label class="control-label" for="confirmsignup"></label>
									<div class="controls">
										<button id="confirmsignup" name="confirmsignup"
											class="btn btn-success">Sign Up</button>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<center>
					<!--  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>-->
					<button type="button" class="btn btn-default">Close</button>
				</center>
			</div>
		</div>
	</div>
</div>