<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>TechEx - Catalogo</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalog.css">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <%@ include file="fragments/header.jsp" %>
        <div id="main">
            <h1 id="catalog_intro">Il catalogo di TechEx: qualità ed affidabilità ad un prezzo sostenibile</h1>
            <div class="catalog-container">
                <div class="filters-sidebar">
                    <h2 class="sidebar-title">Filtri</h2>
                    <div class="filter-block">
                        <h3 class="filter-title">Categoria</h3>
                        <ul class="filter-options">
                            <li>
                                <input type="checkbox" id="cat_display" name="category" value="Display">
                                <label for="cat_display">Display</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_battery" name="category" value="Battery">
                                <label for="cat_battery">Batterie</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_camera" name="category" value="Camera">
                                <label for="cat_camera">Fotocamere</label>
                            </li>
                        </ul>
                    </div>
                    <div class="filter-block">
                        <h3 class="filter-title">Brand</h3>
                        <ul class="filter-options">
                            <li>
                                <input type="checkbox" id="brand_apple" name="brand" value="Apple">
                                <label for="brand_apple">Apple</label>
                            </li>
                            <li>
                                <input type="checkbox" id="brand_samsung" name="brand" value="Samsung">
                                <label for="brand_samsung">Samsung</label>
                            </li>
                        </ul>
                    </div>
                    <div class="filter-block">
                        <h3 class="filter-title">Grado</h3>
                        <ul class="filter-options">
                            <li>
                                <input type="checkbox" id="grade_original" name="grade" value="Original">
                                <label for="grade_original">Originale</label>
                            </li>
                            <li>
                                <input type="checkbox" id="grade_excellent" name="grade" value="Excellent">
                                <label for="grade_excellent">Eccellente</label>
                            </li>
                        </ul>
                    </div>
                    <div class="filter-block">
                        <h3 class="filter-title">Prezzo</h3>
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
                                <input type="radio" id="price_101_200" name="price" value="101-200">
                                <label for="price_101_200">101€ - 200€</label>
                            </li>
                            <li>
                                <input type="radio" id="price_201_plus" name="price" value="201+">
                                <label for="price_201_plus">Oltre 200€</label>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="products-area">
                    <div class="catalog-header">
                        <p class="product-count">Mostrando 12 prodotti</p>
                        <select name="sort" id="sort-options">
                            <option value="default">Ordinamento predefinito</option>
                            <option value="price-asc">Prezzo: crescente</option>
                            <option value="price-desc">Prezzo: decrescente</option>
                        </select>
                    </div>

                    <div class="product-grid">
                        <p>Caricamento prodotti...</p>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="fragments/footer.jsp" %>
    </body>
</html>
