// The new post textarea
var ta = document.getElementsByTagName("textarea")[0];
// Helper for inserting text (emojis) in the textarea
function insertAtCursor (textToInsert) {
    ta.focus();
    const isSuccess = document.execCommand("insertText", false, textToInsert);

    // Firefox (non-standard method)
    if (!isSuccess) {
        // Credits to https://www.everythingfrontend.com/posts/insert-text-into-textarea-at-cursor-position.html
        // get current text of the input
        const value = ta.value;
        // save selection start and end position
        const start = ta.selectionStart;
        const end = ta.selectionEnd;
        // update the value with our text inserted
        ta.value = value.slice(0, start) + textToInsert + value.slice(end);
        // update cursor to be at the end of insertion
        ta.selectionStart = ta.selectionEnd = start + textToInsert.length;
    }
}
// Emoji click callback func
var ji = function (ev) {
    insertAtCursor(ev.target.attributes.alt.value + " ");
    ta.focus()
    //console.log(document.execCommand('insertText', false /*no UI*/, ev.target.attributes.alt.value));
}
// Enable the click for each emojis
var items = document.getElementsByClassName("ji")
for (var i = 0; i < items.length; i++) {
    items[i].addEventListener('click', ji);
}
