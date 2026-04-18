document.addEventListener("DOMContentLoaded", () => {
    const brandModelMap = window.partsBrandModelMap || {};
    const allModels = [...new Set(Object.values(brandModelMap).flat())]
        .sort((left, right) => left.localeCompare(right));

    document.querySelectorAll("[data-brand-model-pair]").forEach((container) => {
        const brandSelect = container.querySelector("[data-brand-select]");
        const modelSelect = container.querySelector("[data-model-select]");

        if (!brandSelect || !modelSelect) {
            return;
        }

        const defaultLabel = modelSelect.dataset.defaultLabel || "ყველა მოდელი";

        const rebuildModels = (selectedModel) => {
            const selectedBrand = brandSelect.value;
            const availableModels = selectedBrand && brandModelMap[selectedBrand]
                ? brandModelMap[selectedBrand]
                : allModels;

            modelSelect.innerHTML = "";

            const defaultOption = document.createElement("option");
            defaultOption.value = "";
            defaultOption.textContent = defaultLabel;
            modelSelect.appendChild(defaultOption);

            availableModels.forEach((model) => {
                const option = document.createElement("option");
                option.value = model;
                option.textContent = model;
                modelSelect.appendChild(option);
            });

            modelSelect.value = availableModels.includes(selectedModel) ? selectedModel : "";
            modelSelect.disabled = !selectedBrand;
        };

        rebuildModels(modelSelect.value);
        brandSelect.addEventListener("change", () => rebuildModels(""));
    });
});
