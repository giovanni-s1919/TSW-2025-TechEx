<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html>
<head>
    <title>TechEx - Registrazione</title>
    <link rel="stylesheet" href="login&register.css">
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div>
    <form method="post" action="register">
        <fieldset>
            <h2 style="font-size: 100%;">Registrazione</h2>
            <div class="divauth">
                <label for="username" class="hiddenlabel">Username: </label>
                <input type="text" id="username" required class="auth" name="username" placeholder="Username">
            </div>
            <div class="divauth">
                <label for="registeremail" class="hiddenlabel">Email: </label>
                <input type="email" id="registeremail" required class="auth" name="email" placeholder="Email">
            </div>
            <div class="divauth">
                <label for="registerpassword" class="hiddenlabel">Password: </label>
                <input type="password" id="registerpassword" required class="auth" name="password" placeholder="Password">
            </div>
            <div class="divauth">
                <label for="confirm" class="hiddenlabel">Verifica Password: </label>
                <input type="password" id="confirm" required class="auth" name="confirm" placeholder="Confirm Password">
            </div>
            <div>
                <button type="submit" class="databutt">Registrati</button>
            </div>
            <a href="home.jsp" class="ref"><img src="images/logowhite.png" class="logo" alt="TechEx"></a>
        </fieldset>
    </form>
</div>
<div class="suggestion">
    <p>Hai già un account? <a href="login.jsp" class="logregswitch">Login</a></p>
</div>
<p style="color:red;"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
<%@ include file="footer.jsp" %>
</body>
</html>
