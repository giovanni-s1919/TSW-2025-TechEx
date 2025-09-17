<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>TechEx - Pezzi di ricambio per smartphone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/headlogo.png">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <style>
        @media (max-width: 1024px) {
            .products-grid {
                flex-wrap: wrap;
                justify-content: space-evenly;
                gap: 1.5rem;
            }

            .presentation .presentation_img {
                right: 1rem;
                top: -2rem;
            }

            .reviews_right {
                justify-content: center;
                flex-wrap: wrap;
            }

            .newsletter_right {
                display: none;
            }

            .newsletter_left {
                padding-left: 2rem;
                width: 100%;
                padding-bottom: 3.4rem;
                justify-content: left;
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            /*#reviews_man {*/
            /*    left: 50%; !* Ricalibra la posizione dell'uomo *!*/
            /*    top: auto;*/
            /*    bottom: -2rem; !* Posizionalo in basso *!*/
            /*}*/

            .faq_decorations {
                right: 1rem;
            }

            #faq\?_image, #faq\!_image {
                display: none;
            }
            .faq_decorations {
                display: none;
            }
            .faq_content {
                text-align: center;
            }
        }


        @media (max-width: 768px) {
            .main {
                padding-top: 1rem;
            }

            .welcome-text h1 {
                font-size: 1.5rem;
            }


            .products-grid {
                align-items: center;
                gap: 2rem;
            }


            .presentation .presentation_img {
                display: none;
            }

            .presentation {
                padding: 2rem;
                text-align: center;
            }

            .presentation_content p {
                text-align: center;
            }

            .guides {
                padding: 2rem;
                justify-content: center;
                text-align: center;
            }

            .reviews {
                flex-direction: column;
                align-items: center;
                padding: 1rem;
            }

            .reviews_left, .reviews_right {
                flex-basis: 100%;
                width: 100%;
                margin-left: 0;
                padding: 1rem;
                text-align: center;
            }

            .reviews_value {
                font-size: 2rem;
            }

            /*#reviews_man {*/
            /*    display: none; !* Nasconde l'immagine dell'uomo che interferirebbe *!*/
            /*}*/

            .review_card {
                width: 100%;
            }


            .collabs {
                padding-bottom: 5rem;
            }

            .brand_column, .certifications_column {
                flex-basis: 100%;
                width: 100%;
                margin-bottom: 1rem;
            }

            .newsletter {
                flex-direction: column;
            }

            .newsletter_left {
                width: 100%;
                padding: 2rem;
            }

            #newsletter_email, .newsletter_submit {
                width: 100%;
            }

            .faq_decorations {
                display: none;
            }

            .faq {
                padding: 1rem;
            }

        }
    </style>
</head>
<body>
<%@ include file="fragments/header.jsp" %>
<div class="main">
    <div id="welcome-img">
        <div class="welcome-text">
            <h1>Dona nuova vita al tuo smartphone con TechEx!</h1>
            <p>Pezzi di ricambio su misura. Qualità garantita.</p>
        </div>
    </div>
    <div class="catalog">
        <h1 class="products">Scopri la nostra selezione di pezzi di ricambio per smartphone:</h1>
        <c:choose>
            <c:when test="${not empty products}">
                <div class="grid-wrapper">
                    <div class="products-grid">
                        <c:forEach var="item" items="${products}">
                            <div class="product-card" id="${item.product.id}">
                                <img src="${pageContext.request.contextPath}/images/products/${item.product.id}.png" alt="${item.product.name}" class="product-img">
                                <h1 class="product-name">${item.product.name}</h1>
                                <p class="product-grade">${item.product.grade.toString()}</p>
                                <p class="product-price">€${item.product.price}</p>
                            </div>
                        </c:forEach>
                        <script>
                            $(".product-card").on("click", function(e){
                                e.preventDefault();
                                window.location.href = "${pageContext.request.contextPath}/product?idProduct="+$(this).attr("id");
                            })
                        </script>
                    </div>
                </div>
            </c:when>
        </c:choose>
        <p class="products" id="catalog_phrase">Trovi questi e tanti altri prodotti all'interno del nostro catalogo!</p>
        <a href="${pageContext.request.contextPath}/catalog" alt="CATALOG"><button class="catalog_button" id="catalog_button_presentation">Catalogo</button></a>
    </div>
    <div class="presentation">
        <p class="presentation_menu">Il futuro della riparazione è qui!</p>
        <div class="presentation_content_wrapper">
            <ul class="presentation_ul">
                <li><span class="icon"><i class="fa-solid fa-circle-check"></i></span>
                    <div class="presentation_content">
                        <p class="presentation_menu">Qualità ed affidabilità</p>
                        <p>Solo ricambi di ottima fattura per prestazioni garantite!</p>
                    </div>
                </li>
                <li><span class="icon"><i class="fa-solid fa-money-bills"></i></span>
                    <div class="presentation_content">
                        <p class="presentation_menu">Prezzi super e convenienza</p>
                        <p>Ricambi top a prezzi stracciati!</p>
                    </div>
                </li>
                <li><span class="icon"><i class="fa-solid fa-truck-fast"></i></span>
                    <div class="presentation_content">
                        <p class="presentation_menu">Spedizioni rapide</p>
                        <p>Ordina oggi, ripara domani.<br>I tuoi smartphone pronti all'uso in un battito di ciglia grazie alla spedizione super-veloce di TechEx!</p>
                    </div>
                </li>
                <li><span class="icon"><i class="fa-solid fa-award"></i></span>
                    <div class="presentation_content">
                        <p class="presentation_menu">Garanzia</p>
                        <p>Fino a 30 giorni di tempo per il reso e 2 anni di garanzia per ogni prodotto acquistato. Notevole vero?</p>
                    </div>
                </li>
                <li><span class="icon"><i class="fa-solid fa-headset"></i></span>
                    <div class="presentation_content">
                        <p class="presentation_menu">Assistenza</p>
                        <p>Con TechEx hai un team di esperti sempre al tuo fianco, pronti ad aiutarti per qualunque evenienza.</p>
                    </div>
                </li>
            </ul>
            <img class="presentation_img" src="${pageContext.request.contextPath}/images/homeimage.png" alt="HOMEIMAGE">
        </div>
    </div>
    <div class="élite_selection">
        <h1 id="élite_selection-intro">ÉLITE SELECTION</h1>
        <p class="élite_selection-top">I migliori articoli presenti nel nostro catalogo. Componenti 100% originali per i clienti più esigenti.</p><br>
        <div class="grid-wrapper">
            <div class="products-grid">
                <c:forEach var="item" items="${eliteProducts}">
                    <div class="product-card" id="elite_${item.product.id}">
                        <img src="${pageContext.request.contextPath}/images/products/${item.product.id}.png" alt="${item.product.name}" class="product-img">
                        <h1 class="product-name">${item.product.name}</h1>
                        <p class="product-grade">${item.product.grade.toString()}</p>
                        <p class="product-price">€${item.product.price}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
        <p class="élite_selection-top" id="élite_selection-phrase">Non finisce qui! Consulta il nostro catalogo per accedere ad altri prodotti premium!</p>
        <a href="${pageContext.request.contextPath}/catalog" alt="CATALOG"><button class="catalog_button">Catalogo</button></a>
    </div>
    <div class="guides">
        <div class="guides_box">
            <h1 id="help">Mai arrendersi di fronte a un "è guasto".</h1>
            <p>TechEx offre guide pratiche passo-passo, istruzioni specifiche e una community di esperti sempre pronta ad aiutare<br>
                tutti coloro che si approcciano al mondo della riparazione, da principianti alle prime armi fino ad arrivare agli utenti più esperti.
            </p>
            <ul class="guides_ul">
                <li><span class="icon"><i class="fa-solid fa-video"></i></span><a href="https://www.youtube.com/watch?v=bf7rAzeuqt4">Clip montaggio-smontaggio per ogni tipo di componente</a></li>
                <li><span class="icon"><i class="fa-solid fa-file"></i></span><a href="https://www.igattipiubellidelmondo.it/upload/Guida-alle-razze.pdf">PDF guida passo-passo per il restauro del tuo smartphone</a></li>
                <li><span class="icon"><i class="fa-solid fa-lightbulb"></i></span><a href="https://mondogatto.org/nostri-gatti/gatti-da-adottare">Tool consigliati per riparazioni fai-da-te</a></li>
                <li><span class="icon"><i class="fa-solid fa-users"></i></span><a href="https://gatti.forumfree.it/">Entra a far parte della community di TechEx!</a></li>
            </ul>
        </div>
    </div>
    <div class="best_sellers">
        <h1 id="best_sellers-intro">BEST SELLERS</h1>
        <p class="best_sellers-favourites">I prodotti più apprezzati ed acquistati dagli utenti. Vuoi andare sul sicuro? Sei nel posto giusto!</p>
        <div class="grid-wrapper">
            <div class="products-grid">
                <c:forEach var="item" items="${bestSellerProducts}">
                    <div class="product-card" id="bestseller_${item.product.id}">
                        <img src="${pageContext.request.contextPath}/images/products/${item.product.id}.png" alt="${item.product.name}" class="product-img">
                        <h1 class="product-name">${item.product.name}</h1>
                        <p class="product-grade">${item.product.grade.toString()}</p>
                        <p class="product-price"><fmt:formatNumber value="${item.product.price}" type="currency" pattern="¤#,##0.00"/></p>
                    </div>
                </c:forEach>
            </div>
        </div>
        <p class="best_sellers-favourites" id="best_sellers-phrase">Hai bisogno di altro? Esplora il nostro catalogo!</p>
        <a href="${pageContext.request.contextPath}/catalog" alt="CATALOG"><button class="catalog_button">Catalogo</button></a>
    </div>
    <div class="reviews">
        <div class="reviews_left">
            <div class="reviews_p">
                <b>Recensioni dei nostri utenti</b><br>
                TechEx è in testa alle classifiche<br>dei siti di pezzi di ricambio per smartphone in Italia.<br>
                Solo ricambi di qualità assoluta per riparazioni top!
            </div>
            <div class="reviews_value">
                TechEx è valutata <b>Eccellente</b><br>
                <div class="reviews_note">Basata su 4324 recensioni<br>
                            <img class="reviews_img" src="${pageContext.request.contextPath}/images/reviews.png" alt="REVIEWS"><br>
                            <a href="https://www.youtube.com/watch?v=iYZkBxByo14" alt="ALL_REVIEWS"><button class="reviews_button">Visualizza tutte le recensioni</button></a>
                        </div>
            </div>
            <%--<img id="reviews_man" src="${pageContext.request.contextPath}/images/reviews_man.png" alt="REVIEWS MAN">--%>
        </div>
        <div class="reviews_right" id="reviewsContainer"></div>
    </div>
    <div class="collabs">
        <h1 id="collabs_top-text" class="collabs_intro">TechEx: un nome, una garanzia.</h1>
        <p class="collabs_intro">Collaboriamo con i migliori brand e operiamo secondo standard internazionali di qualità.</p>
        <div class="brand_column">
            <h1>BRAND</h1>
            <div class="brand_logos">
                <img src="${pageContext.request.contextPath}/images/apple.png" alt="APPLE">
                <img src="${pageContext.request.contextPath}/images/samsung.png" alt="SAMSUNG">
                <img src="${pageContext.request.contextPath}/images/xiaomi.png" alt="XIAOMI">
                <img src="${pageContext.request.contextPath}/images/huawei.png" alt="HUAWEI"><br>
                <img src="${pageContext.request.contextPath}/images/realme.png" alt="REALME">
                <img src="${pageContext.request.contextPath}/images/googlepixel.png" alt="GOOGLE">
                <img src="${pageContext.request.contextPath}/images/oppo.png" alt="OPPO">
                <img src="${pageContext.request.contextPath}/images/oneplus.png" alt="ONEPLUS">
            </div>
        </div>
        <div class="certifications_column">
            <h1>CERTIFICAZIONI</h1>
            <div class="certifications_badges">
                <img src="${pageContext.request.contextPath}/images/ce.png" alt="CE">
                <img src="${pageContext.request.contextPath}/images/iso9001.png" alt="ISO 9001">
                <img src="${pageContext.request.contextPath}/images/rohs.png" alt="RoHS">
                <img src="${pageContext.request.contextPath}/images/ssl.png" alt="SSL ENCRYPTION"><br>
                <img src="${pageContext.request.contextPath}/images/reach.png" alt="REACH">
                <img src="${pageContext.request.contextPath}/images/gdpr.png" alt="GDPR CERTIFICATION">
                <img src="${pageContext.request.contextPath}/images/pci-dss.png" alt="PSI DSS">
                <img src="${pageContext.request.contextPath}/images/iso27001.png" alt="ISO 27001">
            </div>
        </div>
        <div class="discover_button">
            <a href="https://www.youtube.com/watch?v=rJ5e_Bh91SM" class="discover">Scopri di più</a>
        </div>
    </div>
    <div class="newsletter">
        <div class="newsletter_left">
            <h1 class="newsletter_h1">Iscriviti alla newsletter di TechEx!</h1>
            <p class="newsletter_p">Ottieni un buono di 5€, accesso a promo esclusive, news e tanto altro iscrivendoti alla newsletter.</p>
            <form>
                <input type="email" id="newsletter_email" name="email" placeholder=" * E-mail">
                <p class="newsletter_p">I miei interessi: (facoltativo)</p>
                <input type="checkbox" name="categories" class="newsletter_categories">Pezzi di ricambio<br>
                <input type="checkbox" name="categories" class="newsletter_categories">Tool consigliati<br>
                <input type="checkbox" name="categories" class="newsletter_categories">Guide passo-passo<br>
                <input type="checkbox" name="categories" class="newsletter_categories">Attività della community<br>
                <label class="newsletter_consent">
                    <input type="checkbox" name="categories" class="newsletter_categories">
                    <p class="newsletter_consent_text">
                        In futuro, vorrei essere informato da TechEx sulle nuove aggiunte a catalogo,<br>
                        promo esclusive sui pezzi di ricambio, bundle,<br>
                        ultime news in arrivo e sulle collaborazioni esclusive con i partner.<br>
                        Il consenso può essere revocato in qualsiasi momento cliccando sul link di cancellazione<br>
                        presente in ogni email o messaggio ai recapiti indicati nelle note legali. *
                    </p>
                </label>
                <div id="newsletter_success">Ora sei iscritto alla newsletter!</div>
                <button type="submit" class="newsletter_submit">ISCRIVITI ALLA NEWSLETTER</button>
            </form>
        </div>
        <div class="newsletter_right">
        </div>
        <script>
            $(document).ready(function(){
                $("form")[0].reset();
                $(".newsletter_submit").on("click", function (e){
                    e.preventDefault();
                    $("#newsletter_success").fadeIn();
                    $("#newsletter_email").val("");
                    $(".newsletter_categories").prop("checked", false);
                });
            });
        </script>
    </div>
    <div class="faq">
        <h1 class="faq_title">FAQ - Domande frequenti</h1>
        <div class="faq_content">
            <div class="faq_item">
                <button class="faq_question">Quali tipi di pezzi di ricambio posso acquistare?</button>
                <div class="faq_answer">
                    All'interno del nostro sito troverai ogni tipo di pezzi di ricambio per il tuo cellulare:<br>
                    display, touchscreen, fotocamere, batterie, microfoni, speaker, scocche e anche diversi tipi di sensori.
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">A quali brand sono destinati i pezzi di ricambio in vendita?</button>
                <div class="faq_answer">
                    TechEx è rivenditore autorizzato di pezzi di ricambio per:<br>
                    <ul class="faq_ul">
                        <li>brand top di gamma come Apple e Samsung;</li>
                        <li>brand di fascia alta come Google Pixel;</li>
                        <li>brand destinati al grande pubblico come Xiaomi, Huawei, Realme, Oppo e Oneplus.</li>
                    </ul>
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">I pezzi di ricambio sono originali?</button>
                <div class="faq_answer">
                    Offriamo sia componenti originali al 100% sia componenti compatibili,<br>
                    divisi in tre categorie ("eccellente, "ottimo", "buono") a seconda del grado di qualità, tutti di ottima fattura.<br>
                    Puoi trovare le informazioni di compatibilità e tutti gli altri dettagli nella descrizione del prodotto.
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">Quanto tempo impiega la spedizione?</button>
                <div class="faq_answer">
                    La spedizione standard impiega 2–3 giorni lavorativi. È disponibile anche la consegna express in 24h.<br>
                    È possibile scegliere tra quattro fornitori differenti: DHL, UPS, SDA e BRT.
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">Quali sono i metodi di pagamento accettati?</button>
                <div class="faq_answer">
                    TechEx permette l'utilizzo di otto metodi di pagamento differenti:<br>
                    VISA, MasterCard, AmericanExpress, Postepay, PayPal, Klarna, Google Pay e Apple Pay.
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">Quanto dura il periodo di tempo utile per il reso?</button>
                <div class="faq_answer">
                    Ogni utente ha a disposizione un periodo di 30 giorni per poter usufruire del diritto di reso,<br>
                    valido a partire dal giorno di consegna del prodotto, per ogni prodotto acquistato.
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">Si può usufruire del diritto di rimborso?</button>
                <div class="faq_answer">
                    È possibile usufruire del diritto di rimborso durante il periodo utile per il reso.<br>
                    Ogni rimborso verrà emesso entro 14 giorni dalla data di consegna del prodotto reso a TechEx.
                </div>
            </div>
            <div class="faq_item">
                <button class="faq_question">Quanto dura la garanzia?</button>
                <div class="faq_answer">
                    I pezzi di ricambio venduti da TechEx godono di 2 anni di garanzia,<br>
                    validi a partire dalla data di consegna del prodotto.
                </div>
            </div>
            <p id="faq_phrase">Hai ancora bisogno di aiuto? Visita la sezione "Aiuto e contatti" dell'area personale!</p>
        </div>
        <div class="faq_decorations">
            <img id="faq_image" src="${pageContext.request.contextPath}/images/faqimage.png">
            <img id="faq?_image" src="${pageContext.request.contextPath}/images/faq1.png">
            <img id="faq!_image" src="${pageContext.request.contextPath}/images/faq2.png">
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/reviews.js" defer></script>
<script src="${pageContext.request.contextPath}/js/faq.js" defer></script>
<script>
    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('click', function(e) {
            e.preventDefault();
            const productId = this.id.replace('elite_', '');
            window.location.href = "${pageContext.request.contextPath}/product?idProduct=" + productId;
        });
    });
</script>
<script>
    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('click', function(e) {
            e.preventDefault();
            const productId = this.id.replace('bestseller_', '').replace('elite_', '');
            window.location.href = "${pageContext.request.contextPath}/product?idProduct=" + productId;
        });
    });
</script>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>