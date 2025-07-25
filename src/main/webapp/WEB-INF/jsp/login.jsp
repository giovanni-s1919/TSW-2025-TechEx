<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <title>TechEx - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login-register.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function (){
            $("#preg").on("click", function(e){
                e.preventDefault();
                $("form").attr("action", "${pageContext.request.contextPath}/register");
                $(".reg").addClass("open");
                $("#preg").fadeOut(150).promise().done(function(){
                    $("#plog").fadeIn(150);
                });
                $("#hlog").fadeOut(150).promise().done(function(){
                    $("#hreg").fadeIn(150);
                });
                $(".databutt").text("Registrati");
            });

            $("#plog").on("click", function(e){
               e.preventDefault();
               $("form").attr("action", "${pageContext.request.contextPath}/login");
               $(".reg").removeClass("open");
               $("#plog").fadeOut(150).promise().done(function(){
                   $("#preg").fadeIn(150);
               });
               $("#hreg").fadeOut(150).promise().done(function(){
                   $("#hlog").fadeIn(150);
               });
               $(".databutt").text("Login")
            });
        });
    </script>
</head>
    <body class="loginregister">
        <div>
            <form method="post" action="login">
                <fieldset>
                    <h2 id="hlog" style="font-size: 100%;">Login</h2>
                    <h2 hidden id="hreg" style="font-size: 100%;">Registrazione</h2>

                    <div class="divauth reg">
                        <label for="username" class="hiddenlabel" hidden>Username: </label>
                        <input type="text" id="username" required class="auth" name="username" placeholder="Username">
                    </div>
                    <div class="divauth">
                        <label for="loginemail" class="hiddenlabel" hidden>Email: </label>
                        <input type="email" id="loginemail" name="email" required class="auth" placeholder="Email">
                    </div>
                    <div class="divauth">
                        <label for="loginpassword" class="hiddenlabel" hidden>Password: </label>
                        <input type="password" id="loginpassword" name="password" required class="auth" placeholder="Password">
                    </div>
                    <div class="divauth reg">
                        <label for="registerpassword" class="hiddenlabel" hidden></label>
                        <input type="password" id="registerpassword" name="confirm" required class="auth" placeholder="Confirm Password">
                    </div>
                    <div>
                        <button type="submit" class="databutt">Login</button>
                    </div>
                    <a href="${pageContext.request.contextPath}" class="ref"><img src="${pageContext.request.contextPath}/images/logo.png" class="logo" alt="TechEx"></a>
                </fieldset>
            </form>
        </div>
        <div class="suggestion">
            <!--<p>Non hai un account? <a href="${pageContext.request.contextPath}/register" class="logregswitch">Registrati</a></p>-->
            <p id="preg">Non hai un account? <button class="logregswitch">Registrati</button></p>
            <p hidden id="plog">Hai già un account?<button class="logregswitch">Accedi</button></p>
        </div>
            <p style="color:red;" id="error"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
        <%@ include file="fragments/footer.jsp" %>
    </body>
</html>
