let tbody

export function init(datos) {
  console.log(datos);
  if (datos) {
    tbody = document.getElementById("tbodyColaboradores")
    datos.forEach((colaborador) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="p-2">${colaborador.codigo}</td>
        <td class="p-2">${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}</td>
        <td class="p-2"><strong>${colaborador.tipoDocumento}</strong> ${colaborador.numeroDocumento}</td>
        <td class="p-2">${colaborador.rolColaborador}</td>
        <td class="p-2">${colaborador.email}</td>
        <td class="p-2">${colaborador.solicitudesActivas}</td>
        <td class="p-2 text-right">⋮</td>
      `;
      tbody.appendChild(tr);
    });
  }

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