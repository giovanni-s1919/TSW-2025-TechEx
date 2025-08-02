<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
  <title>TechEx - Pezzi di ricambio per smartphone</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
  <h1 class="products">Scopri la nostra selezione di pezzi di ricambio per smartphone:</h1>
  <p class="waitingfordatabase">
    porcodio<br>
    diocane<br>
    luis dici dio cane<br>
    mannaggia la madonna<br>
    ma grazie al cazzo?<br>
    è ripartito, PORCODDIO, DIO TARTUFO<br>
    (si sto scrivendo queste stronzate per simulare lo spazio che verrà occupato dalle schede dei prodotti)<br>
    ubriaco di merda mettila in mezzo<br>
    the game<br>
    ah certo, m'ha spostato Ramos asoaskasewui<br>
    meow<br>
    meow<br>
    meow<br>
    meow<br>
    sybau<br>
    Nikolas<br>
    La prossima è una stampa utile, lasciala<br>
    <%=request.getAttribute("role")%>
  </p>
  <div class="presentation">
    <p class="presentation_menu">Il futuro della riparazione è qui!</p>
    <ul class="presentation_ul">
      <li><span class="icon"><i class="fa-solid fa-circle-check"></i></span>
          <div class="presentation_content">
            <p class="presentation_menu">Qualità ed affidabilità</p>
            <p>Solo ricambi originali e di ottima fattura per prestazioni garantite!</p>
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
  <h1 class="top_articles">IL TOP DEL TOP</h1>
  <p class="the_best">I migliori articoli presenti nel nostro catalogo.<br>Qualità ultra per i clienti più esigenti.</p><br>
  <p class="waitingfordatabase">
    dobbiamo muoverci a fa sto cazzo di database<br>
    mi sono rotto il cazzo di usare ste cagate temporanee<br>
    procratalamasion<br>
    gne gne gne E VABBENE HO SCRITTO CIAO, PAUSA PRANZO?!<br>
    MA COME HA FATTO?!<br>
    chiedilo all'arciduca Ferdinando<br>
    TU MI HAI BARATTATO PER UN NICHELINO?????!!!!!<br>
    come, che cosa vuol dire girare una levet-MANNAGGIA QUEL MORTO DE OIIIIIIIDDDDDD<br>
    denis 3 metri sotto terra<br>
    è in vantaggio Desailly<br>
    ALLORA LA PROSSIMA VOLTA TI CONDISCO QUEL RISO CON IL-<br>
    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA NON LO POSSO DIREH<br>
    inflazione<br>
    cremona capitale<br>
    tuo padre in realtà è il duce<br>
    meow<br>
    <b>scrivi dinuovo quella cagata sul sushi e ti esplode casa</b>
  </p>
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
  <h1 id="mvp" class="most_sales">I PIÙ VENDUTI</h1>
  <p class="most_sales">I prodotti più apprezzati ed acquistati dagli utenti. Vuoi andare sul sicuro? Sei nel posto giusto!</p>
  <p class="waitingfordatabase">
    <b>scrivi dinuovo quella cagata sul sushi e ti ammazzo la famiglia davanti ai tuoi occhi</b><br>
    ti è piaciuto fare il coglione col sushi? bene, ora subisci<br>
    the game<br>
    never gonna give you up, never gonna let you down<br>
    66<br>
    the game<br>
    never gonna give you up, never gonna let you down<br>
    66<br>
    the game<br>
    never gonna give you up, never gonna let you down<br>
    66<br>
    the game<br>
    never gonna give you up, never gonna let you down<br>
    66<br>
    the game<br>
    never gonna give you up, never gonna let you down<br>
    66<br>
  </p>
</div>
<%@ include file="fragments/footer.jsp" %>
</body>
</html>