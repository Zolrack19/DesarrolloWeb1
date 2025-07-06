<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body>
  
  <header class="flex justify-between items-center px-6 py-4 border-b">
    <nav class="flex gap-6">
      <p id="" class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">
        <span>Colaboradores</span>
      </p>
      <button id="btnReinicarTabla" class="text-pink-600 round rounded-2xl border border-pink-600 px-2 hidden">
        Reiniciar solicitudes
      </button>
    </nav>
    <div class="flex items-center gap-4">
      <button id="btnNuevoColaborador" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700">Nuevo Colaborador</button>
    </div>
  </header>

  <div class="mt-20 px-6 w-full overflow-x-auto">
    <table class="table-auto w-full text-left text-[0.98rem] border-collapse rounded-xl shadow-lg overflow-hidden ring-1 ring-gray-200 bg-white">
      <thead>
        <tr class="bg-gradient-to-r from-gray-200 to-gray-300 text-gray-600 text-sm uppercase tracking-wider border-b border-gray-300">
          <th class="p-2 max-w-[100px] min-w-[87px]">Código</th>
          <th class="p-2 max-w-[400px] min-w-[268px]">Nombre</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Documento</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Rol</th>
          <th class="p-2 max-w-[300px] min-w-[200px]">Email</th>
          <th class="p-2 max-w-[130px] min-w-[130px]">Solicitudes activas</th>
          <th class="p-2 max-w-[40px] min-w-[10px] text-right">⋮</th>
        </tr>
      </thead>
      <tbody id="tbodyColaboradores" class="divide-y divide-gray-200 text-gray-800">
      </tbody>
    </table>
  </div>
  
  <div class="bg-gray-100 flex items-center justify-center p-4 mt-10">
    <div class="flex items-center justify-between p-4 bg-white shadow-md">
      <div class="text-md text-gray-600">
        Mostrando filas <span id="pagInicio" class="font-semibold">0</span> a <span id="pagFin" class="font-semibold">10</span> de <span class="font-semibold">200 </span>
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

  <div id="modalColaborador"
  class="fixed inset-0 bg-[rgba(0,0,0,0.5)] flex items-center justify-center z-50 hidden">
    <div id="contenidoColaborador"
      class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-2xl w-full">

      <h2 class="text-2xl font-semibold text-blue-700 mb-4">Crear Colaborador</h2>

      <form id="formColaborador" class="grid grid-cols-1 md:grid-cols-2 gap-4">

        <!-- Número de documento -->
        <div>
          <label for="documento" class="block text-sm font-medium text-gray-700">N° Documento</label>
          <input type="text" id="txtDocumento" name="documento" autocomplete="off"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
          <p id="ocultoDoc" class="hidden text-red-600 text-sm">h</p>
        </div>

        <!-- Tipo de documento -->
        <div>
          <label for="tipoDocumentoId" class="block text-sm font-medium text-gray-700">Tipo de Documento</label>
          <select id="cbxTipoDocumento" name="tipoDocumentoId"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required>
            <option value="">Seleccione una alternativa</option>
            <option value="1">DNI</option>
            <option value="2">RUC</option>
            <option value="3">Carnet de extranjeria</option>
            <option value="4">Pasaporte</option>
          </select>
        </div>

        <!-- Nombre -->
        <div>
          <label for="nombre" class="block text-sm font-medium text-gray-700">Nombre</label>
          <input type="text" id="txtNombre" name="nombre" autocomplete="off"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Apellido Paterno -->
        <div>
          <label for="apellidoP" class="block text-sm font-medium text-gray-700">Apellido Paterno</label>
          <input type="text" id="txtApellidoP" name="apellidoP" autocomplete="off"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Apellido Materno -->
        <div>
          <label for="apellidoM" class="block text-sm font-medium text-gray-700">Apellido Materno</label>
          <input type="text" id="txtApellidoM" name="apellidoM" autocomplete="off"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Rol del colaborador -->
        <div>
          <label for="rolColaboradorId" class="block text-sm font-medium text-gray-700">Rol</label>
          <select id="cbxRolColaborador" name="rolColaboradorId"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required>
            <option value="">Seleccione un rol</option>
            <option value="1">Administrador</option>
            <option value="2">Programador</option>
            <option value="3">Analista</option>
            <option value="4">Diseñador</option>
          </select>
        </div>

        <!-- Botones -->
        <div class="md:col-span-2 flex justify-end gap-3 pt-2">
          <button type="button" id="btnCancelarColaborador"
            class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
            Cancelar
          </button>
          <button type="submit" class="px-4 py-2 rounded bg-green-600 hover:bg-green-700 text-white font-medium">
            Guardar
          </button>
        </div>
      </form>
    </div>
  </div>

</body>