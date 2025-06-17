let lblTelefono
let lblRazon
let lblNombre

export function init() {
  const editContrasena = document.getElementById("editContrasena")

  const btnContrasenaActual = document.getElementById("btnContrasenaActual")
  const btnNuevaContrasena = document.getElementById("btnNuevaContrasena")
  const btnConfirmarContrasena = document.getElementById("btnConfirmarContrasena")
  const btnContraCerrarModal = document.getElementById("btnContraCerrarModal")

  editContrasena.addEventListener("click", () => {abrirModal("modalContrasena", "mContrasenaContenido")})
  btnContrasenaActual.addEventListener("click", () => {togglePassword("contrasenaActual")})
  btnNuevaContrasena.addEventListener("click", () => {togglePassword("nuevaContrasena")})
  btnConfirmarContrasena.addEventListener("click", () => {togglePassword("confirmarContrasena")})
  btnContraCerrarModal.addEventListener("click", () => {cerrarModal("modalContrasena", "mContrasenaContenido")})

  lblTelefono = document.getElementById("lblTelefono")
  if (lblTelefono !== null) {
    const editTelefono = document.getElementById("editTelefono")
    const btnCancelarTelefono = document.getElementById("btnCancelarTelefono")

    editTelefono.addEventListener("click", () => {abrirModal("modalTelefono", "contenidoTelefono")})
    btnCancelarTelefono.addEventListener("click", () => {cerrarModal("modalTelefono", "contenidoTelefono")})

    document.getElementById("formTelefono").addEventListener("submit", async function (e) {
      e.preventDefault();

      const telefono = this.querySelector("input[id='txtTelefono']").value.trim()

      const contextPath = window.location.pathname.split("/")[1];
      await fetch(`/${contextPath}/control/PerfilServlet`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({telefono: telefono})
      })
      .then(response => {
        if (!response.ok) throw new Error("Error al actualizar");
        return response.json();
      }).then(data => {
        if (data.ok) {
          lblTelefono.innerHTML = `${telefono}`
        }
      })

      cerrarModal('modalTelefono', 'contenidoTelefono');
    });

    lblRazon = document.getElementById("lblRazon")

    const editRazon = document.getElementById("editRazon")
    const btnCancelarRazon = document.getElementById("btnCancelarRazon")
    
    editRazon.addEventListener("click", () => {abrirModal("modalRazon", "contenidoRazon")})
    btnCancelarRazon.addEventListener("click", () => {cerrarModal("modalRazon", "contenidoRazon")})

    document.getElementById("formRazon").addEventListener("submit", async function (e) {
      e.preventDefault();
      const razon = this.querySelector("input[id='txtRazon']").value.trim()
      
      const contextPath = window.location.pathname.split("/")[1];
      await fetch(`/${contextPath}/control/PerfilServlet`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({razon: razon})
      })
      .then(response => {
        if (!response.ok) throw new Error("Error al actualizar");
        return response.json();
      }).then(data => {
        if (data.ok) {
          lblRazon.innerHTML = `${razon}`
        }
      })

      cerrarModal('modalRazon', 'contenidoRazon');
    });
  }

  lblNombre = document.getElementById("lblNombre")
  if (lblNombre !== null) {

    const editNombre = document.getElementById("editNombre")
    const btnCancelarNombre = document.getElementById("btnCancelarNombre")
    
    editNombre.addEventListener("click", () => {abrirModal("modalNombre", "contenidoNombre")})
    btnCancelarNombre.addEventListener("click", () => {cerrarModal("modalNombre", "contenidoNombre")})

    document.getElementById("formNombre").addEventListener("submit", async function (e) {
      e.preventDefault();
      const nombre = this.querySelector("input[id='txtNombre']").value.trim()
      const apellidoP = this.querySelector("input[id='txtApellidoPaterno']").value.trim()
      const apellidoM = this.querySelector("input[id='txtApellidoMaterno']").value.trim()

      const contextPath = window.location.pathname.split("/")[1];
      await fetch(`/${contextPath}/control/PerfilServlet`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          nombre: nombre,
          apellidoPaterno: apellidoP,
          apellidoMaterno: apellidoM
        })
      })
      .then(response => {
        if (!response.ok) throw new Error("Error al actualizar");
        return response.json();
      }).then(data => {
        if (data.ok) {
          lblNombre.innerHTML = `${nombre} ${apellidoP} ${apellidoM}`
        }
      })
      cerrarModal('modalNombre', 'contenidoNombre');
    });
  }


  document.getElementById("formContrasena").addEventListener("submit", async function (e) {
    e.preventDefault();
    
    const cActual = this.querySelector("input[id='contrasenaActual']").value.trim()
    const nContra = this.querySelector("input[id='nuevaContrasena']").value.trim()
    const cContra = this.querySelector("input[id='confirmarContrasena']").value.trim()

    const contextPath = window.location.pathname.split("/")[1];
    await fetch(`/${contextPath}/control/PerfilServlet`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        contrasena: cActual,
        nuevaContrasena: nContra,
        confirmContrasena: cContra
      })
    })
    .then(response => {
      if (!response.ok) throw new Error("Error al actualizar");
    })

    cerrarModal('modalContrasena', 'mContrasenaContenido');
  });
  

  function togglePassword(id) {
    const input = document.getElementById(id);
    input.type = input.type === 'password' ? 'text' : 'password';
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

  document.getElementById("btnEliminarCuenta").addEventListener("click", async fucntion => {
    if (!confirm("¿Está seguro que desea eliminar su cuenta?")) return
    
    const contextPath = window.location.pathname.split("/")[1];
    await fetch("/" + contextPath + `/control/PerfilServlet`, {
      method: "DELETE"
    }).then(response => {
      if (response.ok) {
        return response.json()
      } else {
        console.error("Error al eliminar el usuario");
      }
    }).then(data => {
      if (data.ok) {
        window.location.replace(data.redirect);
      }
    })
  })
}

export function actualizar(nodo) {
  if (lblNombre) {
    nodo.querySelector("p[id='lblNombre']").innerHTML = lblNombre.innerHTML
    lblNombre = null
  }
  if (lblTelefono) {
    nodo.querySelector("p[id='lblTelefono']").innerHTML = lblTelefono.innerHTML
    lblTelefono = null
  }
  if (lblRazon) {
    nodo.querySelector("p[id='lblRazon']").innerHTML = lblRazon.innerHTML
    lblRazon = null
  }  
}
