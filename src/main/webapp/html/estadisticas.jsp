<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body>
  <header class="flex justify-between items-center px-6 py-4 border-b border-gray-500">
    <nav class="flex gap-6">
      <p id="pnlSolicitudes" class="text-pink-600 font-semibold border-b-2 border-pink-600 pb-1">
        <span>Solicitudes</span>
      </p>
    </nav>
  </header>

  <!-- Filtros por fecha -->
  <div class="flex justify-end gap-4 px-6 py-4 border-b border-gray-100">
    <div class="w-[180px]">
      <label class="block text-sm font-medium text-gray-700 mb-1">Mes</label>
      <select id="mesCombo" class="w-full border border-gray-300 rounded-md shadow-sm px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-pink-500">
        <option value="1">Enero</option>
        <option value="2">Febrero</option>
        <option value="3">Marzo</option>
        <option value="4">Abril</option>
        <option value="5" selected >Mayo</option>
        <option value="6">Junio</option>
        <option value="7">Julio</option>
        <option value="8">Agosto</option>
        <option value="9">Septiembre</option>
        <option value="10">Octubre</option>
        <option value="11">Noviembre</option>
        <option value="12">Diciembre</option>
      </select>
    </div>
  
    <div class="w-[180px]">
      <label class="block text-sm font-medium text-gray-700 mb-1">Año</label>
      <div class="flex items-center justify-between border border-gray-300 rounded-md shadow-sm px-3 py-2 text-sm focus-within:ring-2 focus-within:ring-pink-500">
        <button id="decrementAnio" class="text-gray-500 hover:text-red-500 focus:outline-none">◀</button>
        <span id="anioSeleccionado" class="font-medium text-gray-800 select-none">2025</span>
        <button id="incrementAnio" class="text-gray-500 hover:text-green-500 focus:outline-none">▶</button>
      </div>
    </div>
  </div>

  <div class="bg-gray-100 p-6" id="contEstadistica"></div>


</body>