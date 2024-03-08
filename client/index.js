(() => {

    $("#loginButton").click(e => {
        const username = $("#inputUsername").val()
        const password = $("#inputPassword").val()
        const token = $("#inputToken").val()
        if (username?.length > 0 && password?.length > 0 && token?.length > 0) {
            login(username, password, token);
        } else {
            showError("Please input username and password");
        }
    });

    $("#registerButton").click(e => {
        window.location.assign("http://localhost:8080/register.html")
    });

    function login(username, password, token) {
        console.log(username, password, token);
        fetch("http://localhost:8081/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: username,
                password: password,
                TOTPToken: token
            })
        }).then(async result => {
            showError(await result.text());
        }).catch(error => {
            showError(error);
        });
    }

    function showError(err) {
        $(".login-response").text(err)
    }

})();