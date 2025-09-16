document.addEventListener("DOMContentLoaded", function () {
    const contextPath = window.contextPath || '';

    const items = document.querySelectorAll("#account_voices li");
    const panels = document.querySelectorAll(".content-panel");

    function showPanel(id) {
        panels.forEach(p => p.classList.remove("active"));
        const target = document.getElementById(id);
        if (target) {
            target.classList.add("active");
            items.forEach(item => item.classList.remove('active-voice'));
            const activeVoice = document.querySelector(`#account_voices li[data-target="${id}"]`);
            if(activeVoice) activeVoice.classList.add('active-voice');
        }
    }

    items.forEach(item => {
        item.addEventListener("click", () => {
            const targetId = item.getAttribute("data-target");
            showPanel(targetId);
            history.pushState("", document.title, window.location.pathname + window.location.search);
        });
    });

    const hash = window.location.hash;
    if (hash) {
        const targetId = hash.substring(1);
        const targetTabExists = document.querySelector(`#account_voices li[data-target="${targetId}"]`);
        if (targetTabExists) {
            showPanel(targetId);
        } else {
            showPanel("account");
        }
    } else {
        showPanel("account");
    }

    const userProfileSection = document.getElementById("account");
    const messagesDiv = document.getElementById("messages");

    userProfileSection.addEventListener('click', function(event) {
        const targetButton = event.target.closest('.edit-btn, .save-btn, .cancel-btn');
        if (!targetButton) return;
        const infoItem = targetButton.closest('.info-item');
        if (!infoItem) return;
        const inputField = infoItem.querySelector('input');
        const fieldName = targetButton.dataset.field;
        clearMessages();
        if (targetButton.classList.contains('edit-btn')) {
            if (fieldName === 'password') {
                showPasswordChangeModal();
                return;
            }
            inputField.removeAttribute('readonly');
            inputField.focus();
            targetButton.classList.remove('edit-btn');
            targetButton.classList.add('save-btn');
            targetButton.textContent = 'Salva';
            const cancelButton = document.createElement('button');
            cancelButton.classList.add('cancel-btn');
            cancelButton.textContent = 'Annulla';
            cancelButton.dataset.field = fieldName;
            infoItem.appendChild(cancelButton);
        } else if (targetButton.classList.contains('save-btn')) {
            const newValue = inputField.value;
            const originalValue = targetButton.dataset.originalValue;
            if (fieldName === 'birthDate') {
                const birthDate = new Date(newValue);
                const today = new Date();
                today.setHours(0, 0, 0, 0); // data impostata a mezzanotte per evitare problemi di fuso orario
                const eighteenYearsAgo = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
                if (birthDate > today) {
                    displayMessage('La data di nascita non può essere una data futura.', 'error');
                    return;
                }
                if (birthDate > eighteenYearsAgo) {
                    displayMessage('Devi avere almeno 18 anni!', 'error');
                    return;
                }
            }
            if (fieldName === 'email') {
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailPattern.test(newValue)) {
                    displayMessage('Per favore, inserisci un formato email valido (es. nome@dominio).', 'error');
                    return;
                }
            }
            if (fieldName !== 'phone' && newValue.trim() === '') {
                displayMessage('Il campo non può essere vuoto.', 'error');
                return;
            }
            if (newValue === originalValue) {
                displayMessage('Nessuna modifica rilevata.', 'info');
                resetField(inputField, targetButton);
                return;
            }
            saveFieldChange(fieldName, newValue, inputField, targetButton);
        } else if (targetButton.classList.contains('cancel-btn')) {
            resetField(inputField, targetButton);
        }
    });

    function displayMessage(message, type = 'info', targetElement = messagesDiv) {
        targetElement.innerHTML = message;
        targetElement.className = '';
        targetElement.classList.add(type);
    }

    function clearMessages(targetElement = messagesDiv) {
        targetElement.textContent = '';
        targetElement.className = '';
    }

    function resetField(inputField, button) {
        inputField.setAttribute('readonly', true);
        if (button.classList.contains('cancel-btn')) {
            inputField.value = button.closest('.info-item').querySelector('.edit-btn, .save-btn').dataset.originalValue;
        }
        const infoItem = inputField.closest('.info-item');
        const saveButton = infoItem.querySelector('.save-btn');
        const cancelButton = infoItem.querySelector('.cancel-btn');
        if (saveButton) {
            saveButton.classList.remove('save-btn');
            saveButton.classList.add('edit-btn');
            saveButton.textContent = 'Modifica';
        }
        if (cancelButton) {
            cancelButton.remove();
        }
    }

    function saveFieldChange(fieldName, newValue, inputField, saveButton) {
        const params = new URLSearchParams();
        params.append('action', 'updateField');
        params.append('field', fieldName);
        params.append('value', newValue);
        fetch(`${contextPath}/personal_area`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => Promise.reject(err));
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    saveButton.dataset.originalValue = newValue;
                    displayMessage(data.message || 'Dato aggiornato con successo!', 'success');
                    resetField(inputField, saveButton);
                } else {
                    displayMessage(data.message || 'Errore durante l\'aggiornamento.', 'error');
                    inputField.value = saveButton.dataset.originalValue;
                    resetField(inputField, saveButton);
                }
            })
            .catch(error => {
                console.error('Errore AJAX:', error);
                displayMessage(error.message || 'Errore di comunicazione con il server.', 'error');
                inputField.value = saveButton.dataset.originalValue;
                resetField(inputField, saveButton);
            });
    }

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

    document.body.insertAdjacentHTML('beforeend', passwordModalHTML);
    const passwordChangeModal = document.getElementById('passwordChangeModal');
    const closeButtons = document.querySelectorAll('.close-button');
    const passwordChangeForm = document.getElementById('passwordChangeForm');
    const passwordModalMessages = document.getElementById('passwordModalMessages');

    function showPasswordChangeModal() {
        passwordChangeModal.style.display = 'block';
        clearMessages(passwordModalMessages);
        passwordChangeForm.reset();
    }

    function hidePasswordChangeModal() {
        passwordChangeModal.style.display = 'none';
    }

    closeButtons.forEach(button => {
        button.addEventListener('click', hidePasswordChangeModal);
    });

    window.addEventListener('click', function(event) {
        if (event.target === passwordChangeModal) {
            hidePasswordChangeModal();
        }
    });

    passwordChangeForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmNewPassword = document.getElementById('confirmNewPassword').value;
        clearMessages(passwordModalMessages);
        if (newPassword !== confirmNewPassword) {
            displayMessage('La nuova password e la conferma non corrispondono.', 'error', passwordModalMessages);
            return;
        }
        const isLengthValid = newPassword.length >= 8;
        const hasEnoughUppercases = (newPassword.match(/[A-Z]/g) || []).length >= 2;
        const hasEnoughLowercases = (newPassword.match(/[a-z]/g) || []).length >= 2;
        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(newPassword);
        const hasNumber = /[0-9]/.test(newPassword);
        const isPasswordValid = isLengthValid && hasEnoughUppercases && hasEnoughLowercases && hasSpecialChar && hasNumber;
        if (!isPasswordValid) {
            const policyMessage = "Errore! La password deve contenere:<ul>" +
                "<li>Almeno 8 caratteri;</li>" +
                "<li>Almeno 2 lettere maiuscole;</li>" +
                "<li>Almeno 2 lettere minuscole;</li>" +
                "<li>Almeno 1 carattere speciale;</li>" +
                "<li>Almeno 1 numero.</li>" +
                "</ul>";
            displayMessage(policyMessage, 'error', passwordModalMessages);
            return;
        }
        sendPasswordChangeRequest(currentPassword, newPassword);
    });

    function sendPasswordChangeRequest(currentPassword, newPassword) {
        const params = new URLSearchParams();
        params.append('action', 'changePassword');
        params.append('currentPassword', currentPassword);
        params.append('newPassword', newPassword);
        const confirmNewPassword = document.getElementById('confirmNewPassword').value;
        params.append('confirmNewPassword', confirmNewPassword);
        fetch(`${contextPath}/personal_area`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => Promise.reject(err));
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    displayMessage(data.message || 'Password aggiornata con successo!', 'success', passwordModalMessages);
                    passwordChangeForm.reset();
                    setTimeout(hidePasswordChangeModal, 2000);
                } else {
                    displayMessage(data.message || 'Errore durante il cambio password.', 'error', passwordModalMessages);
                }
            })
            .catch(error => {
                console.error('Errore AJAX cambio password:', error);
                displayMessage(error.message || 'Errore di comunicazione con il server.', 'error', passwordModalMessages);
            });
    }

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
                        <input type="tel" id="addr_phone" name="phone">
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

    document.body.insertAdjacentHTML('beforeend', addAddressModalHTML);
    const addAddressModal = document.getElementById('addAddressModal');
    const addAddressForm = document.getElementById('addAddressForm');
    const addressModalMessages = document.getElementById('addressModalMessages');
    const addressModalTitle = document.getElementById('address-modal-title');
    const addressModalSaveBtn = document.getElementById('address-modal-save-btn');
    addAddressModal.querySelector('.close-button').addEventListener('click', hideAddAddressModal);

    addAddressModal.addEventListener('click', function(event) {
        if (event.target === addAddressModal) {
            hideAddAddressModal();
        }
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
                    loadAddresses();
                } else {
                    displayMessage(data.message || 'Errore durante il salvataggio.', 'error', addressModalMessages);
                }
            })
            .catch(error => {
                console.error('Errore AJAX salvataggio indirizzo:', error);
                displayMessage(error.message || 'Errore di comunicazione con il server.', 'error', addressModalMessages);
            });
    }

    const addressTab = document.querySelector('#account_voices li[data-target="addresses"]');
    const addressListContainer = document.getElementById('address-list-container');
    const addAddressBtn = document.getElementById('add-address-btn');

    if (addressTab && addressListContainer && addAddressBtn) {
        addressTab.addEventListener('click', loadAddresses);
        addAddressBtn.addEventListener('click', () => showAddAddressModal());
        addressListContainer.addEventListener('click', handleAddressCardClick);
    }

    function handleAddressCardClick(event) {
        const target = event.target;
        const card = target.closest('.address-card');
        if (!card) return;
        const addressId = card.dataset.addressId;
        if (target.matches('.edit-address-btn')) {
            fetch(`${contextPath}/personal_area?action=getAddressDetails&addressId=${addressId}`)
                .then(response => {
                    if (!response.ok) { throw new Error('Impossibile recuperare i dettagli dell\'indirizzo.'); }
                    return response.json();
                })
                .then(addressData => {
                    showAddAddressModal(addressData);
                })
                .catch(error => {
                    console.error("Errore recupero dettagli indirizzo:", error);
                    displayMessage(error.message, 'error');
                });
            return;
        }
        if (target.matches('.delete-address-btn')) {
            if (confirm('Sei sicuro di voler eliminare questo indirizzo?')) {
                sendDeleteAddressRequest(addressId, card);
            }
            return;
        }
        const summary = target.closest('.address-summary');
        if (summary) {
            const details = card.querySelector('.address-details');
            details.classList.toggle('expanded');
        }
    }

    function sendDeleteAddressRequest(addressId, cardElement) {
        const params = new URLSearchParams();
        params.append('action', 'deleteAddress');
        params.append('addressId', addressId);
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
                    cardElement.remove();
                    displayMessage(data.message || 'Indirizzo eliminato con successo!', 'success');
                    setTimeout(() => clearMessages(), 3000);
                    if (addressListContainer.querySelectorAll('.address-card').length === 0) {
                        addressListContainer.innerHTML = '<p class="no-addresses-msg">Nessun indirizzo inserito.</p>';
                    }
                } else {
                    displayMessage(data.message || 'Errore durante l\'eliminazione.', 'error');
                }
            })
            .catch(error => {
                console.error('Errore AJAX eliminazione indirizzo:', error);
                displayMessage(error.message || 'Errore di comunicazione con il server.', 'error');
            });
    }

    function loadAddresses() {
        addressListContainer.innerHTML = '<p class="loading-msg">Caricamento indirizzi in corso...</p>';
        fetch(`${contextPath}/personal_area?action=getAddresses`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Errore di rete o del server.');
                }
                return response.json();
            })
            .then(addresses => {
                addressListContainer.innerHTML = '';
                if (addresses.length === 0) {
                    addressListContainer.innerHTML = '<p class="no-addresses-msg">Nessun indirizzo inserito.</p>';
                } else {
                    addresses.forEach(address => {
                        const addressCardHTML = createAddressCard(address);
                        addressListContainer.insertAdjacentHTML('beforeend', addressCardHTML);
                    });
                }
            })
            .catch(error => {
                console.error('Errore nel caricamento degli indirizzi:', error);
                addressListContainer.innerHTML = '<p class="error-msg">Impossibile caricare gli indirizzi. Riprova più tardi.</p>';
            });
    }

    function createAddressCard(address) {
        const isDefaultBadge = address.isDefault ? `<span class="default-badge">Predefinito</span>` : '';
        return `
        <div class="address-card" data-address-id="${address.id}">
            <div class="address-summary">
                <div class="summary-text">
                     <div class="summary-text-intro">
                        <strong>${address.name} ${address.surname}</strong>
                        ${isDefaultBadge}
                    </div>
                    <span>${address.street}, ${address.city}, ${address.postalCode}</span>
                </div>
                <div class="address-actions">
                    <button class="action-btn edit-address-btn" title="Modifica Indirizzo">Modifica</button>
                    <button class="action-btn delete-address-btn" title="Elimina Indirizzo">Elimina</button>
                </div>
            </div>
            <div class="address-details">
                <p><strong>Nome:</strong> ${address.name}</p>
                <p><strong>Cognome:</strong> ${address.surname}</p>
                <p><strong>Indirizzo:</strong> ${address.street}${address.additionalInfo ? ', ' + address.additionalInfo : ''}</p>
                <p><strong>Città:</strong> ${address.city}</p>
                <p><strong>CAP:</strong> ${address.postalCode}</p>
                <p><strong>Provincia:</strong> ${address.region || 'N/D'}</p>
                <p><strong>Paese:</strong> ${address.country}</p>
                <p><strong>Telefono:</strong> ${address.phone || 'N/D'}</p>
                <p><strong>Tipo di indirizzo:</strong> ${address.translatedAddressType}</p>
            </div>
        </div>
    `;
    }

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

    document.body.insertAdjacentHTML('beforeend', addPaymentMethodModalHTML);
    const addPaymentMethodModal = document.getElementById('addPaymentMethodModal');
    const addPaymentMethodForm = document.getElementById('addPaymentMethodForm');
    const paymentMethodModalMessages = document.getElementById('paymentMethodModalMessages');
    const paymentMethodModalTitle = document.getElementById('payment-method-modal-title');
    const paymentMethodModalSaveBtn = document.getElementById('payment-method-modal-save-btn');
    const paymentMethodTab = document.querySelector('#account_voices li[data-target="payments"]');
    const paymentMethodListContainer = document.getElementById('payment-methods-list-container');
    const addPaymentMethodBtn = document.getElementById('add-payment-methods-btn');
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
        if (methodData) {
            paymentMethodModalTitle.textContent = 'Modifica Metodo di Pagamento';
            paymentMethodModalSaveBtn.textContent = 'Salva Modifiche';
            document.getElementById('pm_id').value = methodData.id;
            document.getElementById('pm_name').value = methodData.name;
            document.getElementById('pm_number').value = methodData.number.replace(/(\d{4})(?=\d)/g, '$1-');
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
        if (event.target === addPaymentMethodModal) {
            hideAddPaymentMethodModal();
        }
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
                    loadPaymentMethods();
                } else {
                    displayMessage(data.message || 'Errore durante il salvataggio.', 'error', paymentMethodModalMessages);
                }
            })
            .catch(error => {
                console.error('Errore AJAX aggiunta metodo di pagamento:', error);
                displayMessage(error.message || 'Errore di comunicazione con il server.', 'error', paymentMethodModalMessages);
            });
    }

    if (paymentMethodTab && paymentMethodListContainer && addPaymentMethodBtn) {
        paymentMethodTab.addEventListener('click', loadPaymentMethods);
        addPaymentMethodBtn.addEventListener('click', () => showAddPaymentMethodModal());
        paymentMethodListContainer.addEventListener('click', handlePaymentMethodCardClick);
    }

    function handlePaymentMethodCardClick(event) {
        const target = event.target;
        const card = target.closest('.payment-method-card');
        if (!card) return;
        const methodId = card.dataset.methodId;
        if (target.matches('.edit-payment-method-btn')) {
            fetch(`${contextPath}/personal_area?action=getPaymentMethodDetails&methodId=${methodId}`)
                .then(response => {
                    if (!response.ok) { throw new Error('Impossibile recuperare i dettagli del metodo.'); }
                    return response.json();
                })
                .then(methodData => {
                    showAddPaymentMethodModal(methodData);
                })
                .catch(error => {
                    console.error("Errore recupero dettagli metodo:", error);
                    displayMessage(error.message, 'error');
                });
            return;
        }
        if (target.matches('.delete-payment-method-btn')) {
            if (confirm('Sei sicuro di voler eliminare questo metodo di pagamento?')) {
                sendDeletePaymentMethodRequest(methodId, card);
            }
        }
    }

    function sendDeletePaymentMethodRequest(methodId, cardElement) {
        const params = new URLSearchParams();
        params.append('action', 'deletePaymentMethod');
        params.append('methodId', methodId);
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
                    cardElement.remove();
                    displayMessage(data.message || 'Metodo eliminato con successo!', 'success');
                    setTimeout(() => clearMessages(), 3000);
                    if (paymentMethodListContainer.querySelectorAll('.payment-method-card').length === 0) {
                        paymentMethodListContainer.innerHTML = '<p class="no-addresses-msg">Nessun metodo di pagamento inserito.</p>';
                    }
                } else {
                    displayMessage(data.message || 'Errore durante l\'eliminazione.', 'error');
                }
            })
            .catch(error => {
                console.error('Errore AJAX eliminazione metodo di pagamento:', error);
                displayMessage(error.message || 'Errore di comunicazione con il server.', 'error');
            });
    }

    function loadPaymentMethods() {
        paymentMethodListContainer.innerHTML = '<p class="loading-msg">Caricamento metodi di pagamento...</p>';
        fetch(`${contextPath}/personal_area?action=getPaymentMethods`)
            .then(response => {
                if (!response.ok) { throw new Error('Errore di rete o del server.'); }
                return response.json();
            })
            .then(paymentMethods => {
                paymentMethodListContainer.innerHTML = '';
                if (paymentMethods.length === 0) {
                    paymentMethodListContainer.innerHTML = '<p class="no-addresses-msg">Nessun metodo di pagamento inserito.</p>';
                } else {
                    paymentMethods.forEach(pm => {
                        const pmCardHTML = createPaymentMethodCard(pm);
                        paymentMethodListContainer.insertAdjacentHTML('beforeend', pmCardHTML);
                    });
                }
            })
            .catch(error => {
                console.error('Errore nel caricamento dei metodi di pagamento:', error);
                paymentMethodListContainer.innerHTML = '<p class="error-msg">Impossibile caricare i metodi di pagamento. Riprova più tardi.</p>';
            });
    }

    function createPaymentMethodCard(pm) {
        const isDefaultBadge = pm.isDefault ? `<span class="default-badge">Predefinito</span>` : '';
        const expirationDate = new Date(pm.expiration);
        const formattedExpiration = `${String(expirationDate.getMonth() + 1).padStart(2, '0')}/${expirationDate.getFullYear()}`;
        const cardTypeClass = pm.cardType.toLowerCase().replace(' ', '-');
        const logoHtml = `<div class="card-logo ${cardTypeClass}"></div>`;
        return `
            <div class="payment-method-card" data-method-id="${pm.id}">
                <div class="payment-method-summary">
                    <div class="summary-text">
                         <div class="summary-text-intro">
                            ${logoHtml}
                            <strong>${pm.cardType} ${pm.maskedNumber}</strong>
                            ${isDefaultBadge}
                         </div>
                         <span>Scade il ${formattedExpiration} - ${pm.name}</span>
                    </div>
                    <div class="payment-method-actions">
                        <button class="action-btn edit-payment-method-btn" title="Modifica Metodo">Modifica</button>
                        <button class="action-btn delete-payment-method-btn" title="Elimina Metodo">Elimina</button>
                    </div>
                </div>
            </div>
        `;
    }

    const ordersTab = document.querySelector('#account_voices li[data-target="orders"]');
    const ordersContentPanel = document.getElementById('orders');
    if (ordersTab && ordersContentPanel) {
        ordersTab.addEventListener('click', loadOrders);
        ordersContentPanel.addEventListener('click', handleOrderCardClick);
        if (window.location.hash === '#orders') {
            loadOrders();
        }
    }

    function loadOrders() {
        const container = document.getElementById('orders-list-container');
        if (!container) {
            console.error('Il contenitore #orders-list-container non è stato trovato nella JSP.');
            return;
        }
        container.innerHTML = '<p class="loading-msg">Caricamento storico ordini...</p>';
        fetch(`${contextPath}/personal_area?action=getOrders`)
            .then(response => {
                if (!response.ok) { throw new Error('Errore di rete o del server.'); }
                return response.json();
            })
            .then(orders => {
                container.innerHTML = '';
                if (orders.length === 0) {
                    container.innerHTML = '<p class="no-items-msg">Non hai ancora effettuato alcun ordine.</p>';
                } else {
                    orders.forEach(order => {
                        const orderCardHTML = createOrderCard(order);
                        container.insertAdjacentHTML('beforeend', orderCardHTML);
                    });
                }
            })
            .catch(error => {
                console.error('Errore nel caricamento degli ordini:', error);
                container.innerHTML = '<p class="error-msg">Impossibile caricare lo storico ordini. Riprova più tardi.</p>';
            });
    }

    function createOrderCard(order) {
        const formattedTotal = `€${parseFloat(order.totalAmount).toFixed(2).replace('.', ',')}`;
        const statusClass = `status-${order.orderStatus.toLowerCase().replace(/\s+/g, '-')}`;
        return `
            <div class="order-card" data-order-id="${order.id}">
                <div class="order-summary">
                    <div class="summary-col">
                        <strong>ORDINE #${order.id}</strong>
                        <span>Effettuato il: ${order.orderDate}</span>
                    </div>
                    <div class="summary-col">
                        <strong>TOTALE</strong>
                        <span>${formattedTotal}</span>
                    </div>
                    <div class="summary-col">
                        <strong>STATO</strong>
                        <span class="order-status-badge ${statusClass}">${order.orderStatus}</span>
                    </div>
                </div>
                <div class="order-details"></div>
            </div>
        `;
    }

    function handleOrderCardClick(event) {
        const summary = event.target.closest('.order-summary');
        if (!summary) return;
        const card = summary.closest('.order-card');
        const detailsContainer = card.querySelector('.order-details');
        const isExpanded = card.classList.contains('expanded');
        document.querySelectorAll('.order-card.expanded').forEach(expandedCard => {
            if (expandedCard !== card) {
                expandedCard.classList.remove('expanded');
            }
        });
        if (isExpanded) {
            card.classList.remove('expanded');
        } else {
            card.classList.add('expanded');
            if (detailsContainer.innerHTML.trim() === '') {
                loadOrderDetails(card.dataset.orderId, detailsContainer);
            }
        }
    }

    function loadOrderDetails(orderId, container) {
        container.innerHTML = '<p class="loading-msg">Caricamento dettagli...</p>';
        fetch(`${contextPath}/personal_area?action=getOrderDetails&orderId=${orderId}`)
            .then(response => {
                if (!response.ok) { throw new Error('Impossibile caricare i dettagli dell\'ordine.'); }
                return response.json();
            })
            .then(data => {
                const detailsHTML = createOrderDetailsHTML(data);
                container.innerHTML = detailsHTML;
            })
            .catch(error => {
                console.error(`Errore nel caricamento dei dettagli per l'ordine #${orderId}:`, error);
                container.innerHTML = '<p class="error-msg">Errore nel caricamento dei dettagli.</p>';
            });
    }

    function createOrderDetailsHTML(data) {
        const itemsHTML = data.items.map(displayItem => {
            const item = displayItem.orderItem;
            const product = displayItem.product;
            const itemTotal = `€${parseFloat(item.itemPrice * item.itemQuantity).toFixed(2).replace('.', ',')}`;
            const productId = product ? product.id : 0;
            const productName = product ? product.name : item.itemName;
            return `
            <div class="summary-item">
                <img class="summary-item-image" src="${contextPath}/images/products/${productId}.png" alt="${productName}">
                <span class="summary-item-name">${productName} (x${item.itemQuantity})</span>
                <span class="summary-item-price">${itemTotal}</span>
            </div>`;
        }).join('');

        const subtotal = data.items.reduce((total, displayItem) => total + (displayItem.orderItem.itemPrice * displayItem.orderItem.itemQuantity), 0);
        const shippingCost = data.order.totalAmount - subtotal;
        const shippingHTML = shippingCost > 0 ? `€${parseFloat(shippingCost).toFixed(2).replace('.', ',')}` : '<span class="free-shipping">GRATIS</span>';

        let shippingAddressHTML = '<h1>Indirizzo di spedizione</h1><p>Indirizzo non disponibile.</p>';
        if (data.shippingAddress) {
            shippingAddressHTML = `
            <h1>Indirizzo di spedizione</h1>
            <div class="order-address-content">
                <p><strong>${data.shippingAddress.name || ''} ${data.shippingAddress.surname || ''}</strong></p>
                <p>${data.shippingAddress.street || ''}</p>
                <p>${data.shippingAddress.city || ''}, ${data.shippingAddress.postalCode || ''}</p>
                <p>${data.shippingAddress.region|| ''}</p>
                <p>${data.shippingAddress.country || ''}</p>
            </div>`;
        }

        let billingAddressHTML = '';
        if (data.order.shippingAddressId === data.order.billingAddressId) {
            billingAddressHTML = '<h1>Indirizzo di fatturazione</h1><p class="billing-same-msg">Uguale all\'indirizzo di spedizione.</p>';
        } else if (data.billingAddress) {
            billingAddressHTML = `
            <h1>Indirizzo di fatturazione</h1>
            <div class="order-address-content">
                <p><strong>${data.billingAddress.name || ''} ${data.billingAddress.surname || ''}</strong></p>
                <p>${data.billingAddress.street || ''}</p>
                <p>${data.billingAddress.city || ''}, ${data.billingAddress.postalCode || ''}</p>
                <p>${data.billingAddress.region|| ''}</p>
                <p>${data.billingAddress.country || ''}</p>
            </div>`;
        }

        return `
        <div class="details-grid">
            <div class="details-col products-col">
                <h1>Prodotti acquistati</h1>
                <div class="summary-items">${itemsHTML}</div>
                <div class="summary-totals">
                    <div class="total-row" id="first_row"><span>Subtotale:</span><span>€${parseFloat(subtotal).toFixed(2).replace('.', ',')}</span></div>
                    <div class="total-row"><span>Costi di spedizione:</span><span>${shippingHTML}</span></div>
                    <div class="total-row grand-total"><span>Totale:</span><span>€${parseFloat(data.order.totalAmount).toFixed(2).replace('.', ',')}</span></div>
                </div>
            </div>
            <div class="details-col addresses-col">
                ${shippingAddressHTML}
                ${billingAddressHTML}
            </div>
        </div>`;
    }
});