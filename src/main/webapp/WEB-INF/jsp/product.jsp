<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Prodotto</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <%@include file="fragments/header.jsp"%>

        <form action="${pageContext.request.contextPath}/product" method="POST" id="productActionForm">

            <input type="hidden" name="idProduct" value="${product.id}">
            <input type="hidden" name="action" id="actionInput">

            <div id="main">
                <c:choose>
                    <c:when test="${not empty product}">
                        <div id="product-box">
                            <div id="product-element">
                                <img id="product-image" src="${pageContext.request.contextPath}/images/products/${product.id}.png">
                                <div id="product-details">
                                    <div id="product-name">${product.name}</div>
                                    <div id="product-description">${product.description}</div>
                                    <div class="product-fieldvalue">
                                        <div class="product-field">Brand:</div>
                                        <div class="product-value">${product.brand}</div>
                                    </div>
                                    <div class="product-fieldvalue">
                                        <div class="product-field">Prezzo:</div>
                                        <div class="product-value"><fmt:formatNumber value="${product.price}" type="currency" pattern="€0.00"/></div>
                                    </div>
                                    <div class="product-fieldvalue">
                                        <div class="product-field">Categoria:</div>
                                        <div class="product-value">${product.category.toString()}</div>
                                    </div>
                                    <div class="product-fieldvalue">
                                        <div class="product-field">Grado:</div>
                                        <div class="product-value">${product.grade.toString()}</div>
                                    </div>
                                    <div class="product-fieldvalue">
                                        <div class="product-field">Disponibilità:</div>
                                        <div class="product-value">${product.stockQuantity}</div>
                                    </div>
                                    <div class="product-fieldvalue">
                                        <label for="quantity-input" class="product-field">Quantità:</label>
                                        <input type="number" id="quantity-input" name="quantity"
                                               class="product-quantity-input"
                                               value="1"
                                               min="1"
                                               max="${product.stockQuantity}">
                                    </div>
                                    <div class="product-buttons">
                                        <c:if test="${not (role == 'Guest')}">
                                            <button type="button" class="product-button" onclick="submitProductAction('addToWishlist')">
                                                Aggiungi alla lista preferiti
                                            </button>
                                            <button type="button" class="product-button" onclick="submitProductAction('addToCart')">
                                                Aggiungi al carrello
                                            </button>
                                        </c:if>
                                        <button type="button" class="product-button" onclick="submitProductAction('buyNow')">
                                            Acquista ora
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="product-box-empty">
                            <div id="not-existing-product-warning">Questo prodotto non esiste</div>
                            <div class="product-proceed"><a class="ref" href="${pageContext.request.contextPath}/home">Torna alla Home</a></div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
        <script>
            function submitProductAction(action) {
                document.getElementById('actionInput').value = action;
                document.getElementById('productActionForm').submit();
            }
        </script>
        <%@include file="fragments/footer.jsp"%>
    </body>
</html>
