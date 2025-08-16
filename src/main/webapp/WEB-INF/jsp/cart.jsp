<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%-- STEP 1: Aggiungi le librerie JSTL in cima alla pagina --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>TechEx - Carrello</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<%@ include file="fragments/header.jsp" %>
<div id="main">
    <%-- STEP 2: Usa un blocco <c:choose> per gestire sia il carrello pieno che quello vuoto --%>
    <c:choose>
        <%-- CASO A: Il carrello NON è vuoto --%>
        <c:when test="${not empty cartItems}">
            <div id="cart-box">
                    <%-- STEP 3: Itera sulla lista "cartItems" inviata dal servlet --%>
                <c:forEach var="item" items="${cartItems}">
                    <div class="cart-element">
                        <img class="cart-product-image" src="${pageContext.request.contextPath}/images/prodotto.jpg"> <%-- NB: L'immagine è ancora statica --%>
                        <div class="cart-product-details">
                                <%-- Usa Expression Language ${...} per accedere ai dati dell'oggetto --%>
                            <div class="cart-product-name">${item.product.name}</div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Brand: </div>
                                <div class="cart-product-value">${item.product.brand}</div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Categoria: </div>
                                <div class="cart-product-value">${item.product.category}</div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Quantità: </div>
                                <div class="cart-product-value">${item.quantity}</div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Prezzo: </div>
                                <div class="cart-product-value">
                                        <%-- Usa la libreria <fmt> per formattare il prezzo come valuta --%>
                                    <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="€"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div id="cart-confirm">
                <div class="cart-total">
                    <span id="cart-total-field">Totale: </span>
                    <span id="cart-total-value">
                        <fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="€"/>
                    </span>
                </div>
                <button id="cart-proceed">Procedi con l'acquisto</button>
            </div>
        </c:when>

        <%-- CASO B: Il carrello è vuoto --%>
        <c:otherwise>
            <div id="cart-box-empty">
                <h2>Il tuo carrello è vuoto</h2>
                <p>Aggiungi prodotti per visualizzarli qui.</p>
                <a href="${pageContext.request.contextPath}/" class="button">Torna alla Home</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="fragments/footer.jsp" %>
</body>
</html>