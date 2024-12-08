import { Directive, DirectiveBinding } from "vue";

interface ExHTMLElement extends HTMLElement {
  resizeListener: EventListener;
}

/** 自适应树形表格高度 */
const setTableHeight = (el: ExHTMLElement, binding: DirectiveBinding) => {
  const top = el.offsetTop;
  const bottom = binding?.value?.bottom || 28;
  const pageHeight = window.innerHeight;
  el.style.height = `${pageHeight - top - bottom}px`;
  el.style.overflowY = "auto";
};

const resizeDirective: Directive<ExHTMLElement> = {
  mounted(el, binding) {
    el.resizeListener = () => {
      requestAnimationFrame(() => {
        setTableHeight(el, binding);
      });
    };

    setTableHeight(el, binding);

    window.addEventListener("resize", el.resizeListener);
  },
  unmounted(el) {
    window.removeEventListener("resize", el.resizeListener);
  },
  updated(el, binding) {
    requestAnimationFrame(() => {
      setTableHeight(el, binding);
    });
  }
};

export default resizeDirective;
