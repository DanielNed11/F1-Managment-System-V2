function getCsrfToken() {
  return document.querySelector("meta[name=\"_csrf\"]")?.content || null;
}

function getCsrfHeaderName() {
  return document.querySelector("meta[name=\"_csrf_header\"]")?.content || null;
}

export function buildCsrfHeader() {
  const token = getCsrfToken();
  const headerName = getCsrfHeaderName();

  if (!token || !headerName) {
    return {};
  }

  return {
    [headerName]: token,
  };
}
