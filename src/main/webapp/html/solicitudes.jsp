<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body>

  <header class="flex justify-between items-center px-6 py-4 border-b border-gray-500">
    <nav class="flex gap-6">
      <p id="pnlSolicitudes" class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">
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
      <button id="btnNuevaSolicitud" class="bg-red-600 text-white px-4 py-2 rounded-full shadow hover:bg-red-700 transition-colors">
        Nueva solicitud
      </button>
    </div>
  </header>
  
  <!-- Filtros por fecha -->
  <div class="flex justify-end gap-4 px-6 py-4 border-b border-gray-100">
    <input type="date" class="border border-gray-300 rounded-md shadow-sm px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-pink-500 w-[230px] max-w-[250px] min-w-[150px]">
    <input type="date" class="border border-gray-300 rounded-md shadow-sm px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-pink-500 w-[230px] max-w-[250px] min-w-[150px]">
  </div>
  
  <!-- Navegación -->
  <div class="px-6 w-full overflow-x-auto">
    <table class="table-auto w-full text-left text-[0.98rem] border-collapse rounded-xl shadow-lg overflow-hidden ring-1 ring-gray-200 bg-white">
      <thead>
        <tr class="bg-gradient-to-r from-gray-200 to-gray-300 text-gray-600 text-sm uppercase tracking-wider border-b border-gray-300">
          <th class="p-2 max-w-[100px] min-w-[87px]">Id</th>
          <th class="p-2 max-w-[400px] min-w-[268px]">Título</th>
          <th class="p-2 max-w-[400px] min-w-[268px]">Cordinador</th>
    
          <c:if test="${rol != 'cliente' && (rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador')}">
            <th class="p-2 max-w-[400px] min-w-[268px]">Cliente</th>
          </c:if>

          <th class="p-2 max-w-[250px] min-w-[220px]">Fecha de registro</th>
          <th class="p-2 max-w-[250px] min-w-[220px]">Fecha de finalización</th>
          <th class="p-2 max-w-[100px] min-w-[100px]">Estado</th>
          <th class="p-2 max-w-[40px] min-w-[10px] text-right">⋮</th>
        </tr>
      </thead>
      <tbody id="tbodySolicitudes" class="divide-y divide-gray-200 text-gray-800">
      </tbody>
    </table>
  </div>

  <div class="bg-gray-100 flex items-center justify-center p-4 mt-10">
    <div class="flex items-center justify-between p-4 bg-white shadow-md">
      <div class="text-md text-gray-600">
        Mostrando filas <span id="pagInicio" class="font-semibold">1</span> a <span id="pagFin" class="font-semibold">10</span> de <span class="font-semibold">200 </span>
      </div>
    
      <div class="flex items-center space-x-2">
        <button
          class="ml-4 px-3 py-1 text-sm text-gray-700 bg-gray-100 border border-gray-300 rounded hover:bg-gray-200 disabled:opacity-50"
          id="atras"
        >
          ◀
        </button>
    
        <button
          class="px-3 py-1 text-sm text-gray-700 bg-gray-100 border border-gray-300 rounded hover:bg-gray-200 disabled:opacity-50"
          id="adelante"
        >
          ▶
        </button>
      </div>
    </div>
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
          <input type="text" id="txtTitulo" name="txtTitulo" required autocomplete="off"
            class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            placeholder="Escribe un título breve">
        </div>

        <!-- Descripción -->
        <div>
          <label for="txtDescripcion" class="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
          <textarea id="txtDescripcion" name="txtDescripcion" rows="4" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 resize-y -80 min-h-50"
            placeholder="Describe los detalles de tu solicitud"></textarea>
        </div>

        <!-- buscar coordinador -->
        <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador'}">
          <div class="relative">
            <label for="txtCoordinador" class="block text-sm font-medium text-gray-700 mb-1">Coordinador</label>
            <input type="text" id="txtCoordinador" name="coordinadorId" required autocomplete="off"
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Buscar coordinador...">
            
            <ul id="popupCoordinador" class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-md -48 overflow-y-auto hidden">
            </ul>
          </div>
        
          <div class="relative">
            <label for="txtCliente" class="block text-sm font-medium text-gray-700 mb-1">Cliente</label>
            <input type="text" id="txtCliente" name="clienteId" required autocomplete="off"
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Buscar cliente...">
            <ul id="popupCliente" tabindex="1" class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-md -40 overflow-y-auto hidden">
            </ul>
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

  <div id="modalVerSolicitud" class="fixed inset-0 bg-[rgba(0,0,0,0.5)] flex items-center justify-center z-50 hidden">
    <div
      class="mx-3 max-h-[85vh] overflow-y-auto bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-7xl w-full"
      id="contenidoVerSolicitud">
      <h2 class="text-2xl font-semibold text-blue-700 mb-4">Detalle de solicitud</h2>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">

        <div class="space-y-3 text-sm text-gray-700">
          <div>
            <span class="font-semibold text-[0.9rem]">Tipo de solicitud:</span>
            <span id="verTipoSolicitud">Error ps</span>
          </div>
          <div>
            <span class="font-semibold text-[0.9rem]">Estado:</span>
            <span id="verEstadoSolicitud">Activo</span>
          </div>
          <div>
            <span class="font-semibold text-[0.9rem]">Título:</span>
            <span id="verTituloSolicitud">Solicitud de prueba si te gusta bien, sino fue ps</span>
          </div>
          <div>
            <span class="font-semibold text-[0.9rem]">Descripción:</span>
            <p id="verDescripcionSolicitud" class="max-h-[40vh] overflow-y-auto">
              molestiae laudantium eveniet id obcaecati exercitationem placeat amet et, earum adipisci perferendis
              vitae, corrupti aliquid? Lorem ipsum dolor sit amet consectetur adipisicing elit. Natus ab iure beatae
              expedita ut nostrum
              molestiae laudantium eveniet id obcaecati exercitationem placeat amet et, earum adipisci perferendis
              vitae, corrupti aliquid?
            </p>
          </div>
          <div>
            <span class="font-semibold text-[0.9rem]">Coordinador:</span>
            <span id="verCoordinador">Cristofer yanpier polnaref</span>
          </div>
          <div>
            <span class="font-semibold text-[0.9rem]">Cliente:</span>
            <span id="verCliente">Debran espinoza</span>
          </div>
        </div>

        <div id="contenedorDerechoVerSolicitud">
          <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador'}">
            <span class="font-semibold text-[1.0rem]">Asignar nuevo colaborador</span>
            <div class="flex flex-col md:flex-row items-stretch md:items-center gap-2 mt-2 mb-4">
              <!-- Input de búsqueda -->
              <div class="relative w-full">
                <input type="text" id="txtBuscarColaborador" placeholder="Buscar colaborador..."
                  class="w-full md:flex-1 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                <ul id="popupColaboradores" tabindex="1" class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-md -40 overflow-y-auto hidden">
                </ul>
              </div>
              <!-- Botón -->
              <button type="button"
                class="w-full md:w-auto px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:ring-2 focus:ring-blue-300"
                id="btnAsignarColaborador">
                Asignar
              </button>
            </div>
          </c:if>
          <div>
            <span class="font-semibold text-[1.0rem]">Colaboradores asignados</span>
          </div>
          <div id="contenedorTarjetas"
            class="rounded-lg p-4 min-h-[40vh] max-h-[50vh] overflow-y-auto bg-gray-50">
        
            <div class="flex items-center gap-3 p-3 bg-white rounded-lg shadow-sm mb-2">
              <!-- Icono de usuario -->
              <div class="flex-shrink-0 bg-blue-100 text-blue-600 rounded-full p-2">
                <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
                </svg>
              </div>
              <!-- Info del colaborador -->
              <div class="text-gray-800 text-sm flex-1 min-w-0">
                <div class="font-medium truncate">
                  Juan Pérez
                </div>
                <div class="text-gray-500 text-sm">
                  Código: 12345
                </div>
              </div>
            </div>
          </div>


          <button type="button" class="mt-3 md:w-auto px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:ring-2 focus:ring-green-300"
            id="btnComenazarAtencion">
            Comenzar atención
          </button>
        </div>
      </div>

      <div class="flex justify-end mt-6">
        <button type="button" id="btnCerrarVerSolicitud"
          class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
          Cerrar
        </button>
      </div>
    </div>
  </div>
  
</body>