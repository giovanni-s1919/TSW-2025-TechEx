document.addEventListener("DOMContentLoaded", function () {
    const items = document.querySelectorAll("#account_voices li");
    const panels = document.querySelectorAll(".content-panel");

    function showPanel(id) {
        panels.forEach(p => p.classList.remove("active"));
        const target = document.getElementById(id);
        if (target) target.classList.add("active");
    }

    items.forEach(item => {
        item.addEventListener("click", () => {
            const targetId = item.getAttribute("data-target");
            showPanel(targetId);
        });
    });

    showPanel("account"); // Mostra "Account" all’avvio
});