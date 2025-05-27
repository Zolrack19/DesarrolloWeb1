<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body class="container bg-gray-50 p-8 text-gray-800 font-sans">

  <div class="container mx-auto :hover">
    <h2 class="text-2xl font-semibold flex items-center gap-2 mb-4">
      <span>🧍</span> Perfil
    </h2>
    <div class="bg-white p-6 rounded-lg shadow-md grid grid-cols-1 md:grid-cols-2 gap-9 hover:shadow-xl transition">

      <c:if test="${(rol == 'cliente' && usuario.getTipoCliente() != 'Empresa') || rol == 'colaborador'}">
        <div>
          <p class="text-xl text-gray-500 mb-1">📄 <strong class="text-black">Nombre completo</strong></p>
          <p id="lblNombre" class="text-lg">${usuario.getNombre()} ${usuario.getApellidoPaterno()} ${usuario.getApellidoMaterno()}</p>
          <p id="editNombre" class="text-blue-600 text-sm inline-flex items-center mt-1">✏️ Editar</p>
        </div>
      </c:if>

      <c:if test="${rol == 'cliente'}">
        <div>
          <p class="text-xl text-gray-500 mb-1">🔐 <strong class="text-black">Razón social</strong></p>
          <p id="lblRazon" class="text-lg">${usuario.getRazonSocial()}</p>
          <p id="editRazon" class="text-blue-600 text-sm inline-flex items-center mt-1">✏️ Editar</p>
        </div>
      </c:if>

      <div>
        <p class="text-xl text-gray-500 mb-1">👥 <strong class="text-black">Email</strong></p>
        <p class="text-lg">${usuario.getEmail()}</p>
      </div>

      <div>
        <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Contraseña</strong></p>
        <p class="text-lg font-semibold">************</p>
        <p id="editContrasena" class="text-blue-600 text-sm inline-flex items-center mt-1">✏️ Editar</p> <!--abrirModal -->
      </div>
  
      <c:if test="${rol == 'cliente'}">
        <div>
          <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Teléfono</strong></p>
          <p id="lblTelefono" class="text-lg">${usuario.getTelefono()}</p>
          <p id="editTelefono" class="text-blue-600 text-sm inline-flex items-center mt-1">✏️ Editar</p>
        </div>
      </c:if>

      <div>
        <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">${usuario.getTipoDocumento()}</strong></p>
        <p class="text-lg">${usuario.getNumeroDocumento()}</p>
      </div>

      <c:if test="${rol == 'cliente'}">
        <div>
          <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Sector económico</strong></p>
          <p class="text-lg">${usuario.getTipoSectorEconomico()}</p>
        </div>
      </c:if>

      <c:choose>
        <c:when test="${rol == 'cliente'}">
          <div>
            <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Tipo de cliente</strong></p>
            <p class="text-lg">${usuario.getTipoCliente()}</p>
          </div>
        </c:when>
        
        <c:when test="${rol == 'colaborador'}">
          <div>
            <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Rol de colaborador</strong></p>
            <p class="text-lg">${usuario.getRolColaborador()}</p>
          </div>
        </c:when>
        
        <c:otherwise>
          <p>No identificado</p>
        </c:otherwise>
      </c:choose>
  
      <c:if test="${rol == 'colaborador'}">
        <div>
          <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Código</strong></p>
          <p class="text-lg">${usuario.getCodigo()}</p>
        </div>      
      </c:if>

      <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() != 'Administrador'}">
        <div>
          <p class="text-xl text-gray-500 mb-1">✉️ <strong class="text-black">Solicitudes activas</strong></p>
          <p class="text-lg">${usuario.getSolicitudesActivas()}</p>
        </div>      
      </c:if>

    </div>

    <div>
      <button id="btnEliminarCuenta" class="bg-red-600 text-white px-5 py-2 rounded-md hover:bg-red-700 flex items-center gap-2">
        🗑️ Eliminar cuenta
      </button>
    </div>

  </div>


  <div id="modalContrasena"
    class="fixed inset-0 bg-opacity-60 backdrop-blur-[5px] flex items-center justify-center z-50 hidden">
    <div
      class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-md w-full"
      id="mContrasenaContenido">
      <h2 class="text-2xl font-semibold text-blue-700 mb-4">Cambiar contraseña</h2>
      <form id="formContrasena" action="" class="space-y-4">
        <div>
          <label for="contrasenaActual" class="block text-sm font-medium text-gray-700">Contraseña actual</label>
          <div class="relative">
            <input type="password" id="contrasenaActual" name="contrasenaActual" placeholder="********" required
              class="mt-1 block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <button type="button" id="btnContrasenaActual"
              class="absolute inset-y-0 right-0 px-3 text-gray-500">
              👁️
            </button>
          </div>
        </div>

        <div>
          <label for="nuevaContrasena" class="block text-sm font-medium text-gray-700">Nueva contraseña</label>
          <div class="relative">
            <input type="password" id="nuevaContrasena" name="nuevaContrasena" placeholder="********" required
              class="mt-1 block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <button type="button" id="btnNuevaContrasena"
              class="absolute inset-y-0 right-0 px-3 text-gray-500">
              👁️
            </button>
          </div>
        </div>

        <div>
          <label for="confirmarContrasena" class="block text-sm font-medium text-gray-700">Confirmar nueva
            contraseña</label>
          <div class="relative">
            <input type="password" id="confirmarContrasena" name="confirmarContrasena" placeholder="********" required
              class="mt-1 block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            <button type="button" id="btnConfirmarContrasena"
              class="absolute inset-y-0 right-0 px-3 text-gray-500">
              👁️
            </button>
          </div>
        </div>

        <div class="flex justify-end gap-2 pt-4">
          <button type="button" id="btnContraCerrarModal"
            class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
            Cancelar
          </button>
          <button type="submit"
            class="px-4 py-2 rounded bg-red-600 hover:bg-red-700 text-white font-medium focus:ring-2 focus:ring-red-300">
            Confirmar
          </button>
        </div>
      </form>
    </div>
  </div>

  <c:if test="${rol == 'cliente'}">
    <div id="modalRazon"
      class="fixed inset-0 bg-opacity-60 backdrop-blur-[5px] flex items-center justify-center z-50 hidden">
      <div
        class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-md w-full"
        id="contenidoRazon">
        <h2 class="text-2xl font-semibold text-blue-700 mb-4">Actualizar razón social</h2>
        <form id="formRazon" class="space-y-4">
          <input type="text" name="txtRazon" id="txtRazon"
            class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Mi nueva razón" required>
          <div class="flex justify-end gap-2 pt-2">
            <button type="button" id="btnCancelarRazon"
              class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
              Cancelar
            </button>
            <button type="submit"
              class="px-4 py-2 rounded bg-blue-600 hover:bg-blue-700 text-white font-medium focus:ring-2 focus:ring-blue-300">
              Aceptar
            </button>
          </div>
        </form>
      </div>
    </div>


    <div id="modalTelefono"
      class="fixed inset-0 bg-opacity-60 backdrop-blur-[5px] flex items-center justify-center z-50 hidden">
      <div
        class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-md w-full"
        id="contenidoTelefono">
        <h2 class="text-2xl font-semibold text-blue-700 mb-4">Actualizar teléfono</h2>
        <form id="formTelefono" class="space-y-4">
          <input type="tel" name="telefono" id="txtTelefono"
            class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Ej: +52 123 456 7890" required>
          <div class="flex justify-end gap-2 pt-2">
            <button type="button" id="btnCancelarTelefono"
              class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
              Cancelar
            </button>
            <button type="submit"
              class="px-4 py-2 rounded bg-blue-600 hover:bg-blue-700 text-white font-medium focus:ring-2 focus:ring-blue-300">
              Aceptar
            </button>
          </div>
        </form>
      </div>
    </div>
  </c:if>

  <c:if test="${(rol == 'cliente' && usuario.getTipoCliente() != 'Empresa') || rol == 'colaborador'}">
    <div id="modalNombre"
      class="fixed inset-0 bg-opacity-60 backdrop-blur-[5px] flex items-center justify-center z-50 hidden">
      <div
        class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-md w-full"
        id="contenidoNombre">
        <h2 class="text-2xl font-semibold text-blue-700 mb-4">Actualizar nombre</h2>
        <form id="formNombre" class="space-y-4">
          <label for="txtNombre">Nombres</label>
          <input type="text" name="txtNombre" id="txtNombre"
          class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          placeholder="Nombre(s)" required>
          <label for="txtApellidoPaterno">Apellido paterno</label>
          <input type="text" name="txtApellidoPaterno" id="txtApellidoPaterno"
            class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Apellido paterno" required>
          <label for="txtApellidoMaterno">Apellido materno</label>
          <input type="text" name="txtApellidoMaterno" id="txtApellidoMaterno"
            class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Apellido materno" required>
          <div class="flex justify-end gap-2 pt-2">
            <button type="button" id="btnCancelarNombre"
              class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
              Cancelar
            </button>
            <button type="submit"
              class="px-4 py-2 rounded bg-blue-600 hover:bg-blue-700 text-white font-medium focus:ring-2 focus:ring-blue-300">
              Aceptar
            </button>
          </div>
        </form>
      </div>
    </div>
  </c:if>

</body>
