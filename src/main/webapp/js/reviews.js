const reviews = [
    {
        name: "Giulia R.",
        avatar: "https://i.pravatar.cc/100?img=5",
        rating: 5,
        date: "12 Marzo 2025",
        text: "Servizio eccellente, pezzi originali e spedizione in 24h!"
    },
    {
        name: "Luca M.",
        avatar: "https://i.pravatar.cc/100?img=12",
        rating: 4,
        date: "05 Aprile 2025",
        text: "Ottimo prezzo, peccato per la confezione leggermente danneggiata."
    },
    {
        name: "Sara B.",
        avatar: "https://i.pravatar.cc/100?img=47",
        rating: 5,
        date: "28 Maggio 2025",
        text: "Massima professionalità e componenti originali. Consigliatissimo!"
    },
    {
        name: "Martina P.",
        avatar: "https://i.pravatar.cc/100?img=32",
        rating: 5,
        date: "18 Giugno 2025",
        text: "Assistenza clienti impeccabile e pezzi garantiti. Consegna rapidissima!"
    },
    {
        name: "Silvio A.",
        avatar: "https://i.pravatar.cc/100?img=68",
        rating: 4,
        date: "22 Luglio 2025",
        text: "Ottima qualità, tempi d'attesa più lunghi del normale."
    },
    {
        name: "Alessandro F.",
        avatar: "https://i.pravatar.cc/100?img=52",
        rating: 5,
        date: "30 Luglio 2025",
        text: "Componenti perfetti, prezzo competitivo e spedizione puntuale. Consigliato!"
    }
];
function renderReviews(reviews, containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    reviews.forEach(r => {
        let starsHtml = "";
        for (let i = 1; i <= 5; i++) {
            const filled = i <= r.rating ? "filled" : "";
            starsHtml += `<span class="star ${filled}">★</span>`;
        }

        const cardHtml = `
      <div class="review_card">
        <img src="${r.avatar}" alt="${r.name}" class="avatar">
        <div class="review_body">
          <div class="review_header">
            <strong>${r.name}</strong>
            <span class="date">${r.date}</span>
          </div>
          <div class="stars">${starsHtml}</div>
          <p class="review_text">${r.text}</p>
        </div>
      </div>
    `;
        container.insertAdjacentHTML("beforeend", cardHtml);
    });
}
document.addEventListener("DOMContentLoaded", () => {
    renderReviews(reviews, "reviewsContainer");
});