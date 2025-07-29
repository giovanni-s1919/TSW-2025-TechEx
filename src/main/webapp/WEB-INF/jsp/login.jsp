<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <title>TechEx - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login-register.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        let islogin = <%= request.getAttribute("islogin")%>;
        $(document).ready(function (){
            $(".logregswitch").on("click", function(e){
                e.preventDefault();
                if(islogin === true){
                    //Si preme registrati
                    islogin = false;
                    $("form").attr("action", "${pageContext.request.contextPath}/register");
                    $(".reg").addClass("open");
                    $("#preg").fadeOut(150).promise().done(function(){
                        $("#plog").fadeIn(150);
                    });
                    $("#ftitle").fadeOut(150).promise().done(function(){
                        $("#ftitle").text("Registrati").promise().done(function(){
                            $("#ftitle").fadeIn(150);
                        });
                    });
                    $(".databutt").fadeOut(150).promise().done(function(){
                        $(".databutt").text("Registrati").promise().done(function(){
                            $(".databutt").fadeIn(150);
                        });
                    });
                    $("#username").attr("required", true);
                    $("#registerpassword").attr("required", true);
                }
                else{
                    //Si preme accedi
                    islogin = true;
                    $("form").attr("action", "${pageContext.request.contextPath}/login");
                    $(".reg").removeClass("open");
                    $("#plog").fadeOut(150).promise().done(function(){
                        $("#preg").fadeIn(150);
                    });

                    $("#ftitle").fadeOut(250).promise().done(function(){
                        $("#ftitle").text("Login").promise().done(function(){
                            $("#ftitle").fadeIn(250);
                        })
                    });

                    $(".databutt").fadeOut(250).promise().done(function(){
                        $(".databutt").text("Login").promise().done(function(){
                            $(".databutt").fadeIn(250);
                        });
                    });
                    $("#username").attr("required", false);
                    $("#registerpassword").attr("required", false);
                }
            });
        });
    </script>
</head>
    <body class="loginregister">
        <div id="content">
            <form method="post" action="login">
                <fieldset>
                    <h2 id="ftitle" style="font-size: 100%;">Login</h2>

                    <div class="divauth reg">
                        <label for="username" class="hiddenlabel" hidden>Username: </label>
                        <input type="text" id="username" class="auth" name="username" placeholder="Username">
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
                        <input type="password" id="registerpassword" name="confirm" class="auth" placeholder="Confirm Password">
                    </div>
                    <div class="divauth reg" id="roleselection">
                        <label for="roleuser" class="rolevalues">Utente</label>
                        <input type="radio" name="role" id="roleuser" value="user" checked>
                        <label for="roleadmin" class="rolevalues">Amministratore</label>
                        <input type="radio" name="role" id="roleadmin" value="admin">
                    </div>
                    <div>
                        <button type="submit" class="databutt">Login</button>
                    </div>
                    <a href="${pageContext.request.contextPath}/" class="ref"><img src="${pageContext.request.contextPath}/images/logo.png" class="logo" alt="TechEx"></a>
                </fieldset>
            </form>
            <div class="suggestion">
                <p id="preg">Non hai un account? <button class="logregswitch">Registrati</button></p>
                <p hidden id="plog">Hai già un account?<button class="logregswitch">Accedi</button></p>
            </div>
        </div>
            <p id="error"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %></p>
        <%@ include file="fragments/footer.jsp" %>
        <script>
            $("#error").attr("hidden", true);
            if(islogin === false){
            $("form").attr("action", "${pageContext.request.contextPath}/register");
            $(".reg").addClass("open");
            $("#preg").hide();
            $("#plog").show();
            $("#ftitle").text("Registrati");
            $(".databutt").text("Registrati");
            $("#username").attr("required", true);
            $("#registerpassword").attr("required", true);
        }</script>
    </body>
</html>
