let tbody

export function init(datos) {
  if (datos) {
    tbody = document.getElementById("tbodySolicitudes")
    datos.forEach((solicitud) => {
      const tr = document.createElement("tr");
      tr.className = "border-b hover:bg-gray-50";
    
      tr.innerHTML = `
        <td class="p-2">${solicitud.id}</td>
        <td class="p-2">${solicitud.titulo}</td>
        <td class="p-2">${solicitud.coordinador ?? "--- --- ---"}</td>
        <td class="p-2">${solicitud.fechaRegistro}</td>
        <td class="p-2">${solicitud.fechaFinalizacion ?? "-- -- --"}</td>
        <td class="p-2">${solicitud.estadoSolicitud}</td>
        <td class="p-2 text-right">⋮</td>
      `;
      tbody.appendChild(tr);
    });
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
        console.log(data.solicitud);
        const solicitud = data.solicitud
        const tr = document.createElement("tr");
        tr.className = "border-b hover:bg-gray-50";
      
        tr.innerHTML = `
          <td class="p-2">${solicitud.id}</td>
          <td class="p-2">${solicitud.titulo}</td>
          <td class="p-2">${solicitud.coordinador ?? "--- --- ---"}</td>
          <td class="p-2">${solicitud.fechaRegistro}</td>
          <td class="p-2">${solicitud.fechaFinalizacion ?? "-- -- --"}</td>
          <td class="p-2">${solicitud.estadoSolicitud}</td>
          <td class="p-2 text-right">⋮</td>
        `;
        tbody.appendChild(tr);

        cerrarModal("modalSolicitud", "contenidoSolicitud");
      }
    }));
  });
  
} 

export function actualizar(nodo) {
  if (tbody) {
    nodo.querySelector("tbody[id='tbodySolicitudes']").innerHTML = tbody.innerHTML
  }
}