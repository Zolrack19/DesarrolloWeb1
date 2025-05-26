export function init() {
  const btnNuevoCliente = document.getElementById("btnNuevoCliente")
  const btnCancelarCliente = document.getElementById("btnCancelarCliente")
  
  btnNuevoCliente.addEventListener("click", () => {abrirModal("modalCliente", "contenidoCliente")})
  btnCancelarCliente.addEventListener("click", () => {cerrarModal("modalCliente", "contenidoCliente")})
  
  
  function abrirModal(idModal, idContenido) {
    const modal = document.getElementById(idModal);
    const contenido = document.getElementById(idContenido);
    modal.classList.remove("hidden");
    setTimeout(() => {
      contenido.classList.remove("scale-95", "opacity-0");
      contenido.classList.add("scale-100", "opacity-100");
    }, 10);
  }

  function cerrarModal(idModal, idContenido) {
    const modal = document.getElementById(idModal);
    const contenido = document.getElementById(idContenido);
    contenido.classList.add("scale-95", "opacity-0");
    contenido.classList.remove("scale-100", "opacity-100");
    setTimeout(() => {
      modal.classList.add("hidden");
    }, 200);
  }
  
  document.getElementById("formCliente").addEventListener("submit", function (e) {
    e.preventDefault();

    cerrarModal("modalCliente", "contenidoCliente");
  });
}