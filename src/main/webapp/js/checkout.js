document.addEventListener("DOMContentLoaded", function () {
    const passwordModalHTML = `
        <div id="passwordChangeModal" class="modal">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <h2>Modifica Password</h2>
                <form id="passwordChangeForm">
                    <div class="form-group">
                        <label for="currentPassword">Password Attuale:</label>
                        <input type="password" id="currentPassword" name="currentPassword" required>
                    </div>
                    <div class="form-group">
                        <label for="newPassword">Nuova Password:</label>
                        <input type="password" id="newPassword" name="newPassword" required>
                    </div>
                    <div class="form-group">
                        <label for="confirmNewPassword">Conferma Nuova Password:</label>
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>
                    </div>
                    <button type="submit" class="save-btn">Salva Nuova Password</button>
                </form>
                <div id="passwordModalMessages"></div>
            </div>
        </div>
    `;

    const addAddressModalHTML = `
        <div id="addAddressModal" class="modal">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <h2 id="address-modal-title">Aggiungi Nuovo Indirizzo</h2>
                <form id="addAddressForm">
                    <input type="hidden" id="addr_id" name="addressId">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="addr_name">Nome:</label>
                            <input type="text" id="addr_name" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="addr_surname">Cognome:</label>
                            <input type="text" id="addr_surname" name="surname" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="addr_street">Indirizzo:</label>
                        <input type="text" id="addr_street" name="street" required>
                    </div>
                    <div class="form-group">
                        <label for="addr_additionalInfo">Informazioni addizionali:</label>
                        <input type="text" id="addr_additionalInfo" name="additionalInfo">
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="addr_city">Città:</label>
                            <input type="text" id="addr_city" name="city" required>
                        </div>
                        <div class="form-group">
                            <label for="addr_postalCode">CAP:</label>
                            <input type="text" id="addr_postalCode" name="postalCode" required>
                        </div>
                    </div>
                     <div class="form-row">
                        <div class="form-group">
                            <label for="addr_region">Provincia:</label>
                            <input type="text" id="addr_region" name="region">
                        </div>
                        <div class="form-group">
                            <label for="addr_country">Paese:</label>
                            <input type="text" id="addr_country" name="country" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="addr_phone">Telefono:</label>
                        <input type="text" id="addr_phone" name="phone">
                    </div>
                    <div class="form-group">
                         <label for="addr_addressType">Tipo di Indirizzo:</label>
                         <select id="addr_addressType" name="addressType" required>
                            <option value="Shipping">Spedizione</option>
                            <option value="Billing">Fatturazione</option>
                         </select>
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="addr_isDefault" name="isDefault">
                        <label for="addr_isDefault">Imposta come indirizzo predefinito</label>
                    </div>
                    <div class="modal-actions">
                        <button type="submit" class="save-btn" id="address-modal-save-btn">Salva Indirizzo</button>
                    </div>
                </form>
                <div id="addressModalMessages"></div>
            </div>
        </div>
    `;

    const addPaymentMethodModalHTML = `
        <div id="addPaymentMethodModal" class="modal">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <h2 id="payment-method-modal-title">Aggiungi Metodo di Pagamento</h2>
                <form id="addPaymentMethodForm" novalidate>
                <input type="hidden" id="pm_id" name="methodId">
                    <div class="form-group">
                        <label for="pm_name">Nome del titolare della carta:</label>
                        <input type="text" id="pm_name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="pm_number">Numero carta:</label>
                        <input type="text" id="pm_number" name="number" required placeholder="xxxx-xxxx-xxxx-xxxx" maxlength="19">
                    </div>
                    <div class="form-group">
                        <label for="pm_expiration">Data di scadenza (MM/AAAA):</label>
                        <input type="text" id="pm_expiration" name="expiration" required placeholder="MM/AAAA" maxlength="7">
                    </div>
                    <div class="form-check">
                        <input type="checkbox" id="pm_isDefault" name="isDefault">
                        <label for="pm_isDefault">Imposta come metodo di pagamento predefinito</label>
                    </div>
                    <div class="modal-actions">
                        <button type="submit" class="save-btn" id="payment-method-modal-save-btn">Salva Metodo di Pagamento</button>
                    </div>
                </form>
                <div id="paymentMethodModalMessages"></div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', passwordModalHTML);
    document.body.insertAdjacentHTML('beforeend', addAddressModalHTML);
    document.body.insertAdjacentHTML('beforeend', addPaymentMethodModalHTML);

    function displayMessage(message, type = 'info', targetElement) {
        if (targetElement) {
            targetElement.innerHTML = message;
            targetElement.className = '';
            targetElement.classList.add(type);
        }
    }

    function clearMessages(targetElement) {
        if (targetElement) {
            targetElement.textContent = '';
            targetElement.className = '';
        }
    }

    const addAddressModal = document.getElementById('addAddressModal');
    if (addAddressModal) {
        const addAddressForm = document.getElementById('addAddressForm');
        const addressModalMessages = document.getElementById('addressModalMessages');
        const addressModalTitle = document.getElementById('address-modal-title');
        const addressModalSaveBtn = document.getElementById('address-modal-save-btn');

        addAddressModal.querySelector('.close-button').addEventListener('click', hideAddAddressModal);
        addAddressModal.addEventListener('click', function(event) {
            if (event.target === addAddressModal) hideAddAddressModal();
        });

        function showAddAddressModal(addressData = null) {
            clearMessages(addressModalMessages);
            addAddressForm.reset();
            document.getElementById('addr_id').value = '';
            if (addressData) {
                addressModalTitle.textContent = 'Modifica Indirizzo';
                addressModalSaveBtn.textContent = 'Salva Modifiche';
                document.getElementById('addr_id').value = addressData.id;
                document.getElementById('addr_name').value = addressData.name;
                document.getElementById('addr_surname').value = addressData.surname;
                document.getElementById('addr_street').value = addressData.street;
                document.getElementById('addr_additionalInfo').value = addressData.additionalInfo || '';
                document.getElementById('addr_city').value = addressData.city;
                document.getElementById('addr_postalCode').value = addressData.postalCode;
                document.getElementById('addr_region').value = addressData.region || '';
                document.getElementById('addr_country').value = addressData.country;
                document.getElementById('addr_phone').value = addressData.phone || '';
                document.getElementById('addr_addressType').value = addressData.addressType;
                document.getElementById('addr_isDefault').checked = addressData.isDefault;
            } else {
                addressModalTitle.textContent = 'Aggiungi Nuovo Indirizzo';
                addressModalSaveBtn.textContent = 'Salva Indirizzo';
            }
            addAddressModal.style.display = 'block';
        }

        function hideAddAddressModal() {
            addAddressModal.style.display = 'none';
        }

        addAddressForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(addAddressForm);
            const params = new URLSearchParams();
            for (const pair of formData.entries()) {
                params.append(pair[0], pair[1]);
            }
            const addressId = document.getElementById('addr_id').value;
            if (addressId) {
                params.append('action', 'updateAddress');
            } else {
                params.append('action', 'addAddress');
            }
            if (!params.has('isDefault')) {
                params.append('isDefault', 'false');
            } else {
                params.set('isDefault', 'true');
            }
            sendSaveAddressRequest(params);
        });

        function sendSaveAddressRequest(params) {
            fetch(`${contextPath}/personal_area`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: params
            })
                .then(response => {
                    if (!response.ok) { return response.json().then(err => Promise.reject(err)); }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        hideAddAddressModal();
                        // Ricarica la pagina di checkout per mostrare il nuovo indirizzo
                        window.location.reload();
                    } else {
                        displayMessage(data.message || 'Errore.', 'error', addressModalMessages);
                    }
                })
                .catch(error => {
                    console.error('Errore AJAX salvataggio indirizzo:', error);
                    displayMessage(error.message || 'Errore di comunicazione.', 'error', addressModalMessages);
                });
        }

        const addAddressBtn = document.getElementById('checkout-add-address-btn');
        if (addAddressBtn) {
            addAddressBtn.addEventListener('click', () => showAddAddressModal());
        }
    }

    const addPaymentMethodModal = document.getElementById('addPaymentMethodModal');
    if (addPaymentMethodModal) {
        const addPaymentMethodForm = document.getElementById('addPaymentMethodForm');
        const paymentMethodModalMessages = document.getElementById('paymentMethodModalMessages');
        const paymentMethodModalTitle = document.getElementById('payment-method-modal-title');
        const paymentMethodModalSaveBtn = document.getElementById('payment-method-modal-save-btn');
        const cardNumberInput = document.getElementById('pm_number');
        const cardExpirationInput = document.getElementById('pm_expiration');

        if (cardNumberInput && cardExpirationInput) {
            cardNumberInput.addEventListener('input', function(e) {
                let value = e.target.value.replace(/\D/g, '');
                let formattedValue = (value.match(/.{1,4}/g) || []).join('-');
                e.target.value = formattedValue.substring(0, 19);
            });
            cardExpirationInput.addEventListener('input', function(e) {
                let value = e.target.value.replace(/\D/g, '');
                if (value.length > 2) {
                    value = value.substring(0, 2) + '/' + value.substring(2, 6);
                }
                e.target.value = value;
            });
        }

        function showAddPaymentMethodModal(methodData = null) {
            clearMessages(paymentMethodModalMessages);
            addPaymentMethodForm.reset();
            document.getElementById('pm_id').value = '';
            document.getElementById('pm_number').disabled = false;
            if (methodData) {
                paymentMethodModalTitle.textContent = 'Modifica Metodo di Pagamento';
                paymentMethodModalSaveBtn.textContent = 'Salva Modifiche';
                document.getElementById('pm_id').value = methodData.id;
                document.getElementById('pm_name').value = methodData.name;
                document.getElementById('pm_number').value = methodData.number.replace(/(\d{4})(?=\d)/g, '$1-');
                document.getElementById('pm_number').disabled = true;
                const expirationDate = new Date(methodData.expiration);
                const formattedExpiration = `${String(expirationDate.getMonth() + 1).padStart(2, '0')}/${expirationDate.getFullYear()}`;
                document.getElementById('pm_expiration').value = formattedExpiration;
                document.getElementById('pm_isDefault').checked = methodData.isDefault;
            } else {
                paymentMethodModalTitle.textContent = 'Aggiungi Metodo di Pagamento';
                paymentMethodModalSaveBtn.textContent = 'Salva Metodo di Pagamento';
            }
            addPaymentMethodModal.style.display = 'block';
        }

        function hideAddPaymentMethodModal() {
            addPaymentMethodModal.style.display = 'none';
        }

        addPaymentMethodModal.querySelector('.close-button').addEventListener('click', hideAddPaymentMethodModal);
        addPaymentMethodModal.addEventListener('click', function(event) {
            if (event.target === addPaymentMethodModal) hideAddPaymentMethodModal();
        });

        addPaymentMethodForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(addPaymentMethodForm);
            const params = new URLSearchParams();
            for (const pair of formData.entries()) {
                params.append(pair[0], pair[1]);
            }
            const methodId = document.getElementById('pm_id').value;
            if (methodId) {
                params.append('action', 'updatePaymentMethod');
            } else {
                params.append('action', 'addPaymentMethod');
            }
            if (!params.has('isDefault')) {
                params.append('isDefault', 'false');
            } else {
                params.set('isDefault', 'true');
            }
            sendSavePaymentMethodRequest(params);
        });

        function sendSavePaymentMethodRequest(params) {
            fetch(`${contextPath}/personal_area`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: params
            })
                .then(response => {
                    if (!response.ok) { return response.json().then(err => Promise.reject(err)); }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        hideAddPaymentMethodModal();
                        // Ricarica la pagina di checkout per mostrare il nuovo metodo
                        window.location.reload();
                    } else {
                        displayMessage(data.message || 'Errore.', 'error', paymentMethodModalMessages);
                    }
                })
                .catch(error => {
                    console.error('Errore AJAX aggiunta metodo:', error);
                    displayMessage(error.message || 'Errore di comunicazione.', 'error', paymentMethodModalMessages);
                });
        }

        const addPaymentBtn = document.getElementById('checkout-add-payment-btn');
        if (addPaymentBtn) {
            addPaymentBtn.addEventListener('click', () => showAddPaymentMethodModal());
        }
    }

    const guestCardNumberInput = document.getElementById('guest_card_number');
    const guestCardExpirationInput = document.getElementById('guest_card_expiration');
    if (guestCardNumberInput && guestCardExpirationInput) {
        guestCardNumberInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            let formattedValue = (value.match(/.{1,4}/g) || []).join('-');
            e.target.value = formattedValue.substring(0, 19);
        });
        guestCardExpirationInput.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 6);
            }
            e.target.value = value;
        });
    }

    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function(event) {
            event.preventDefault();
            let isValid = true;
            let errorMessage = '';
            const requiredInputs = checkoutForm.querySelectorAll('input[required]:not(:disabled)');
            for(const input of requiredInputs) {
                if (!input.value.trim()) {
                    isValid = false;
                    const label = document.querySelector(`label[for='${input.id}']`);
                    errorMessage = `Il campo "${label ? label.textContent.replace(':', '') : input.name}" è obbligatorio.`;
                    break;
                }
            }
            if (isValid && guestCardExpirationInput && !guestCardExpirationInput.disabled) {
                const expirationValue = guestCardExpirationInput.value;
                if (expirationValue.match(/^\d{2}\/\d{4}$/)) {
                    const [month, year] = expirationValue.split('/').map(num => parseInt(num, 10));
                    const now = new Date();
                    const currentMonth = now.getMonth() + 1;
                    const currentYear = now.getFullYear();
                    if (year < currentYear || (year === currentYear && month < currentMonth)) {
                        isValid = false;
                        errorMessage = "La data di scadenza della carta non può essere nel passato.";
                    }
                } else if (expirationValue) {
                    isValid = false;
                    errorMessage = "Il formato della data di scadenza non è valido. Usa MM/AAAA.";
                }
            }
            if (isValid) {
                const paymentSelect = checkoutForm.querySelector('select[name="paymentMethodId"]');
                if (paymentSelect && !paymentSelect.value) {
                    isValid = false;
                    errorMessage = "Nessun metodo di pagamento disponibile. Aggiungine uno per procedere.";
                }
            }
            if (!isValid) {
                alert(errorMessage);
                return;
            }
            const formData = new FormData(checkoutForm);
            const params = new URLSearchParams();
            if (!document.getElementById('billing-same-as-shipping').checked) {
                for (const pair of formData.entries()) {
                    params.append(pair[0], pair[1]);
                }
            } else {
                for (const pair of formData.entries()) {
                    if (!pair[0].startsWith('billing_') && pair[0] !== 'billingAddressId') {
                        params.append(pair[0], pair[1]);
                    }
                }
                params.append('billingSameAsShipping', 'true');
            }
            params.append('action', 'placeOrder');
            fetch(`${contextPath}/checkout`, {
                method: 'POST',
                body: params
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        window.location.href = `${contextPath}/order_confirmation?id=${data.orderId}`;
                    } else {
                        alert('Errore: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Errore:', error);
                    alert('Si è verificato un errore critico.');
                });
        });
    }

    const billingCheckbox = document.getElementById('billing-same-as-shipping');
    const billingSection = document.getElementById('billing-address-section');
    if (billingCheckbox && billingSection) {
        const billingInputs = billingSection.querySelectorAll('input, select');
        billingCheckbox.addEventListener('change', function() {
            if (this.checked) {
                billingSection.classList.add('hidden-section');
                billingInputs.forEach(input => {
                    input.disabled = true;
                });
            } else {
                billingSection.classList.remove('hidden-section');
                billingInputs.forEach(input => {
                    input.disabled = false;
                });
            }
        });
    }

    const cardLogoContainer = document.getElementById('card-logo-container');
    if (guestCardNumberInput && cardLogoContainer) {
        function detectCardType(number) {
            number = number.replace(/[\s-]/g, '');
            if (/^4/.test(number)) {
                return 'visa';
            }
            if (/^(5[1245]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)/.test(number)) {
                return 'mastercard';
            }
            if (/^3[47]/.test(number)) {
                return 'american-express';
            }
            if (/^53/.test(number)) {
                return 'postepay';
            }
            return null;
        }

        guestCardNumberInput.addEventListener('input', function(e) {
            const cardNumber = e.target.value;
            const cardType = detectCardType(cardNumber);
            cardLogoContainer.classList.remove('visa', 'mastercard', 'american-express', 'postepay', 'generic-card', 'visible');
            if (cardType) {
                cardLogoContainer.classList.add(cardType);
            } else {
                cardLogoContainer.classList.add('generic-card');
            }
            cardLogoContainer.classList.add('visible');
        });
    }

    window.addEventListener('pageshow', function(event) {
        const form = document.getElementById('checkout-form');
        if (form) {
            form.reset();
        }
        if (billingCheckbox && billingSection) {
            billingCheckbox.checked = true;
            billingCheckbox.dispatchEvent(new Event('change'));
        }
    });
});