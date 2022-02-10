function selectNode(node) {
    const selection = window.getSelection();
    selection.removeAllRanges();
    const range = document.createRange();
    range.selectNode(node);
    selection.addRange(range);
}

export function copy(text, parent) {
    const span = document.createElement("span");
    span.innerText = text;
    parent.appendChild(span);
    selectNode(span);
    const result = document.execCommand("copy");
    parent.removeChild(span);
    return result;
}

export function copyButton(t, getCopyText, label, classNames) {
    return t.button({className: `${classNames} icon copy`, onClick: evt => {
        const button = evt.target;
        if (copy(getCopyText(), button)) {
            button.innerText = "Copied!";
            button.classList.remove("copy");
            button.classList.add("tick");
            setTimeout(() => {
                button.classList.remove("tick");
                button.classList.add("copy");
                button.innerText = label;
            }, 2000);
        }
    }}, label);
}
