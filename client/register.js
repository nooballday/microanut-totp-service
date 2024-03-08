(() => {
    $("#registerButton").click(e => {
        const username = $("#inputUsername").val()
        const password = $("#inputPassword").val()
        if (username?.length > 0 && password?.length > 0) {
            register(username, password);
        } else {
            showError("Please input username and password");
        }
    });

    function register(username, password) {
        fetch("http://localhost:8081/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        }).then(async result => {
            showQr(await result.json());
        }).catch(error => {
            showError(error);
        });
    }
    
    function showError(err) {
        $(".register-response").text(err)
    }

    function showQr(response) {
        const username = response.username;
        const secretKey = response.secretKey;
        const totpUrl = `otpauth://totp/Example:${username}?secret=${secretKey}&issuer=Example`;
        new QRCode(document.getElementById("qrCode"), totpUrl);
    }
})();