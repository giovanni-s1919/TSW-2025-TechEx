document.addEventListener('DOMContentLoaded', function () {
    const menuItems = document.querySelectorAll('#account_voices li');
    const panels = document.querySelectorAll('.content-panel');
    menuItems.forEach(item => {
        item.addEventListener('click', () => {
            const targetId = item.getAttribute('data-target');
            panels.forEach(panel => {
                panel.classList.remove('active');
                if (panel.id === targetId) panel.classList.add('active');
            });
        });
    });
    if (menuItems.length > 0) menuItems[0].click();

    const productListContainer = document.querySelector('#product-list-container tbody');
    const addProductBtn = document.getElementById('add-product-btn');
    const productModal = document.getElementById('product-modal');
    const productForm = document.getElementById('product-form');
    const closeModalBtn = productModal.querySelector('.close-button');
    const modalTitle = document.getElementById('modal-title');
    const imagePreview = document.getElementById('image-preview');

    async function loadProducts() {
        try {
            const response = await fetchWithAction('getProducts');
            if (!response.ok) throw new Error('La risposta del server non è valida.');
            const products = await response.json();
            productListContainer.innerHTML = '';
            if (products.length === 0) {
                productListContainer.innerHTML = '<tr><td colspan="7" style="text-align:center;">Nessun prodotto trovato.</td></tr>';
                return;
            }
            products.forEach(p => {
                const row = document.createElement('tr');
                const cacheBuster = `?v=${new Date().getTime()}`;
                const imageUrl = `${window.contextPath}/images/products/${p.id}.png${cacheBuster}`;
                const placeholderUrl = `${window.contextPath}/images/placeholder.png`;
                row.innerHTML = `
                    <td>${p.id}</td>
                    <td><img src="${imageUrl}" alt="${p.name}" style="width: 50px; height: 50px; border-radius: 5px; object-fit: cover;" onerror="this.onerror=null;this.src='${placeholderUrl}';"></td>
                    <td>${p.name}</td>
                    <td>${p.brand}</td>
                    <td>€${p.price.toFixed(2)}</td>
                    <td>${p.stockQuantity}</td>
                    <td><div id="last-child-buttons">
                        <button class="edit-btn" data-product-id="${p.id}">Modifica</button>
                        <button class="delete-btn" data-product-id="${p.id}">Elimina</button>
                    </div></td>
                `;
                productListContainer.appendChild(row);
            });
        } catch (error) {
            console.error('Errore nel caricamento dei prodotti:', error);
            productListContainer.innerHTML = '<tr><td colspan="7" style="text-align:center;">Errore nel caricamento dei prodotti.</td></tr>';
        }
    }
    loadProducts();

    addProductBtn.addEventListener('click', () => {
        productForm.reset();
        modalTitle.textContent = 'Aggiungi Nuovo Prodotto';
        document.getElementById('productId').value = '';
        imagePreview.innerHTML = '';
        productModal.classList.add('active');
    });

    closeModalBtn.addEventListener('click', () => productModal.classList.remove('active'));
    window.addEventListener('click', (event) => {
        if (event.target == productModal) productModal.classList.remove('active');
    });

    function checkImageExists(url, callback) {
        const img = new Image();
        img.onload = () => callback(true);
        img.onerror = () => callback(false);
        img.src = url;
    }

    productListContainer.addEventListener('click', async function (event) {
        const target = event.target;
        if (target.classList.contains('edit-btn')) {
            const productId = target.dataset.productId;
            const response = await fetchWithAction('getProductDetails', { productId });
            const product = await response.json();
            document.getElementById('productId').value = product.id;
            document.getElementById('productName').value = product.name;
            document.getElementById('productBrand').value = product.brand;
            document.getElementById('productPrice').value = product.price;
            document.getElementById('productQuantity').value = product.stockQuantity;
            document.getElementById('productCategory').value = product.category;
            document.getElementById('productGrade').value = product.grade;
            document.getElementById('productVat').value = product.vat;
            document.getElementById('productDescription').value = product.description;
            const cacheBuster = `?v=${new Date().getTime()}`;
            const imageUrl = `${window.contextPath}/images/products/${product.id}.png${cacheBuster}`;
            checkImageExists(imageUrl, (exists) => {
                imagePreview.innerHTML = exists ? `<p>Immagine attuale:</p><img src="${imageUrl}" alt="Immagine prodotto" style="max-width: 100px; border-radius: 5px;">` : '<p>Nessuna immagine presente.</p>';
            });
            modalTitle.textContent = 'Modifica Prodotto';
            productModal.classList.add('active');
        }
        if (target.classList.contains('delete-btn')) {
            const productId = target.dataset.productId;
            if (confirm(`Sei sicuro di voler eliminare il prodotto ID ${productId}?`)) {
                const response = await fetchWithAction('deleteProduct', { productId });
                const result = await response.json();
                if (result.success) loadProducts();
                else alert('Errore: ' + result.message);
            }
        }
    });

    productForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const formData = new FormData(productForm);
        const productId = formData.get('productId');
        let url = `${window.contextPath}/admin/panel?action=saveProduct`;
        if (productId) {
            url += `&productId=${productId}`;
        }
        try {
            const response = await fetch(url, {
                method: 'POST',
                body: formData
            });
            if (!response.ok) throw new Error(`Errore del server: ${response.statusText}`);
            const result = await response.json();
            if (result.success) {
                productModal.classList.remove('active');
                loadProducts();
            } else {
                document.getElementById('productModalMessages').textContent = 'Errore: ' + result.message;
            }
        } catch (error) {
            console.error('Errore durante l\'invio del form:', error);
            document.getElementById('productModalMessages').textContent = 'Si è verificato un errore di connessione.';
        }
    });

    const filterOrdersBtn = document.getElementById('filter-orders-btn');
    const orderListContainer = document.getElementById('order-list-container');
    const startDateInput = document.getElementById('start-date');
    const endDateInput = document.getElementById('end-date');
    const customerIdInput = document.getElementById('customer-id');

    if (filterOrdersBtn) {
        filterOrdersBtn.addEventListener('click', loadOrders);
    }
    const orderPanelTab = document.querySelector('li[data-target="order-overview"]');
    if(orderPanelTab) {
        orderPanelTab.addEventListener('click', () => {
            if (orderListContainer.innerHTML.trim() === '') {
                loadOrders();
            }
        }, { once: true });
    }

    async function loadOrders() {
        orderListContainer.innerHTML = '<p>Caricamento ordini in corso...</p>';
        try {
            const response = await fetchWithAction('getOrders', {
                startDate: startDateInput.value,
                endDate: endDateInput.value,
                customerId: customerIdInput.value
            });
            if (!response.ok) throw new Error('Errore di rete.');
            const orders = await response.json();
            displayOrders(orders);
        } catch (error) {
            console.error('Errore caricamento ordini:', error);
            orderListContainer.innerHTML = '<p style="color: red;">Impossibile caricare gli ordini.</p>';
        }
    }

    function displayOrders(orders) {
        if (!orders || orders.length === 0) {
            orderListContainer.innerHTML = '<p>Nessun ordine trovato con i filtri specificati.</p>';
            return;
        }
        let tableHtml = `<table><thead>...</thead><tbody>`;
        orders.forEach(order => {
            const orderDate = new Date(order.orderDate).toLocaleString('it-IT', { dateStyle: 'short', timeStyle: 'short' });
            tableHtml += `
                <tr class="order-row" data-order-id="${order.id}">
                    <td>${order.id}</td>
                    <td>${order.userID}</td>
                    <td>${orderDate}</td>
                    <td>${order.orderStatus}</td>
                    <td>€${order.totalAmount.toFixed(2)}</td>
                    <td><button class="view-details-btn" data-order-id="${order.id}">Dettagli</button></td>
                </tr>`;
        });
        tableHtml += '</tbody></table>';
        orderListContainer.innerHTML = tableHtml;
        orderListContainer.querySelector('thead').innerHTML = `
            <tr>
                <th>ID Ordine</th>
                <th>ID Cliente</th>
                <th>Data Ordine</th>
                <th>Stato</th>
                <th>Importo Totale</th>
                <th>Azioni</th>
            </tr>`;
    }

    orderListContainer.addEventListener('click', async function(event) {
        const target = event.target;
        if (!target.classList.contains('view-details-btn')) return;

        const orderId = target.dataset.orderId;
        const currentRow = target.closest('.order-row');
        const existingDetailsRow = document.getElementById(`details-for-order-${orderId}`);

        if (existingDetailsRow) {
            const panel = existingDetailsRow.querySelector('.order-details-panel');
            panel.classList.remove('expanded');
            panel.addEventListener('transitionend', () => {
                existingDetailsRow.remove();
            }, { once: true });
            return;
        }

        document.querySelectorAll('.order-details-row').forEach(row => {
            const panel = row.querySelector('.order-details-panel');
            panel.classList.remove('expanded');
            panel.addEventListener('transitionend', () => {
                row.remove();
            }, { once: true });
        });

        const detailsRow = document.createElement('tr');
        detailsRow.id = `details-for-order-${orderId}`;
        detailsRow.className = 'order-details-row';
        detailsRow.innerHTML = `<td colspan="6"><div class="order-details-panel"><p>Caricamento dettagli...</p></div></td>`;
        currentRow.after(detailsRow);

        const panel = detailsRow.querySelector('.order-details-panel');

        setTimeout(() => {
            panel.classList.add('expanded');
        }, 10);

        try {
            const response = await fetchWithAction('getOrderDetails', { orderId: orderId });
            if (!response.ok) throw new Error('Errore di rete');
            const details = await response.json();

            const deliveryDate = details.order.deliveryDate ? new Date(details.order.deliveryDate.year, details.order.deliveryDate.month - 1, details.order.deliveryDate.day).toLocaleDateString('it-IT') : 'Non specificata';

            const detailsHtml = `
            <h4>Dettagli Ordine #${details.order.id}</h4>
            <div class="order-details-grid">
                <div><strong>Indirizzo Spedizione ID:</strong> ${details.order.shippingAddressId}</div>
                <div><strong>Indirizzo Fatturazione ID:</strong> ${details.order.billingAddressId}</div>
                <div><strong>Data Consegna Prevista:</strong> ${deliveryDate}</div>
            </div>
            <h5>Prodotti Acquistati:</h5>
            <ul class="order-item-list">
                ${details.items.map(item => `
                    <li>
                        <span class="quantity">${item.itemQuantity} x</span>
                        <span class="item-name">${item.itemName} (${item.itemBrand})</span>
                        <span class="item-price">€${item.itemPrice.toFixed(2)}</span>
                    </li>
                `).join('')}
            </ul>`;
            panel.innerHTML = detailsHtml;
        } catch(error) {
            console.error("Errore nel caricare i dettagli dell'ordine:", error);
            panel.innerHTML = '<p style="color:red;">Impossibile caricare i dettagli.</p>';
        }
    });

    async function fetchWithAction(action, data = {}) {
        const body = new URLSearchParams();
        body.append('action', action);
        for (const key in data) {
            if (data[key]) {
                body.append(key, data[key]);
            }
        }
        return await fetch(`${window.contextPath}/admin/panel`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: body
        });
    }
});