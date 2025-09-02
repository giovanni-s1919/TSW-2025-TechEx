<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Area Personale</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/personal_area.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>const contextPath = "${pageContext.request.contextPath}";</script>
    </head>
    <body id="personal_area">
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <ul id="account_voices">
                <li data-target="account"><i class="fa-solid fa-user"></i>Account</li>
                <li data-target="addresses"><i class="fa-solid fa-location-dot"></i>Indirizzi</li>
                <li data-target="payments"><i class="fa-solid fa-credit-card"></i>Metodi di pagamento</li>
                <li data-target="orders"><i class="fa-solid fa-cart-shopping"></i>Ordini</li>
                <li data-target="returns"><i class="fa-solid fa-wrench"></i>Resi e sostituzioni</li>
                <li data-target="support"><i class="fa-solid fa-phone"></i>Aiuto e contatti</li>
            </ul>
            <div id="account_content">
                <div class="content-panel active" id="account">
                    <h1 class="content_intro">Account</h1>
                    <p class="content_description">Benvenuto nel tuo profilo personale. Qui puoi visualizzare e modificare le tue informazioni personali.</p>
                    <div class="user-info-section">
                        <div class="info-item">
                            <label for="name">Nome:</label>
                            <input type="text" id="name" name="name" value="${userProfile.name}" readonly>
                            <button class="edit-btn" data-field="name" data-original-value="${userProfile.name}">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="surname">Cognome:</label>
                            <input type="text" id="surname" name="surname" value="${userProfile.surname}" readonly>
                            <button class="edit-btn" data-field="surname" data-original-value="${userProfile.surname}">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="birthDate">Data di nascita:</label>
                            <input type="date" id="birthDate" name="birthDate" value="${userProfile.birthDate}" readonly>
                            <button class="edit-btn" data-field="birthDate" data-original-value="${userProfile.birthDate}">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="phone">Telefono:</label>
                            <input type="tel" id="phone" name="phone" value="${userProfile.phone}" readonly>
                            <button class="edit-btn" data-field="phone" data-original-value="${userProfile.phone}">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="username">Username:</label>
                            <input type="text" id="username" name="username" value="${userProfile.username}" readonly>
                            <button class="edit-btn" data-field="username" data-original-value="${userProfile.username}">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" value="${userProfile.email}" readonly>
                            <button class="edit-btn" data-field="email" data-original-value="${userProfile.email}">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="password">Password:</label>
                            <input type="password" id="password" name="password" value="*************" readonly>
                            <button class="edit-btn" data-field="password">Modifica</button>
                        </div>
                        <div class="info-item">
                            <label for="role">Ruolo:</label>
                            <input type="text" id="role" name="role" value="${userProfile.role}" readonly>
                        </div>
                    </div>
                    <div id="messages"></div>
                </div>
                <div class="content-panel" id="addresses">
                    <h1 class="content_intro">Indirizzi</h1>
                    <p class="content_description">Qui puoi gestire i tuoi indirizzi di spedizione e fatturazione.</p>
                    <div id="address-list-container"></div>
                    <div class="add-address-container">
                        <button id="add-address-btn" class="save-btn">
                            <i class="fa-solid fa-plus"></i> Aggiungi un nuovo indirizzo
                        </button>
                    </div>
                </div>
                <div class="content-panel" id="payments">
                    <h1 class="content_intro">Metodi di pagamento</h1>
                    <p class="content_description">Aggiungi e visualizza i tuoi metodi di pagamento.</p>
                </div>
                <div class="content-panel" id="orders">
                    <h1 class="content_intro">Ordini</h1>
                    <p class="content_description">Visualizza lo storico dei tuoi ordini.</p>
                </div>
                <div class="content-panel" id="returns">
                    <h1 class="content_intro">Resi e sostituzioni</h1>
                    <p class="content_description">Gestisci resi e sostituzioni.</p>
                </div>
                <div class="content-panel" id="support">
                    <h1 class="content_intro">Aiuto e contatti</h1>
                    <p class="content_description">Hai bisogno di aiuto? Contattaci qui.</p>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/personal_area.js"></script>
        <%@ include file="fragments/footer.jsp" %>
    </body>
</html>