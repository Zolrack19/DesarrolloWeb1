let tbody
const contextPath = window.location.pathname.split("/")[1];

function llenarTabla(clientes, limpiar = false) {
  if (limpiar) {
    tbody.innerHTML = ""
  }

  clientes.forEach((cliente) => {
    const tr = document.createElement("tr");
    tr.className = "odd:bg-white even:bg-gray-100 hover:bg-blue-100 transition-colors";
  
    tr.innerHTML = `
      <td class="p-2">${cliente.razonSocial}</td>
      <td class="p-2"><strong>${cliente.tipoDocumento}</strong> ${cliente.numeroDocumento}</td>
      <td class="p-2">${cliente.tipoCliente}</td>
      <td class="p-2">${cliente.tipoSectorEconomico}</td>
      <td class="p-2">${cliente.telefono}</td>
      <td class="p-2 text-right">⋮</td>
    `;
    tbody.appendChild(tr);
  });
}


export function init(datos) {

  if (datos) {
    tbody = document.getElementById("tbodyClientes")
    llenarTabla(datos)
  }

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

  const form = document.getElementById("formulario")

  form.addEventListener("submit", async function (e) {

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
        
        const cliente = data.cliente

        if (!tbody) {
          tbody = document.getElementById("tbodyClientes")
        }
        const tr = document.createElement("tr");
        tr.className = "border-b hover:bg-gray-50";
        tr.innerHTML = `
          <td class="p-2">${cliente.razonSocial}</td>
          <td class="p-2"><strong>${cliente.tipoDocumento}</strong> ${cliente.numeroDocumento}</td>
          <td class="p-2">${cliente.tipoCliente}</td>
          <td class="p-2">${cliente.tipoSectorEconomico}</td>
          <td class="p-2">${cliente.telefono}</td>
          <td class="p-2 text-right">⋮</td>
        `;

        tbody.insertBefore(tr, tbody.firstChild);
        cerrarModal("modalCliente", "contenidoCliente");
      }
    }));
  })

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

  const btnNuevoCliente = document.getElementById("btnNuevoCliente")
  const btnCancelarCliente = document.getElementById("btnCancelarCliente")

  btnNuevoCliente.addEventListener("click", () => { abrirModal("modalCliente", "contenidoCliente") })
  btnCancelarCliente.addEventListener("click", () => { cerrarModal("modalCliente", "contenidoCliente") })


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

}

export function actualizar(nodo) {
  if (tbody) {
    nodo.querySelector("tbody[id='tbodyClientes']").innerHTML = tbody.innerHTML
    tbody = null
  }
}