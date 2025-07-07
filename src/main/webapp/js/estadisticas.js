const contextPath = window.location.pathname.split("/")[1];

export function init() {
  const panelCache = {}
  let pnlActual

  const contEstadistica = document.getElementById("contEstadistica")
  const pnlSolicitudes = document.getElementById("pnlSolicitudes")
  const pnlClientes = document.getElementById("pnlClientes")
  const pnlColaboradores = document.getElementById("pnlColaboradores")
  
  const mesCombo = document.getElementById("mesCombo");
  const spanAnio = document.getElementById("anioSeleccionado");
  const btnInc = document.getElementById("incrementAnio");
  const btnDec = document.getElementById("decrementAnio");

  let anio = new Date().getFullYear();
  spanAnio.textContent = anio;
  btnInc.addEventListener("click", () => {
    anio++;
    spanAnio.textContent = anio;
  });

  btnDec.addEventListener("click", () => {
    anio--;
    spanAnio.textContent = anio;
  });

  const getFechaData = () => {
    return {"mes": mesCombo.value, "anio": anio}
  }

  import(`/${contextPath}/js/estadisticaSolicitud.js`).then(({ PanelSolicitudes }) => {
    panelCache["solicitudes"] = new PanelSolicitudes(contEstadistica, getFechaData, contextPath)
    panelCache["solicitudes"].init()
    pnlActual = pnlSolicitudes
  })
  pnlSolicitudes.addEventListener("click", () => {
    if (pnlActual !== pnlSolicitudes) {
      pnlActual.className = "cursor-pointer hover:text-red-600 pb-1"
      pnlSolicitudes.className = "text-pink-600 font-semibold border-b-2 border-pink-600 pb-1"
      pnlActual = pnlSolicitudes
      panelCache["solicitudes"].init()
    }
  })

  if (pnlClientes) {
    pnlClientes.addEventListener("click", () => {
      if (!panelCache["clientes"]) {
        import(`/${contextPath}/js/estadisticaCliente.js`).then(({ PanelClientes }) => {
          panelCache["clientes"] = new PanelClientes(contEstadistica, getFechaData, contextPath)
          pnlActual.className = "cursor-pointer hover:text-red-600 pb-1"
          pnlClientes.className = "text-pink-600 font-semibold border-b-2 border-pink-600 pb-1"
          pnlActual = pnlClientes
          panelCache["clientes"].init()
        })
      } else if (pnlActual !== pnlClientes) {
        pnlActual.className = "cursor-pointer hover:text-red-600 pb-1"
        pnlClientes.className = "text-pink-600 font-semibold border-b-2 border-pink-600 pb-1"
        pnlActual = pnlClientes
        panelCache["clientes"].init()
      }
    })

    pnlColaboradores.addEventListener("click", () => {
      if (!panelCache["colaboradores"]) {
        import(`/${contextPath}/js/estadisticaColaborador.js`).then(({ PanelColaboradores }) => {
          panelCache["colaboradores"] = new PanelColaboradores(contEstadistica, getFechaData, contextPath)
          pnlActual.className = "cursor-pointer hover:text-red-600 pb-1"
          pnlColaboradores.className = "text-pink-600 font-semibold border-b-2 border-pink-600 pb-1"
          pnlActual = pnlColaboradores
          panelCache["colaboradores"].init()
        })
      } else if (pnlActual !== pnlColaboradores) {
        pnlActual.className = "cursor-pointer hover:text-red-600 pb-1"
        pnlColaboradores.className = "text-pink-600 font-semibold border-b-2 border-pink-600 pb-1"
        pnlActual = pnlColaboradores
        panelCache["colaboradores"].init()
      }
    })
  }

  
  
  

}

export function actualizar(nodo) {
  
}

