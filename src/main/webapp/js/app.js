const contextPath = window.location.pathname.split("/")[1];
const vistasCache = {};
let paginaActual = ""

async function init() {
  const respuesta = await fetch(`/${contextPath}/html/inicio.jsp`);
  if (!respuesta.ok) throw new Error("No se pudo cargar la vista.");
  
  const html = await respuesta.text();
  
  document.getElementById("contenido").innerHTML = html;
  const divContenedor = document.createElement("div")
  divContenedor.innerHTML = html

  history.replaceState({ nombre: "inicio" }, '', '/semana6-1.0-SNAPSHOT/html/menu/inicio');

  const modulo = await import(`/${contextPath}/js/inicio.js`);
  modulo?.init?.();
  vistasCache["inicio"] = {
    nodo: divContenedor,
    modulo
  };
}


async function cargarContenido(nombre, acutalizarURL = true) {
  const contenedor = document.getElementById("contenido");
  if (paginaActual === nombre) return
  
  paginaActual = nombre;
  if (acutalizarURL) {
    history.pushState({nombre}, '', `/${contextPath}/html/menu/${nombre}`);
  }
  
  if (vistasCache[nombre]) {
    vistasCache[nombre].modulo?.actualizar?.(vistasCache[nombre].nodo); 
    contenedor.innerHTML = vistasCache[nombre].nodo.innerHTML;
    vistasCache[nombre].modulo?.init?.(); 
    return;
  }

  try {
    const rutaJS = `/${contextPath}/js/${nombre}.js`;
    const respuesta = await fetch(`/${contextPath}/control/EvaluarJSP?vista=${nombre}.jsp`);
    if (!respuesta.ok) throw new Error("No se pudo cargar la vista.");

    const html = await respuesta.text();
    
    const divContenedor = document.createElement("div")
    divContenedor.innerHTML = html
    contenedor.innerHTML = html;

    const modulo = await import(rutaJS);
    modulo?.init?.();
    // modulo?.init?.(divContenedor);

    vistasCache[nombre] = {
      nodo: divContenedor,
      modulo
    };

  } catch (error) {
    console.error(error);
  }
}

function cerrarSesion() {
  const contextPath = window.location.pathname.split("/")[1];
  fetch(`/${contextPath}/control/LogoutServlet`)
  .then(resp => resp.json())
  .then(data => {
    window.location.replace(data.redirect);
  });
  vistasCache = {}
}

window.addEventListener("popstate", (e) => {
  const nombre = e.state?.nombre || location.pathname.split("/").pop() || "inicio";
  cargarContenido(nombre, false)
})

document.addEventListener("DOMContentLoaded", () => {
  init()
})