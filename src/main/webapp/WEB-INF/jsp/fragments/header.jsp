<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
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
            <ul>
                <li>
                    CATEGORIE
                    <ul class="submenu">
                        <li>Display & Touchscreen</li>
                        <li>Fotocamere</li>
                        <li>Batterie</li>
                        <li>Microfoni</li>
                        <li>Speakers</li>
                        <li>Scocche</li>
                        <li>Tasti</li>
                        <li>Sensori</li>
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
                        <li>Google</li>
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
                        <li><a href="${pageContext.request.contextPath}/personal_area">Area Personale</a></li>
                        <li>Esci</li>
                    </ul>
                    <ul id="foradmin" hidden>
                        <li>Centro di controllo</li>
                        <li>Esci</li>
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
        document.querySelectorAll('.menu > ul > li').forEach(li => {
            const submenu = li.querySelector('.submenu');
            if (!submenu) return;
            li.addEventListener('mouseenter', () => {
                submenu.classList.remove('flip');
                const { right } = submenu.getBoundingClientRect();
                if (right > window.innerWidth) {
                    submenu.classList.add('flip');
                }
            });
        });
    </script>
</header>