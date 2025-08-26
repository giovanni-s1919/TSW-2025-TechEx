<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Area Personale</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/personal_area.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body id="personal_area">
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <ul id="account_voices">
                <li data-target="account"><i class="fa-solid fa-house"></i>Account</li>
                <li data-target="addresses"><i class="fa-solid fa-house"></i>Indirizzi</li>
                <li data-target="payments"><i class="fa-solid fa-house"></i>Metodi di pagamento</li>
                <li data-target="orders"><i class="fa-solid fa-house"></i>Ordini</li>
                <li data-target="returns"><i class="fa-solid fa-house"></i>Resi e sostituzioni</li>
                <li data-target="support"><i class="fa-solid fa-house"></i>Aiuto e contatti</li>
            </ul>
            <div id="account_content">
                <div class="content-panel" id="account">
                    Benvenuto nel tuo account personale.
                </div>
                <div class="content-panel" id="addresses">
                    Qui puoi gestire i tuoi indirizzi di spedizione.
                </div>
                <div class="content-panel" id="payments">
                    Aggiungi o modifica i tuoi metodi di pagamento.
                </div>
                <div class="content-panel" id="orders">
                    Visualizza lo storico dei tuoi ordini.
                </div>
                <div class="content-panel" id="returns">
                    Gestisci resi e sostituzioni.
                </div>
                <div class="content-panel" id="support">
                    Hai bisogno di aiuto? Contattaci qui.
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/personal_area.js"></script>
        <%@ include file="fragments/footer.jsp" %>
    </body>
</html>