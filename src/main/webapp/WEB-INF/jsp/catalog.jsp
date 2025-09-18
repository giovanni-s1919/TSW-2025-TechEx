<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Catalogo</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalog.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/headlogo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>const contextPath = "${pageContext.request.contextPath}";</script>
    </head>
    <body>
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <h1 id="catalog_intro">Il catalogo di TechEx: qualità ed affidabilità ad un prezzo sostenibile</h1>
            <div class="catalog-container">
                <div class="filters-sidebar">
                    <form id="filters-form">
                        <h1 class="sidebar-title">Filtri</h1>
                        <div class="filter-block">
                            <h1 class="filter-title">Categoria</h1>
                            <ul class="filter-options">
                                <c:forEach items="${categories}" var="category">
                                    <li>
                                        <input type="checkbox" id="cat_${category.name()}" name="category" value="${category.name()}">
                                        <label for="cat_${category.name()}">${category.toString()}</label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="filter-block">
                            <h1 class="filter-title">Brand</h1>
                            <ul class="filter-options">
                                <li>
                                    <input type="checkbox" id="brand_apple" name="brand" value="Apple">
                                    <label for="brand_apple">Apple</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_samsung" name="brand" value="Samsung">
                                    <label for="brand_samsung">Samsung</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_xiaomi" name="brand" value="Xiaomi">
                                    <label for="brand_xiaomi">Xiaomi</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_huawei" name="brand" value="Huawei">
                                    <label for="brand_huawei">Huawei</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_realme" name="brand" value="Realme">
                                    <label for="brand_realme">Realme</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_google" name="brand" value="Google">
                                    <label for="brand_google">Google</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_oppo" name="brand" value="Oppo">
                                    <label for="brand_oppo">Oppo</label>
                                </li>
                                <li>
                                    <input type="checkbox" id="brand_oneplus" name="brand" value="Oneplus">
                                    <label for="brand_oneplus">Oneplus</label>
                                </li>
                            </ul>
                        </div>
                        <div class="filter-block">
                            <h1 class="filter-title">Grado</h1>
                            <ul class="filter-options">
                                <c:forEach items="${grades}" var="grade">
                                    <li>
                                        <input type="checkbox" id="grade_${grade.name()}" name="grade" value="${grade.name()}">
                                        <label for="grade_${grade.name()}">${grade.toString()}</label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="filter-block">
                            <h1 class="filter-title">Prezzo</h1>
                            <ul class="filter-options">
                                <li>
                                    <input type="radio" id="price_any" name="price" value="any" checked>
                                    <label for="price_any">Qualsiasi prezzo</label>
                                </li>
                                <li>
                                    <input type="radio" id="price_0_50" name="price" value="0-50">
                                    <label for="price_0_50">0€ - 50€</label>
                                </li>
                                <li>
                                    <input type="radio" id="price_51_100" name="price" value="51-100">
                                    <label for="price_51_100">51€ - 100€</label>
                                </li>
                                <li>
                                    <input type="radio" id="price_101_300" name="price" value="101-300">
                                    <label for="price_101_300">101€ - 300€</label>
                                </li>
                                <li>
                                    <input type="radio" id="price_301_500" name="price" value="301-500">
                                    <label for="price_301_500">301€ - 500€</label>
                                </li>
                                <li>
                                    <input type="radio" id="price_501_plus" name="price" value="501+">
                                    <label for="price_501_plus">Oltre 500€</label>
                                </li>
                            </ul>
                        </div>
                    </form>
                    <div id="apply-filters-container">
                        <button id="apply-filters-btn" form="filters-form" type="button" hidden>Applica Filtri</button>
                    </div>
                </div>
                <div class="products-area">
                    <div class="catalog-header">
                        <select name="sort" id="sort-options">
                            <option value="default">Ordinamento predefinito</option>
                            <option value="price-asc">Prezzo: crescente</option>
                            <option value="price-desc">Prezzo: decrescente</option>
                        </select>
                    </div>
                    <div class="products-grid">
                        <c:if test="${not empty products}">
                            <c:forEach items="${products}" var="product">
                                <a href="${pageContext.request.contextPath}/product?idProduct=${product.id}" class="product-card-link">
                                    <div class="product-card">
                                        <img src="${pageContext.request.contextPath}/images/products/${product.id}.png" alt="${product.name}" class="product-img">
                                        <h1 class="product-name">${product.name}</h1>
                                        <p class="product-grade">${product.grade.toString()}</p>
                                        <p class="product-price"><fmt:formatNumber value="${product.price}" type="currency" pattern="€0.00"/></p>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty products}">
                            <p id="no-products-found">Nessun prodotto trovato.</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="fragments/footer.jsp" %>
        <script src="${pageContext.request.contextPath}/js/catalog.js"></script>
    </body>
</html>