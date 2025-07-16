<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<body>
  <div class="container mx-auto">
    <h1 class="text-4xl font-extrabold text-blue-800 mb-4">Bienvenido al Sistema de Soporte Técnico</h1>

    <p class="text-lg text-gray-700 mb-4">
      Administra con eficiencia las solicitudes de tus clientes y mantén una trazabilidad completa en cada proceso.
    </p>

    <p class="text-base text-gray-600 mb-8">
      Desde este panel podrás registrar nuevos casos, asignar tareas a tu equipo, revisar estadísticas de atención
      y mantener la comunicación activa con tus usuarios. Nuestra plataforma está diseñada para adaptarse a tus
      flujos de trabajo y escalar según las necesidades de tu negocio.
    </p>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div id="btnSolicitudes" class="group relative bg-gradient-to-br from-indigo-100 via-white to-blue-100 p-6 rounded-xl shadow-lg hover:shadow-2xl transition cursor-pointer">
        <h2 class="text-xl font-bold text-indigo-700 mb-2 group-hover:underline">📌 Solicitudes</h2>
        <p class="text-gray-700">
          Visualiza, clasifica y da seguimiento a cada solicitud ingresada. Controla cada etapa del proceso de atención.
        </p>
      </div>

      <div id="btnEstadisticas" class="group relative bg-gradient-to-br from-indigo-100 via-white to-blue-100 p-6 rounded-xl shadow-lg hover:shadow-2xl transition cursor-pointer">
        <h2 class="text-xl font-bold text-indigo-700 mb-2 group-hover:underline">📈 Reportes</h2>
        <p class="text-gray-700">
          Accede a estadísticas claras sobre el rendimiento del equipo, tiempos de respuesta y áreas más consultadas.
        </p>
      </div>
    
      <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador'}">
        <div id="btnColaboradores" class="group relative bg-gradient-to-br from-indigo-100 via-white to-blue-100 p-6 rounded-xl shadow-lg hover:shadow-2xl transition cursor-pointer">
          <h2 class="text-xl font-bold text-indigo-700 mb-2 group-hover:underline">👥 Colaboradores</h2>
          <p class="text-gray-700">
            Administra roles, asigna tareas y monitorea el trabajo diario de tu equipo técnico con trazabilidad completa.
          </p>
        </div>
      </c:if>

      <c:if test="${rol == 'colaborador' && usuario.getRolColaborador() == 'Administrador'}">
        <div id="btnClientes" class="group relative bg-gradient-to-br from-indigo-100 via-white to-blue-100 p-6 rounded-xl shadow-lg hover:shadow-2xl transition cursor-pointer">
          <h2 class="text-xl font-bold text-indigo-700 mb-2 group-hover:underline">👥 Clientes</h2>
          <p class="text-gray-700">
            Administra roles, asigna tareas y monitorea el trabajo diario de tu equipo técnico con trazabilidad completa.
          </p>
        </div>
      </c:if>

      <div id="btnPerfil" class="group relative bg-gradient-to-br from-indigo-100 via-white to-blue-100 p-6 rounded-xl shadow-lg hover:shadow-2xl transition cursor-pointer">
        <h2 class="text-xl font-bold text-indigo-700 mb-2 group-hover:underline">👥 Perfil de usuario</h2>
        <p class="text-gray-700">
          Visualiza y actualiza propiedades de tu perfil aquí.
        </p>
      </div>
    </div>
  
    <div class="mt-12 p-6 text-gray-900">
      <h3 class="text-lg font-semibold text-indigo-800 mb-2">¿Primera vez aquí?</h3>
      <p class="text-md mb-2">
        Usa el menú lateral o las tarjetas para navegar por el sistema. También puedes acceder a tu perfil para actualizar tu información personal.
      </p>
      <p class="text-md">
        Si necesitas asistencia, comunícate con el área de soporte o revisa la documentación en la sección de ayuda.
      </p>
    </div>

    <div class="mt-16 text-center text-gray-500 text-lg italic">
      “La eficiencia comienza con una atención clara y estructurada. Estamos aquí para ayudarte a lograrlo.”
    </div>
  
  </div>

</body>