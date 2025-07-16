const contextPath = window.location.pathname.split("/")[1];
let solicitudes = []
let numPag = 1
let tbody
let initModalForm = true
let confReinicioTabla = false
let app = null

export function init(datos, appContexto = null) {
  if (app === null && appContexto !== null) {
    app = appContexto
  }
  const pagInicio = document.getElementById("pagInicio")
  const pagFin = document.getElementById("pagFin")
  tbody = document.getElementById("tbodySolicitudes")
  let rellenarVista = null
  let rellenarModal = null
  tbody.addEventListener("click", async (e) => {
    if (e.target.classList.contains("btn-ver-detalles")) {
      const fila = e.target.closest("tr");
      if (!fila) return;
      if (!rellenarVista) {
        rellenarVista = confVistaSolicitud()
      }
      rellenarVista(solicitudes.find(s => s.id == fila.dataset.id))
      abrirModal("modalVerSolicitud", "contenidoVerSolicitud")

    } else if (e.target.classList.contains("btn-editar")) {
      const fila = e.target.closest("tr");
      if (!fila) return;
      if (initModalForm) {
        rellenarModal = confModalForm()
        initModalForm = false
      }
      rellenarModal(solicitudes.find(s => s.id == fila.dataset.id))
      abrirModal("modalSolicitud", "contenidoSolicitud")
    } else if (e.target.classList.contains("btn-eliminar")) {
      const fila = e.target.closest("tr");
      if (!fila || !confirm("¿Está seguro que quiere elimminar esta solicitud?")) return;

      await fetch(`/${contextPath}/control/SolicitudServlet?id=${fila.dataset.id}`, {
        method: "DELETE"
      }).then(response => {
        if (response.ok) {
          const index = solicitudes.findIndex(s => s.id == fila.dataset.id);
          if (index !== -1) {
            solicitudes.splice(index, 1);
          }
          fila.remove()
          return response.json()
        } else {
          console.error("Error al eliminar solicitud");
        }
      })
    }
  });
  
  document.getElementById("atras").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/SolicitudServlet?numPag=${numPag - 1}`)
    const data = await res.json()
    if (data.ok) {
      solicitudes = data.solicitudes
      numPag--
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10 - (10 - datos.length) 
      tbody.innerHTML = ''
      solicitudes.forEach(solicitud => {
        tbody.appendChild(crearFila(solicitud));
      });
    } else {
      alert(data.error)
    }
  })

  document.getElementById("adelante").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/SolicitudServlet?numPag=${numPag + 1}`)
    const data = await res.json()
    if (data.ok) {
      solicitudes = data.solicitudes
      numPag++
      pagInicio.innerHTML = (numPag - 1)*10 + 1
      pagFin.innerHTML = numPag*10 - (10 - datos.length) 
      tbody.innerHTML = ''
      solicitudes.forEach(solicitud => {
        tbody.appendChild(crearFila(solicitud));
      });
    } else {
      alert(data.error)
    }
  })
  
  if (datos) {
    solicitudes = datos
  }
  solicitudes.forEach(solicitud => {
    tbody.appendChild(crearFila(solicitud));
  });
  
  if (document.getElementById("btnNuevaSolicitud") !== null) {
    document.getElementById("btnNuevaSolicitud").addEventListener("click", () => {
      if (initModalForm) {
        rellenarModal = confModalForm()
        initModalForm = false
      }
      abrirModal("modalSolicitud", "contenidoSolicitud")}
    )
  }

  if (confReinicioTabla) {
    const btnReinicarTabla = document.getElementById("btnReinicarTabla")
    btnReinicarTabla.addEventListener("click", async () => {
      fetch(`/${contextPath}/control/SolicitudServlet?numPag=1`)
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      }).then(data => {
        if (data.ok) {
          solicitudes = data.solicitudes
          tbody.innerHTML = ""
          solicitudes.forEach(solicitud => {
            tbody.appendChild(crearFila(solicitud));
          });
        } else {
          alert(data.error)
        }
      })
      
      btnReinicarTabla.classList.add("hidden")
    })
    confReinicioTabla = false
  }
}

export function actualizar(nodo) {
  if (!confReinicioTabla) {
    nodo.querySelector("button[id='btnReinicarTabla']").classList.remove("hidden")
    confReinicioTabla = true
  }
  if (nodo.querySelector("span[id='pagFin']").innerHTML !== numPag*10) {
    nodo.querySelector("span[id='pagInicio']").innerHTML = (numPag - 1)*10 + 1
    nodo.querySelector("span[id='pagFin']").innerHTML = numPag*10
  }
  initModalForm = true
  tbody = null
}


function confModalForm() {
  let actualizar = false
  let solicitudId = null

  const cbxTipoSolicitud = document.getElementById("cbxTipoSolicitud")
  const txtTitulo = document.getElementById("txtTitulo")
  const txtDescripcion = document.getElementById("txtDescripcion")
  
  const txtCoordinador = document.getElementById("txtCoordinador")
  const txtCliente = document.getElementById("txtCliente")

  if (txtCoordinador !== null) {
    const popupCoordinador = document.getElementById("popupCoordinador")
    const popupCliente = document.getElementById("popupCliente")

    confTextSearch(txtCoordinador, popupCoordinador, `/${contextPath}/control/ColaboradorServlet?action=1&estricto=true&`, (colaboradores) => {
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

    confTextSearch(txtCliente, popupCliente, `/${contextPath}/control/ClienteServlet?action=1&`, (clientes) => {
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
  }

  document.getElementById("formSolicitud").addEventListener("submit", async function (e) {
    e.preventDefault();
    const tipoSolicitudId = cbxTipoSolicitud.value
    const titulo = txtTitulo.value.trim()
    const descripcion = txtDescripcion.value.trim()
    const campos = {"id": solicitudId, tipoSolicitudId, titulo, descripcion}

    if (txtCoordinador !== null) {
      const txtCliente = document.getElementById("txtCliente")
      campos.coordinadorId = txtCoordinador.dataset.id
      campos.clienteId = txtCliente.dataset.id
    };
    await fetch(`/${contextPath}/control/SolicitudServlet`, {
      method: actualizar ? "PUT" : "POST",
      headers: {
        "Content-Type": actualizar ? "application/json" : "application/x-www-form-urlencoded",
      },
      body: actualizar ? JSON.stringify(campos) : new URLSearchParams(campos)
    })
    .then(resp => resp.json()
    .then(data => {
      if (data.ok) {
        if (actualizar) {
          const fila = tbody.querySelector(`tr[data-id='${solicitudId}']`)
          fila.replaceWith(crearFila(data.solicitud))
          const index = solicitudes.findIndex(s => s.id == solicitudId);
          solicitudes[index] = data.solicitud
        } else {
          tbody.insertBefore(crearFila(data.solicitud), tbody.firstChild)
          if (solicitudes.length > 10) {
            const ultimaFila = tbody.lastElementChild
            const index = solicitudes.findIndex(s => s.id == ultimaFila.dataset.id);
            if (index !== -1) {
              solicitudes.splice(index, 1);
            }
            
            tbody.lastElementChild.remove()
          }
          solicitudes.push(data.solicitud)
        }
        cerrarModal("modalSolicitud", "contenidoSolicitud");
      }
    }));
  });

  document.getElementById("btnCancelarSolicitud").addEventListener("click", () => {
    cbxTipoSolicitud.value = ""
    txtDescripcion.value = ""
    txtTitulo.value = ""

    if (txtCoordinador !== null) {
      txtCoordinador.value = ""
      txtCliente.value = ""
      txtCoordinador.disabled = false
      txtCliente.disabled = false
    }

    actualizar = false
    solicitudId = null

    cerrarModal("modalSolicitud", "contenidoSolicitud")
  })

  return (solicitud) => {
    cbxTipoSolicitud.value = ""
    txtDescripcion.value = solicitud.descripcion
    txtTitulo.value = solicitud.titulo
    
    if (txtCoordinador !== null) {
      txtCoordinador.value = solicitud.coordinador
      txtCliente.value = solicitud.cliente
      txtCoordinador.disabled = true
      txtCliente.disabled = true
    }

    actualizar = true
    solicitudId = solicitud.id
  }
}

function confTextSearch(txtBuscar, popUp, fetchURL, funcPopup) {

  let task = null

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
    clearTimeout(task)
    task = setTimeout(async () => {
      const txtTokens = e.target.value.trim().replace(/\s+/g, ' ')
      // const params = filtrarTokens(tokens)
      if (!txtTokens || txtTokens.length === 0) return
      
      await fetch(`${fetchURL}txtTokens=${encodeURIComponent(txtTokens)}`)
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      })
      .then(data => {
        if (data.ok) {
          let resultados
          if (data?.colaboradores) {
            resultados = data.colaboradores
          } else {
            resultados = data.clientes
          }
          if (resultados === null) {
            popUp.classList.add("hidden")
            return
          }
          funcPopup(resultados)
        } else {
          alert(data.error)
        }
      })
    }, 400);
  })
}

function confVistaSolicitud() {

  const caraInfoSolicitud = document.getElementById("caraInfoSolicitud") 
  const vistaAtras = document.getElementById("vistaAtras") 
  
  const verTipoSolicitud = caraInfoSolicitud.querySelector("span[id='verTipoSolicitud']")
  const verEstadoSolicitud = caraInfoSolicitud.querySelector("span[id='verEstadoSolicitud']")
  const verTituloSolicitud = caraInfoSolicitud.querySelector("span[id='verTituloSolicitud']")
  const verDescripcionSolicitud = caraInfoSolicitud.querySelector("p[id='verDescripcionSolicitud']")
  const verCoordinador = caraInfoSolicitud.querySelector("span[id='verCoordinador']")
  const verCliente = caraInfoSolicitud.querySelector("span[id='verCliente']")

  
  const caraFormActividad = document.getElementById("caraFormActividad")
  const txtHoraInicio = caraFormActividad.querySelector("input[id='horaInicio']")
  const txtHoraFin = caraFormActividad.querySelector("input[id='horaFin']")
  const txtDescripcionInforme = caraFormActividad.querySelector("textarea[id='descripcionInforme']")


  const miniFormDerecho = document.getElementById("miniFormDerecho")
  const contenedorTarjetas = document.getElementById("contenedorTarjetas")
  const tituloContenedorTarjetas = document.getElementById("tituloContenedorTarjetas")
  const btnComenazarAtencion = document.getElementById("btnComenazarAtencion")
  const contenedorTarjetas2 = document.createElement("div")
  contenedorTarjetas2.className = "rounded-lg p-4 min-h-[40vh] max-h-[50vh] overflow-y-auto bg-gray-50"

  const estiloSimple = "tarjetilla flex items-center gap-3 p-3 bg-white rounded-lg shadow-sm mb-2"
  const estiloResaltante = "tarjetilla flex items-center gap-3 p-3 bg-yellow-100 border border-yellow-400 shadow-md rounded-lg p-3 mb-2"

  let solicitudActual = null
  let colaboradorSeleccionadoId = null

  if (btnComenazarAtencion) {
    btnComenazarAtencion.addEventListener("click", async () => {
      await fetch(`/${contextPath}/control/SolicitudServlet`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"action": "2", "solicitudId": solicitudActual.id, "estadoId": `${solicitudActual.estadoSolicitud === "Pendiente" ? "3": "4"}`})
      }).then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      }).then(data => {
        if (data.ok) {
          
        } else {
          alert(data.error)
        }
      })
    })
  }


  caraFormActividad.addEventListener("submit", async (e) => {
    e.preventDefault()
    const horaInicio = txtHoraInicio.value
    const horaFin = txtHoraFin.value
    const descripcion = txtDescripcionInforme.value
    
    await fetch(`/${contextPath}/control/ActividadRealizadaServlet`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({"solicitudId": solicitudActual.id, "colaboradorId": colaboradorSeleccionadoId, horaInicio, horaFin, descripcion})
    }).then(resp => {
      if (resp.ok) {
        return resp.json()
      }
    }).then(data => {
      if (data.ok) {
        contenedorTarjetas2.insertBefore(crearTarjetilla(data.actividad.id, data.actividad.fechaEmision, `Id: ${data.actividad.id}`, false),
          contenedorTarjetas2.firstChild
        )
      } else {
        alert(data.error)
      }
    })
  })


  vistaAtras.addEventListener("click", () => {
    tituloContenedorTarjetas.innerHTML = "Colaboradores asignados"
    contenedorTarjetas2.replaceWith(contenedorTarjetas)

    vistaAtras.classList.add("hidden")
    caraFormActividad.classList.add("hidden")
    caraInfoSolicitud.classList.remove("hidden")
    if (miniFormDerecho) {
      miniFormDerecho.classList.remove("hidden")
    }
    txtDescripcionInforme.value = ""
    txtHoraInicio.value = ""
    txtHoraFin.value = ""
    contenedorTarjetas2.innerHTML = "" // Para una rápida implementación
  })

  const crearTarjetilla = (id, textoPrincipal, textSecundario, conPopUp) => {
    const tarjetilla = document.createElement("div")
    tarjetilla.dataset.id = id
    tarjetilla.className = "tarjetilla flex items-center gap-3 p-3 bg-white rounded-lg shadow-sm mb-2"
    tarjetilla.innerHTML = `
      <div class="flex-shrink-0 bg-blue-100 text-blue-600 rounded-full p-2">
        ${conPopUp ? `
          <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
          </svg>
        `
        : `
          <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
            <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
          </svg>
        `
        }
        
      </div>
      <div class="text-gray-800 text-sm flex-1 min-w-0">
        <div class="font-medium truncate">
          ${textoPrincipal}
        </div>
        <div class="text-gray-500 text-sm">
          ${textSecundario}
        </div>
      </div>
      ${!conPopUp ? "" : 
      `
        <div class="relative">
          <div class="popup-btn text-center w-5 cursor-pointer hover:bg-gray-200">
            ⋮
          </div>
          <div tabindex="-1" class="popup-menu absolute right-0 mt-2 w-[200px] bg-white border border-gray-200 rounded shadow-md hidden z-10">
            <button class="btn-ver-tareas block w-full px-4 py-2 text-left text-sm hover:bg-gray-100">Ver actividades</button>
            <button class="btn-asignar-coordinador block w-full px-4 py-2 text-left text-sm hover:bg-gray-100 text-black-600">Asignar como coordinador</button>
            <button class="btn-eliminar-asignacion block w-full px-4 py-2 text-left text-sm hover:bg-gray-100 text-red-600">Desasignar colaborador</button>
          </div>
        </div>
      `
      }
    `
    if (!conPopUp) return tarjetilla

    const popupBtn = tarjetilla.querySelector('.popup-btn');
    const popupMenu = tarjetilla.querySelector('.popup-menu');
    
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

    return tarjetilla
  }

  contenedorTarjetas.addEventListener("click", async (e) => {
    if (e.target.classList.contains("btn-ver-tareas")) {
      const tarjeta = e.target.closest("div.tarjetilla")
      if (!tarjeta) return;

      setTimeout(async () => {
        await fetch(`/${contextPath}/control/ActividadRealizadaServlet?solicitudId=${solicitudActual.id}&colaboradorId=${tarjeta.dataset.id}`)
        .then(resp => {
          if (resp.ok) {
            return resp.json()
          }
        })
        .then(data => {

          if (data.ok) {
            data.actividades.forEach(actividad => {
              contenedorTarjetas2.appendChild(crearTarjetilla(actividad.id, actividad.fechaEmision, `Id: ${actividad.id}`, false))
            });
  
            caraInfoSolicitud.classList.add("hidden")
            if (miniFormDerecho) {
              miniFormDerecho.classList.add("hidden")
            }
            vistaAtras.classList.remove("hidden")
            caraFormActividad.classList.remove("hidden")
            
            colaboradorSeleccionadoId = tarjeta.dataset.id
      
            tituloContenedorTarjetas.innerHTML = "Actividades realizadas"
            contenedorTarjetas.replaceWith(contenedorTarjetas2)
          } else {
            alert(data.error)
          }

        })
      }, 0);
    } else if (e.target.classList.contains("btn-asignar-coordinador")) {
      const tarjeta = e.target.closest("div.tarjetilla")
      if (!tarjeta || !confirm("¿Está seguro que quiere asignar como coordinador de la solicitud a este colaborador?")) return;
      
      await fetch(`/${contextPath}/control/SolicitudServlet`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({"action": "1", "solicitudId": solicitudActual.id, "colaboradorId": Number.parseInt(tarjeta.dataset.id)})
      }).then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      }).then(data => {
        if (data.ok) {
          const index = solicitudes.findIndex(s => s.id = data.solicitud.id)
          solicitudes[index] = data.solicitud
          verCoordinador.innerHTML = data.solicitud.coordinador
          
          const antiguo = contenedorTarjetas.querySelector("div.tarjetilla[data-coor='true']")
          if (antiguo) {
            antiguo.classList = estiloSimple
            antiguo.removeAttribute("data-coor")
          }
          tarjeta.dataset.coor = true
          tarjeta.className = estiloResaltante
        }
      })
    } else if (e.target.classList.contains("btn-eliminar-asignacion")) {
      const tarjeta = e.target.closest("div.tarjetilla")
      if (!tarjeta || !confirm("¿Está seguro que quiere quitar a este colaborador de la solicitud?")) return;

      await fetch(`/${contextPath}/control/AsignacionServlet?colaboradorId=${tarjeta.dataset.id}&solicitudId=${solicitudActual.id}`, {
        method: "DELETE"
      }).then(response => {
        if (response.ok) {
          tarjeta.remove()
        } else {
          console.error("Error al eliminar asignación");
        }
      })
    }
  })


  document.getElementById("btnCerrarVerSolicitud").addEventListener("click", () => {
    contenedorTarjetas.innerHTML = ""
    solicitudActual = null
    colaboradorSeleccionadoId = null
    if (btnComenazarAtencion) {
      btnComenazarAtencion.classList.remove("hidden")
    }
    cerrarModal("modalVerSolicitud", "contenidoVerSolicitud")
  })

  const txtBuscarColaborador = document.getElementById("txtBuscarColaborador")
  if (txtBuscarColaborador !== null) {
    const popupColaboradores = document.getElementById("popupColaboradores")
  
    confTextSearch(txtBuscarColaborador, popupColaboradores, `/${contextPath}/control/ColaboradorServlet?action=1&estricto=true&`, (colaboradores) => {
      popupColaboradores.innerHTML = ""
      colaboradores.forEach(colaborador => {
        const li = document.createElement("li");
        li.tabIndex = 0
        li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
        li.dataset.id = colaborador.id
        li.innerHTML = `${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}`
        popupColaboradores.appendChild(li);
      });
      popupColaboradores.classList.remove("hidden")
    })
  }

  if (miniFormDerecho) {
    document.getElementById("btnAsignarColaborador").addEventListener("click", async () => {
      const txtBuscarColaborador = document.getElementById("txtBuscarColaborador")
      
      if (!txtBuscarColaborador.value.trim()) return
      const colaboradorId = txtBuscarColaborador.dataset.id
      const solicitudId = verTipoSolicitud.dataset.id
      await fetch(`/${contextPath}/control/AsignacionServlet`, {
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
      .then(data => {
        if (data.ok) {
          contenedorTarjetas.insertBefore(crearTarjetilla(
            data.colaborador.id, `${data.colaborador.nombre} ${data.colaborador.apellidoPaterno} ${data.colaborador.apellidoMaterno}`,
              `Código: ${data.colaborador.codigo}`, true
            ),
            contenedorTarjetas.firstChild
          )
        } else {
          alert(data.error)
        }
      })
    }) 
  }


  return (solicitud) => {
    setTimeout(async () => {
      await fetch(`/${contextPath}/control/ColaboradorServlet?action=2&solicitudId=${solicitud.id}`)
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      })
      .then(data => {
        if (data.ok) {
          solicitudActual = solicitud
          let tarjetaCoord;
          if (solicitudActual.coordinador !== null) {
            const colaborador = data.colaboradores.splice(0, 1)[0]
            tarjetaCoord = crearTarjetilla(
              colaborador.id, `${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}`,
              `Código: ${colaborador.codigo}`, true
            ),
            tarjetaCoord.className = estiloResaltante
            tarjetaCoord.dataset.coor = true
          }
          data.colaboradores.forEach(colaborador => {
            contenedorTarjetas.appendChild(crearTarjetilla(
              colaborador.id, `${colaborador.nombre} ${colaborador.apellidoPaterno} ${colaborador.apellidoMaterno}`,
              `Código: ${colaborador.codigo}`, true
            ),)
          });
  
          if (tarjetaCoord) {
            contenedorTarjetas.insertBefore(tarjetaCoord, contenedorTarjetas.firstChild)
          }
        } else {
          alert(data.error);
        }
      })
    }, 0);
    if (btnComenazarAtencion) {
      if (solicitud.estadoSolicitud === "Pendiente") {
        btnComenazarAtencion.innerHTML = "Comenzar atención"
      } else if (solicitud.estadoSolicitud === "Atendida") {
        btnComenazarAtencion.classList.add("hidden")
      } else {
        btnComenazarAtencion.innerHTML = "Finalizar atención"
      }
    }

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

let nose = ""
!JSON.parse(sessionStorage.getItem("usuario")).rolColaborador
? nose = `
  <button class="btn-editar block w-full px-4 py-2 text-left text-sm hover:bg-gray-100">Editar</button>
  <button class="btn-eliminar block w-full px-4 py-2 text-left text-sm hover:bg-gray-100 text-red-600">Eliminar</button>
`
: JSON.parse(sessionStorage.getItem("usuario")).rolColaborador === "Administrador"
? nose = `
  <button class="btn-editar block w-full px-4 py-2 text-left text-sm hover:bg-gray-100">Editar</button>
  <button class="btn-eliminar block w-full px-4 py-2 text-left text-sm hover:bg-gray-100 text-red-600">Eliminar</button>
`
: ""
function crearFila(solicitud) {
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
    <td class="p-2 text-right">
      <button class="popup-btn cursor-pointer w-7 h-7 flex items-center justify-center rounded hover:bg-gray-100">
        ⋮
      </button>
      <div tabindex="-1" class="popup-menu absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded shadow-md hidden z-10">
        <button class="btn-ver-detalles block w-full px-4 py-2 text-left text-sm hover:bg-gray-100">Ver detalles</button>
        ${nose}
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

  return tr
}