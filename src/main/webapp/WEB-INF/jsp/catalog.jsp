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
                                <label for="cat_display">Display & Touchscreen</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_camera" name="category" value="Camera">
                                <label for="cat_camera">Fotocamera</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_battery" name="category" value="Battery">
                                <label for="cat_battery">Batteria</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_microphone" name="category" value="Microphone">
                                <label for="cat_microphone">Microfono</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_speaker" name="category" value="Speaker">
                                <label for="cat_speaker">Speaker</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_case" name="category" value="Case">
                                <label for="cat_case">Scocca</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_button" name="category" value="Button">
                                <label for="cat_button">Tasto</label>
                            </li>
                            <li>
                                <input type="checkbox" id="cat_sensor" name="category" value="Sensor">
                                <label for="cat_sensor">Sensore</label>
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
                            <li>
                                <input type="checkbox" id="grade_great" name="grade" value="Great">
                                <label for="grade_great">Ottimo</label>
                            </li>
                            <li>
                                <input type="checkbox" id="grade_goodt" name="grade" value="Good">
                                <label for="grade_good">Buono</label>
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
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Fotocamera anteriore Samsung Galaxy S25+" class="product-img">
                            <h1 class="product-name">Fotocamera anteriore Samsung Galaxy S25+</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">39,99€</p>
                        </div>
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Display Apple Iphone 16 Pro Max" class="product-img">
                            <h1 class="product-name">Display Apple Iphone 16 Pro Max</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">749,99€</p>
                        </div>
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Batteria Apple Iphone 16 Pro" class="product-img">
                            <h1 class="product-name">Batteria Apple Iphone 16 Pro</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">135,99€</p>
                        </div>
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Display Samsung Galaxy S25 Ultra" class="product-img">
                            <h1 class="product-name">Display Samsung Galaxy S25 Ultra</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">269,99€</p>
                        </div>
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Fotocamera posteriore Apple Iphone 16" class="product-img">
                            <h1 class="product-name">Fotocamera posteriore Apple Iphone 16</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">79,99€</p>
                        </div>
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Display Samsung Galaxy S25 Ultra" class="product-img">
                            <h1 class="product-name">Display Samsung Galaxy S25 Ultra</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">269,99€</p>
                        </div>
                        <div class="product-card">
                            <img src="${pageContext.request.contextPath}/images/products/1.png" alt="Fotocamera posteriore Apple Iphone 16" class="product-img">
                            <h1 class="product-name">Fotocamera posteriore Apple Iphone 16</h1>
                            <p class="product-grade">Originale</p>
                            <p class="product-price">79,99€</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="fragments/footer.jsp" %>
    </body>
</html>