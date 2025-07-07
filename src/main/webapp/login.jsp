<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <title>TechEx - Login</title>
    <link rel="stylesheet" href="login&register.css">
</head>
<body>
<div>
    <form method="post" action="login">
        <fieldset>
            <h2 style="font-size: 100%;">Login</h2>
            <div class="divauth">
                <label for="loginemail" class="hiddenlabel">Email: </label>
                <input type="email" id="loginemail" name="email" required class="auth" placeholder="Email"><br>
            </div>
            <div class="divauth">
                <label for="loginpassword" class="hiddenlabel">Password: </label>
                <input type="password" id="loginpassword" name="password" required class="auth" placeholder="Password"><br>
            </div>
            <div>
                <button type="submit" class="databutt">Login</button>
            </div>
            <a href="home.jsp" class="ref"><img src="images/logowhite.png" class="logo" alt="TechEx"></a>
        </fieldset>
    </form>
</div>
<div class="suggestion">
    <p>Non hai un account? <a href="register.jsp" class="logregswitch">Registrati</a></p>
</div>
<p style="color:red;"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
</body>
</html>
