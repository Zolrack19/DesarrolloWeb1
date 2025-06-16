const contextPath = window.location.pathname.split("/")[1];

let numPag = 1
let tbody

function llenarTabla(solicitudes, limpiar = false) {
  if (limpiar) {
    tbody.innerHTML = ""
  }
  solicitudes.forEach((solicitud) => {
    const tr = document.createElement("tr");
    tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors";

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

  const btnNuevaSolicitud = document.getElementById("btnNuevaSolicitud")
  const btnCancelarSolicitud = document.getElementById("btnCancelarSolicitud")
  
  btnNuevaSolicitud.addEventListener("click", () => {abrirModal("modalSolicitud", "contenidoSolicitud")})
  btnCancelarSolicitud.addEventListener("click", () => {cerrarModal("modalSolicitud", "contenidoSolicitud")})

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

  document.getElementById("formSolicitud").addEventListener("submit", async function (e) {
    e.preventDefault();
    const formData = new FormData(document.getElementById("formSolicitud"))

    console.log("ejecutando el fetch");
    const contextPath = window.location.pathname.split("/")[1];
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