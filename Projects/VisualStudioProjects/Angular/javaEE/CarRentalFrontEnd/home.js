console.log(" js is connected");
//create a
$(function() {
        $('#createButtonSubmit').click(function() {
            console.log("Button pressed");
            ajaxPost();
        });

        function ajaxPost() {
            var roleData;
            if ($('#role option:selected').text() === "Customer") {
                roleData = 2;
            } else if ($('#role option:selected').text() === "Admin") {
                roleData = 1;
            }
            var userData = {
                name: $('#name').val(),
                address: $('#address').val(),
                username: $('#userName1').val(),
                password: $('#password1').val(),
                enabled: true,
                role: $('#role option:selected').text(),
                roles: [{
                    roleId: roleData
                }]
            }
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "http://localhost:8081/api/v1/allUsers/postUser",
                data: JSON.stringify(userData),
                dataType: 'json',
                success: function(result) {
                    if (result.responseText == "User created") {
                        alert("" + result.data.name +
                            "post Successful! <br>" +
                            "---> Congrats !!"
                        );
                    } else {
                        alert("Error");
                    }
                    console.log(result);
                },
                error: function(e) {
                    // alert("Error")
                    alert(
                        "User successfully created"
                    );
                    console.log("ERROR", e);
                }
            });
        }
    })
    //to populate the iframe
function setSource1(url) {
    document.getElementById("leftIframe").setAttribute("src", url);
    var button = document.getElementById("loginButton");
    $('#loginButton').hide();
    $('#createAccountForm').hide();
    $('#loginForm').hide();
    $('#createButton').hide();
    $('#homeInfo').show();
    $('#logoutButton').show();
}
//for buttons att the home header

$(function() {
    $('#logoutButton').hide();
    $('#createAccountForm').hide();
    $('#loginForm').hide();
    $('#homeButton').click(function() {
        $('#createAccountForm').hide();
        $('#loginForm').hide();
        $('#homeInfo').toggle();
    })
    $('#homeButton').click(function() {
        $('#createAccountForm').hide();
        $('#loginForm').hide();
        $('#homeInfo').show();
    })
    $('#createButton').click(function() {
        $('#loginForm').hide();
        $('#homeInfo').hide();
        $('#createAccountForm').show();
    })
    $('#loginButton').click(function() {
        $('#loginForm').show();
        $('#homeInfo').hide();
        $('#createAccountForm').hide();
    })
    $('#logoutButton').click(function() {
        logout();

    })
})

function logout() {
    document.getElementById("leftIframe").setAttribute("src", "");
    $('#loginForm').hide();
    $('#homeInfo').show();
    $('#createAccountForm').hide();
    $('#createButton').show();
    $('#logoutButton').hide();
    $('#loginButton').show();
}

function loginLogic() {
    var userList = [];
    $.ajax({
        headers: {
            "Accept": "application/json"
        },
        type: "GET",
        url: "http://localhost:8081/api/v1/customers",
        crossDomain: true,
        beforeSend: function(xhr) {
            xhr.withCredentials = true;
        },
        success: function(data, textStatus, request) {

            var username = $('#userName').val();
            var password = $('#password').val();
            data.forEach(user => {
                userList.push(user);
                userList.forEach(user => {
                    console.log(user.username);
                    console.log(user.password);
                    if (username === user.username && password === user.password) {
                        localStorage.setItem('username', user.username);
                        localStorage.setItem('userId', user.userId);
                        localStorage.setItem('role', user.role);
                        // setSource1('../CarRentalFrontEnd/users/loggedIn.html');
                        setSource1('users/loggedIn.html')
                        $('#loginForm').hide();
                        $('#homeInfo').show();
                        $('#createAccountForm').hide();
                        // alert(user.userName + " Successfully logged in");
                        // } else {
                        //     alert(" User name and/or password not correct Try again");
                        //     $('#loginForm').show();
                        //     $('#homeInfo').hide();
                        //     $('#createAccountForm').hide();

                    }
                })
                console.log("loggin button was clicked")
            });
        }

    })

}

// function loginLogic1() {
//     var username = $('#userName').val();
//     var password = $('#password').val();
//     var userList = [];
//     $.ajax({
//                 headers: {
//                     "Accept": "application/json"
//                 },
//                 type: "GET",
//                 url: "http://localhost:8081/api/v1/customers",
//                 crossDomain: true,
//                 beforeSend: function(xhr) {
//                     xhr.withCredentials = true;
//                 },
//                 success: function(data, textStatus, request) {
//                     console.log(data);

//                     var username = $('#userName').val();
//                     var password = $('#password').val();
//                     data.forEach(user => {
//                         userList.push(user);
//                         userList.forEach(user => {
//                                 console.log(user.username);
//                                 console.log(user.password);
//                                 if (username === user.username && password === user.password) {
//                                     $.ajax({
//                                         type: "POST",
//                                         contentType: "application/json",
//                                         url: "http://localhost:8081/api/v1/login",
//                                         data: JSON.stringify(user),
//                                         dataType: 'json',
//                                         success: function(data, textStatus, request) {
//                                             console.log(data);

//                                             localStorage.setItem('username', user.username);
//                                             localStorage.setItem('userId', user.userId);
//                                             localStorage.setItem('role', user.role);
//                                             setSource1('../CarRentalFrontEnd/users/loggedIn.html');
//                                             $('#loginForm').hide();
//                                             $('#homeInfo').show();
//                                             $('#createAccountForm').hide();
//                                             // alert(user.userName + " Successfully logged in");
//                                             // } else {
//                                             //     alert(" User name and/or password not correct Try again");
//                                             //     $('#loginForm').show();
//                                             //     $('#homeInfo').hide();
//                                             //     $('#createAccountForm').hide();

//                                         }
//                                     })
//                                     console.log("loggin button was clicked")
//                                 });
//                         }

//                     })

//                 }