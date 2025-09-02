<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
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
    <c:choose>
        <c:when test="${not empty cartItems}">
            <div id="cart-box">
                <c:forEach var="item" items="${cartItems}">
                    <div class="cart-element">
                        <img class="cart-product-image" src="${pageContext.request.contextPath}/images/prodotto.jpg"> <%-- L'immagine è ancora statica --%>
                        <div class="cart-product-details">
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
                <button class="cart-proceed">Procedi con l'acquisto</button>
            </div>
        </c:when>

        <c:otherwise>
            <div id="cart-box-empty">
                <div id="cart-box-empty-warning">Il tuo carrello è vuoto</div>
                <div class="cart-proceed"><a class="ref" href="${pageContext.request.contextPath}/home">Torna alla Home</a></div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="fragments/footer.jsp" %>
</body>
</html>