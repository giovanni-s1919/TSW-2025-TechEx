<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Conferma dell'ordine</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_confirmation.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/headlogo.png">
    </head>
    <body>
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <div class="confirmation-container">
                <div class="confirmation-header">
                    <i class="fas fa-check-circle"></i>
                    <h1>Grazie per il tuo acquisto!</h1>
                    <h2>Il tuo ordine con ID #${order.id} è stato confermato con successo!</h2>
                    <p>Abbiamo inviato un'email di riepilogo al tuo indirizzo di posta elettronica. Prepareremo il tuo ordine per la spedizione il prima possibile.</p>
                </div>
                <div class="details-grid">
                    <div class="order-summary-card summary-box">
                        <h1 class="checkout-phrase">Riepilogo Ordine</h1>
                        <div class="summary-items">
                            <c:forEach items="${items}" var="displayItem">
                                <div class="summary-item">
                                    <img class="summary-item-image"
                                         src="${pageContext.request.contextPath}/images/products/${displayItem.product.id}.png"
                                         alt="${displayItem.product.name}">
                                    <span class="summary-item-name">${displayItem.product.name} (x${displayItem.orderItem.itemQuantity})</span>
                                    <span class="summary-item-price">
                                        <fmt:formatNumber value="${displayItem.orderItem.itemPrice * displayItem.orderItem.itemQuantity}" type="currency" pattern="¤#,##0.00"/>
                                    </span>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="summary-totals">
                            <div class="total-row">
                                <span>Subtotale:</span>
                                <span><fmt:formatNumber value="${subtotal}" type="currency" pattern="¤#,##0.00"/></span>
                            </div>
                            <div class="total-row">
                                <span>Costi di spedizione:</span>
                                <c:choose>
                                    <c:when test="${shippingCost <= 0}">
                                        <span class="free-shipping">GRATIS</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span><fmt:formatNumber value="${shippingCost}" type="currency" pattern="¤#,##0.00"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="total-row grand-total">
                                <span>Totale:</span>
                                <span><fmt:formatNumber value="${order.totalAmount}" type="currency" pattern="¤#,##0.00"/></span>
                            </div>
                        </div>
                    </div>
                    <div class="shipping-details-card summary-box">
                        <h1 class="checkout-phrase">Indirizzo di spedizione</h1>
                        <div class="address-details">
                            <p><strong>${shippingAddress.name} ${shippingAddress.surname}</strong></p>
                            <p>${shippingAddress.street}</p>
                            <p>${shippingAddress.city},  ${shippingAddress.postalCode}</p>
                            <p>${shippingAddress.region}</p>
                            <p>${shippingAddress.country}</p>
                            <p>${shippingAddress.phone}</p>
                        </div>
                    </div>
                </div>
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/home" class="cta-button primary">Continua lo shopping</a>
                    <c:if test="${role ne 'Guest'}">
                        <a href="${pageContext.request.contextPath}/personal_area#orders" class="cta-button secondary">I miei ordini</a>
                    </c:if>
                </div>
            </div>
        </div>
        <%@ include file="fragments/footer.jsp" %>
    </body>
</html>