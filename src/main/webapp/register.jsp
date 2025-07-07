<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>TechEx - Registrazione</title>
    <link rel="stylesheet" href="login&register.css">
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
        <label for="confirm">Verifica Password: </label>
        <input type="password" id="confirm" name="confirm"><br>
        <button type="submit">Registrati</button>
    </fieldset>
</form>

<p>Hai già un account? <a href="login.jsp">Login</a></p>
<p style="color:red;"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
</body>
</html>
