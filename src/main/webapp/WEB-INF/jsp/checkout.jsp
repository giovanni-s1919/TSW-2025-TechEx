<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Checkout</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/checkout.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/headlogo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>const contextPath = "${pageContext.request.contextPath}";</script>
    </head>
    <body>
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <div class="checkout-container">
                <div class="customer-details-column">
                    <h1 id="checkout-intro">Checkout</h1>
                    <form id="checkout-form" novalidate>
                        <div class="checkout-section">
                            <h1 class="checkout-phrase">Indirizzo di spedizione</h1>
                            <c:choose>
                                <c:when test="${role eq 'Guest'}">
                                    <p>Inserisci i tuoi dati per la spedizione:</p>
                                    <div class="form-group">
                                        <label for="guest_email">Email:</label>
                                        <input type="email" id="guest_email" name="email" required>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="guest_name">Nome:</label>
                                            <input type="text" id="guest_name" name="name" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_surname">Cognome:</label>
                                            <input type="text" id="guest_surname" name="surname" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="guest_street">Indirizzo:</label>
                                        <input type="text" id="guest_street" name="street" required>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="guest_city">Città:</label>
                                            <input type="text" id="guest_city" name="city" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_postalCode">CAP:</label>
                                            <input type="text" id="guest_postalCode" name="postalCode" required>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="guest_region">Provincia:</label>
                                            <input type="text" id="guest_region" name="region">
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_country">Paese:</label>
                                            <input type="text" id="guest_country" name="country" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="guest_phone">Telefono:</label>
                                        <input type="text" id="guest_phone" name="phone">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${not empty userShippingAddresses}">
                                            <div class="selection-group">
                                                <select name="addressId" class="checkout-select">
                                                    <c:forEach items="${userShippingAddresses}" var="addr">
                                                        <option value="${addr.id}" ${addr.isDefault() ? 'selected' : ''}>
                                                                ${addr.name} ${addr.surname}, ${addr.street}, ${addr.city} ${addr.postalCode}, ${addr.region}, ${addr.country}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <button type="button" class="add-new-btn" id="checkout-add-address-btn">Aggiungi</button>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="no-items-message">
                                                <span>Nessun indirizzo di spedizione salvato.</span>
                                                <button type="button" class="add-new-btn" id="checkout-add-address-btn">Aggiungi</button>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="checkout-section">
                            <div class="billing-address-toggle">
                                <input type="checkbox" id="billing-same-as-shipping" name="billingSameAsShipping" value="true" default checked>
                                <label for="billing-same-as-shipping">L'indirizzo di fatturazione è lo stesso di quello di spedizione.</label>
                            </div>
                            <div id="billing-address-section" class="hidden-section">
                                <h1 class="checkout-phrase">Indirizzo di fatturazione</h1>
                                <c:choose>
                                    <c:when test="${role eq 'Guest'}">
                                        <p>Inserisci i tuoi dati per la fatturazione:</p>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="guest_billing_name">Nome:</label>
                                                <input type="text" id="guest_billing_name" name="billing_name" disabled required>
                                            </div>
                                            <div class="form-group">
                                                <label for="guest_billing_surname">Cognome:</label>
                                                <input type="text" id="guest_billing_surname" name="billing_surname" disabled required>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_billing_street">Indirizzo:</label>
                                            <input type="text" id="guest_billing_street" name="billing_street" disabled required>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="guest_billing_city">Città:</label>
                                                <input type="text" id="guest_billing_city" name="billing_city" disabled required>
                                            </div>
                                            <div class="form-group">
                                                <label for="guest_billing_postalCode">CAP:</label>
                                                <input type="text" id="guest_billing_postalCode" name="billing_postalCode" disabled required>
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="guest_billing_region">Provincia:</label>
                                                <input type="text" id="guest_billing_region" name="billing_region" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="guest_billing_country">Paese:</label>
                                                <input type="text" id="guest_billing_country" name="billing_country" disabled required>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_billing_phone">Telefono:</label>
                                            <input type="text" id="guest_billing_phone" name="billing_phone" disabled>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${not empty userBillingAddresses}">
                                                <div class="selection-group">
                                                    <select name="billingAddressId" class="checkout-select" disabled>
                                                        <c:forEach items="${userBillingAddresses}" var="addr">
                                                            <option value="${addr.id}" ${addr.isDefault() ? 'selected' : ''}>
                                                                    ${addr.name} ${addr.surname}, ${addr.street}, ${addr.city} ${addr.postalCode}, ${addr.region}, ${addr.country}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="no-items-message">
                                                    <span>Nessun indirizzo di fatturazione salvato.</span>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="checkout-section">
                            <h1 class="checkout-phrase" id="checkout-phrase-cc">Metodo di pagamento</h1>
                            <c:choose>
                                <c:when test="${role eq 'Guest'}">
                                    <p>Inserisci i dati della tua carta:</p>
                                    <div class="form-group">
                                        <label for="guest_card_name">Nome del titolare della carta:</label>
                                        <input type="text" id="guest_card_name" name="cardName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="guest_card_number">Numero carta:</label>
                                        <div class="card-number-wrapper">
                                            <input type="text" id="guest_card_number" name="cardNumber" required placeholder="xxxx-xxxx-xxxx-xxxx" maxlength="19">
                                            <span id="card-logo-container" class="card-logo"></span>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="guest_card_expiration">Scadenza (MM/AAAA)</label>
                                            <input type="text" id="guest_card_expiration" name="cardExpiration" required placeholder="MM/AAAA" maxlength="7">
                                        </div>
                                        <div class="form-group">
                                            <label for="guest_card_cvc">CVC</label>
                                            <input type="text" id="guest_card_cvc" name="cardCvc" required placeholder="123" maxlength="3">
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${not empty userPaymentMethods}">
                                            <div class="selection-group">
                                                <select name="paymentMethodId" class="checkout-select">
                                                    <c:forEach items="${userPaymentMethods}" var="pm">
                                                        <option value="${pm.id}" ${pm.isDefault() ? 'selected' : ''}>
                                                                ${pm.cardType} ${pm.maskedNumber}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <button type="button" class="add-new-btn" id="checkout-add-payment-btn">Aggiungi</button>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="no-items-message">
                                                <span>Nessun metodo di pagamento salvato.</span>
                                                <button type="button" class="add-new-btn" id="checkout-add-payment-btn">Aggiungi</button>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form>
                </div>
                <div class="order-summary-column">
                    <div class="checkout-section summary-box">
                        <h1 class="checkout-phrase">Riepilogo Ordine</h1>
                        <div class="summary-items">
                            <c:forEach items="${checkoutItems}" var="item">
                                <div class="summary-item">
                                    <img class="summary-item-image"
                                         src="${pageContext.request.contextPath}/images/products/${item.product.id}.png"
                                         alt="${item.product.name}">
                                    <span class="summary-item-name">${item.product.name} (x${item.quantity})</span>
                                    <span class="summary-item-price">
                                        <fmt:formatNumber value="${item.product.price * item.quantity}" type="currency" pattern="¤#,##0.00"/>
                                    </span>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="summary-totals">
                            <c:set var="shippingCost" value="7.99" />
                            <c:set var="isFreeShipping" value="${role ne 'Guest' and checkoutTotal > 50}" />
                            <c:if test="${isFreeShipping}">
                                <c:set var="shippingCost" value="0.0" />
                            </c:if>
                            <div class="total-row">
                                <span>Subtotale:</span>
                                <span><fmt:formatNumber value="${checkoutTotal}" type="currency" pattern="¤#,##0.00"/></span>
                            </div>
                            <div class="total-row">
                                <span>Costi di spedizione:</span>
                                <c:choose>
                                    <c:when test="${isFreeShipping}">
                                        <span id="free-shipping">GRATIS</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span><fmt:formatNumber value="${shippingCost}" type="currency" pattern="¤#,##0.00"/></span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="total-row grand-total">
                                <span>Totale:</span>
                                <span><fmt:formatNumber value="${checkoutTotal + shippingCost}" type="currency" pattern="¤#,##0.00"/></span>
                            </div>
                        </div>
                        <button type="submit" class="pay-button" form="checkout-form">Paga e conferma ordine</button>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="fragments/footer.jsp" %>
        <script src="${pageContext.request.contextPath}/js/checkout.js"></script>
    </body>
</html>