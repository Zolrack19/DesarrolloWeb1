<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <script defer src="../js/app.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-100 h-screen">
  
  <div class="flex h-full">
    <aside class="w-64 bg-blue-800 text-white p-6 flex flex-col min-h-screen">
      <h2 class="text-2xl font-bold mb-20">MiPanel</h2>
    
      <nav class="space-y-3">
        <button onclick="cargarContenido('inicio')" class="font-semibold block w-full text-left py-2 px-4 hover:bg-blue-700">Inicio</button>
        <button onclick="cargarContenido('solicitudes')" class="font-semibold block w-full text-left py-2 px-4 hover:bg-blue-700">Solicitudes</button>
        <c:if test="${rol != 'cliente' || (rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador')}">
          <button onclick="cargarContenido('clientes')" class="font-semibold block w-full text-left py-2 px-4 hover:bg-blue-700">Clientes</button>
        </c:if>
        <c:if test="${rol != 'cliente' || (rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador')}">
          <button onclick="cargarContenido('colaboradores')" class="font-semibold block w-full text-left py-2 px-4 hover:bg-blue-700">Colaboradores</button>
        </c:if>
        <c:if test="${rol != 'cliente' || (rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador')}">
          <button onclick="cargarContenido('estadisticas')" class="font-semibold block w-full text-left py-2 px-4 hover:bg-blue-700">Estadísticas</button>
        </c:if>
      </nav>

      <div class="mt-auto space-y-2">
        <button onclick="cargarContenido('perfil')" class="font-semibold block w-full text-left py-2 px-4 hover:bg-blue-700">Perfil</button>
        <button onclick="cerrarSesion()" class="font-bold text-red-400 block w-full text-left py-2 px-4 hover:bg-blue-700">Cerrar sesión</button>
      </div>
    </aside>

    <main id="contenido" class="bg-zinc-50 flex-1 p-8 overflow-y-auto">
      
    </main>
    
  </div>

</body>
</html>