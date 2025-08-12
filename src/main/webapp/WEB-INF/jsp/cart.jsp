<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<html>
<head>
    <title>TechEx - Carrello</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<%@ include file="fragments/header.jsp" %>

<div id="main">
    <div id="cart-box">
        <div class="cart-element">
            <img class="cart-product-image" src="${pageContext.request.contextPath}/images/prodotto.jpg">
            <div class="cart-product-details">
                <div class="cart-product-name">iPhone 11 Pro Display</div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Brand: </div>
                    <div class="cart-product-value">Yodoit</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Categoria: </div>
                    <div class="cart-product-value">Schermi</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Quantità: </div>
                    <div class="cart-product-value">5</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Price: </div>
                    <div class="cart-product-value">€29,99</div>
                </div>
            </div>
        </div>
        <div class="cart-element">
            <img class="cart-product-image" src="${pageContext.request.contextPath}/images/prodotto.jpg">
            <div class="cart-product-details">
                <div class="cart-product-name">iPhone 11 Pro Display</div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Brand: </div>
                    <div class="cart-product-value">Yodoit</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Categoria: </div>
                    <div class="cart-product-value">Schermi</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Quantità: </div>
                    <div class="cart-product-value">5</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Price: </div>
                    <div class="cart-product-value">€29,99</div>
                </div>
            </div>
        </div>
        <div class="cart-element">
            <img class="cart-product-image" src="${pageContext.request.contextPath}/images/prodotto.jpg">
            <div class="cart-product-details">
                <div class="cart-product-name">iPhone 11 Pro Display</div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Brand: </div>
                    <div class="cart-product-value">Yodoit</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Categoria: </div>
                    <div class="cart-product-value">Schermi</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Quantità: </div>
                    <div class="cart-product-value">5</div>
                </div>
                <div class="cart-product-fieldvalue">
                    <div class="cart-product-field">Prezzo: </div>
                    <div class="cart-product-value">€29,99</div>
                </div>
            </div>
        </div>
    </div>
    <div id="cart-confirm">
        <div class="cart-total">
            <span id="cart-total-field">Totale: </span>
            <span id="cart-total-value">€89,97</span>
        </div>
        <button id="cart-proceed">Procedi con l'acquisto</button>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>
</body>
</html>
