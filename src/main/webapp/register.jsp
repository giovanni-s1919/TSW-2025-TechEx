<%--
  Created by IntelliJ IDEA.
  User: matt
  Date: 6/25/25
  Time: 11:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Registrazione</title>
</head>
<body>
<form method="post" action="register">
    <fieldset>
        <legend><h2>Registrazione</h2></legend>
        <label for="username">Username: </label>
        <input type="text" id="username" name="username"><br>
        <label for="email">Email: </label>
        <input type="email" id="email" name="email"><br>
        <label for="password">Password: </label>
        <input type="password" id="password" name="password"><br>
        <button type="submit">Registrati</button>
    </fieldset>
</form>

<p>Hai già un account? <a href="login.jsp">Login</a></p>
<p style="color:red;"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
</body>
</html>
