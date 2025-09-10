<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>TechEx - Pannello Amministrazione</title>
    <%-- Usa lo stesso CSS o creane uno nuovo admin_panel.css --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/personal_area.css">
    <%-- ... altri link ... --%>
</head>
<body>
<%@ include file="fragments/header.jsp" %>
<div id="main">
    <%-- MENU LATERALE PER ADMIN --%>
    <ul id="account_voices">
        <li data-target="product-management"><i class="fa-solid fa-box-archive"></i>Gestione Prodotti</li>
        <li data-target="order-overview"><i class="fa-solid fa-cart-shopping"></i>Visualizzazione Ordini</li>
        <%-- Aggiungi altre voci se necessario --%>
    </ul>

    <div id="account_content">
        <%-- PANNELLO 1: GESTIONE PRODOTTI --%>
        <div class="content-panel active" id="product-management">
            <h1 class="content_intro">Gestione Catalogo Prodotti</h1>
            <p class="content_description">Aggiungi, modifica o rimuovi prodotti dal catalogo.</p>

            <div class="add-product-container">
                <button id="add-product-btn" class="save-btn">
                    <i class="fa-solid fa-plus"></i> Aggiungi Nuovo Prodotto
                </button>
            </div>

            <%-- La tabella dei prodotti verrà popolata qui da JavaScript --%>
            <div id="product-list-container">
                <%-- Esempio di struttura tabella --%>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Brand</th>
                        <th>Prezzo</th>
                        <th>Quantità</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%-- Le righe <tr> verranno inserite da JS --%>
                    </tbody>
                </table>
            </div>
        </div>

        <%-- PANNELLO 2: VISUALIZZAZIONE ORDINI --%>
        <div class="content-panel" id="order-overview">
            <h1 class="content_intro">Visualizzazione Ordini</h1>
            <p class="content_description">Filtra e visualizza tutti gli ordini effettuati.</p>

            <%-- Form per i filtri --%>
            <div id="order-filters">
                <input type="date" id="start-date" name="startDate">
                <input type="date" id="end-date" name="endDate">
                <input type="text" id="customer-id" name="customerId" placeholder="ID Cliente (opzionale)">
                <button id="filter-orders-btn">Filtra Ordini</button>
            </div>

            <%-- I risultati degli ordini verranno popolati qui da JavaScript --%>
            <div id="order-list-container"></div>
        </div>
    </div>
</div>
<%-- Includi il NUOVO file JS per l'admin --%>
<script src="${pageContext.request.contextPath}/js/admin_panel.js"></script>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>