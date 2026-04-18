document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-gallery]").forEach((gallery) => {
        const mainImage = gallery.querySelector("[data-gallery-main]");
        const counter = gallery.querySelector("[data-gallery-counter]");
        const previousButton = gallery.querySelector("[data-gallery-prev]");
        const nextButton = gallery.querySelector("[data-gallery-next]");
        const mediaRoot = gallery.closest(".details-media");
        const strip = mediaRoot?.querySelector("[data-gallery-strip]");
        const thumbnails = [...(mediaRoot?.querySelectorAll("[data-gallery-thumb]") || [])];
        const stripPrevious = mediaRoot?.querySelector("[data-gallery-strip-prev]");
        const stripNext = mediaRoot?.querySelector("[data-gallery-strip-next]");

        if (!mainImage || thumbnails.length === 0) {
            return;
        }

        let activeIndex = Math.max(thumbnails.findIndex((thumb) => thumb.classList.contains("is-active")), 0);

        const render = (index, smooth) => {
            activeIndex = index;

            thumbnails.forEach((thumb, thumbIndex) => {
                const isActive = thumbIndex === index;
                thumb.classList.toggle("is-active", isActive);
                thumb.setAttribute("aria-pressed", String(isActive));
            });

            const activeThumb = thumbnails[index];
            const nextImage = activeThumb.dataset.fullImage;

            if (nextImage) {
                mainImage.src = nextImage;
            }

            if (counter) {
                counter.textContent = `${index + 1} / ${thumbnails.length}`;
            }

            activeThumb.scrollIntoView({
                behavior: smooth ? "smooth" : "auto",
                block: "nearest",
                inline: "center"
            });
        };

        thumbnails.forEach((thumb, index) => {
            thumb.addEventListener("click", () => render(index, true));
        });

        previousButton?.addEventListener("click", () => {
            render((activeIndex - 1 + thumbnails.length) % thumbnails.length, true);
        });

        nextButton?.addEventListener("click", () => {
            render((activeIndex + 1) % thumbnails.length, true);
        });

        stripPrevious?.addEventListener("click", () => {
            strip?.scrollBy({ left: -240, behavior: "smooth" });
        });

        stripNext?.addEventListener("click", () => {
            strip?.scrollBy({ left: 240, behavior: "smooth" });
        });

        render(activeIndex, false);
    });
});
