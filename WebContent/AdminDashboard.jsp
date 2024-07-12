<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Usuario"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Panel de Administración</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<style>
.centered-btn {
    text-align: center;
    margin-top: 20px;
}
</style>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</head>
<body>
<%
    try {
        Usuario usuarioLog = (Usuario) session.getAttribute("usuario");
        if ((usuarioLog == null || usuarioLog.getUsuario().isEmpty())
                || !(usuarioLog.getTipoUsuario().getNombre().equals("ADMINISTRADOR"))) {
            throw new CustomSecurityException("El usuario no tiene los permisos necesarios por favor Ingrese sus credenciales");
        }
%>
    <div class="d-flex">
        <jsp:include page="Sidebar.jsp"></jsp:include>
        <div class="container-fluid m-4">
            <div class="row">
                <div class="col-lg-9 mb-4 order-0">
                    <div class="card">
                        <div class="d-flex align-items-center justify-content-around w-100 row">
                            <div class="col-sm-7">
                                <div class="card-body">
                                    <h5 class="card-title text-primary">Bienvenido, Administrador</h5>
                                    <p>Utiliza los enlaces de navegación para gestionar clientes, cuentas, autorizar préstamos y generar informes.</p>
                                    <hr>
                                    <p>Selecciona una de las opciones del menú para comenzar.</p>
                                </div>
                            </div>
                            <div class="col-sm-3 text-end">
                                <img src="Resources/admin-pic.jpg" alt="Admin pic" class="img-fluid p-3" style="max-height: 100%; max-width: 100%;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-md-12 col-4 mb-4">
                    <div class="card h-100">
                        <div class="card-body d-flex flex-column">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <svg fill="#000000" width="64px" height="64px" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 600 602.2" enable-background="new 0 0 600 602.2" xml:space="preserve">
                                        <path d="M122.2,234c-61.8,0-112-50.2-112-112s50.5-112,112-112c61.8,0,112,50.2,112,112S184.1,234,122.2,234z M122.2,31.2
                                        c-49.9,0-90.7,40.8-90.7,90.7s40.8,90.7,90.7,90.7s90.7-40.8,90.7-90.7S172.5,31.2,122.2,31.2z M395.4,426
                                        c20.2-25.8,16.7-63.4-10.2-84.5c-25.8-20.2-64.3-15.6-84.5,10.2s-16.7,63.4,10.2,84.5C336.7,456.2,375.2,451.6,395.4,426z
                                         M322.5,398.4c-8.1-6.5-10.4-16.5-5.1-27.4l-8.1-6.5l7.2-9.3l7.2,5.6c3.3-4.2,9.3-7.7,13.7-10.9l7.9,11.1c-2.6,1.2-7,4.6-11.8,10.7
                                        c-3.9,5.1-4.4,9.8-1.4,12.3c3,2.3,7.7,0.9,16.3-3.7c12.1-7,20.4-9.1,28.6-2.6c6.5,6.7,7.7,17.6,3.3,27.6l8.1,6.5l-7.2,9.3l-7.2-5.6
                                        c-3.3,4.2-11.8,10.7-15.1,13l-7.9-11.1c4.4-1.4,11.4-6,15.3-11.1c4.9-6.3,5.1-10.9,1.2-14.2c-3-2.3-6.7-2.1-15.6,2.8
                                        C341.3,401.6,330.7,404.9,322.5,398.4z M110.4,143.8c-3.2-1.9-5.1-5.4-5.1-9.2V60.9c0-5.9,4.9-10.5,10.5-10.5s10.5,4.9,10.5,10.5
                                        v55.9l51.8-28.3c5.1-2.7,11.6-0.8,14.3,4.3c2.7,5.1,0.8,11.6-4.3,14.3l-67.5,36.7C119.3,144.9,113.9,145.7,110.4,143.8z
                                         M451.4,186.6l-35.1,39.5l-37.8-29.5l-93.8,120.3c-1.9-1.2-7.4-3.7-7.9-4.4c-20-9.3-46.9-8.4-66.4,1.6L7.4,427.4v150.5L97.3,534
                                        c40.4,19,86.6,14.2,123.8-8.8l87.8,68.5l26.2-33.7l20.7,18.3l236.8-265.9L451.4,186.6z M329.3,540.5c-17-8.8-38.1-6-52,7.2
                                        l-69.9-54.8c-15.3,12.3-33.9,18.8-52.9,18.8c-8.6,0-17-1.4-25.3-3.9c-4.2-1.4-6.5-5.8-5.3-10c1.4-4.2,5.8-6.5,10-5.3
                                        c20.7,6.5,43.2,2.8,60.6-9.8l0,0c4.4-3.3,9.1-7.2,13.9-12.5l21.4-25.3l61.8,48.5c10.7,7.4,23.2,5.6,30.2-2.8
                                        c7.4-10.7,5.6-23.5-2.8-30.2l-111-89.4c-3.3-2.6-3.7-7.4-1.2-10.7c2.6-3.3,7.4-3.7,10.7-1.2l34.1,27.4L359,248.4
                                        c16.5,8.6,36.7,6,50.4-6.3l64.8,50.4c-8.8,17-5.8,37.8,7.4,51.8L329.3,540.5z M365.5,520.8l162.1-208.1L434,239.8
                                        c14.9,6.5,32.5,4.4,45.5-5.8l61.3,54.6c-9.8,16.3-8.4,37.4,3.9,52L379.6,526.1C375.2,523.8,370.6,521.9,365.5,520.8z" />
                                    </svg>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Prestamos pendientes de aprobación</span>
                            <%
                                if (request.getAttribute("prestamos") != null) {
                            %>
                            <h3 class="card-title mb-2 mt-auto"><%= request.getAttribute("prestamos") %></h3>
                            <%
                                } else {%>
                                    <h3 class="card-title mb-2 mt-auto">0</h3>
                            <% }
                            %>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-12 col-4 mb-4">
                    <div class="card h-100">
                        <div class="card-body d-flex flex-column">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <svg fill="#000000" height="64px" width="64px" version="1.1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 226 226" xmlns:xlink="http://www.w3.org/1999/xlink" enable-background="new 0 0 226 226">
                                        <g>
                                            <path d="M210.12,39.812L115.688,0.537c-1.721-0.716-3.655-0.716-5.376,0L15.88,39.812c-2.611,1.086-4.312,3.636-4.312,6.463V219 c0,3.866,3.134,7,7,7h188.864c3.866,0,7-3.134,7-7V46.276C214.432,43.448,212.731,40.898,210.12,39.812z M200.432,212H25.568 V50.946L113,14.582l87.432,36.364V212z" />
                                            <path d="m76.94,95.529l-29.667,29.667c-2.734,2.734-2.734,7.166 0,9.899 1.367,1.367 3.158,2.05 4.95,2.05s3.583-0.684 4.95-2.05l29.667-29.667c5.019-5.019 12.952-5.422 18.455-0.936 11.09,9.042 27.083,8.23 37.201-1.887l24.283-24.283v7.907c0,3.866 3.134,7 7,7s7-3.134 7-7v-24.703c0-3.866-3.134-7-7-7h-24.703c-3.866,0-7,3.134-7,7s3.134,7 7,7h7.7l-24.18,24.18c-5.019,5.019-12.954,5.422-18.455,0.937-11.09-9.044-27.083-8.233-37.201,1.886z" />
                                            <path d="m35.256,150.711c0,3.866 3.134,7 7,7h141.487c3.866,0 7-3.134 7-7s-3.134-7-7-7h-141.487c-3.866,0-7,3.133-7,7z" />
                                            <path d="m183.744,164.139h-141.488c-3.866,0-7,3.134-7,7s3.134,7 7,7h141.487c3.866,0 7-3.134 7-7s-3.133-7-6.999-7z" />
                                            <path d="m183.744,184.567h-141.488c-3.866,0-7,3.134-7,7s3.134,7 7,7h141.487c3.866,0 7-3.134 7-7s-3.133-7-6.999-7z" />
                                        </g>
                                    </svg>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Prestamos solicitados en el mes</span>
                            <%
                                if (request.getAttribute("prestamosSolicitados") != null) {
                            %>
                            <h3 class="card-title mb-2 mt-auto"><%= request.getAttribute("prestamosSolicitados") %></h3>
                            <%
                                } else {%>
                                    <h3 class="card-title mb-2 mt-auto">0</h3>
                            <% }
                            %>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-12 col-4 mb-4">
                    <div class="card h-100">
                        <div class="card-body d-flex flex-column">
                            <div class="card-title d-flex align-items-start justify-content-between">
                                <div class="avatar flex-shrink-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" height="64px" viewBox="0 -960 960 960" width="64px" fill="black">
                                        <path d="M200-200v-560 560Zm0 80q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v100h-80v-100H200v560h560v-100h80v100q0 33-23.5 56.5T760-120H200Zm320-160q-33 0-56.5-23.5T440-360v-240q0-33 23.5-56.5T520-680h280q33 0 56.5 23.5T880-600v240q0 33-23.5 56.5T800-280H520Zm280-80v-240H520v240h280Zm-160-60q25 0 42.5-17.5T700-480q0-25-17.5-42.5T640-540q-25 0-42.5 17.5T580-480q0 25 17.5 42.5T640-420Z" /></svg>
                                </div>
                            </div>
                            <span class="fw-semibold d-block mb-1">Cantidad de cuentas operativas</span>
                            <%
                                if (request.getAttribute("cuentasOperativas") != null) {
                            %>
                            <h3 class="card-title mb-2 mt-auto"><%= request.getAttribute("cuentasOperativas") %></h3>
                            <%
                                } else {%>
                                    <h3 class="card-title mb-2 mt-auto">0</h3>
                            <% }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%
    } catch (CustomSecurityException ex) {
%>
    <div class="alert alert-danger" role="alert">
        <strong>Error:</strong> <%= ex.getMessage() %>
    </div>

    <div class="centered-btn">
        <a href="Inicio.jsp" class="btn btn-danger">Ir al inicio de sesión</a>
    </div>
<%
    }
%>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
