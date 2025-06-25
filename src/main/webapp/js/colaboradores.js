const contextPath = window.location.pathname.split("/")[1];

let colaboradores = []
let numPag = 1
let tbody
let initModalForm = true

export function init(datos) {
  const pagInicio = document.getElementById("pagInicio")
  const pagFin = document.getElementById("pagFin")
  tbody = document.getElementById("tbodyColaboradores")
  let rellenarModal = null
  tbody.addEventListener("click", async (e) => {
    if (e.target.classList.contains("btn-editar")) {
      const fila = e.target.closest("tr");
      if (!fila) return;
      if (initModalForm) {
        rellenarModal = confModalForm()
        initModalForm = false
      }
      rellenarModal(colaboradores.find((colaborador) => colaborador.id == fila.dataset.id))
      abrirModal("modalColaborador", "contenidoColaborador")
    } else if (e.target.classList.contains("btn-eliminar")) {
      const fila = e.target.closest("tr");
      if (!fila) return;

      await fetch("/" + contextPath + `/control/ColaboradorServlet?id=${fila.dataset.id}`, {
        method: "DELETE"
      }).then(response => {
        if (response.ok) {
          const index = colaboradores.findIndex(s => s.id == fila.dataset.id);
          if (index !== -1) {
            colaboradores.splice(index, 1);
          }
          fila.remove()
          // return response.json()
        } else {
          console.error("Error al eliminar colbaroador");
        }
      })
    }
  });

  document.getElementById("atras").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/ColaboradorServlet?numPag=${numPag - 1}`)
    datos = await res.json()
    if (datos) {
      numPag--
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10 - (10 - datos.length) 
      tbody.innerHTML = ''
      llenarTabla(datos)
    }
  })

  document.getElementById("adelante").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/ColaboradorServlet?numPag=${numPag + 1}`)
    datos = await res.json()
    if (datos) {
      numPag++
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10 - (10 - datos.length) 
      tbody.innerHTML = ''
      llenarTabla(datos)
    }   
  })

  if (datos) {
    colaboradores = datos
    llenarTabla(datos)
  } else {
    llenarTabla(colaboradores)
  }

  document.getElementById("btnNuevoColaborador").addEventListener("click", () => {
    if (initModalForm) {
      rellenarModal = confModalForm()
      initModalForm = false
    }
    abrirModal("modalColaborador", "contenidoColaborador")
  })
  document.getElementById("btnCancelarColaborador").addEventListener("click", () => {cerrarModal("modalColaborador", "contenidoColaborador")})
  
}

export function actualizar(nodo) {
  if (nodo.querySelector("span[id='pagFin']").innerHTML !== numPag*10) {
    nodo.querySelector("span[id='pagInicio']").innerHTML = (numPag - 1)*10 + 1
    nodo.querySelector("span[id='pagFin']").innerHTML = numPag*10
  }
  initModalForm = true
}

function confModalForm() {
  const ocultoDoc = document.getElementById("ocultoDoc")
  document.getElementById("tipoDocumentoId").addEventListener("change", (e) => {
    switch (e.target.value) {
      case "1": //dni
        ocultoDoc.innerHTML = "DNI debe tener 8 dígitos"
        break;
      case "2": //ruc
        ocultoDoc.innerHTML = "El RUC debe tener 11 dígitos"
        break;
      case "3": // carnet
      case "4": // pasaporte
        ocultoDoc.innerHTML = "Asegúrese de que este campo no tenga más de 15 caracteres."
        break;
      default:
        break;
    }
  })

  document.getElementById("formColaborador").addEventListener("submit", async function (e) {
    e.preventDefault();
    const formData = new FormData(document.getElementById("formColaborador"))
    let stop = false;

    const documentoId = formData.get("tipoDocumentoId")
    const documento = formData.get("documento").trim()
    const nombre = formData.get("nombre").trim()
    const apellidoP = formData.get("apellidoP").trim()
    const apellidoM = formData.get("apellidoM").trim()

    if (nombre.length === 0 || apellidoP.length === 0 || apellidoM.length === 0
    || documento.length === 0)  {
      stop = true
    }

    switch (documentoId) {
      case "1":
        if (/^\d{8}$/.test(documento)) {
          ocultoDoc.classList.add("hidden")
        } else {
          ocultoDoc.classList.remove("hidden")
          stop = true
        }
        break;
      case "2":
        if (/^\d{11}$/.test(documento)) {
          ocultoDoc.classList.add("hidden")
        } else {
          ocultoDoc.classList.remove("hidden")
          stop = true
        }
        break;
      case "3":
      case "4":
        const ln = documento.length;
        if (ln >= 4 && ln < 15) {
          ocultoDoc.classList.add("hidden")
        } else {
          ocultoDoc.classList.remove("hidden")
          stop = true
        }
        break;
      default:
        break;
    }


    if (stop) return;
    formData.set("documento", documento)
    formData.set("nombre", nombre)
    formData.set("apellidoP", apellidoP)
    formData.set("apellidoM", apellidoM)

    await fetch("/" + contextPath + `/control/ColaboradorServlet`, {
      method: "POST",
      body: new URLSearchParams(formData)
    })
    .then(resp => resp.json())
    .then(data => {
      if (data.ok) {
        const colaborador = []
        colaborador.push(data.colaborador)
        llenarTabla(colaborador, false)
        if (colaboradores.length > 10) {
          const ultimaFila = tbody.lastElementChild
          const index = colaboradores.findIndex(s => s.id == ultimaFila.dataset.id);
          if (index !== -1) {
            colaboradores.splice(index, 1);
          }
          tbody.lastElementChild.remove()
        }
        colaboradores.push(data.colaborador)
        cerrarModal("modalColaborador", "contenidoColaborador");
      }
    })
  });

  return (colaborador) => {
    console.log(colaborador);
  }
}


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

function llenarTabla(colaboradores, append = true) {
  colaboradores.forEach((colaborador) => {
    const tr = document.createElement("tr")
    tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors"
    tr.dataset.id = colaborador.id
    tr.innerHTML = `
      <td class="p-2">${colaborador.codigo}</td>
      <td class="p-2">${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}</td>
      <td class="p-2"><strong>${colaborador.tipoDocumento}</strong> ${colaborador.numeroDocumento}</td>
      <td class="p-2">${colaborador.rolColaborador}</td>
      <td class="p-2">${colaborador.email}</td>
      <td class="p-2 w-full flex justify-between items-start text-sm">
        ${colaborador.solicitudesActivas}
          ${colaborador.solicitudesActivas <= 1 ? `
          <span class="inline-block px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-700">
            Muy disponible
          </span>` :  
          colaborador.solicitudesActivas == 2 ? `<span class="inline-block px-2 py-1 text-xs font-semibold rounded-full bg-lime-100 text-lime-700">
            Disponible
          </span>` : 
          colaborador.solicitudesActivas == 3 ? `<span class="inline-block px-2 py-1 text-xs font-semibold rounded-full bg-yellow-100 text-yellow-700">
            Moderado
          </span>` :  
          colaborador.solicitudesActivas == 4 ? `<span class="inline-block px-2 py-1 text-xs font-semibold rounded-full bg-orange-100 text-orange-700">
            Casi al límite
          </span>` : `<span class="inline-block px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-700">
            Sin disponibilidad
          </span>` 
        }
      </td>
      <td class="p-2 text-right">
        <button class="popup-btn cursor-pointer w-7 h-7 flex items-center justify-center rounded hover:bg-gray-100">
          ⋮
        </button>
        <div tabindex="-1" class="popup-menu absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded shadow-md hidden z-10">
          <button class="btn-editar block w-full px-4 py-2 text-left text-sm hover:bg-gray-100">Editar</button>
          <button class="btn-eliminar block w-full px-4 py-2 text-left text-sm hover:bg-gray-100 text-red-600">Eliminar</button>
        </div>
      </td>
    `;

    const popupBtn = tr.querySelector('.popup-btn');
    const popupMenu = tr.querySelector('.popup-menu');
    
    popupBtn.addEventListener('click', e => {
      e.stopPropagation();
    
      document.querySelectorAll('.popup-menu').forEach(menu => {
        if (menu !== popupMenu) {
          menu.classList.add('hidden');
        }
      });
      popupMenu.classList.remove('hidden');
      popupMenu.focus();
    });
    
    popupMenu.addEventListener('blur', () => {
      popupMenu.classList.add('hidden');
    });
    
    popupMenu.addEventListener('mousedown', (e) => {
      e.preventDefault();
    });

    if (append) {
      tbody.appendChild(tr);
    } else {
      tbody.insertBefore(tr, tbody.firstChild)
    }
  });
}