document.addEventListener("DOMContentLoaded", function () {
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
        });
    });

    showPanel("account");

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

            if (newValue.trim() === '') {
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
        targetElement.textContent = message;
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

        fetch(`${document.body.dataset.contextPath}/personal_area`, {
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
        if (newPassword.length < 8) {
            displayMessage('La nuova password deve contenere almeno 8 caratteri.', 'error', passwordModalMessages);
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

        fetch(`${document.body.dataset.contextPath}/personal_area`, {
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
});