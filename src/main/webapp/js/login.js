const errorMensaje = document.getElementById("errorMensaje")
const txtEmail = document.getElementById("email")
const txtContrasena = document.getElementById("contrasena")

document.getElementById("formulario").addEventListener("submit", async function (e) {
  e.preventDefault();
  
  const email = txtEmail.value
  const contrasena = txtContrasena.value
  const contextPath = window.location.pathname.split("/")[1];

  await fetch("/" + contextPath + "/LoginServlet", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams({ email, contrasena }),
  })
  .then(res => res.json()
  .then(data => {
    if (data.ok) {
      sessionStorage.setItem("usuario", JSON.stringify(data.usuario));
      window.location.href = data.redirect;
      if (!errorMensaje.classList.contains("hidden")) {
        errorMensaje.classList.add("hidden")
      }
    } else {
      if (errorMensaje.classList.contains("hidden")) {
        errorMensaje.classList.remove("hidden")
      }
    }
  }));
})