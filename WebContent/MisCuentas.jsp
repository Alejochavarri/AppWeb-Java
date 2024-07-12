<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="entities.Cuenta" %>
<%@ page import="entities.Usuario"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mis Cuentas</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.min.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<style>
    .compact-btn {
        padding: 0.2rem 0.5rem;
        font-size: 0.875rem;
        margin: 0;
    }
    .compact-form {
        margin: 0;
    }
</style>
</head>
<body>
<%
    try {
        Usuario usuarioLog = (Usuario) session.getAttribute("usuario");
        if ((usuarioLog == null || usuarioLog.getUsuario().isEmpty())
                || !(usuarioLog.getTipoUsuario().getNombre().equals("CLIENTE"))) {
            throw new CustomSecurityException("El usuario no tiene los permisos necesarios por favor Ingrese sus credenciales");
        }
%>
	<div class="d-flex vh-100">
	<jsp:include page="Sidebar.jsp"></jsp:include>
	<div class="container mt-5">
    <h2 class="text-center mb-4">Mis Cuentas</h2>
	<table id="table_id" class='display text-center'>
            <thead>
                <tr>
                    <th>Tipo de Cuenta</th>
                    <th>Fecha de Creacion</th>
                    <th>Cbu</th>
                    <th>Saldo</th>
                    <th>Historial</th>	
                </tr>
            </thead>
		    <tbody>
		        <% 
		            List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentas"); 
		            if (cuentas != null && !cuentas.isEmpty()) { 
		                for (Cuenta cuenta : cuentas) { 
		        %>
		        <tr>
		            <td><%= cuenta.getTipoCuenta().getNombre() %></td>
		            <td><%= cuenta.getFechaCreacion() %></td>
		            <td><%= cuenta.getCbu() %></td>
		            <td><%= cuenta.getSaldo().toString().format("$ %.2f", cuenta.getSaldo()) %></td>
		            <td>
			            <form action="HistorialServlet" method="get" class="compact-form">
			            <input type="hidden" name="cuenta" value="<%= cuenta.getId() %>">
			            	<button type="submit" class="btn btn-primary btn-sm compact-btn">Ver movimientos</button>
			            </form>
		            </td> 
		        </tr>
		        <% 
		                } 
		            } else { 
		        %>
		        <tr>
		            <td colspan="4" class="text-center">No se encontraron cuentas.</td>
		        </tr>
		        <% 
		            } 
		        %>
       	 </table>
        </div>
    </div>
    
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script
		src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
	<script>
		$(document).ready(function() {
			$('#table_id').DataTable({
				responsive : true,
				language: {
					emptyTable : '"No hay datos para mostrar"',
					lengthMenu : "Mostrar _MENU_ registros por p�gina",
					search : "Buscar:",
					info : "Mostrando del _START_ al _END_, de un total de _TOTAL_ entradas",
					paginate : {
						previous : "Anterior",
						next : "Siguiente",
					},
				
				},
			});
		});
	</script>
		    <%
    } catch (CustomSecurityException ex) {
%>
    <div class="alert alert-danger" role="alert">
        <strong>Error:</strong> <%= ex.getMessage() %>
    </div>

    <div class="centered-btn">
        <a href="Inicio.jsp" class="btn btn-danger">Ir al inicio de sesi�n</a>
    </div>
<%
    }
%>
</body>
</html>