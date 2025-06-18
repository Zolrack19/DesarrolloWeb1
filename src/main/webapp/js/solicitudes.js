const contextPath = window.location.pathname.split("/")[1];
let solicitudes = []
let numPag = 1
let tbody
let actualizarTbody = false
let initModalForm = true

export function init(datos) {
  const pagInicio = document.getElementById("pagInicio")
  const pagFin = document.getElementById("pagFin")
  tbody = document.getElementById("tbodySolicitudes")
  let rellenarVista = null
  tbody.addEventListener("click", (event) => {
    const fila = event.target.closest("tr")
    if (fila && tbody.contains(fila)) {
      if (!rellenarVista) {
        rellenarVista = confVistaSolicitud()
      }
      rellenarVista(solicitudes.find((solicitud) => solicitud.id == fila.dataset.id))
      abrirModal("modalVerSolicitud", "contenidoVerSolicitud")
    }
  });
  
  document.getElementById("atras").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/SolicitudServlet?numPag=${numPag - 1}`)
    datos = await res.json()
    if (datos) {
      solicitudes 
      solicitudes = datos
      numPag--
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10
      llenarTabla(tbody, datos, true)
    }
  })

  document.getElementById("adelante").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/SolicitudServlet?numPag=${numPag + 1}`)
    datos = await res.json()
    if (datos) {
      solicitudes = datos
      numPag++
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10
      llenarTabla(tbody, datos, true)
    }   
  })

  if (datos) {
    solicitudes = datos
    llenarTabla(tbody, datos)
  }
  
  document.getElementById("btnNuevaSolicitud").addEventListener("click", () => {
    if (initModalForm) {
      confModalForm()
      initModalForm = false
    }
    abrirModal("modalSolicitud", "contenidoSolicitud")}
  )
  document.getElementById("btnCancelarSolicitud").addEventListener("click", () => {cerrarModal("modalSolicitud", "contenidoSolicitud")})
  
}

export function actualizar(nodo) {
  if (actualizarTbody) {
    nodo.querySelector("tbody[id='tbodySolicitudes']").innerHTML = tbody.innerHTML
    actualizarTbody = false
    tbody = null
  }
  if (nodo.querySelector("span[id='pagFin']").innerHTML !== numPag*10) {
    nodo.querySelector("span[id='pagInicio']").innerHTML = (numPag - 1)*10 + 1
    nodo.querySelector("span[id='pagFin']").innerHTML = numPag*10
  }
  initModalForm = true
}


function confModalForm() {
  function popUpCoordinador(popUp, colaboradores) {
    popUp.innerHTML = ""
    colaboradores.forEach(colaborador => {
      const li = document.createElement("li");
      li.tabIndex = 0
      li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
      li.dataset.id = colaborador.id
      li.innerHTML = `${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}`
      popUp.appendChild(li);
    });
    popUp.classList.remove("hidden")
  }
  function popUpCliente(popUp, clientes) {
    popUp.innerHTML = ""
    clientes.forEach(cliente => {
      const li = document.createElement("li");
      li.tabIndex = 0
      li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
      li.dataset.id = cliente.id
      li.innerHTML = `${cliente.razonSocial}`
      popUp.appendChild(li);
    });
    popUp.classList.remove("hidden")
  }
  confTextSearch("txtCoordinador", "popupCoordinador", `/${contextPath}/control/ColaboradorServlet?action=1&`, popUpCoordinador)
  confTextSearch("txtCliente", "popupCliente", `/${contextPath}/control/ClienteServlet?action=1&`, popUpCliente)


  document.getElementById("formSolicitud").addEventListener("submit", async function (e) {
    e.preventDefault();
    const formData = new FormData(document.getElementById("formSolicitud"))

    const txtCoordinador = document.getElementById("txtCoordinador")
    if (txtCoordinador !== null) {
      const txtCliente = document.getElementById("txtCliente")
      formData.set("coordinadorId", txtCoordinador.dataset.id)
      formData.set("clienteId", txtCliente.dataset.id)
    };
    
    await fetch(`/${contextPath}/control/SolicitudServlet&action=1`, {
      method: "POST",
      body: new URLSearchParams(formData)
    })
    .then(resp => resp.json()
    .then(data => {
      if (data.ok) {
        const solicitud = data.solicitud
        const tr = document.createElement("tr");
        tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors"
      
        tr.innerHTML = `
          <td class="p-2">${solicitud.id}</td>
          <td class="p-2">${solicitud.titulo}</td>
          <td class="p-2">${solicitud.coordinador ?? "--- --- ---"}</td>
          ${window.usuario?.rolColaborador == 'Administrador' ?
          `<td class="p-2">${solicitud.cliente ?? "--- --- ---"}</td>`
            : 
            ""
          }
          <td class="p-2">${solicitud.fechaRegistro}</td>
          <td class="p-2">${solicitud.fechaFinalizacion ?? "-- -- --"}</td>
          <td class="p-2">
            <span class="inline-block px-2 py-1 text-xs font-semibold rounded-full ${
              solicitud.estadoSolicitud === 'Pendiente' ? 'bg-yellow-100 text-yellow-700' :
              solicitud.estadoSolicitud === 'En proceso' ? 'bg-blue-100 text-blue-700' :
              solicitud.estadoSolicitud === 'Asignada' ? 'bg-indigo-100 text-indigo-700' :
              solicitud.estadoSolicitud === 'Atendida' ? 'bg-green-100 text-green-700' :
              'bg-gray-100 text-gray-700'
            }">
              ${solicitud.estadoSolicitud}
          </span>
          </td>
          <td class="p-2 text-right">⋮</td>
        `;
        tbody.insertBefore(tr, tbody.firstChild)
        if (tbody.children.length > 10) {
          tbody.lastElementChild.remove()
        }
        actualizarTbody = true
        cerrarModal("modalSolicitud", "contenidoSolicitud");
      }
    }));
  });
}

function filtrarTokens(tokens) {
  if (tokens.length === 0) return null
  const params = new URLSearchParams();
  for (let i = 0; i < tokens.length; i++) {
    if (params.size > 4) break
    if (tokens[i].length > 3) {
      params.append("token", tokens[i])
    }
  }
  return params
}

function confTextSearch(idText, idPopup, fetchURL, funcPopup) {
  const txtBuscar = document.getElementById(idText)
  if (txtBuscar === null) return;
  const popUp = document.getElementById(idPopup)

  let taskColaborador = null

  popUp.addEventListener('click', e => {
    if (e.target.tagName === 'LI') {
      txtBuscar.value = e.target.textContent
      txtBuscar.dataset.id = e.target.dataset.id
      popUp.classList.add("hidden")
    }
  });
  popUp.addEventListener("focusout", () => {
    setTimeout(() => {
      if (!popUp.contains(document.activeElement)) {
        popUp.classList.add("hidden")
      }
    }, 0);
  });
  popUp.addEventListener("keypress", e => {
    if ((e.key === 'Enter' || e.key === ' ')) {
      e.preventDefault();
      txtBuscar.value = document.activeElement.innerHTML
      txtBuscar.dataset.id = e.target.dataset.id
      popUp.classList.add("hidden")
    }
  });

  txtBuscar.addEventListener("focusout", () => {
    setTimeout(() => {
      if (popUp === document.activeElement.parentElement) return
      popUp.classList.add("hidden")
    }, 100);
  })
  txtBuscar.addEventListener("focusin", () => {
    if (popUp.hasChildNodes()) {
      popUp.classList.remove("hidden")
    }
  })

  txtBuscar.addEventListener("input", e => {
    clearTimeout(taskColaborador)
    taskColaborador = setTimeout(async () => {
      const tokens = e.target.value.trim().replace(/\s+/g, ' ').split(" ")
      const params = filtrarTokens(tokens)
      if (!params || params.size === 0) return
      
      await fetch(`${fetchURL}${params.toString()}`)
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      })
      .then(colaboradores => {
        if (colaboradores === null) {
          popUp.classList.add("hidden")
          return
        }
        funcPopup(popUp, colaboradores)
      })
    }, 400);
  })
}

function confVistaSolicitud() {
  document.getElementById("btnCerrarVerSolicitud").addEventListener("click", () => {
    cerrarModal("modalVerSolicitud", "contenidoVerSolicitud")
  })
  const verTipoSolicitud = document.getElementById("verTipoSolicitud")
  const verEstadoSolicitud = document.getElementById("verEstadoSolicitud")
  const verTituloSolicitud = document.getElementById("verTituloSolicitud")
  const verDescripcionSolicitud = document.getElementById("verDescripcionSolicitud")
  const verCoordinador = document.getElementById("verCoordinador")
  const verCliente = document.getElementById("verCliente")
  const contenedorTarjetas = document.getElementById("contenedorTarjetas")
  
  function prueba(popUp, colaboradores) {
    popUp.innerHTML = ""
    colaboradores.forEach(colaborador => {
      const li = document.createElement("li");
      li.tabIndex = 0
      li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
      li.dataset.id = colaborador.id
      li.innerHTML = `${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}`
      popUp.appendChild(li);
    });
    popUp.classList.remove("hidden")
  }
  confTextSearch("txtBuscarColaborador", "popupColaboradores", `/${contextPath}/control/ColaboradorServlet?action=1&`, prueba)

  document.getElementById("btnAsignarColaborador")?.addEventListener("click", async () => {
    const txtBuscarColaborador = document.getElementById("txtBuscarColaborador")
    console.log("ejeuctando el post al servlet");
    
    if (!txtBuscarColaborador.value.trim()) return
    const colaboradorId = txtBuscarColaborador.dataset.id
    const solicitudId = verTipoSolicitud.dataset.id
    await fetch(`/${contextPath}/control/SolicitudServlet?action=2`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({colaboradorId, solicitudId})
    })
    .then(resp => {
      if (resp.ok) {
        return resp.json()
      }
    })
    .then(colaborador => {
      const tarjetilla = document.createElement("div")
      tarjetilla.dataset.id = colaborador.id
      tarjetilla.className = "flex items-center gap-3 p-3 bg-white rounded-lg shadow-sm mb-2"
      tarjetilla.innerHTML = `
        <div class="flex-shrink-0 bg-blue-100 text-blue-600 rounded-full p-2">
          <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
          </svg>
        </div>
        <div class="text-gray-800 text-sm flex-1 min-w-0">
          <div class="font-medium truncate">
            ${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}
          </div>
          <div class="text-gray-500 text-sm">
            Código: ${colaborador.codigo}
          </div>
        </div>
      `
      contenedorTarjetas.appendChild(tarjetilla)
    })
  }) 




  return function llenarVista(solicitud) {
    verTipoSolicitud.innerHTML = solicitud.tipoSolicitud
    verTipoSolicitud.dataset.id = solicitud.id
    verEstadoSolicitud.innerHTML = solicitud.estadoSolicitud
    verTituloSolicitud.innerHTML = solicitud.titulo
    verDescripcionSolicitud.innerHTML = solicitud.descripcion
    verCoordinador.innerHTML = solicitud.coordinador ? solicitud.coordinador : "---- ----- -----"
    verCliente.innerHTML = solicitud.cliente ? solicitud.cliente : "---- ----- -----"
  }
}


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
  contenido.classList.remove("scale-100", "opacity-100");
  contenido.classList.add("scale-95", "opacity-0");
  setTimeout(() => {
    modal.classList.add("hidden");
  }, 300);
}

function llenarTabla(tbody, solicitudes, limpiar = false) {
  if (limpiar) {
    tbody.innerHTML = ""
  }
  actualizarTbody = true
  solicitudes.forEach((solicitud) => {
    const tr = document.createElement("tr");
    tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors";
    tr.dataset.id = solicitud.id

    tr.innerHTML = `
      <td class="p-2">${solicitud.id}</td>
      <td class="p-2">${solicitud.titulo}</td>
      <td class="p-2">${solicitud.coordinador ?? "--- --- ---"}</td>
      ${window.usuario?.rolColaborador == 'Administrador' ?
        `<td class="p-2">${solicitud.cliente ?? "--- --- ---"}</td>`
        : 
        ""
      }
      <td class="p-2">${solicitud.fechaRegistro}</td>
      <td class="p-2">${solicitud.fechaFinalizacion ?? "-- -- --"}</td>
      <td class="p-2">
        <span class="inline-block px-2 py-1 text-xs font-semibold rounded-full ${
          solicitud.estadoSolicitud === 'Pendiente' ? 'bg-yellow-100 text-yellow-700' :
          solicitud.estadoSolicitud === 'En proceso' ? 'bg-blue-100 text-blue-700' :
          solicitud.estadoSolicitud === 'Asignada' ? 'bg-indigo-100 text-indigo-700' :
          solicitud.estadoSolicitud === 'Atendida' ? 'bg-green-100 text-green-700' :
          'bg-gray-100 text-gray-700'
        }">
          ${solicitud.estadoSolicitud}
        </span>
      </td>
      <td class="p-2 text-right">⋮</td>
    `;
    tbody.appendChild(tr);
  });
}