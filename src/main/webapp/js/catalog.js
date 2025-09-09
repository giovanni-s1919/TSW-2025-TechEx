document.addEventListener("DOMContentLoaded", function () {
    const filtersSidebar = document.querySelector('.filters-sidebar');
    const applyFiltersBtn = document.getElementById('apply-filters-btn');
    const filtersForm = document.getElementById('filters-form');
    const productGrid = document.querySelector('.products-grid');
    const sortOptions = document.getElementById('sort-options');

    if (filtersForm) {
        filtersForm.reset();
    }

    function showApplyButton() {
        applyFiltersBtn.hidden = false;
    }

    filtersSidebar.addEventListener('change', function(event) {
        if (event.target.matches('input[type="checkbox"], input[type="radio"]')) {
            showApplyButton();
        }
    });
    sortOptions.addEventListener('change', showApplyButton);

    applyFiltersBtn.addEventListener('click', function() {
        productGrid.innerHTML = '<p>Applicazione filtri in corso...</p>';
        const activeFilters = getActiveFilters();
        fetchProducts(activeFilters);
        applyFiltersBtn.hidden = true;
    });

    function getActiveFilters() {
        const params = new URLSearchParams();
        params.append('action', 'filterProducts');
        document.querySelectorAll('input[name="category"]:checked').forEach(input => {
            params.append('category', input.value);
        });
        document.querySelectorAll('input[name="brand"]:checked').forEach(input => {
            params.append('brand', input.value);
        });
        document.querySelectorAll('input[name="grade"]:checked').forEach(input => {
            params.append('grade', input.value);
        });
        const selectedPrice = document.querySelector('input[name="price"]:checked');
        if (selectedPrice) {
            params.append('price', selectedPrice.value);
        }
        params.append('sort', sortOptions.value);
        return params;
    }

    function fetchProducts(filters) {
        fetch(`${contextPath}/catalog`, {
            method: 'POST',
            body: filters
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Errore di rete o del server.');
                }
                return response.json();
            })
            .then(products => {
                productGrid.innerHTML = '';
                if (products.length === 0) {
                    productGrid.innerHTML = '<p class="no-products-found">Nessun prodotto trovato.</p>';
                } else {
                    products.forEach(product => {
                        const productCardHTML = createProductCard(product);
                        productGrid.insertAdjacentHTML('beforeend', productCardHTML);
                    });
                }
            })
            .catch(error => {
                console.error('Errore durante il filtraggio dei prodotti:', error);
                productGrid.innerHTML = '<p class="error-msg">Impossibile caricare i prodotti. Riprova più tardi.</p>';
            });
    }

    function createProductCard(product) {
        const price = parseFloat(product.price).toFixed(2).replace('.', ',');
        return `
            <a href="${contextPath}/product?idProduct=${product.id}" class="product-card-link">
                <div class="product-card">
                    <img src="${contextPath}/images/products/${product.id}.png" alt="${product.name}" class="product-img">
                    <h1 class="product-name">${product.name}</h1>
                    <p class="product-grade">${product.grade}</p>
                    <p class="product-price">${price}€</p>
                </div>
            </a>
        `;
    }
});