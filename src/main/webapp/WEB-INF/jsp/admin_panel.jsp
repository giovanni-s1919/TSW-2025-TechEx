<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>TechEx - Pannello Amministrazione</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_panel.css">
    <script>window.contextPath = "${pageContext.request.contextPath}";</script>
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
                        <th>Immagine</th> <!-- <-- COLONNA MANCANTE AGGIUNTA QUI -->
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

<div id="product-modal" class="modal">
    <div class="modal-content">
        <span class="close-button">&times;</span>
        <h2 id="modal-title">Aggiungi Nuovo Prodotto</h2>
        <form id="product-form">
            <input type="hidden" id="productId" name="productId">

            <div class="form-group">
                <label for="productName">Nome Prodotto:</label>
                <input type="text" id="productName" name="name" required>
            </div>
            <div class="form-group">
                <label for="productBrand">Brand:</label>
                <input type="text" id="productBrand" name="brand" required>
            </div>
            <div class="form-group">
                <label for="productPrice">Prezzo:</label>
                <input type="number" step="0.01" id="productPrice" name="price" required>
            </div>
            <div class="form-group">
                <label for="productQuantity">Quantità in Stock:</label>
                <input type="number" id="productQuantity" name="stockQuantity" required>
            </div>

            <!-- CAMPO CATEGORIA (MIGLIORATO) -->
            <div class="form-group">
                <label for="productCategory">Categoria:</label>
                <select id="productCategory" name="category" required>
                    <option value="Display">Display & Touchscreen</option>
                    <option value="Camera">Fotocamera</option>
                    <option value="Battery">Batteria</option>
                    <option value="Microphone">Microfono</option>
                    <option value="Speaker">Altoparlante</option>
                    <option value="Case">Scocca</option>
                    <option value="Button">Tasto</option>
                    <option value="Sensor">Sensore</option>
                </select>
            </div>

            <!-- CAMPO GRADE (AGGIUNTO) -->
            <div class="form-group">
                <label for="productGrade">Grado:</label>
                <select id="productGrade" name="grade" required>
                    <option value="Original">Originale</option>
                    <option value="Excellent">Eccellente</option>
                    <option value="Great">Ottimo</option>
                    <option value="Good">Buono</option>
                </select>
            </div>

            <!-- CAMPO VAT (AGGIUNTO) -->
            <div class="form-group">
                <label for="productVat">IVA (%):</label>
                <input type="number" step="0.01" id="productVat" name="vat" required>
            </div>

            <div class="form-group">
                <label for="productImage">Immagine Prodotto (verrà rinominata in base all'ID):</label>
                <input type="file" id="productImage" name="image" accept="image/png, image/jpeg, image/webp">
                <div id="image-preview" style="margin-top:10px;"></div> <!-- Area per l'anteprima -->
            </div>

            <div class="form-group">
                <label for="productDescription">Descrizione:</label>
                <textarea id="productDescription" name="description" rows="4"></textarea>
            </div>

            <div class="modal-actions">
                <button type="submit" class="save-btn">Salva Prodotto</button>
            </div>
            <div id="productModalMessages"></div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/admin_panel.js"></script>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>