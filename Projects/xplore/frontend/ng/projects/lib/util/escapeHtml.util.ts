export function escapeHtml(unsafe) {
  if (unsafe == null) {
    return unsafe;
  }

  if (typeof unsafe === "string") {
    return unsafe.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;").replace(/'/g, "&#039;");
  } else {
    return unsafe;
  }
}
