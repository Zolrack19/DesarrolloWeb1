export function init() {
  
  const btnNuevoColaborador = document.getElementById("btnNuevoColaborador")
  const btnCancelarColaborador = document.getElementById("btnCancelarColaborador")

  
  btnNuevoColaborador.addEventListener("click", () => {abrirModal("modalColaborador", "contenidoColaborador")})
  btnCancelarColaborador.addEventListener("click", () => {cerrarModal("modalColaborador", "contenidoColaborador")})

  function abrirModal(modalId, contenidoId) {
    const modal = document.getElementById(modalId);
    const contenido = document.getElementById(contenidoId);
    modal.classList.remove('hidden');
    setTimeout(() => {
      contenido.classList.remove('scale-95', 'opacity-0');
      contenido.classList.add('scale-100', 'opacity-100');
    }, 10);
  }

  function cerrarModal(modalId, contenidoId) {
    const modal = document.getElementById(modalId);
    const contenido = document.getElementById(contenidoId);
    contenido.classList.remove('scale-100', 'opacity-100');
    contenido.classList.add('scale-95', 'opacity-0');
    setTimeout(() => {
      modal.classList.add('hidden');
    }, 300);
  }

  document.getElementById("formColaborador").addEventListener("submit", function (e) {
    e.preventDefault();
    cerrarModal("modalColaborador", "contenidoColaborador");
  });
}