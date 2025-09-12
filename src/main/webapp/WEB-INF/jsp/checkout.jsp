<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Checkout</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>const contextPath = "${pageContext.request.contextPath}";</script>
    </head>
    <body>
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <div class="checkout-container">
                <div class="customer-details-column">
                    <h1>Checkout</h1>
                    <form id="checkout-form" novalidate>
                        <div class="checkout-section">
                            <h2>Indirizzo di Spedizione</h2>
                            <c:choose>
                                <c:when test="${role ne 'Guest'}">
                                    <div class="selection-group">
                                        <select name="addressId" class="checkout-select">
                                            <c:forEach items="${userAddresses}" var="addr">
                                                <option value="${addr.id}" ${addr['default'] ? 'selected' : ''}>
                                                        ${addr.street}, ${addr.city}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <button type="button" class="add-new-btn" id="checkout-add-address-btn">Aggiungi</button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <p>Inserisci i tuoi dati per la spedizione:</p>
                                    <div class="form-group">
                                        <label for="guest_email">Email</label>
                                        <input type="email" id="guest_email" name="email" required>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="guest_name">Nome</label>
                                            <input type="text" id="guest_name" name="name" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_surname">Cognome</label>
                                            <input type="text" id="guest_surname" name="surname" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="guest_street">Indirizzo</label>
                                        <input type="text" id="guest_street" name="street" required>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="checkout-section">
                            <h2>Metodo di Pagamento</h2>
                            <c:choose>
                                <c:when test="${role ne 'Guest'}">
                                    <div class="selection-group">
                                        <select name="paymentMethodId" class="checkout-select">
                                            <c:forEach items="${userPaymentMethods}" var="pm">
                                                <option value="${pm.id}" ${pm['default'] ? 'selected' : ''}>
                                                        ${pm.cardType} ${pm.maskedNumber}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <button type="button" class="add-new-btn" id="checkout-add-payment-btn">Aggiungi</button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <p>Inserisci i dati della tua carta:</p>
                                    <div class="form-group">
                                        <label for="guest_card_name">Nome sulla Carta</label>
                                        <input type="text" id="guest_card_name" name="cardName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="guest_card_number">Numero Carta</label>
                                        <input type="text" id="guest_card_number" name="cardNumber" required placeholder="xxxx-xxxx-xxxx-xxxx" maxlength="19">
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="guest_card_expiration">Scadenza (MM/AAAA)</label>
                                            <input type="text" id="guest_card_expiration" name="cardExpiration" required placeholder="MM/AAAA" maxlength="7">
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_card_cvc">CVC</label>
                                            <input type="text" id="guest_card_cvc" name="cardCvc" required placeholder="123" maxlength="4">
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form>
                </div>
                <div class="order-summary-column">
                    <div class="checkout-section summary-box">
                        <h2>Riepilogo Ordine</h2>
                        <div class="summary-items">
                            <c:forEach items="${checkoutItems}" var="item">
                                <div class="summary-item">
                                    <span>${item.product.name} (x${item.quantity})</span>
                                    <span><fmt:formatNumber value="${item.product.price * item.quantity}" type="currency" currencySymbol="€"/></span>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="summary-total">
                            <span>Totale</span>
                            <span><fmt:formatNumber value="${checkoutTotal}" type="currency" currencySymbol="€"/></span>
                        </div>
                        <button type="submit" class="pay-button" form="checkout-form">Paga e Conferma Ordine</button>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="fragments/footer.jsp" %>
        <script src="${pageContext.request.contextPath}/js/checkout.js"></script>
    </body>
</html>