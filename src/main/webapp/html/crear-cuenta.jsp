<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Crear Cuenta</title>
  <script src="https://cdn.tailwindcss.com"></script>
</head>
<p style="background: url(../assets/imagenes/istockphoto-1340452442-612x612.jpg);"></p>
<body class="bg-[url('../assets/imagenes/nolose.avif')] backdrop-blur-md bg-cover bg-center min-h-screen flex items-center justify-center">
  <div class="w-full max-w-lg bg-white rounded-2xl shadow-lg p-8">
    <h2 class="text-3xl font-bold text-center text-blue-700 mb-6">Crear cuenta</h2>
    
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
          <input type="text" id="documento" name="documento" required
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
        <input type="text" id="razon" name="razon" required
          class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
      </div>

      <div class="grid grid-cols-2 gap-5">

        <div id="divNombre" class="hidden">
          <label for="nombre" class="block text-sm font-medium text-gray-700">Nombres</label>
          <input type="text" id="nombre" name="nombre"
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div id="divApellidoP" class="hidden">
          <label for="apellidoPaterno" class="block text-sm font-medium text-gray-700">Apellido paterno</label>
          <input type="text" id="apellidoPaterno" name="apellidoPaterno"
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div id="divApellidoM" class="hidden">
          <label for="apellidoMaterno" class="block text-sm font-medium text-gray-700">Apellido materno</label>
          <input type="text" id="apellidoMaterno" name="apellidoMaterno"
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
        </div>

        <div>
          <label for="telefono" class="block text-sm font-medium text-gray-700">Número celular +51</label>
          <input type="tel" id="telefono" name="telefono" required
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
            <p id="ocultoTel" class="hidden text-red-600 text-sm">número no válido</p>
        </div>

        <!-- <div>
          <label for="telefono" class="block text-sm font-medium text-gray-700">Número de teléfono +51 </label>
          <input type="text" id="telefono" name="telefono" required
            class="mt-1 w-full px-4 py-1 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500" />
            <p id="ocultoTel" class="hidden text-red-600 text-sm">número no válido</p>
        </div> -->
      </div>


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

      <button type="submit" id="btnEnviar"
        class="w-full bg-blue-600 text-white py-2 rounded-lg font-semibold hover:bg-blue-700 transition">
        Crear cuenta
      </button>
    </form>

    <p class="mt-6 text-center text-sm text-gray-600">
      ¿Ya tienes una cuenta?
      <a href="./login.html" class="text-blue-600 font-medium hover:underline">Iniciar sesión</a>
    </p>
  </div>

  <script src="../js/crear-cuenta.js"></script>
</body>
</html>
