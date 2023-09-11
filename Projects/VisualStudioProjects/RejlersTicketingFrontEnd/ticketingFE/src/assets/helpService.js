// var orders = [];
// var users = [];

// $(function() {
//     $.ajax({
//         headers: {
//             "Accept": "application/json"
//         },
//         type: "GET",
//         url: "http://localhost:8080/rejlers/allorders",
//         crossDomain: true,
//         beforeSend: function(xhr) {
//             xhr.withCredentials = true;
//         },
//         success: function(data, textStatus, request) {
//             data.forEach(order => {
//                 orders.push(order);
//                 console.log(orders)
//             })
//         }
//     });
//     $.ajax({
//         headers: {
//             "Accept": "application/json"
//         },
//         type: "GET",
//         url: "http://localhost:8080/rejlers/allUsers",
//         crossDomain: true,
//         beforeSend: function(xhr) {
//             xhr.withCredentials = true;
//         },
//         success: function(data, textStatus, request) {
//             console.log(data)
//             data.forEach(user => {
//                 users.push(user);
//                 console.log(users)
//             })
//         }
//     });
// })