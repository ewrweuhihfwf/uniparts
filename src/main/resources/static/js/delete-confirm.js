document.addEventListener("DOMContentLoaded", () => {
    const dialog = document.querySelector("[data-delete-confirm-dialog]");

    if (!dialog) {
        return;
    }

    const titleNode = dialog.querySelector("[data-delete-confirm-title]");
    const acceptButton = dialog.querySelector("[data-delete-confirm-accept]");
    const cancelButton = dialog.querySelector("[data-delete-confirm-cancel]");
    let pendingForm = null;

    const closeDialog = () => {
        pendingForm = null;
        if (dialog.open) {
            dialog.close();
        }
    };

    document.querySelectorAll("form[data-confirm-submit]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            if (form.dataset.confirmed === "true") {
                delete form.dataset.confirmed;
                return;
            }

            event.preventDefault();
            pendingForm = form;

            if (titleNode) {
                titleNode.textContent = form.dataset.listingTitle || "განცხადება";
            }

            if (typeof dialog.showModal === "function") {
                dialog.showModal();
                return;
            }

            if (window.confirm("ნამდვილად გსურს წაშლა?")) {
                form.dataset.confirmed = "true";
                form.requestSubmit();
                pendingForm = null;
            }
        });
    });

    acceptButton?.addEventListener("click", () => {
        if (!pendingForm) {
            closeDialog();
            return;
        }

        pendingForm.dataset.confirmed = "true";
        const formToSubmit = pendingForm;
        closeDialog();
        formToSubmit.requestSubmit();
    });

    cancelButton?.addEventListener("click", closeDialog);
    dialog.addEventListener("cancel", closeDialog);
});
