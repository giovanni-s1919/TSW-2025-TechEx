document.addEventListener('DOMContentLoaded', function() {
    // Logica per cambiare pannello (uguale a personal_area.js)
    const menuItems = document.querySelectorAll('#account_voices li');
    const panels = document.querySelectorAll('.content-panel');

    menuItems.forEach(item => {
        item.addEventListener('click', () => {
            const targetId = item.getAttribute('data-target');
            panels.forEach(panel => {
                panel.style.display = panel.id === targetId ? 'block' : 'none';
            });
        });
    });

    // --- LOGICA GESTIONE PRODOTTI ---
    loadProducts();

    // Funzione per caricare la lista dei prodotti
    async function loadProducts() {
        const response = await fetch(`${contextPath}/admin/panel`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'action=getProducts'
        });
        const products = await response.json();

        const tbody = document.querySelector('#product-list-container tbody');
        tbody.innerHTML = ''; // Svuota la tabella
        products.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.brand}</td>
                    <td>${p.price} €</td>
                    <td>${p.stockQuantity}</td>
                    <td>
                        <button class="edit-btn" data-product-id="${p.id}">Modifica</button>
                        <button class="delete-btn" data-product-id="${p.id}">Elimina</button>
                    </td>
                </tr>
            `;
        });
    }

    // Aggiungi qui gli event listener per i bottoni Modifica, Elimina, Aggiungi...
    // E le funzioni per aprire il modal di modifica/creazione, salvare e cancellare.

    // --- LOGICA GESTIONE ORDINI ---
    const filterBtn = document.getElementById('filter-orders-btn');
    filterBtn.addEventListener('click', loadOrders);

    // Funzione per caricare gli ordini con i filtri
    async function loadOrders() {
        const startDate = document.getElementById('start-date').value;
        const endDate = document.getElementById('end-date').value;
        const customerId = document.getElementById('customer-id').value;

        const response = await fetch(`${contextPath}/admin/panel`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `action=getOrders&startDate=${startDate}&endDate=${endDate}&customerId=${customerId}`
        });
        const orders = await response.json();

        // Logica per visualizzare gli ordini nel container #order-list-container
    }
});