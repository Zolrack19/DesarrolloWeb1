<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Ventas</title>
</head>
<body class="bg-white font-sans text-sm text-gray-800">

  <header class="flex justify-between items-center px-6 py-4 border-b">
    <nav class="flex gap-6">
      <a href="#" class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">Clientes</a>
    </nav>
    <div class="flex items-center gap-4">
      <button id="btnNuevoCliente" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700">Nuevo Cliente</button>
    </div>
  </header>

  <!-- Filtros -->
  <!-- <div class="flex justify-end gap-4 px-6 py-4">
    <input type="date" class="border rounded px-3 py-2">
    <input type="date" class="border rounded px-3 py-2">
  </div> -->

  <!-- Tabla -->
  <div class="mt-20 px-6 w-full overflow-x-auto">
    <table class="table-auto w-full text-left border-collapse">
      <thead>
        <tr class="text-gray-500 border-b">
          <th class="p-2 max-w-[400px] min-w-[268px]">Razón social</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Documento</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Tipo</th>
          <th class="p-2 max-w-[300px] min-w-[220px]">Sector</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Teléfono</th>
          <th class="p-2 max-w-[40px] min-w-[10px] text-right">⋮</th>
        </tr>
      </thead>
      <tbody>
        <tr class="break-normal border-b hover:bg-gray-50">
          <td class="p-2 ">
            <div class="break-words line-clamp-3">
              Administrador de hoteles
            </div>
          </td>
          <td class="p-2 ">
            <span><strong>RUC:</strong> 1239924332</span>
          </td>
          <td class="p-2">
            <span>Empresa</span>
          </td>
          <td class="p-2">
            <span>Primario</span>
          </td>
          <td class="p-2 whitespace-nowrap">
            <span>931414911</span>
          </td>
          <td class="p-2 text-right">⋮</td>
        </tr>
      </tbody>
    </table>
  </div>


  <div id="modalCliente"
    class="fixed inset-0 bg-[rgba(0,0,0,0.5)] flex items-center justify-center z-50 hidden">
    <div id="contenidoCliente" class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-xl w-full">
      <h2 class="text-2xl font-bold text-blue-700 mb-4">Crear Cliente</h2>
      
      <form id="formCliente" class="grid grid-cols-1 md:grid-cols-2 gap-4">

        <div class="md:col-span-2">
          <label for="razonSocial" class="block text-sm font-medium text-gray-700">Razón Social</label>
          <input type="text" id="razonSocial" name="razonSocial" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required />
        </div>
      
        <div>
          <label for="numeroDocumento" class="block text-sm font-medium text-gray-700">N° Documento</label>
          <input type="text" id="numeroDocumento" name="numeroDocumento" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required />
        </div>
      
        <div>
          <label for="tipoDocumentoId" class="block text-sm font-medium text-gray-700">Tipo Documento</label>
          <select id="tipoDocumentoId" name="tipoDocumentoId" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required>
            <option value="">Seleccione...</option>
            <option value="1">DNI</option>
            <option value="2">Pasaporte</option>
          </select>
        </div>
      
        <div>
          <label for="tipoClienteId" class="block text-sm font-medium text-gray-700">Tipo Cliente</label>
          <select id="tipoClienteId" name="tipoClienteId" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required>
            <option value="">Seleccione...</option>
            <option value="1">Persona Natural</option>
            <option value="2">Empresa</option>
          </select>
        </div>
      
        <div>
          <label for="sectorEconomicoId" class="block text-sm font-medium text-gray-700">Sector Económico</label>
          <select id="sectorEconomicoId" name="sectorEconomicoId" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required>
            <option value="">Seleccione...</option>
            <option value="1">Tecnología</option>
            <option value="2">Comercio</option>
            <option value="3">Agroindustria</option>
          </select>
        </div>
      
        <div>
          <label for="txtTelefono" class="block text-sm font-medium text-gray-700">Teléfono</label>
          <input type="text" id="txtTelefono" name="txtTelefono" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required />
        </div>
        <p></p>
      
        <div>
          <label for="txtEmail" class="block text-sm font-medium text-gray-700">Email</label>
          <input type="email" id="txtEmail" name="txtEmail" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required />
        </div>
      
        <div>
          <label for="txtContrasena" class="block text-sm font-medium text-gray-700">Contraseña</label>
          <input type="password" id="txtContrasena" name="txtContrasena" class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:ring-blue-500 focus:border-blue-500" required />
        </div>
      
        <div class="md:col-span-2 flex justify-end gap-3 pt-2">
          <button type="button" id="btnCancelarCliente" class="px-4 py-2 rounded-lg bg-gray-200 hover:bg-gray-300 text-gray-800 font-medium">
            Cancelar
          </button>
          <button type="submit" class="px-4 py-2 rounded-lg bg-green-600 hover:bg-green-700 text-white font-medium">
            Guardar
          </button>
        </div>
      </form>

    </div>
  </div>

</body>
</html>
