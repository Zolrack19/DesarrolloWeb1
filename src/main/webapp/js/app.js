const vistasCache = {};
let paginaActual = ""

async function init() {
  paginaActual = "inicio"
  const respuesta = await fetch(`inicio.jsp`);
  if (!respuesta.ok) throw new Error("No se pudo cargar la vista.");
  
  const html = await respuesta.text();
  document.getElementById("contenido").innerHTML = html;
  const divContenedor = document.createElement("div")
  divContenedor.innerHTML = html

  const modulo = await import(`../js/inicio.js`);
  modulo?.init?.();
  vistasCache["inicio"] = {
    nodo: divContenedor,
    modulo
  };
}

document.addEventListener("DOMContentLoaded", () => {
  init()
})

async function cargarContenido(nombre) {
  const contenedor = document.getElementById("contenido");
  if (paginaActual === nombre) return
  paginaActual = nombre;
  if (vistasCache[nombre]) {
    vistasCache[nombre].modulo?.actualizar?.(vistasCache[nombre].nodo); 
    contenedor.innerHTML = vistasCache[nombre].nodo.innerHTML;
    vistasCache[nombre].modulo?.init?.(); 
    return;
  }

  try {
    const rutaJS = `../js/${nombre}.js`;
    const contextPath = window.location.pathname.split("/")[1];
    const respuesta = await fetch("/" + contextPath + `/control/EvaluarJSP?vista=${nombre}.jsp`);
    if (!respuesta.ok) throw new Error("No se pudo cargar la vista.");

    const html = await respuesta.text();
    
    const divContenedor = document.createElement("div")
    divContenedor.innerHTML = html
    contenedor.innerHTML = html;

    const modulo = await import(rutaJS);
    modulo?.init?.();

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