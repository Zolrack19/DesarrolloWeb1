let tbody

function llenarTabla(colaboradores, limpiar = false) {
  if (limpiar) {
    tbody.innerHTML = ""
  }
  colaboradores.forEach((colaborador) => {
    const tr = document.createElement("tr")
    tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors"

    tr.innerHTML = `
      <td class="p-2">${colaborador.codigo}</td>
      <td class="p-2">${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}</td>
      <td class="p-2"><strong>${colaborador.tipoDocumento}</strong> ${colaborador.numeroDocumento}</td>
      <td class="p-2">${colaborador.rolColaborador}</td>
      <td class="p-2">${colaborador.email}</td>
      <td class="p-2">${colaborador.solicitudesActivas}</td>
      <td class="p-2 text-right">⋮</td>
    `;
    tbody.appendChild(tr)
  });
}


export function init(datos) {
  
  if (datos) {
    tbody = document.getElementById("tbodyColaboradores")
    llenarTabla(datos)
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

export function actualizar(nodo) {
  if (tbody) {
    nodo.querySelector("tbody[id='tbodyColaboradores']").innerHTML = tbody.innerHTML
    tbody = null
  }
}