const contextPath = window.location.pathname.split("/")[1];

let clientes = []
let numPag = 1
let tbody
let initModalForm = true

export function init(datos) {  
  const pagInicio = document.getElementById("pagInicio")
  const pagFin = document.getElementById("pagFin")
  tbody = document.getElementById("tbodyClientes")
  let rellenarModal = null
  tbody.addEventListener("click", async (e) => {
    if (e.target.classList.contains("btn-editar")) {
      const fila = e.target.closest("tr");
      if (!fila) return;
      if (initModalForm) {
        rellenarModal = confModalForm()
        initModalForm = false
      }
      rellenarModal(clientes.find((cliente) => cliente.id == fila.dataset.id))
      abrirModal("modalCliente", "contenidoCliente")
    } else if (e.target.classList.contains("btn-eliminar")) {
      const fila = e.target.closest("tr");
      if (!fila) return;

      await fetch("/" + contextPath + `/control/ClienteServlet?id=${fila.dataset.id}`, {
        method: "DELETE"
      }).then(response => {
        if (response.ok) {
          const index = clientes.findIndex(s => s.id == fila.dataset.id);
          if (index !== -1) {
            clientes.splice(index, 1);
          }
          fila.remove()
          // return response.json()
        } else {
          console.error("Error al eliminar cliente");
        }
      })
    }
  });


  document.getElementById("atras").addEventListener("click", async function() {
    const res = await fetch(`/${contextPath}/control/ClienteServlet?numPag=${numPag - 1}`)
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
    const res = await fetch(`/${contextPath}/control/ClienteServlet?numPag=${numPag + 1}`)
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
    clientes = datos
    llenarTabla(datos)
  } else {
    llenarTabla(clientes)
  }

  document.getElementById("btnNuevoCliente").addEventListener("click", () => {
    if (initModalForm) {
      rellenarModal = confModalForm()
      initModalForm = false
    }
    abrirModal("modalCliente", "contenidoCliente")
  })
  
  document.getElementById("btnCancelarCliente").addEventListener("click", () => {
    cerrarModal("modalCliente", "contenidoCliente")
  })

}

export function actualizar(nodo) {
  if (nodo.querySelector("span[id='pagFin']").innerHTML !== numPag*10) {
    nodo.querySelector("span[id='pagInicio']").innerHTML = (numPag - 1)*10 + 1
    nodo.querySelector("span[id='pagFin']").innerHTML = numPag*10
  }
  initModalForm = true
}

function confModalForm() {
  const divNombre = document.getElementById("divNombre")
  const divApellidoP = document.getElementById("divApellidoP")
  const divApellidoM = document.getElementById("divApellidoM")

  const cbxDoc = document.getElementById("cbxDoc")
  const cbxCliente = document.getElementById("cbxCliente")
  const cbxSectorEco = document.getElementById("cbxSectorEco")

  const txtRazon = document.getElementById("razon")
  const txtNombre = document.getElementById("nombre")
  const txtApellidoP = document.getElementById("apellidoPaterno")
  const txtApellidoM = document.getElementById("apellidoMaterno")

  const txtEmail = document.getElementById("email")

  const txtContrasena = document.getElementById("contrasena")
  const ocultoContra = document.getElementById("ocultoContra")

  const ocultoDoc = document.getElementById("ocultoDoc")
  const txtDocumento = document.getElementById("documento")
  const ocultoTel = document.getElementById("ocultoTel")
  const txtTelefono = document.getElementById("telefono")


  cbxCliente.addEventListener("change", (e) => {
    switch (e.target.value) {
      case "2": //Persona con negocio
        divNombre.classList.remove("hidden")
        divApellidoP.classList.remove("hidden")
        divApellidoM.classList.remove("hidden")
        txtNombre.required = true
        txtApellidoP.required = true
        txtApellidoM.required = true

        break;
      default:
        divNombre.classList.add("hidden")
        divApellidoP.classList.add("hidden")
        divApellidoM.classList.add("hidden")
        txtNombre.required = false
        txtApellidoP.required = false
        txtApellidoM.required = false
        break;
    }
  })

  cbxDoc.addEventListener("change", (e) => {
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

  txtDocumento.addEventListener("blur", () => {
    switch (cbxDoc.value) {
      case "1":
        if (/^\d{8}$/.test(txtDocumento.value)) {
          ocultoDoc.classList.add("hidden")
        } else {
          ocultoDoc.classList.remove("hidden")
        }
        break;
      case "2":
        if (/^\d{11}$/.test(txtDocumento.value)) {
          ocultoDoc.classList.add("hidden")
        } else {
          ocultoDoc.classList.remove("hidden")
        }
        break;
      case "3":
      case "4":
        const ln = txtDocumento.value.length;
        if (ln >= 4 && ln < 15) {
          ocultoDoc.classList.add("hidden")
        } else {
          ocultoDoc.classList.remove("hidden")
        }
        break;
      default:
        break;
    }
  })

  txtTelefono.addEventListener("blur", () => {
    if (/^9\d{8}$/.test(txtTelefono.value)) {
      ocultoTel.classList.add("hidden")
    } else {
      ocultoTel.classList.remove("hidden")
    }
  })

  txtContrasena.addEventListener("blur", () => {
    if (txtContrasena.value.length >= 8) {
      ocultoContra.classList.add("hidden")
    } else {
      ocultoContra.classList.remove("hidden")
    }
  })

  document.getElementById("formulario").addEventListener("submit", async function (e) {
    e.preventDefault()
    let stop = false
    const tipoDocumentoId = cbxDoc.value
    const documento = txtDocumento.value
    const telefono = txtTelefono.value
    const contrasena = txtContrasena.value

    switch (tipoDocumentoId) {
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

    if (/^9\d{8}$/.test(telefono)) {
      ocultoTel.classList.add("hidden")
    } else {
      ocultoTel.classList.remove("hidden")
      stop = true;
    }

    if (contrasena.length < 8) {
      ocultoContra.classList.remove("hidden")
      stop = true
    } else {
      ocultoContra.classList.add("hidden")
    }
    if (stop) {
      return
    }

    const tipoClienteId = cbxCliente.value
    const tipoSectorEconomicoId = cbxSectorEco.value
    const razon = txtRazon.value.trim()
    const nombre = txtNombre.value.trim()
    const apellidoP = txtApellidoP.value.trim()
    const apellidoM = txtApellidoM.value.trim()
    const email = txtEmail.value.trim()

    await fetch("/" + contextPath + "/control/ClienteServlet", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({
        tipoDocumentoId, documento, tipoClienteId, tipoSectorEconomicoId, razon, email, contrasena, telefono,
        nombre, apellidoP, apellidoM
      }),
    })
    .then(res => res.json()
    .then(data => {
      if (data.ok) {
        const cliente = []
        cliente.push(data.cliente)
        llenarTabla(cliente, false)
        if (clientes.length > 10) {
          const ultimaFila = tbody.lastElementChild
          const index = clientes.findIndex(s => s.id == ultimaFila.dataset.id);
          if (index !== -1) {
            clientes.splice(index, 1);
          }
          
          tbody.lastElementChild.remove()
        }
        clientes.push(data.cliente)

        cerrarModal("modalCliente", "contenidoCliente");
      }
    }));
  })
  return (cliente) => {
    cbxDoc.value = ""
    cbxCliente.value = ""
    cbxSectorEco.value = ""
    txtDocumento.value = cliente.numeroDocumento
    txtRazon.value = cliente.razonSocial
    txtTelefono.value = cliente.telefono
    txtEmail.value = cliente.email
    if (cliente.tipoCliente !== "Empresa") {
      txtNombre.value = cliente.nombre
      txtApellidoP.value = cliente.apellidoPaterno
      txtApellidoM.value = cliente.apellidoMaterno
    }
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
  contenido.classList.add("scale-95", "opacity-0");
  contenido.classList.remove("scale-100", "opacity-100");
  setTimeout(() => {
    modal.classList.add("hidden");
  }, 200);
}

function llenarTabla(clientes, append = true) {
  clientes.forEach((cliente) => {
    const tr = document.createElement("tr");
    tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors";
    tr.dataset.id = cliente.id

    tr.innerHTML = `
      <td class="p-2">${cliente.razonSocial}</td>
      <td class="p-2"><strong>${cliente.tipoDocumento}</strong> ${cliente.numeroDocumento}</td>
      <td class="p-2">${cliente.tipoCliente}</td>
      <td class="p-2">${cliente.tipoSectorEconomico}</td>
      <td class="p-2">${cliente.telefono}</td>
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