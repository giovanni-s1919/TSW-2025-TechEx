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
    const imagePreview = document.getElementById('image-preview');

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
                productListContainer.innerHTML = '<tr><td colspan="7" style="text-align:center;">Nessun prodotto trovato.</td></tr>';
                return;
            }

            products.forEach(p => {
                const row = document.createElement('tr');
                // Assumiamo che le immagini siano .jpg o .png etc. e rinominate sul server.
                // L'attributo `onerror` gestisce i casi in cui l'immagine non esiste, mostrando un placeholder.
                const imageUrl = `${window.contextPath}/images/products/${p.id}.png`;
                const placeholderUrl = `${window.contextPath}/images/placeholder.png`;

                // Aggiunta la colonna per l'immagine
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
            console.error(error);
            productListContainer.innerHTML = '<tr><td colspan="7" style="text-align:center;">Errore nel caricamento dei prodotti.</td></tr>';
        }
    }

    // 2. GESTIRE L'APERTURA DEL MODAL
    addProductBtn.addEventListener('click', () => {
        productForm.reset();
        modalTitle.textContent = 'Aggiungi Nuovo Prodotto';
        document.getElementById('productId').value = '';
        imagePreview.innerHTML = ''; // Pulisce l'anteprima
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

    // Funzione helper per verificare se un'immagine esiste
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

            // Popola i campi del form
            document.getElementById('productId').value = product.id;
            document.getElementById('productName').value = product.name;
            document.getElementById('productBrand').value = product.brand;
            document.getElementById('productPrice').value = product.price;
            document.getElementById('productQuantity').value = product.stockQuantity;
            document.getElementById('productCategory').value = product.category;
            document.getElementById('productGrade').value = product.grade;
            document.getElementById('productVat').value = product.vat;
            document.getElementById('productDescription').value = product.description;

            // Mostra l'anteprima dell'immagine corrente se esiste
            const imageUrl = `${window.contextPath}/images/products/product_${product.id}.jpg`;
            checkImageExists(imageUrl, (exists) => {
                if(exists) {
                    imagePreview.innerHTML = `<p>Immagine attuale:</p><img src="${imageUrl}" alt="Immagine prodotto" style="max-width: 100px; border-radius: 5px;">`;
                } else {
                    imagePreview.innerHTML = '<p>Nessuna immagine presente.</p>';
                }
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

    // 4. GESTIRE IL SALVATAGGIO (Create/Update) CON UPLOAD IMMAGINE
    productForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        // Usiamo FormData per inviare sia i dati del form che il file
        const formData = new FormData(productForm);
        formData.append('action', 'saveProduct');

        try {
            const response = await fetch(`${window.contextPath}/admin/panel`, {
                method: 'POST',
                body: formData // L'header 'Content-Type' è impostato automaticamente dal browser
            });

            if (!response.ok) {
                throw new Error(`Errore del server: ${response.statusText}`);
            }

            const result = await response.json();

            if (result.success) {
                productModal.classList.remove('active');
                loadProducts();
            } else {
                const msgBox = document.getElementById('productModalMessages');
                msgBox.textContent = 'Errore: ' + result.message;
            }
        } catch (error) {
            console.error('Errore durante l\'invio del form:', error);
            const msgBox = document.getElementById('productModalMessages');
            msgBox.textContent = 'Si è verificato un errore di connessione.';
        }
    });

    // --- FUNZIONE HELPER PER LE CHIAMATE FETCH SEMPLICI (NON PER UPLOAD) ---
    async function fetchWithAction(action, data = {}) {
        const body = new URLSearchParams();
        body.append('action', action);
        for (const key in data) {
            body.append(key, data[key]);
        }

        return await fetch(`${window.contextPath}/admin/panel`, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: body
        });
    }

    // ... La tua logica per gli ordini ...
});