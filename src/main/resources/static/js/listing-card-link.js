document.addEventListener("DOMContentLoaded", () => {
    const interactiveSelector = "a, button, form, input, select, textarea, label, dialog";

    document.querySelectorAll("[data-click-url]").forEach((card) => {
        const targetUrl = card.dataset.clickUrl;
        if (!targetUrl) {
            return;
        }

        const openCard = () => {
            window.location.href = targetUrl;
        };

        card.addEventListener("click", (event) => {
            if (event.target.closest(interactiveSelector)) {
                return;
            }
            openCard();
        });

        card.addEventListener("keydown", (event) => {
            if (event.key !== "Enter" && event.key !== " ") {
                return;
            }
            if (event.target.closest(interactiveSelector) && event.target !== card) {
                return;
            }
            event.preventDefault();
            openCard();
        });
    });
});
