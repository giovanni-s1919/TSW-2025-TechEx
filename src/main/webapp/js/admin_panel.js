document.addEventListener('DOMContentLoaded', function () {
    // Non definire contextPath qui. Lo leggeremo da 'window.contextPath'

    // --- GESTIONE CAMBIO PANNELLI ---
    const menuItems = document.querySelectorAll('#account_voices li');
    const panels = document.querySelectorAll('.content-panel');
    menuItems.forEach(item => {
        item.addEventListener('click', () => {
            // Rimuovi la classe 'active-voice' da tutti
            menuItems.forEach(i => i.classList.remove('active-voice'));
            // Aggiungi la classe 'active-voice' a quello cliccato
            item.classList.add('active-voice');

            const targetId = item.getAttribute('data-target');
            panels.forEach(panel => {
                panel.classList.remove('active');
                if (panel.id === targetId) {
                    panel.classList.add('active');
                }
            });
        });
    });
    // Attiva il primo pannello di default
    if (menuItems.length > 0) {
        menuItems[0].click();
    }

    // --- ELEMENTI DEL DOM PER PRODOTTI ---
    const productListContainer = document.querySelector('#product-list-container tbody');
    const addProductBtn = document.getElementById('add-product-btn');
    const productModal = document.getElementById('product-modal');
    const productForm = document.getElementById('product-form');
    const closeModalBtn = productModal.querySelector('.close-button');
    const modalTitle = document.getElementById('modal-title');

    // --- LOGICA GESTIONE PRODOTTI ---

    // 1. CARICARE LA LISTA INIZIALE
    loadProducts();

    async function loadProducts() {
        try {
            const response = await fetchWithAction('getProducts');
            if (!response.ok) throw new Error('Errore nel caricamento dei prodotti');
            const products = await response.json();

            productListContainer.innerHTML = '';
            if (products.length === 0) {
                productListContainer.innerHTML = '<tr><td colspan="6" style="text-align:center;">Nessun prodotto trovato.</td></tr>';
                return;
            }

            products.forEach(p => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${p.id}</td>
                    <td>${p.name}</td>
                    <td>${p.brand}</td>
                    <td>${p.price.toFixed(2)} €</td>
                    <td>${p.stockQuantity}</td>
                    <td>
                        <button class="edit-btn" data-product-id="${p.id}">Modifica</button>
                        <button class="delete-btn" data-product-id="${p.id}">Elimina</button>
                    </td>
                `;
                productListContainer.appendChild(row);
            });
        } catch (error) {
            console.error(error);
            productListContainer.innerHTML = '<tr><td colspan="6" style="text-align:center;">Errore nel caricamento dei prodotti.</td></tr>';
        }
    }

    // 2. GESTIRE L'APERTURA DEL MODAL
    addProductBtn.addEventListener('click', () => {
        productForm.reset();
        modalTitle.textContent = 'Aggiungi Nuovo Prodotto';
        document.getElementById('productId').value = '';
        productModal.classList.add('active');
    });

    closeModalBtn.addEventListener('click', () => {
        productModal.classList.remove('active');
    });
    window.addEventListener('click', (event) => {
        if (event.target == productModal) {
            productModal.classList.remove('active');
        }
    });

    // 3. GESTIRE L'EDIT E IL DELETE (usando event delegation)
    productListContainer.addEventListener('click', async function (event) {
        const target = event.target;

        if (target.classList.contains('edit-btn')) {
            const productId = target.dataset.productId;
            const response = await fetchWithAction('getProductDetails', { productId });
            const product = await response.json();

            // Popola tutti i campi del form, inclusi i nuovi
            document.getElementById('productId').value = product.id;
            document.getElementById('productName').value = product.name;
            document.getElementById('productBrand').value = product.brand;
            document.getElementById('productPrice').value = product.price;
            document.getElementById('productQuantity').value = product.stockQuantity;
            document.getElementById('productDescription').value = product.description;

            // --- RIGHE AGGIUNTE E MODIFICATE ---
            document.getElementById('productCategory').value = product.category; // Imposta il valore del select
            document.getElementById('productGrade').value = product.grade;       // Imposta il valore del select per Grade
            document.getElementById('productVat').value = product.vat;           // Imposta il valore di VAT

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

    // 4. GESTIRE IL SALVATAGGIO (Create/Update)
    productForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const formData = new FormData(productForm);
        const data = Object.fromEntries(formData.entries());

        const response = await fetchWithAction('saveProduct', data);
        const result = await response.json();

        if (result.success) {
            productModal.classList.remove('active');
            loadProducts();
        } else {
            const msgBox = document.getElementById('productModalMessages');
            msgBox.textContent = result.message;
        }
    });

    // --- FUNZIONE HELPER PER LE CHIAMATE FETCH ---
    async function fetchWithAction(action, data = {}) {
        const body = new URLSearchParams();
        body.append('action', action);
        for (const key in data) {
            body.append(key, data[key]);
        }

        // CORREZIONE: Usa window.contextPath, che è definito nella JSP
        return await fetch(`${window.contextPath}/admin/panel`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: body
        });
    }

    // ... La tua logica per gli ordini ...
});