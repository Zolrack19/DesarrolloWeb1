<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body class="bg-white font-sans text-sm text-gray-800">

  <header class="flex justify-between items-center px-6 py-4 border-b border-gray-500">
    <nav class="flex gap-6">
      <a href="#" class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">Clientes</a>
    </nav>
    <div class="flex items-center gap-4">
      <button id="btnNuevoCliente" class="bg-red-600 text-white px-4 py-2 rounded-full hover:bg-red-700">Nuevo Cliente</button>
    </div>
  </header>


  <!-- Tabla -->
  <div class="mt-20 px-6 w-full overflow-x-auto">
    <table class="table-auto w-full text-left text-[0.98rem] border-collapse rounded-xl shadow-lg overflow-hidden ring-1 ring-gray-200 bg-white">
      <thead>
        <tr class="bg-gradient-to-r from-gray-200 to-gray-300 text-gray-600 text-sm uppercase tracking-wider border-b border-gray-300">
          <th class="p-2 max-w-[400px] min-w-[268px]">Razón social</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Documento</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Tipo</th>
          <th class="p-2 max-w-[300px] min-w-[220px]">Sector</th>
          <th class="p-2 max-w-[100px] min-w-[87px]">Teléfono</th>
          <th class="p-2 max-w-[40px] min-w-[10px] text-right">⋮</th>
        </tr>
      </thead>
      <tbody id="tbodyClientes" class="divide-y divide-gray-200 text-gray-800">
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

  <div id="modalCliente"
    class="fixed inset-0 bg-[rgba(0,0,0,0.5)] flex items-center justify-center z-50 hidden">
    <div id="contenidoCliente" class="bg-white rounded-2xl p-6 shadow-xl transform scale-95 opacity-0 transition-all duration-300 max-w-xl w-full">
      <h2 class="text-2xl font-bold text-blue-700 mb-4">Crear Cliente</h2>
      

      <form id="formulario" class="space-y-5">
        <div class="grid grid-cols-2 gap-5">
          <div>
            <label class="text-sm font-medium text-gray-700" for="cbxDoc">Tipo de documento</label>
            <select name="cbxDoc" id="cbxDoc" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
              <option selected value="">Selecciona una opción</option>
              <option value="1">DNI</option>
              <option value="2">RUC</option>
              <option value="3">Carnet de extranjería</option>
              <option value="4">Pasaporte</option>
            </select>
          </div>
          <div>
            <label class="text-sm font-medium text-gray-700" for="documento" class="block text-sm font-medium text-gray-700">N° de documento</label>
            <input autocomplete="off" type="text" id="documento" name="documento" required
              class="mt-1 w-full px-4 py-1 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
            <p id="ocultoDoc" class="hidden text-red-600 text-sm">DNI debe tener 8 dígitos</p>
          </div>
  
          <div>
            <label class="text-sm font-medium text-gray-700" for="cbxCliente">Tipo de cliente</label>
            <select name="cbxCliente" id="cbxCliente" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
              <option selected value="">Selecciona una opción</option>
              <option value="1">Empresa</option>
              <option value="2">Persona con negocio</option>
            </select>
          </div>
  
          <div>
            <label class="text-sm font-medium text-gray-700" for="cbxSectorEco">Sector económico</label>
            <select name="cbxSectorEco" id="cbxSectorEco" required
              class="block w-full rounded-md border border-gray-300 shadow-sm px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
              <option selected value="">Selecciona una opción</option>
              <option value="1">Primario</option>
              <option value="2">Secundario</option>
              <option value="3">Terciario</option>
            </select>
          </div>
  
        </div>
  
        <div>
          <label for="razon" class="block text-sm font-medium text-gray-700">Razón social</label>
          <input autocomplete="off" type="text" id="razon" name="razon" required
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
  
        <div class="grid grid-cols-2 gap-5">
  
          <div id="divNombre" class="hidden">
            <label for="nombre" class="block text-sm font-medium text-gray-700">Nombres</label>
            <input autocomplete="off" type="text" id="nombre" name="nombre"
              class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
  
          <div id="divApellidoP" class="hidden">
            <label for="apellidoPaterno" class="block text-sm font-medium text-gray-700">Apellido paterno</label>
            <input autocomplete="off" type="text" id="apellidoPaterno" name="apellidoPaterno"
              class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
  
          <div id="divApellidoM" class="hidden">
            <label for="apellidoMaterno" class="block text-sm font-medium text-gray-700">Apellido materno</label>
            <input autocomplete="off" type="text" id="apellidoMaterno" name="apellidoMaterno"
              class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
  
          <div>
            <label for="telefono" class="block text-sm font-medium text-gray-700">Número celular +51</label>
            <input type="tel" id="telefono" name="telefono" required
              class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
              <p id="ocultoTel" class="hidden text-red-600 text-sm">número no válido</p>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-5">
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
          <input type="email" id="email" name="email" required
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>
  
        <div>
          <label for="contrasena" class="block text-sm font-medium text-gray-700">Contraseña</label>
          <input type="password" id="contrasena" name="contrasena" required
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
          <p id="ocultoContra" class="hidden text-red-600 text-sm">Contraseña debe ser mayor a 8 dígitos</p>  
        </div>
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