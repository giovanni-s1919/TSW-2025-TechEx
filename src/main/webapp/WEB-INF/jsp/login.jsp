<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <title>TechEx - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login-register.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        let islogin = <%= request.getAttribute("islogin")%>;
        let usermail = true;
        let mailvalue;
        let usernamevalue;
        $(document).ready(function (){
            $(".logregswitch").on("click", function(e){
                e.preventDefault();
                if(islogin === true){
                    //Si preme registrati
                    islogin = false;
                    $("title").text("TechEx - Sign in");
                    $("form").attr("action", "${pageContext.request.contextPath}/register");
                    $(".reg").addClass("open");
                    if(!usermail){
                        $("#divemail").addClass("open");
                    }
                    $("#usermailswitch").removeClass("open");
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
                    if(usermail){
                        $("#username").attr("required", true);
                    }
                    $("#registerpassword").attr("required", true);
                }
                else{
                    //Si preme accedi
                    islogin = true;
                    $("title").text("TechEx - Login");
                    $("form").attr("action", "${pageContext.request.contextPath}/login");
                    if(usermail){
                        $(".reg").removeClass("open");
                    }
                    else{
                        $("#pwdcnf").removeClass("open");
                        $("#divemail").removeClass("open");
                        $("#role-table").removeClass("open");
                        $(".roleselection").removeClass("open");
                    }
                    $("#usermailswitch").addClass("open");
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

            $("#usermailswitch").on("click", function(e){
                e.preventDefault();
               if(usermail){    //Si sceglie di accedere con Username
                   usermail = false;
                   $("#usermailswitch").fadeOut(250).promise().done(function(){
                       $("#usermailswitch").text("Accedi con Email").promise().done(function(){
                           $("#usermailswitch").fadeIn(250);
                       });
                   });
                   $("#divemail").removeClass("open");
                   $("#divusername").addClass("open");
                   $("#loginemail").attr("required", false);
                   $("#username").attr("required", true);

                   mailvalue = $("#loginemail").val();
                   $("#loginemail").val(null);
                   $("#username").val(usernamevalue);

               }
               else{        //Si sceglie di accedere con Email
                   usermail = true;
                   $("#usermailswitch").fadeOut(250).promise().done(function(){
                       $("#usermailswitch").text("Accedi con Username").promise().done(function(){
                           $("#usermailswitch").fadeIn(250);
                       });
                   });
                   $("#divusername").removeClass("open");
                   $("#divemail").addClass("open");
                   $("#username").attr("required", false);
                   $("#loginemail").attr("required", true);

                   usernamevalue = $("#username").val();
                   $("#username").val(null);
                   $("#loginemail").val(mailvalue)
               }
            });
        });
    </script>
</head>
    <body class="loginregister">
        <div id="content">
            <form method="post" action="login" id="signin">
                <fieldset>
                    <h2 id="ftitle" style="font-size: 100%;">Login</h2>

                    <div class="divauth reg" id="divname">
                        <label for="name" class="hiddenlabel" hidden></label>
                        <input type="text" id="name" class="auth" name="name" placeholder="Nome">
                    </div>
                    <div class="divauth reg" id="divsurname">
                        <label for="surname" class="hiddenlabel" hidden></label>
                        <input type="text" id="surname" class="auth" name="surname" placeholder="Cognome">
                    </div>
                    <div class="divauth reg" id="divdate">
                        <label for="birthDate" class="hiddenlabel" hidden></label>
                        <input type="date" class="auth" name="birthDate">
                    </div>
                    <div class="divauth reg" id="divusername">
                        <label for="username" class="hiddenlabel" hidden></label>
                        <input type="text" id="username" class="auth" name="username" placeholder="Username">
                    </div>
                    <div class="divauth open" id="divemail">
                        <label for="loginemail" class="hiddenlabel" hidden></label>
                        <input type="email" id="loginemail" name="email" required class="auth" placeholder="Email">
                    </div>
                    <div class="divauth reg" id="divphone">
                        <label for="phonenumber" class="hiddenlabel" hidden></label>
                        <input type="tel" name="phonenumber" class="auth" placeholder="Numero di telefono">
                    </div>
                    <div class="divauth">
                        <label for="loginpassword" class="hiddenlabel" hidden></label>
                        <input type="password" id="loginpassword" name="password" required class="auth" placeholder="Password">
                    </div>
                    <div class="divauth reg" id="pwdcnf">
                        <label for="registerpassword" class="hiddenlabel" hidden></label>
                        <input type="password" id="registerpassword" name="confirm" class="auth" placeholder="Confirm Password">
                    </div>
                    <div id="role-table" class="reg">
                        <div class="roleselection reg">
                            <label for="roleuser" class="rolevalues">Utente</label>
                            <input type="radio" name="role" id="roleuser" value="Customer" checked>
                        </div>
                        <div class="roleselection reg">
                            <label for="roleadmin" class="rolevalues">Amministratore</label>
                            <input type="radio" name="role" id="roleadmin" value="Admin">
                        </div>
                    </div>
                    <div>
                        <button type="submit" class="databutt">Login</button>
                    </div>
                    <div id="usermailswitch" class="divauth open">
                        Accedi con Username
                    </div>
                    <a href="${pageContext.request.contextPath}/" class="ref"><img src="${pageContext.request.contextPath}/images/logo.png" class="logo" alt="TechEx"></a>
                </fieldset>
            </form>
            <div class="suggestion">
                <p id="preg">Non hai un account? <button class="logregswitch">Registrati</button></p>
                <p hidden id="plog">Hai già un account?<button class="logregswitch">Accedi</button></p>
            </div>
        </div>
        <p id="error"><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
        <%@ include file="fragments/footer.jsp" %>
        <script>
            if(islogin === false){
                $("title").text("TechEx - Sign in");
                $("form").attr("action", "${pageContext.request.contextPath}/register");
                $(".reg").addClass("open");
                $("#usermailswitch").removeClass("open");
                $("#preg").hide();
                $("#plog").show();
                $("#ftitle").text("Registrati");
                $(".databutt").text("Registrati");
                $("#username").attr("required", true);
                $("#registerpassword").attr("required", true);
            }
        </script>
    </body>
</html>