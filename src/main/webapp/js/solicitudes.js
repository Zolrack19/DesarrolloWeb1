const contextPath = window.location.pathname.split("/")[1];

let numPag = 1
let tbody

export function init(datos) {
  const pagInicio = document.getElementById("pagInicio")
  const pagFin = document.getElementById("pagFin")

  document.getElementById("atras").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/SolicitudServlet?numPag=${numPag - 1}`)
    datos = await res.json()
    if (datos) {
      numPag--
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10
      if (!tbody) {
        tbody = document.getElementById("tbodySolicitudes")
      }

      llenarTabla(datos, true)
    }
  })

  document.getElementById("adelante").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/SolicitudServlet?numPag=${numPag + 1}`)
    datos = await res.json()
    if (datos) {
      numPag++
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10
      if (!tbody) {
        tbody = document.getElementById("tbodySolicitudes")
      }
      llenarTabla(datos, true)
    }   
  })

  if (datos) {
    tbody = document.getElementById("tbodySolicitudes")
    llenarTabla(datos)
  }

  confInputText();

  const btnNuevaSolicitud = document.getElementById("btnNuevaSolicitud")
  const btnCancelarSolicitud = document.getElementById("btnCancelarSolicitud")
  
  btnNuevaSolicitud.addEventListener("click", () => {abrirModal("modalSolicitud", "contenidoSolicitud")})
  btnCancelarSolicitud.addEventListener("click", () => {cerrarModal("modalSolicitud", "contenidoSolicitud")})

  document.getElementById("formSolicitud").addEventListener("submit", async function (e) {
    e.preventDefault();
    const formData = new FormData(document.getElementById("formSolicitud"))

    await fetch("/" + contextPath + `/control/SolicitudServlet`, {
      method: "POST",
      body: new URLSearchParams(formData)
    })
    .then(resp => resp.json()
    .then(data => {
      if (data.ok) {
        const solicitud = data.solicitud
        if (!tbody) {
          tbody = document.getElementById("tbodySolicitudes")
        }
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
        tbody.insertBefore(tr, tbody.firstChild);

        cerrarModal("modalSolicitud", "contenidoSolicitud");
      }
    }));
  });
  
}

export function actualizar(nodo) {
  if (tbody) {
    nodo.querySelector("tbody[id='tbodySolicitudes']").innerHTML = tbody.innerHTML
    tbody = null
  }
  if (nodo.querySelector("span[id='pagFin']").innerHTML !== numPag*10) {
    nodo.querySelector("span[id='pagInicio']").innerHTML = (numPag - 1)*10 + 1
    nodo.querySelector("span[id='pagFin']").innerHTML = numPag*10
  }
}


function confInputText() {
  const txtCoordinador = document.getElementById("txtCoordinador")
  if (txtCoordinador === null) return;

  const txtCliente = document.getElementById("txtCliente")
  const popupCliente = document.getElementById("popupCliente")
  const popupCoordinador = document.getElementById("popupCoordinador")

  let taskCliente = null
  let taskCoordinador = null

  txtCoordinador.addEventListener("focusout", () => {
    setTimeout(() => {
      if (popupCoordinador === document.activeElement.parentElement) return
      popupCoordinador.classList.add("hidden")
    }, 100);
  })
  txtCoordinador.addEventListener("focusin", () => {
    if (popupCoordinador.hasChildNodes()) {
      popupCoordinador.classList.remove("hidden")
    }
  })

  txtCliente.addEventListener("focusout", () => {
    setTimeout(() => {
      if (popupCliente === document.activeElement.parentElement) return
      popupCliente.classList.add("hidden")
    }, 100);
  })
  txtCliente.addEventListener("focusin", () => {
    if (popupCliente.hasChildNodes()) {
      popupCliente.classList.remove("hidden")
    }
  })

  popupCoordinador.addEventListener('click', e => {
    if (e.target.tagName === 'LI') {
      txtCoordinador.value = e.target.textContent
    }
  });
  popupCoordinador.addEventListener("focusout", e => {
    setTimeout(() => {
      if (!popupCoordinador.contains(document.activeElement)) {
        console.log('La lista completa perdió el foco');
        popupCoordinador.classList.add("hidden")
      }
    }, 0);
  });
  popupCoordinador.addEventListener("keypress", e => {
    if ((e.key === 'Enter' || e.key === ' ')) {
      e.preventDefault();
      txtCoordinador.value = document.activeElement.innerHTML
      txtCliente.focus()
      popupCoordinador.classList.add("hidden")
    }
  });

  popupCliente.addEventListener('click', e => {
    if (e.target.tagName === 'LI') {
      txtCliente.value = e.target.textContent
    }
  });
  popupCliente.addEventListener("focusout", e => {
    setTimeout(() => {
      if (!popupCliente.contains(document.activeElement)) {
        console.log('La lista completa perdió el foco');
        popupCliente.classList.add("hidden")
      }
    }, 0);
  });

  popupCliente.addEventListener("keypress", e => {
    if ((e.key === 'Enter' || e.key === ' ')) {
      e.preventDefault();
      txtCliente.value = document.activeElement.innerHTML
      popupCliente.classList.add("hidden")
    }
  });

  txtCoordinador.addEventListener("input", e => {
    clearTimeout(taskCoordinador)
    taskCoordinador = setTimeout(async () => {
      const tokens = e.target.value.trim().replace(/\s+/g, ' ').split(" ")
      if (tokens.length === 0) return
      const params = new URLSearchParams();
      for (let i = 0; i < tokens.length; i++) {
        if (params.size > 4) break
        if (tokens[i].length > 3) {
          params.append("token", tokens[i])
        }
      }
      if (params.size === 0) return
      await fetch("/" + contextPath + `/control/ColaboradorServlet?action=1&` + params.toString())
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      })
      .then(colaboradores => {
        if (colaboradores === null) {
          popupCoordinador.classList.add("hidden")
          return
        }
        popupCoordinador.innerHTML = ""
        colaboradores.forEach(colaborador => {
          const li = document.createElement("li");
          li.tabIndex = 0
          li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
          li.dataset.id = colaborador.id
          li.innerHTML = `${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}`
          popupCoordinador.appendChild(li);
        });
        popupCoordinador.classList.remove("hidden")
      })
    }, 400);
  })

  txtCliente.addEventListener("input", e => {
    clearTimeout(taskCliente)
    taskCliente = setTimeout(async () => {
      const tokens = e.target.value.trim().replace(/\s+/g, ' ').split(" ")
      if (tokens.length === 0) return
      const params = new URLSearchParams();
      for (let i = 0; i < tokens.length; i++) {
        if (params.size > 4) break
        if (tokens[i].length > 3) {
          params.append("token", tokens[i])
        }
      }
      if (params.size === 0) return
      await fetch("/" + contextPath + `/control/ClienteServlet?action=1&` + params.toString())
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      })
      .then(clientes => {
        if (clientes === null) {
          popupCliente.classList.add("hidden")
          return
        }
        popupCliente.innerHTML = ""
        clientes.forEach(cliente => {
          const li = document.createElement("li");
          li.tabIndex = 0
          li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
          li.dataset.id = cliente.id
          li.innerHTML = `${cliente.razonSocial}`
          popupCliente.appendChild(li);
        });
        popupCliente.classList.remove("hidden")
      })
    }, 400);
  })

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

function llenarTabla(solicitudes, limpiar = false) {
  if (limpiar) {
    tbody.innerHTML = ""
  }
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