<%--
  Created by IntelliJ IDEA.
  User: matt
  Date: 6/25/25
  Time: 11:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form method="post" action="login">
    <fieldset>
        <legend><h2>Login</h2></legend>
        <label for="email">Email: </label>
        <input type="email" id="email" name="email" required><br>
        <label for="password">Password: </label>
        <input type="password" id="password" name="password" required><br>
        <button type="submit">Login</button>
    </fieldset>
</form>
<p>Non hai un account? <a href="register.jsp">Registrati</a></p>
<p style="color:red;"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
</body>
</html>
