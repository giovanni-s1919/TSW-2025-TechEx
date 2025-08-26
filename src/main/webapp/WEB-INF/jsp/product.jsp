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
        <div id="main">
            <div id="product-box">
                <div id="product-element">
                    <img id="product-image" src="${pageContext.request.contextPath}/images/prodotto.jpg">
                    <div id="product-details">
                        <div id="product-name">Yodoit Kit di sostituzione dello schermo per iPhone 11 Pro con strumento di riparazione</div>
                        <div id="product-fieldvalue">
                            <div id="product-field">Brand: </div>
                            <div id="product-value">Yodoit</div>
                        </div>
                </div>
            </div>
        </div>

        <%@include file="fragments/footer.jsp"%>
    </body>
</html>
