document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-image-upload]").forEach((root) => {
        const input = root.querySelector("[data-image-input]");
        const pickerButton = root.querySelector("[data-image-picker]");
        const countNode = root.querySelector("[data-image-count]");
        const previewGrid = root.querySelector("[data-image-preview]");

        if (!input || !pickerButton || !countNode || !previewGrid) {
            return;
        }

        let bufferedFiles = [];

        const syncInputFiles = () => {
            const transfer = new DataTransfer();
            bufferedFiles.forEach((file) => transfer.items.add(file));
            input.files = transfer.files;
        };

        const formatSize = (size) => {
            if (size >= 1024 * 1024) {
                return `${(size / (1024 * 1024)).toFixed(1)} MB`;
            }
            return `${Math.max(1, Math.round(size / 1024))} KB`;
        };

        const render = () => {
            previewGrid.innerHTML = "";

            if (bufferedFiles.length === 0) {
                previewGrid.hidden = true;
                countNode.textContent = "ფოტოები ჯერ არ არის არჩეული";
                return;
            }

            previewGrid.hidden = false;
            countNode.textContent = `არჩეულია ${bufferedFiles.length} ფოტო`;

            bufferedFiles.forEach((file, index) => {
                const item = document.createElement("figure");
                item.className = "selected-image-card";

                const image = document.createElement("img");
                image.alt = file.name;
                image.src = file.type.startsWith("image/")
                    ? URL.createObjectURL(file)
                    : "/images/empty-part.svg";
                image.addEventListener("load", () => {
                    if (image.src.startsWith("blob:")) {
                        URL.revokeObjectURL(image.src);
                    }
                }, { once: true });

                const meta = document.createElement("figcaption");
                meta.className = "selected-image-meta";

                const name = document.createElement("span");
                name.className = "selected-image-name";
                name.textContent = file.name;

                const size = document.createElement("span");
                size.className = "selected-image-size";
                size.textContent = formatSize(file.size);

                const removeButton = document.createElement("button");
                removeButton.type = "button";
                removeButton.className = "selected-image-remove";
                removeButton.textContent = "წაშლა";
                removeButton.addEventListener("click", () => {
                    bufferedFiles = bufferedFiles.filter((_, currentIndex) => currentIndex !== index);
                    syncInputFiles();
                    render();
                });

                meta.append(name, size, removeButton);
                item.append(image, meta);
                previewGrid.appendChild(item);
            });
        };

        pickerButton.addEventListener("click", () => input.click());

        input.addEventListener("change", () => {
            const nextFiles = Array.from(input.files || []);
            if (nextFiles.length === 0) {
                return;
            }

            const existingKeys = new Set(
                bufferedFiles.map((file) => `${file.name}:${file.size}:${file.lastModified}`)
            );

            nextFiles.forEach((file) => {
                const key = `${file.name}:${file.size}:${file.lastModified}`;
                if (!existingKeys.has(key)) {
                    bufferedFiles.push(file);
                    existingKeys.add(key);
                }
            });

            syncInputFiles();
            render();
        });

        render();
    });
});
