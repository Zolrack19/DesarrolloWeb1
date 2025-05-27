<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body>

  <header class="flex justify-between items-center px-6 py-4 border-b">
    <nav class="flex gap-6">
      <p class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">
        <c:choose>
          <c:when test="${rol == 'cliente'}">
            <span>Mis solicitudes</span>
          </c:when>
          <c:otherwise>
            <span>Solicitudes</span>
          </c:otherwise>
        </c:choose>
      </p>
    </nav>
    <div class="flex items-center gap-4">
      <button id="btnNuevaSolicitud" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700">Nueva solicitud</button>
    </div>
  </header>

  <div class="flex justify-end gap-4 px-6 py-4">
    <input type="date" class="border max-w-[250px] w-[230px] min-w-[150px] rounded px-3 py-2">
    <input type="date" class="border max-w-[250px] w-[230px] min-w-[150px] rounded px-3 py-2">
  </div>

  <div class="px-6 w-full overflow-x-auto">
    <table class="table-auto w-full text-left border-collapse">
      <thead>
        <tr class="text-gray-500 border-b">
          <th class="p-2 max-w-[100px] min-w-[87px]">Id</th>
          <th class="p-2 max-w-[400px] min-w-[268px]">Título</th>
          <th class="p-2 max-w-[400px] min-w-[268px]">Cordinador</th>
          <th class="p-2 max-w-[300px] min-w-[220px]">Fecha de registro</th>
          <th class="p-2 max-w-[300px] min-w-[220px]">Fecha de finalización</th>
          <th class="p-2 max-w-[80px] min-w-[80px]">Estado</th>
          <th class="p-2 max-w-[40px] min-w-[10px] text-right">⋮</th>
        </tr>
      </thead>
      <tbody id="tbodySolicitudes">
      </tbody>
    </table>
  </div>

  <div id="modalSolicitud"
    class="fixed inset-0 bg-[rgba(0,0,0,0.5)] flex items-center justify-center z-50 hidden">
    <div
      class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-xl w-full"
      id="contenidoSolicitud">
      <h2 class="text-2xl font-semibold text-blue-700 mb-4">Crear solicitud</h2>
      <form id="formSolicitud" class="space-y-4">

        <c:set var="claseValor" value="${(rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador') ? 'grid grid-cols-1 md:grid-cols-2 gap-4' : ''}" />
        <div class="${claseValor}">
          <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador'}">
            <div>
              <label for="cbxEstadoSolicitud" class="block text-sm font-medium text-gray-700 mb-1">Tipo de solicitud</label>
              <select id="cbxEstadoSolicitud" name="cbxEstadoSolicitud" required
                class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                <option value="">Selecciona una opción</option>
                <option value="1">Pendiente</option>
                <option value="2">Asignada</option>
                <option value="3">En proceso</option>
                <option value="4">Atendida</option>
              </select>
            </div>
          </c:if>

          <div>
            <label for="cbxTipoSolicitud" class="block text-sm font-medium text-gray-700 mb-1">Tipo de solicitud</label>
            <select id="cbxTipoSolicitud" name="cbxTipoSolicitud" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
              <option value="">Selecciona una opción</option>
              <option value="1">Error</option>
              <option value="2">Capacitación</option>
              <option value="3">Requerimiento</option>
            </select>
          </div>
        </div>
        <!-- Título -->
        <div>
          <label for="txtTitulo" class="block text-sm font-medium text-gray-700 mb-1">Título</label>
          <input type="text" id="txtTitulo" name="txtTitulo" required
            class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Escribe un título breve">
        </div>

        <!-- Descripción -->
        <div>
          <label for="txtDescripcion" class="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
          <textarea id="txtDescripcion" name="txtDescripcion" rows="4" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 resize-y max-h-100 min-h-50"
            placeholder="Describe los detalles de tu solicitud"></textarea>
        </div>

        <!-- buscar coordinador -->
        <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador'}">
          <div>
            <label for="txtCoordinador" class="block text-sm font-medium text-gray-700 mb-1">Coordinador ('-1' sin coordinador)</label>
            <input type="text" id="txtCoordinador" name="txtCoordinador" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Buscar coordinador...">
          </div>
        
          <div>
            <label for="txtCliente" class="block text-sm font-medium text-gray-700 mb-1">Cliente</label>
            <input type="text" id="txtCliente" name="txtCliente" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Buscar coordinador...">
          </div>
        </c:if>

        <div class="flex justify-end gap-2 pt-2">
          <button type="button" id="btnCancelarSolicitud"
            class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
            Cancelar
          </button>
          <button type="submit"
            class="px-4 py-2 rounded bg-green-600 hover:bg-green-700 text-white font-medium focus:ring-2 focus:ring-green-300">
            Enviar solicitud
          </button>
        </div>
      </form>
    </div>
  </div>
  
</body>