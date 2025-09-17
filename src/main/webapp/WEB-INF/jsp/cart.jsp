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
<script>
    if ("${role}" === "Guest") {
        window.location.replace("${pageContext.request.contextPath}/login");
    }
</script>
<%@ include file="fragments/header.jsp" %>
<div id="main">
    <c:choose>
        <c:when test="${not empty cartItems}">
            <div id="cart-box">
                <c:forEach var="item" items="${cartItems}" varStatus="loop">
                    <div class="cart-element" id="item-row-${item.product.id}">
                        <img class="cart-product-image" id="${item.product.id}" src="${pageContext.request.contextPath}/images/products/${item.product.id}.png" alt="${item.product.name}">
                        <div class="cart-product-details">
                            <div class="cart-product-name">${item.product.name}</div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Brand: </div>
                                <div class="cart-product-value">${item.product.brand}</div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Categoria: </div>
                                <div class="cart-product-value">${item.product.category.toString()}</div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Quantità: </div>
                                <div class="cart-product-value">
                                    <input type="number"
                                           class="quantity-input"
                                           value="${item.quantity}"
                                           min="1"
                                           max="${item.product.stockQuantity}"
                                           data-product-id="${item.product.id}"
                                           onchange="updateCartQuantity(this, this.value)">
                                </div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Prezzo unitario:</div>
                                <div class="cart-product-value">
                                    <fmt:formatNumber value="${item.product.price}" type="currency" pattern="€0.00"/>
                                </div>
                            </div>
                            <div class="cart-product-fieldvalue">
                                <div class="cart-product-field">Subtotale:</div>
                                <div class="cart-product-value" id="subtotal-${item.product.id}">
                                    <fmt:formatNumber value="${item.product.price * item.quantity}" type="currency" pattern="€0.00"/>
                                </div>
                            </div>
                            <div class="cart-item-actions">
                                <button class="remove-button" data-product-id="${item.product.id}" onclick="updateCartQuantity(this, 0)">Rimuovi dal carrello</button>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not loop.last}">
                        <div class="separator" id="separator-${item.product.id}"></div>
                    </c:if>
                </c:forEach>
            </div>
            <div id="cart-confirm">
                <div class="cart-total">
                    <span id="cart-total-field">Totale: </span>
                    <span id="cart-total-value">
                        <fmt:formatNumber value="${cartTotal}" type="currency" pattern="€0.00"/>
                    </span>
                </div>
                <button class="cart-proceed" id="proceed-to-checkout-btn">Procedi con l'acquisto</button>
            </div>
        </c:when>
        <c:otherwise>
            <script>
                $("body").addClass("empty");
            </script>
            <div id="cart-box-empty">
                <div id="cart-box-empty-warning">Il tuo carrello è vuoto</div>
                <div class="cart-proceed"><a class="ref" href="${pageContext.request.contextPath}/home">Torna alla Home</a></div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<%@ include file="fragments/footer.jsp" %>
<script>
    $(".cart-product-image").on("click", function(e){
        e.preventDefault();
        window.location.href = "${pageContext.request.contextPath}/product?idProduct="+$(this).attr("id");
    });

    async function updateCartQuantity(element, forceQuantity) {
        const isRemoving = (forceQuantity === 0);
        const productId = element.dataset.productId;
        console.log(productId);
        const quantity = isRemoving ? 0 : element.value;

        const formData = new FormData();
        formData.append('action', 'updateQuantity');
        formData.append('productId', productId);
        formData.append('quantity', quantity);

        try {
            const response = await fetch('${pageContext.request.contextPath}/cart', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error(`Errore dal server: ${response.status} ${response.statusText}`);
            }

            const data = await response.json();

            if (data.success) {
                if (isRemoving) {
                    console.log("Removing cart element");
                    const elementIdToFind = `item-row-` + productId;
                    const separatorToFind = `separator-` + productId;
                    console.log("Tentativo di rimuovere l'elemento con ID:", elementIdToFind);
                    const elementToRemove = document.getElementById(elementIdToFind);
                    console.log("Elemento trovato:", elementToRemove);
                    document.getElementById(elementIdToFind).remove();
                    let separator = document.getElementById(separatorToFind);
                    if(separator) {
                        separator.remove();
                    } else {
                        separator = document.getElementById('separator-' + (productId-1));
                        if(separator) {
                            separator.remove();
                        }
                    }
                } else {
                    const subTotalElement = `subtotal-`+ productId;
                    document.getElementById(subTotalElement).textContent = data.newSubtotalFormatted;
                }
                document.getElementById('cart-total-value').textContent = data.newCartTotalFormatted;

                if (data.cartIsEmpty) {
                    console.log('Cart is empty');
                    window.location.reload();
                }
            } else {

                alert("Impossibile aggiornare: " + data.message);
                window.location.reload();
            }
        } catch (error) {
            console.error('Errore nella chiamata AJAX:', error);
            alert('Errore di comunicazione con il server. Apri la console (F12) per i dettagli.');
        }
    }
</script>
<script>
    const checkoutBtn = document.getElementById('proceed-to-checkout-btn');
    if (checkoutBtn) {
        checkoutBtn.addEventListener('click', function() {
            window.location.href = '${pageContext.request.contextPath}/checkout?from=cart';
        });
    }
</script>
</body>
</html>