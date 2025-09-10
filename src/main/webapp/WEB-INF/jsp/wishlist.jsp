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
    <form action="${pageContext.request.contextPath}/wishlist" method="POST" id="wishlistActionForm">

        <input type="hidden" name="idProduct" id="productIdInput">
        <input type="hidden" name="action" id="actionInput">
    </form>
    <div id="main">
        <c:choose>
            <c:when test="${not empty displayItems}">
                <div id="wishlist-box">
                    <c:forEach var="item" items="${displayItems}" varStatus="loop">
                        <div class="wishlist-item">
                            <img class="wishlist-product-image" id="${item.product.id}" src="${pageContext.request.contextPath}/images/products/${item.product.id}.png">
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
                                <button class="wishlist-interation" id="${item.product.id}" onclick="submitWishlistAction('${item.product.id}', 'addToCart')">
                                    Aggiungi al carrello
                                </button>
                                <button class="wishlist-interation" onclick="submitWishlistAction('${item.product.id}', 'removeFromWishlist')">
                                    Rimuovi dalla lista
                                </button>
                            </div>
                        </div>
                        <c:if test="${not loop.last}">
                            <div id="separator"></div>
                        </c:if>
                    </c:forEach>
                    <script>
                        $(".wishlist-product-image").on("click", function(e) {
                            e.preventDefault();
                            window.location.href = "${pageContext.request.contextPath}/product?idProduct=" + $(this).attr("id");
                        })
                    </script>
                </div>
            </c:when>
            <c:otherwise>
                <div id="wishlist-box-empty">
                    <div id="empty-wishlist-warning">La lista desideri è vuota</div>
                    <a class="ref" href="${pageContext.request.contextPath}/home"><div class="wishlist-proceed">Torna alla Home</div></a>
    </div>
    </c:otherwise>
    </c:choose>
    </div>
    <script>
        function submitWishlistAction(productId, action) {
            document.getElementById('productIdInput').value = productId;
            document.getElementById('actionInput').value = action;
            document.getElementById('wishlistActionForm').submit();
        }

        $(".wishlist-product-image").on("click", function(e) {
            e.preventDefault();
            window.location.href = "${pageContext.request.contextPath}/product?idProduct=" + $(this).attr("id");
        });
    </script>
    <%@ include file="fragments/footer.jsp" %>
</body>
</html>
