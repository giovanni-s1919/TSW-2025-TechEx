<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<header>
    <div class="top-left">
        <a href="${pageContext.request.contextPath}">
            <img id="logo" src="${pageContext.request.contextPath}/images/logo.png" alt="TECHEX">
        </a>
        <nav class="menu">
            <ul>
                <li>
                    CATEGORIE
                    <ul class="submenu">
                        <li>Display & Touchscreen</li>
                        <li>Fotocamere</li>
                        <li>Batterie</li>
                        <li>Microfoni & Speaker</li>
                        <li>Scocche</li>
                    </ul>
                </li>
                <li>
                    BRAND
                    <ul class="submenu">
                        <li>Apple</li>
                        <li>Samsung</li>
                        <li>Xiaomi</li>
                        <li>Huawei</li>
                        <li>Realme</li>
                        <li>Oppo</li>
                        <li>Oneplus</li>
                    </ul>
                </li>
                <li>
                    GRADO
                    <ul class="submenu">
                        <li>Premium</li>
                        <li>Eccellente</li>
                        <li>Ottimo</li>
                        <li>Buono</li>
                    </ul>
                </li>
                <li id="promo">PROMO</li>
            </ul>
        </nav>
    </div>
    <div class="top-right">
        <div class="search">
            <input type="text" id="searchbar" placeholder="Cerca...">
            <i class="fas fa-search lens"></i>
        </div>
        <a href="preferiti.jsp">
            <img class="header_icons" src="${pageContext.request.contextPath}/images/preferiti.svg" alt="PREFERITI">
        </a>
        <a href="${pageContext.request.contextPath}/jsp/carrello.jsp"><!--${pageContext.request.contextPath}-->
            <img class="header_icons" src="${pageContext.request.contextPath}/images/carrello.svg" alt="CARRELLO">
        </a>
        <a href="${pageContext.request.contextPath}/login">
            <img class="header_icons" src="${pageContext.request.contextPath}/images/utente.svg" alt="AREA PERSONALE">
        </a>
    </div>
</header>