<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="entities.Usuario"%>
<%@page import="entities.TipoUsuario"%>
<%@page import="entities.Cliente"%>
<%@page import="java.util.List"%>
<%@page import="entities.Pais" %>
<%@page import="entities.Provincia" %>
<%@page import="entities.Localidad" %>
<%@page import="java.time.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Gestión de Clientes</title>
<link rel="stylesheet"
    href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.min.css">
<link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>
    
    <div class="d-flex">
        <jsp:include page="Sidebar.jsp"></jsp:include>

        <div class="container m-4 vh-100">
            <% if (request.getAttribute("operacion") != null && request.getAttribute("operacion").equals("OK")) { %>
                <div class="alert alert-success" role="alert">Cliente creado correctamente</div>
            <% } else if (request.getAttribute("operacion") != null && request.getAttribute("operacion").equals("NOT OK")) { %>
                <div class="alert alert-danger" role="alert">
                    Error al crear el cliente
                    <% if (request.getAttribute("validationErrorMessage") != null) { %>
                        : <%= request.getAttribute("validationErrorMessage") %>
                    <% } %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("deleteResult") != null && Boolean.TRUE.equals(request.getAttribute("deleteResult"))) { %>
                <div class="alert alert-success" role="alert">Cliente eliminado correctamente</div>
            <% } else if (request.getAttribute("deleteResult") != null && Boolean.FALSE.equals(request.getAttribute("deleteResult"))) { %>
                <div class="alert alert-danger" role="alert">Error al eliminar el cliente</div>
            <% } %>
            
            <% if(request.getAttribute("modificarResult") != null && request.getAttribute("modificarResult") == Boolean.TRUE) { %>
                <div class="alert alert-success" role="alert">
                    Cliente modificado correctamente
                </div>
            <% } else if(request.getAttribute("modificarResult") != null && request.getAttribute("modificarResult") == Boolean.FALSE) { %>
                <div class="alert alert-danger" role="alert">
                    Error al modificar el cliente
                </div>
            <% } %>
            
            <% if(request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" role="alert">
                    Oops! Hubo un error.
                </div>
            <% } %>

            <h2 class="mb-3">Gestión de Clientes</h2>

            <form action="GestionClientes" method="get" id="formularioBusqueda">
                <div class="row p-3">
                    <div class="col">
                        <label for="nombre" class="form-label">Nombre</label>
                        <input type="text" name="nombreBuscar" placeholder="Nombre" id="nombreBuscar" class="form-control">
                    </div>
                    <div class="col">
                        <label for="apellido" class="form-label">Apellido</label>
                        <input type="text" name="apellidoBuscar" placeholder="Apellido" id="apellidoBuscar" class="form-control">
                    </div>
                    <div class="col">
                        <label for="nacionalidad" class="form-label">Nacionalidad</label>
                        <select class="form-control" name="paisBuscar">
                            <option value="">Seleccione nacionalidad</option>
                            <% if(request.getAttribute("paises") != null) {
                                List<Pais> paises = (List<Pais>) request.getAttribute("paises");
                                for(Pais pais : paises) { %>
                                    <option value="<%= pais.getId() %>"><%= pais.getNombre() %></option>
                            <% } } %>
                        </select>
                    </div>
                    <div class="col">
                        <label for="correo" class="form-label">Correo electrónico</label>
                        <input type="text" name="correoBuscar" placeholder="email@example.com" id="correoBuscar" class="form-control">
                    </div>
                    <div class="col">
                        <button type="submit" class="btn btn-primary mt-4" name="buscarCriteria">Filtrar</button>
                        <button type="button" class="btn btn-secondary mt-4" name="resetFiltros">Reset</button>
                    </div>
                </div>
            </form>

            <div class="row mt-3">
                <div class="d-grid gap-2 d-md-flex justify-content-md-start">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">Cargar Nuevo Cliente</button>
                </div>
            </div>

            <% if (request.getAttribute("clientes") != null) { %>
            <div class="row mt-3 shadow-lg p-3 overflow-x-scroll">
                <table id="clientes_table" class="table rounded table-stripped">
                    <thead>
                        <tr>
                            <th>Tipo de usuario</th>
                            <th>Usuario</th>
                            <th>Contraseña</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>DNI</th>
                            <th>CUIL</th>
                            <th>Correo electrónico</th>
                            <th>Fecha de nac</th>
                            <th>Sexo</th>
                            <th>Teléfono</th>
                            <th>Dirección</th>
                            <th>Nacionalidad</th>
                            <th>Localidad</th>
                            <th>Provincia</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <% List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
                        for (Cliente cliente : clientes) { %>
                        <tr>
                            <td><%= cliente.getUsuario().getTipoUsuario().getNombre() %></td>
                            <td><%= cliente.getUsuario().getUsuario() %></td>
                            <td><%= cliente.getUsuario().getContrasena() %></td>
                            <td><%= cliente.getNombre() %></td>
                            <td><%= cliente.getApellido() %></td>
                            <td><%= cliente.getDni() %></td>
                            <td><%= cliente.getCuil() %></td>
                            <td><%= cliente.getCorreoElectronico() %></td>
                            <td><%= cliente.getFechaNacimiento() %></td>
                            <td><%= cliente.getSexo() %></td>
                            <td><%= cliente.getTelefono() %></td>
                            <td><%= cliente.getDireccion() %></td>
                            <td><%= cliente.getNacionalidad().getNombre() %></td>
                            <td><%= cliente.getLocalidad().getNombre() %></td>
                            <td><%= cliente.getProvincia().getNombre() %></td>
                            <td>
                                <div class="d-flex justify-content-between gap-3">
                                    <i class="bi bi-pencil-square" style="cursor: pointer"
                                        data-bs-target="#modifyModal" data-bs-toggle="modal"
                                        data-id="<%= cliente.getId() %>"
                                        data-nombre="<%= cliente.getNombre() %>"
                                        data-apellido="<%= cliente.getApellido() %>"
                                        data-dni="<%= cliente.getDni() %>"
                                        data-cuil="<%= cliente.getCuil() %>"
                                        data-correo="<%= cliente.getCorreoElectronico() %>"
                                        data-fechanacimiento="<%= cliente.getFechaNacimiento() %>"
                                        data-sexo="<%= cliente.getSexo() %>"
                                        data-telefono="<%= cliente.getTelefono() %>"
                                        data-nacionalidad="<%= cliente.getNacionalidad().getId() %>"
                                        data-localidad="<%= cliente.getLocalidad().getId() %>"
                                        data-provincia="<%= cliente.getProvincia().getId() %>"
                                        data-direccion="<%= cliente.getDireccion() %>"
                                        data-usuario="<%= cliente.getUsuario().getUsuario() %>"
                                        data-contrasena="<%= cliente.getUsuario().getContrasena() %>"
                                        data-tipousuario="<%= cliente.getUsuario().getTipoUsuario().getId() %>"
                                        data-idusuariomodificar="<%= cliente.getUsuario().getId() %>"></i>
                                    <i class="bi bi-trash3-fill" data-toggle="modal" data-target="#deleteModal" data-id="<%= cliente.getId() %>" style="cursor: pointer;"></i>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>

            <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModal" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>Eliminar</h4>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <p>
                                ¿Está seguro que quiere eliminar el cliente con ID: <span id="idToDelete"></span>?
                            </p>
                            <input type="hidden" id="idToDeleteInput" name="idToDelete">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-danger" id="confirmDelete">Eliminar</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade modal-xl" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">Crear</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionClientes" method="post" id="createClienteForm" class="needs-validation" novalidate>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label for="tipousuario" class="form-label">Tipo de usuario</label>
                                        <select id="tipousuario" name="tipousuario" required class="form-control">
                                            <% if(request.getAttribute("tipoUsuarios") != null) {
                                                List<TipoUsuario> tipos = (List<TipoUsuario>) request.getAttribute("tipoUsuarios");
                                                for(TipoUsuario tipo : tipos) { %>
                                                    <option value="<%= tipo.getId() %>"><%= tipo.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="dni" class="form-label">DNI</label>
                                        <input type="text" class="form-control" id="dni" name="dni" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="cuil" class="form-label">CUIL</label>
                                        <input type="text" class="form-control" id="cuil" name="cuil" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3">
                                        <label for="nombre" class="form-label">Nombre</label>
                                        <input type="text" class="form-control" id="nombre" name="nombre" required>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="apellido" class="form-label">Apellido</label>
                                        <input type="text" class="form-control" id="apellido" name="apellido" required>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="sexo" class="form-label">Sexo</label>
                                        <select id="sexo" name="sexo" class="form-select" required>
                                            <option value="MASCULINO">Masculino</option>
                                            <option value="FEMENINO">Femenino</option>
                                            <option value="OTRO">Otro</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento</label>
                                        <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" max="<%= LocalDate.now() %>" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label for="nacionalidad" class="form-label">Nacionalidad</label>
                                        <select name="nacionalidad" class="form-control" required>
                                            <% if(request.getAttribute("paises") != null) {
                                                List<Pais> paises = (List<Pais>) request.getAttribute("paises");
                                                for(Pais pais : paises) { %>
                                                    <option value="<%= pais.getId() %>"><%= pais.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="provincia" class="form-label">Provincia</label>
                                        <select name="provincia" class="form-control" required>
                                            <% if(request.getAttribute("provincias") != null) {
                                                List<Provincia> provincias = (List<Provincia>) request.getAttribute("provincias");
                                                for(Provincia provincia : provincias) { %>
                                                    <option value="<%= provincia.getId() %>"><%= provincia.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="localidad" class="form-label">Localidad</label>
                                        <select name="localidad" class="form-control" required>
                                            <% if(request.getAttribute("localidades") != null) {
                                                List<Localidad> localidades = (List<Localidad>) request.getAttribute("localidades");
                                                for(Localidad localidad : localidades) { %>
                                                    <option value="<%= localidad.getId() %>"><%= localidad.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="direccion" class="form-label">Dirección</label>
                                        <input type="text" class="form-control" id="direccion" name="direccion" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="correo" class="form-label">Correo Electrónico</label>
                                        <input type="email" class="form-control" id="correo" name="correo" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="telefono" class="form-label">Teléfonos</label>
                                        <input type="text" class="form-control" id="telefono" name="telefono" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="username" class="form-label">Usuario</label>
                                        <input type="text" class="form-control" id="username" name="username" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="password" class="form-label">Contraseña</label>
                                        <input type="password" class="form-control" id="password" name="password" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="confirmPassword" class="form-label">Confirmar Contraseña</label>
                                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                        <div class="invalid-feedback">Las contraseñas no coinciden.</div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                    <button type="submit" name="save" class="btn btn-primary">Guardar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade modal-xl" id="modifyModal" tabindex="-1" aria-labelledby="modifyModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="modifyModalLabel">Modificar</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="modificarClienteForm" action="GestionClientes" method="post" class="need-validation" novalidate>
                                <input type="hidden" id="clientId" name="id">
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <input type="hidden" id="usuarioIdModificar" name="usuarioIdModificar">
                                        <label for="tipousuarioModificar" class="form-label">Tipo de usuario</label>
                                        <select id="tipousuarioModificar" name="tipousuarioModificar" required class="form-control">
                                            <% if(request.getAttribute("tipoUsuarios") != null) {
                                                List<TipoUsuario> tipos = (List<TipoUsuario>) request.getAttribute("tipoUsuarios");
                                                for(TipoUsuario tipo : tipos) { %>
                                                    <option value="<%= tipo.getId() %>"><%= tipo.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="dniModificar" class="form-label">DNI</label>
                                        <input type="text" class="form-control" id="dniModificar" name="dni" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="cuilModificar" class="form-label">CUIL</label>
                                        <input type="text" class="form-control" id="cuilModificar" name="cuil" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3">
                                        <label for="nombreModificar" class="form-label">Nombre</label>
                                        <input type="text" class="form-control" id="nombreModificar" name="nombre" required>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="apellidoModificar" class="form-label">Apellido</label>
                                        <input type="text" class="form-control" id="apellidoModificar" name="apellido" required>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="sexoModificar" class="form-label">Sexo</label>
                                        <select id="sexoModificar" name="sexo" class="form-select" required>
                                            <option value="MASCULINO">Masculino</option>
                                            <option value="FEMENINO">Femenino</option>
                                            <option value="OTRO">Otro</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <label for="fechaNacimientoModificar" class="form-label">Fecha de Nacimiento</label>
                                        <input type="date" class="form-control" id="fechaNacimientoModificar" name="fechaNacimiento" max="<%= LocalDate.now() %>" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label for="nacionalidadModificar" class="form-label">Nacionalidad</label>
                                        <select name="nacionalidad" class="form-control" required>
                                            <% if(request.getAttribute("paises") != null) {
                                                List<Pais> paises = (List<Pais>) request.getAttribute("paises");
                                                for(Pais pais : paises) { %>
                                                    <option value="<%= pais.getId() %>"><%= pais.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="provinciaModificar" class="form-label">Provincia</label>
                                        <select name="provincia" class="form-control" required>
                                            <% if(request.getAttribute("provincias") != null) {
                                                List<Provincia> provincias = (List<Provincia>) request.getAttribute("provincias");
                                                for(Provincia provincia : provincias) { %>
                                                    <option value="<%= provincia.getId() %>"><%= provincia.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="localidadModificar" class="form-label">Localidad</label>
                                        <select name="localidad" class="form-control" required>
                                            <% if(request.getAttribute("localidades") != null) {
                                                List<Localidad> localidades = (List<Localidad>) request.getAttribute("localidades");
                                                for(Localidad localidad : localidades) { %>
                                                    <option value="<%= localidad.getId() %>"><%= localidad.getNombre() %></option>
                                            <% } } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="direccionModificar" class="form-label">Dirección</label>
                                        <input type="text" class="form-control" id="direccionModificar" name="direccion" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="correoModificar" class="form-label">Correo Electrónico</label>
                                        <input type="email" class="form-control" id="correoModificar" name="correo" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="telefonoModificar" class="form-label">Teléfonos</label>
                                        <input type="text" class="form-control" id="telefonoModificar" name="telefono" required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="usernameModificar" class="form-label">Usuario</label>
                                        <input readonly type="text" class="form-control" id="usuarioModificar" name="usuarioModificar" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="passwordModificar" class="form-label">Contraseña</label>
                                        <input type="password" class="form-control" id="contrasenaModificar" name="password" required>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                    <button id="modificar" type="submit" name="modificar" class="btn btn-primary">Guardar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#clientes_table').DataTable({
                responsive: true,
                scrollX: 400,
                search: false,
                language: {
                    emptyTable: '"No hay datos para mostrar"',
                    lengthMenu: "Mostrar _MENU_ registros por página",
                    search: "Buscar:",
                    info: "Mostrando del _START_ al _END_, de un total de _TOTAL_ entradas",
                    paginate: {
                        previous: "Anterior",
                        next: "Siguiente",
                    },
                },
            });

            $('#deleteModal').on('show.bs.modal', function(event) {
                var button = $(event.relatedTarget);
                var id = button.data('id');
                var modal = $(this);
                modal.find('.modal-body #idToDelete').text(id);
                modal.find('.modal-body #idToDeleteInput').val(id);
            });

            $('#modifyModal').on('show.bs.modal', function(event) {
                var button = $(event.relatedTarget);
                var id = button.data('id');
                var nombre = button.data('nombre');
                var apellido = button.data('apellido');
                var dni = button.data('dni');
                var cuil = button.data('cuil');
                var correo = button.data('correo');
                var fechaNacimiento = button.data('fechanacimiento');
                var sexo = button.data('sexo');
                var telefono = button.data('telefono');
                var localidad = button.data('localidad');
                var nacionalidad = button.data('nacionalidad');
                var provincia = button.data('provincia');
                var direccion = button.data('direccion');
                var usuario = button.data('usuario');
                var contrasena = button.data('contrasena');
                var tipousuario = button.data('tipousuario');
                var idUsuarioModificar = button.data('idusuariomodificar');
                console.log(idUsuarioModificar);

                var modal = $(this);
                modal.find('.modal-body #clientId').val(id);
                modal.find('.modal-body #nombreModificar').val(nombre);
                modal.find('.modal-body #apellidoModificar').val(apellido);
                modal.find('.modal-body #dniModificar').val(dni);
                modal.find('.modal-body #cuilModificar').val(cuil);
                modal.find('.modal-body #correoModificar').val(correo);
                modal.find('.modal-body #fechaNacimientoModificar').val(fechaNacimiento);
                modal.find('.modal-body #sexoModificar').val(sexo);
                modal.find('.modal-body #telefonoModificar').val(telefono);
                modal.find('.modal-body #nacionalidadModificar').val(nacionalidad);
                modal.find('.modal-body #localidadModificar').val(localidad);
                modal.find('.modal-body #provinciaModificar').val(provincia);
                modal.find('.modal-body #direccionModificar').val(direccion);
                modal.find('.modal-body #usuarioModificar').val(usuario);
                modal.find('.modal-body #contrasenaModificar').val(contrasena);
                modal.find('.modal-body #tipousuarioModificar').val(tipousuario);
                modal.find('.modal-body #usuarioIdModificar').val(idUsuarioModificar);
            });

            $('#confirmDelete').on('click', function() {
                var id = $('#idToDeleteInput').val();
                window.location.href = 'GestionClientes?eliminar=1&id=' + id;
            });

            $('#createClienteForm').on('submit', function(e) {
                var password = $('#password').val();
                var confirmPassword = $('#confirmPassword').val();
                var fechaNacimiento = $('#fechaNacimiento').val();

                if (this.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }

                $(this).addClass("was-validated");

                if (password !== confirmPassword) {
                    event.preventDefault();
                    $('#confirmPassword').addClass('is-invalid');
                } else {
                    $('#confirmPassword').removeClass('is-invalid');
                }

            });
            $('#modificarClienteForm').on('submit', function(e) {
                if (this.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                $(this).addClass("was-validated")
            })
            $('#confirmPassword').on('input', function() {
                var password = $('#password').val();
                var confirmPassword = $(this).val();

                if (password !== confirmPassword) {
                    $(this).addClass('is-invalid');
                } else {
                    $(this).removeClass('is-invalid');
                }
            });
            $('button[name="resetFiltros"]').click(function() {
                $('#formularioBusqueda')[0].reset();
            });

        });
    </script>
</body>
</html>
