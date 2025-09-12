document.addEventListener('DOMContentLoaded', function () {
    // --- GESTIONE CAMBIO PANNELLI ---
    const menuItems = document.querySelectorAll('#account_voices li');
    const panels = document.querySelectorAll('.content-panel');
    menuItems.forEach(item => {
        item.addEventListener('click', () => {
            menuItems.forEach(i => i.classList.remove('active-voice'));
            item.classList.add('active-voice');
            const targetId = item.getAttribute('data-target');
            panels.forEach(panel => {
                panel.classList.remove('active');
                if (panel.id === targetId) panel.classList.add('active');
            });
        });
    });
    if (menuItems.length > 0) menuItems[0].click();

    // --- ELEMENTI DEL DOM ---
    const productListContainer = document.querySelector('#product-list-container tbody');
    const addProductBtn = document.getElementById('add-product-btn');
    const productModal = document.getElementById('product-modal');
    const productForm = document.getElementById('product-form');
    const closeModalBtn = productModal.querySelector('.close-button');
    const modalTitle = document.getElementById('modal-title');
    const imagePreview = document.getElementById('image-preview');

    // --- LOGICA GESTIONE PRODOTTI ---

    // 1. CARICARE LA LISTA INIZIALE
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
                const imageUrl = `${window.contextPath}/images/products/${p.id}.png`;
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

    // 2. GESTIRE L'APERTURA DEL MODAL
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

    // 3. GESTIRE L'EDIT E IL DELETE
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
            const imageUrl = `${window.contextPath}/images/products/${product.id}.png`;
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

    // 4. GESTIRE IL SALVATAGGIO (Create/Update)
    // QUESTA È LA PARTE CORRETTA CHE RISOLVE IL PROBLEMA
    productForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        // FormData raccoglie tutti i campi, inclusa l'immagine
        const formData = new FormData(productForm);

        // L'URL conterrà l'azione, come richiesto
        const url = `${window.contextPath}/admin/panel?action=saveProduct`;

        try {
            // Usiamo fetch direttamente, ma lasciamo che il browser imposti
            // l'header 'Content-Type' a 'multipart/form-data' in automatico.
            // NON lo impostiamo noi.
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

    // FUNZIONE HELPER FETCH
    async function fetchWithAction(action, data = {}) {
        const body = new URLSearchParams();
        body.append('action', action);
        for (const key in data) {
            body.append(key, data[key]);
        }
        return await fetch(`${window.contextPath}/admin/panel`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}, // Corretto
            body: body
        });
    }
});