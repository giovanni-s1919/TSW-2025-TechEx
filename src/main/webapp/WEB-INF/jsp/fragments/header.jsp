<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<header>
    <div class="top-left">
        <a href="${pageContext.request.contextPath}/home">
            <img id="logo" src="${pageContext.request.contextPath}/images/logo.png" alt="TECHEX">
        </a>
        <nav class="menu">
            <div class="hamburger">
                <div class="line"></div>
                <div class="line"></div>
                <div class="line"></div>
            </div>
            <ul>
                <li>
                    CATEGORIE
                    <ul class="submenu">
                        <c:forEach items="${categoriesForHeader}" var="category">
                            <li><a class="submenu-link filter-link" href="#" data-filter-type="category" data-filter-value="${category.name()}">${category.toString()}</a></li>
                        </c:forEach>
                    </ul>
                </li>
                <li>
                    BRAND
                    <ul class="submenu">
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Apple">Apple</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Samsung">Samsung</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Xiaomi">Xiaomi</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Huawei">Huawei</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Realme">Realme</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Google">Google</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Oppo">Oppo</a></li>
                        <li><a class="submenu-link filter-link" href="#" data-filter-type="brand" data-filter-value="Oneplus">Oneplus</a></li>
                    </ul>
                </li>
                <li>
                    GRADO
                    <ul class="submenu">
                        <c:forEach items="${gradesForHeader}" var="grade">
                            <li><a class="submenu-link filter-link" href="#" data-filter-type="grade" data-filter-value="${grade.name()}">${grade.toString()}</a></li>
                        </c:forEach>
                    </ul>
                </li>
                <li id="promo">PROMO</li>
            </ul>
        </nav>
    </div>
    <div class="top-right">
        <div class="search">
            <%-- Pulsante visibile solo su Mobile per APRIRE la ricerca --%>
            <button class="search-toggle-btn">
                <img src="${pageContext.request.contextPath}/images/bluelens.svg" alt="Apri Ricerca">
            </button>

            <%-- Wrapper per la barra e il pulsante di chiusura --%>
            <div class="search-input-wrapper">
                <input type="text" id="searchbar" placeholder="Cerca..." style="background-image: url('${pageContext.request.contextPath}/images/bluelens.svg')">
                <%-- Pulsante visibile solo in modalità ricerca attiva per CHIUDERE --%>
                <%--the game--%>
                <button class="search-close-btn">
                    <img src="${pageContext.request.contextPath}/images/close.svg" alt="Chiudi Ricerca">
                </button>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/wishlist">
            <img class="header_icons" src="${pageContext.request.contextPath}/images/preferiti.svg" alt="PREFERITI">
        </a>
        <a href="${pageContext.request.contextPath}/cart">
            <img class="header_icons" src="${pageContext.request.contextPath}/images/carrello.svg" alt="CARRELLO">
        </a>
        <nav class="menu topright">
            <ul>
                <li>
                    <img class="header_icons" src="${pageContext.request.contextPath}/images/utente.svg" alt="AREA PERSONALE">
                    <ul id="forguest" hidden>
                        <a href="${pageContext.request.contextPath}/login?action=login" style="text-decoration: none; color: #003459;"><li>Accedi</li></a>
                        <a href="${pageContext.request.contextPath}/login?action=register" style="text-decoration: none; color: #003459;"><li>Registrati</li></a>
                    </ul>
                    <ul id="forcustomer" hidden>
                        <a href="${pageContext.request.contextPath}/personal_area" style="text-decoration: none; color: #003459;"><li>Area Personale</li></a>
                        <a href="${pageContext.request.contextPath}/logout" class="logout-link" style="text-decoration: none; color: #003459;"><li>Esci</li></a>
                    </ul>
                    <ul id="foradmin" hidden>
                        <a href="${pageContext.request.contextPath}/admin/panel" style="text-decoration: none; color: #003459;"><li>Centro di controllo</li>
                            <a href="${pageContext.request.contextPath}/logout" style="text-decoration: none; color: #003459;"><li>Esci</li></a>
                        </a>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
    <script>
        let userole = "<%= request.getAttribute("role")%>";
        if(userole === "Customer"){
            $("#forcustomer").addClass("submenu");
        }else if(userole === "Guest"){
            $("#forguest").addClass("submenu");
        }else if(userole === "Admin"){
            $("#foradmin").addClass("submenu");
        }
        /*in base alla distanza di un elemento di classe "submenu" dal bordo destro, viene aggiunta o meno la classe "flip"
        * che in css è fornita di proprietà che capovolge l'aggancio laterale dell'elemento in questione*/
        <%--document.querySelectorAll('.menu > ul > li').forEach(li => {
            const submenu = li.querySelector('.submenu');
            if (!submenu) return;
            li.addEventListener('mouseenter', () => {
                submenu.classList.remove('flip');
                const { right } = submenu.getBoundingClientRect();
                if (right > window.innerWidth) {
                    submenu.classList.add('flip');
                }
            });
        });--%>
    </script>
    <script>
        document.querySelectorAll('.logout-link').forEach(link => {
            link.addEventListener('click', function(event) {
                event.preventDefault();
                const userConfirmed = confirm("Sei sicuro di voler uscire?");
                if (userConfirmed) {
                    window.location.href = this.href;
                }
            });
        });
    </script>
    <script>
        document.querySelectorAll('.filter-link').forEach(link => {
            link.addEventListener('click', function(event) {
                event.preventDefault();
                const filterType = this.dataset.filterType;
                const filterValue = this.dataset.filterValue;
                const filter = {
                    type: filterType,
                    value: filterValue
                };
                sessionStorage.setItem('catalogFilter', JSON.stringify(filter));
                window.location.href = "${pageContext.request.contextPath}/catalog";
            });
        });
    </script>
    <script>
        document.querySelector('.hamburger').addEventListener('click', function() {
            document.querySelector('.menu').classList.toggle('active');
        });
    </script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const header = document.querySelector('header');
            const searchToggleBtn = document.querySelector('.search-toggle-btn');
            const searchCloseBtn = document.querySelector('.search-close-btn');
            const searchInput = document.getElementById('searchbar');

            if (searchToggleBtn) {
                searchToggleBtn.addEventListener('click', function() {
                    header.classList.add('search-active');
                    // Appena la barra appare, mettiamo il focus per poter scrivere subito
                    searchInput.focus();
                });
            }

            if (searchCloseBtn) {
                searchCloseBtn.addEventListener('click', function() {
                    header.classList.remove('search-active');
                });
            }
        });
    </script>
</header>