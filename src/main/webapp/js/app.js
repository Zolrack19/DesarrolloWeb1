const carga = `
  <div class="flex items-center justify-center h-full p-10">
    <div class="text-center">
      <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500 border-solid mx-auto mb-4"></div>
      <p class="text-gray-600 text-lg font-medium">Cargando, por favor espere...</p>
    </div>
  </div>
`
const servlets = {
  "solicitudes": "SolicitudServlet",
  "clientes": "ClienteServlet",
  "colaboradores": "ColaboradorServlet",
}

const btnInicio = document.getElementById("pnl-inicio") 
const btnSolicitudes = document.getElementById("pnl-solicitudes") 
const btnClientes = document.getElementById("pnl-clientes") 
const btnColaboradores = document.getElementById("pnl-colaboradores") 
const btnEstadisticas = document.getElementById("pnl-estadisticas") 
const btnPerfil = document.getElementById("pnl-perfil") 
const btnCerrarSesion = document.getElementById("btn-cerrarSesion") 

btnInicio.addEventListener(("click"), () => {cargarContenido("inicio")})
btnSolicitudes.addEventListener(("click"), () => {cargarContenido("solicitudes")})
btnClientes.addEventListener(("click"), () => {cargarContenido("clientes")})
btnColaboradores.addEventListener(("click"), () => {cargarContenido("colaboradores")})
btnEstadisticas.addEventListener(("click"), () => {cargarContenido("estadisticas")})
btnPerfil.addEventListener(("click"), () => {cargarContenido("perfil")})

btnPerfil.addEventListener(("click"), cerrarSesion)


const contenido = document.getElementById("contenido");

const contextPath = window.location.pathname.split("/")[1];
let vistasCache = {};
let paginaActual = ""

async function init() {
  const respuesta = await fetch(`/${contextPath}/html/inicio.jsp`);
  if (!respuesta.ok) throw new Error("No se pudo cargar la vista.");
  
  const html = await respuesta.text();
  contenido.innerHTML = html;
  const divContenedor = document.createElement("div")
  divContenedor.innerHTML = html

  history.replaceState({ nombre: "inicio" }, '', '/semana6-1.0-SNAPSHOT/html/menu/inicio');

  const modulo = await import(`/${contextPath}/js/inicio.js`);
  modulo?.init?.(cargarContenido);
  vistasCache["inicio"] = {
    nodo: divContenedor,
    modulo
  };
}


async function cargarContenido(nombre, acutalizarURL = true) {
  if (paginaActual === nombre) return
  
  paginaActual = nombre;
  if (acutalizarURL) {
    history.pushState({nombre}, '', `/${contextPath}/html/menu/${nombre}`);
  }
  
  if (vistasCache[nombre]) {
    vistasCache[nombre].modulo?.actualizar?.(vistasCache[nombre].nodo); 
    contenido.innerHTML = vistasCache[nombre].nodo.innerHTML;
    vistasCache[nombre].modulo?.init?.();
    return;
  }

  initVista(nombre, `/${contextPath}/control/${servlets[nombre]}?numPag=1`)
}

async function initVista(nombre, fetchURL) {
  const rutaJS = `/${contextPath}/js/${nombre}.js`;
  const respuesta = await fetch(`/${contextPath}/control/EvaluarJSP?vista=${nombre}.jsp`);
  if (!respuesta.ok) throw new Error("No se pudo cargar la vista.");

  const html = await respuesta.text();
  
  const divContenedor = document.createElement("div")
  divContenedor.innerHTML = html

  let datos = null
  if (servlets[nombre]) {
    contenido.innerHTML = carga;
    const resp = await fetch(fetchURL)
    if (resp.ok) {
      const data = await resp.json()
      if (data.ok) {
        datos = data[nombre]
      } else {
        alert("error en cargar la vista: " + nombre)
        return
      }
    } else {
      alert("Error en petición http")
      return
    }
  }

  const modulo = await import(rutaJS);
  contenido.innerHTML = html;
  modulo?.init?.(datos, {vistasCache, cargarContenido, contenido, initVista});

  vistasCache[nombre] = {
    nodo: divContenedor,
    modulo
  };
}

function cerrarSesion() {
  const contextPath = window.location.pathname.split("/")[1];
  fetch(`/${contextPath}/control/LogoutServlet`)
  .then(resp => resp.json())
  .then(data => {
    window.usuario = null
    sessionStorage.removeItem("usuario")
    vistasCache = {}
    window.location.href = data.redirect
  });
}

window.addEventListener("popstate", (e) => {
  // console.log(sessionStorage.getItem("usuario") == null);
  // if (sessionStorage.getItem("usuario")) {
  //   console.log("al fin");
  //   window.location.href = `/${contextPath}/index.html`
  //   return;
  // }
  const nombre = e.state?.nombre || location.pathname.split("/").pop() || "inicio";
  cargarContenido(nombre, false)
})

document.addEventListener("DOMContentLoaded", () => {
  window.usuario = JSON.parse(sessionStorage.getItem("usuario"))
  init()
})