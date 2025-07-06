let cargarContenido = null

export function init(funcion = null) {
  if (cargarContenido === null && funcion !== null) {
    cargarContenido = funcion
  }
  
  document.getElementById("btnPerfil").addEventListener("click", () => {cargarContenido("perfil")})
  document.getElementById("btnSolicitudes").addEventListener("click", () => {cargarContenido("solicitudes")})
  document.getElementById("btnEstadisticas").addEventListener("click", () => {cargarContenido("estadisticas")})
  const btnClientes = document.getElementById("btnClientes")
  if (btnClientes !== null) {
    btnClientes.addEventListener("click", () => {cargarContenido("clientes")})
    document.getElementById("btnColaboradores").addEventListener("click", () => {cargarContenido("colaboradores")})
  }
}