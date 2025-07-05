<%--
  Created by IntelliJ IDEA.
  User: matt
  Date: 6/25/25
  Time: 11:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <title>Tech-Ex Login</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
<div>
    <form method="post" action="login">
        <fieldset>
            <h2>Login</h2>
           <!-- <legend><img src="images/logo.png" id="toplogo"></legend>-->
            <div class="divauth">
                <label for="email">Email: </label>
                <input type="email" id="email" name="email" required class="auth" placeholder="Email"><br>
            </div>
            <div class="divauth">
                <label for="password">Password: </label>
                <input type="password" id="password" name="password" required class="auth" placeholder="Password"><br>
            </div>
            <div id="loginbutton">
                <button type="submit" id="loginbutt">Login</button>
            </div>
            <img src="images/logo.png" id="logo" alt="TechEx">
        </fieldset>
    </form>
</div>
<div id="registrati">
    <p>Non hai un account? <a href="register.jsp">Registrati</a></p>
</div>
<p style="color:red;"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
</body>
</html>
