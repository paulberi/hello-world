function login(username, password) {
    var http = new HttpClient();
    const headers = new HttpHeaders({ Authorize: 'Basic' + betoa(username + ":" + password) });
    this.http.get("http://localhost:8081/api/v1/login", { headers, reponseType: 'text' });
}