//document loading entry point
$(function () {
    console.log("Document loaded");

    // starts carousel
    $('#myCarousel').carousel({
        interval: 10000
    });

    // getting the user email and password and sending it across the server using ajax
    $("#signin")
        .click(
            function () {

                // disabling ajax caching
                $.ajaxSetup({
                    cache: false
                });

                var url = "login?action=signin";
                
                if ($('#isAdmin').prop('checked')) {
                    url = "login?action=signinadmin";
                    console.log("admin checked");
                }

                // TODO implement ajax loading indicator
                $
                    .post(
                        url, {
                            email: $("#userid").val(),
                            password: $("#passwordinput").val()
                        },
                        function (data, status) {
                            console.log("Data: " + data + "Status: " + status);
                            if ($.trim(data) == "success") {
                                
                            	console.log("hiding modal");
                                
                                $("#loginstatus")
                                    .html(
                                        '<div class="alert alert-success alert-dismissable"><button type="button"' +
                                        'class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<span>Login Successfull</span></div>');
                                
                                $("#userid").val("");
                                $("#passwordinput").val("");
                                $('#myModal').modal("hide");

                                location.reload(true);
                            }
                            if ($.trim(data) == "successadmin") {
                                $("#userid").val("");
                                $("#passwordinput").val("");
                                $('#myModal').modal("hide");

                                // reload the page
                                location.reload(true);
                            }

                            if ($.trim(data) == "fail") {
                                $("#loginstatus")
                                    .html(
                                        '<div class="alert alert-danger alert-dismissable"><button type="button"' +
                                        'class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<span>Login Failed<br>The credentials are not valid!</span></div>'
                                );
                            }
                        });

            });

    // enabling the librarian id input
    $("#signup input[type='radio']")
        .change(
            function () {
                console.log("check button changed");

                if ($("#signup input[type='radio']:checked").val() === "librarian") {
                    $("#libID")
                        .html(
                            '<label class="control-label" >Librarian ID (required)</label>' +
                            '<div class="controls"> <input id="librarianID" name="Librarian ID" class="form-control"' +
                            'type="number" placeholder="123456789" class="input-large" required=""></div>'
                    );
                } else {
                    $("#libID").html("");
                }
            });

    // user registration
    $("#confirmsignup")
        .click(
            function () {
                $("#emailstatus").html("");

                var firstname = $("#firstname").val();
                var lastname = $("#lastname").val();
                var email = $("#email").val();
                var password = $("#password").val();
                var librarianID = $("#librarianID").val();

                var url = "login?action=registeradmin";
                if ($("#signup input[type='radio']:checked").val() === "normaluser") {
                    url = "login?action=register";
                }
                console.log("first: " + firstname + " last: " + lastname + " email: " + email +
                    " password: " + password);

                // TODO Get the selected value, if librarian then ask
                // for id, check for unique id
                console.log("librarian: " + $("#signup input[type='radio']:checked")
                    .val());

                $
                    .post(
                        url, {
                            firstname: firstname,
                            lastname: lastname,
                            email: email,
                            password: password,
                            adminid: librarianID
                        },
                        function (data, status) {
                            console.log("Data: " + data + "Status: " + status);
                            if ($.trim(data) == "success") {

                                $("#firstname").val("");
                                $("#lastname").val("");
                                $("#email").val("");
                                $("#password").val("");
                                $("#reenterpassword").val("");

                                location.reload(true);
                                $('#myModal').modal("hide");
                            }

                            if ($.trim(data) == "nonvalidemail") {
                                $("#emailstatus")
                                    .html(
                                        '<div class="alert alert-danger alert-dismissable"><button type="button"' +
                                        'class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<span>Email already registered<br>Please use another email.</span></div>'
                                );

                                $("#registerstatus").html("");
                            }

                            if ($.trim(data) == "badid") {
                                console.log("Invalid Input");
                                $("#idstatus")
                                    .html(
                                        '<div class="alert alert-danger alert-dismissable"><button type="button"' +
                                        'class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<span>Registration Failed<br>The Librarian ID entered is invalid</span></div>'
                                );
                            }
                        });

            });

    // show modal if user tried to rent a book without signing in
    $("#loginbeforerent").click(function () {
        console.log("Rent button clicked");
        $('#myModal').modal("show");
    });

    // rent book by making an ajax call
    $("#rentButton")
        .click(
            function () {
                var userid = $(this).attr('data-userid');
                var bookisbn = $(this).attr('data-isbn');

                $
                    .post(
                        "view?action=rent", {
                            isbn: bookisbn,
                            userid: userid,

                        },
                        function (data, status) {

                            if ($.trim(data) == "success") {
                                location.reload(true);
                                $("#rentstatus")
                                    .append(
                                        '<div class="alert alert-success alert-dismissable">' +
                                        '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<strong>Success !</strong> You have rented this book successfully. Enjoy!</div>'
                                );
                            }

                            if ($.trim(data) == "fail") {
                                $("#rentstatus")
                                    .append(
                                        '<div class="alert alert-warning alert-dismissable">' +
                                        '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<strong>Warning !</strong> You have already rented this book.</div>'
                                );
                            }
                            // location.reload(true);
                        });
            });

    // assigning a book to the user by the admin
    $("#assign")
        .click(
            function () {
                var userid = $("#assignid").val();
                var bookisbn = $("#assignisbn").val();

                console.log("assigning book with isbn: " + bookisbn + " for user " + userid);

                $
                    .post(
                        "view?action=rent", {
                            isbn: bookisbn,
                            userid: userid,

                        },
                        function (data, status) {

                            if ($.trim(data) == "success") {
                                $("#assignstatus")
                                    .html(
                                        '<div class="alert alert-success alert-dismissable">' +
                                        '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<strong>Success !</strong> You have assigned this book successfully.</div>'
                                );
                            }

                            if ($.trim(data) == "fail") {
                                $("#assignstatus")
                                    .html(
                                        '<div class="alert alert-danger alert-dismissable">' +
                                        '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                                        '<strong>Warning !</strong> Assign failed either the UserID or the ISBN is invalid</div>'
                                );
                            }
                            // location.reload(true);
                        });
            });

});