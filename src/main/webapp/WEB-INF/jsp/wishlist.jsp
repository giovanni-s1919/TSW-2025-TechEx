<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>TechEx - Lista Desideri</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/wishlist.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <script>
        if ("${role}" === "Guest") {
            window.location.replace("${pageContext.request.contextPath}/login");
        }
    </script>
    <%@ include file="fragments/header.jsp" %>
    <div id="main">
        <c:choose>
            <c:when test="${not empty displayItems}">
                <div id="wishlist-box">
                    <c:forEach var="item" items="${displayItems}" varStatus="loop">
                        <div class="wishlist-item">
                            <img class="wishlist-product-image" src="${pageContext.request.contextPath}/images/products/${item.product.id}.png">
                            <div class="wishlist-product-details">
                                <div class="wishlist-product-name">${item.product.name}</div>
                                <div class="wishlist-product-fieldvalue">
                                    <div class="wishlist-product-field">Brand:</div>
                                    <div class="wishlist-product-value">${item.product.brand}</div>
                                </div>
                                <div class="wishlist-product-fieldvalue">
                                    <div class="wishlist-product-field">Categoria:</div>
                                    <div class="wishlist-product-value">${item.product.category}</div>
                                </div>
                                <div class="wishlist-product-fieldvalue">
                                    <div class="wishlist-product-field">Grado:</div>
                                    <div class="wishlist-product-value">${item.product.grade}</div>
                                </div>
                                <div class="wishlist-product-fieldvalue">
                                    <div class="wishlist-product-field">Prezzo:</div>
                                    <div class="wishlist-product-value">
                                        <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="€"/>
                                    </div>
                                </div>
                            </div>
                            <div class="wishlist-interact">
                                <div class="wishlist-interation">
                                    Aggiungi al carrello
                                </div>
                                <div class="wishlist-interation">
                                    Rimuovi dalla lista preferiti
                                </div>
                            </div>
                        </div>
                        <c:if test="${not loop.last}">
                            <div id="separator"></div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div id="wishlist-box-empty">
                    <div id="empty-wishlist-warning">La lista desideri è vuota</div>
                    <div class="wishlist-proceed"><a class="ref" href="${pageContext.request.contextPath}/home">Torna alla homepage</a></div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
