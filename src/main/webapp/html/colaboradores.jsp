<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
</head>
<body>
  
  <header class="flex justify-between items-center px-6 py-4 border-b">
    <nav class="flex gap-6">
      <a href="#" class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">Colaboradores</a>
    </nav>
    <div class="flex items-center gap-4">
      <button id="btnNuevoColaborador" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700">Nuevo Colaborador</button>
    </div>
  </header>

  <div class="mt-20 px-6 w-full overflow-x-auto">
    <table class="table-auto w-full text-left border-collapse">
      <thead>
        <tr class="text-gray-500 border-b">
          <th class="p-2 max-w-[100px] min-w-[87px]">Código</th>
          <th class="p-2 max-w-[400px] min-w-[268px]">Nombre</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Documento</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Rol</th>
          <th class="p-2 max-w-[400px] min-w-[230px]">Email</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Solicitudes activas</th>
          <th class="p-2 max-w-[40px] min-w-[10px] text-right">⋮</th>
        </tr>
      </thead>
      <tbody>
        <tr class="break-normal border-b hover:bg-gray-50">
          <td class="p-2 ">casfd01296</td>
          
          <td class="p-2 ">
            <div class="break-words line-clamp-3">
              Benito Juares Lara
            </div>
          </td>
          <td class="p-2 ">
            <span><strong>RUC:</strong> 1239924332</span>
          </td>
          <td class="p-2">
            <span>Programador</span>
          </td>
          <td class="p-2">
            <span>supermario@gmail.com</span>
          </td>
          <td class="p-2 whitespace-nowrap">
            <span>5</span>
          </td>
          <td class="p-2 text-right">⋮</td>
        </tr>
      </tbody>
    </table>
  </div>

  <div id="modalColaborador"
  class="fixed inset-0 bg-[rgba(0,0,0,0.5)] flex items-center justify-center z-50 hidden">
    <div id="contenidoColaborador"
      class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-2xl w-full">

      <h2 class="text-2xl font-semibold text-blue-700 mb-4">Crear Colaborador</h2>

      <form id="formColaborador" class="grid grid-cols-1 md:grid-cols-2 gap-4">

        <!-- Número de documento -->
        <div>
          <label for="numeroDocumento" class="block text-sm font-medium text-gray-700">N° Documento</label>
          <input type="text" id="numeroDocumento" name="numeroDocumento"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Tipo de documento -->
        <div>
          <label for="tipoDocumentoId" class="block text-sm font-medium text-gray-700">Tipo de Documento</label>
          <select id="tipoDocumentoId" name="tipoDocumentoId"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required>
            <option value="">Seleccione...</option>
            <option value="1">DNI</option>
            <option value="2">Pasaporte</option>
          </select>
        </div>

        <!-- Nombre -->
        <div>
          <label for="nombre" class="block text-sm font-medium text-gray-700">Nombre</label>
          <input type="text" id="nombre" name="nombre"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Apellido Paterno -->
        <div>
          <label for="apellidoPaterno" class="block text-sm font-medium text-gray-700">Apellido Paterno</label>
          <input type="text" id="apellidoPaterno" name="apellidoPaterno"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Apellido Materno -->
        <div>
          <label for="apellidoMaterno" class="block text-sm font-medium text-gray-700">Apellido Materno</label>
          <input type="text" id="apellidoMaterno" name="apellidoMaterno"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required />
        </div>

        <!-- Rol del colaborador -->
        <div>
          <label for="rolColaboradorId" class="block text-sm font-medium text-gray-700">Rol</label>
          <select id="rolColaboradorId" name="rolColaboradorId"
            class="w-full rounded-md border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
            required>
            <option value="">Seleccione...</option>
            <option value="1">Administrador</option>
            <option value="2">Asistente</option>
            <option value="3">Supervisor</option>
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
</html>