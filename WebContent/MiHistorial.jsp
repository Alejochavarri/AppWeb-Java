<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="entities.Movimiento" %>
<%@ page import="entities.Usuario"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Historial</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.min.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
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
        <h2 class="text-center mb-4">Historial de Movimientos</h2>
        <div class="card mb-4">
            <div class="card-body">
                <form id="filtros-form" method="post" action="HistorialServlet">
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label for="periodo">Periodo</label>
                            <select id="periodo" class="form-control" name="periodo">
                                <option selected>Todo</option>
                                <option>Ultima semana</option>
                                <option>Ultimo mes</option>
                                <option>Ultimo ano</option>
                            </select>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="operacion">Movimientos</label>
                            <select id="operacion" class="form-control" name="operacion">
                                <option selected>Todo</option>
                                <option>Alta de Cuenta</option>
                                <option>Alta de Prestamo</option>
                                <option>Pago Prestamo</option>
                                <option>Transferencia</option>
                            </select>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="estado">Tipo de Cuenta</label>
                            <select id="estado" class="form-control" name="estado">
                                <option selected>Todo</option>
                                <option>Corriente</option>
                                <option>Ahorro</option>
                            </select>
                        </div>
                        <!-- 
                        	<div class="form-group col-md-3">
                            <label for="medio-pago">Pagos</label>
                            <select id="medio-pago" class="form-control">
                                <option selected>Todo...</option>
                                <option>Men</option>
                                <option>Tarjeta de D�bito</option>
                                <option>Cuenta Bancaria</option>
                            </select>
                        </div>
                         -->
                    </div>
                    <button type="submit" class="btn btn-primary">Aplicar Filtros</button>
                </form>
            </div>
        </div>
        <table id="table_id" class='display text-center'>
            <thead>
                <tr>
                    <th>Fecha</th>
                    <th>Descripcion</th>
                    <th>Monto</th>
                    <th>Movimiento</th>
                    <th>Tipo de Cuenta</th>
                    <th>CBU</th>
                </tr>
            </thead>
            <tbody>
                <% 
	            List<Movimiento> movimientos = (List<Movimiento>) request.getAttribute("movimientos"); 
	            if (movimientos != null && !movimientos.isEmpty()) { 
	                for (Movimiento movimiento : movimientos) { 
	        %>
	        <tr>
	            <td><%= movimiento.getFecha() %></td>
	            <td><%= movimiento.getDetalle() %></td>
	            <td><%= movimiento.getImporte().toString().format("$ %.2f", movimiento.getImporte()) %></td>
	            <td><%= movimiento.getTipoMovimiento() %></td>
	            <td><%= movimiento.getCuentaOrigen().getTipoCuenta().getNombre() %></td>
	            <td><%= movimiento.getCuentaOrigen().getCbu() %></td>            
	        </tr>
	        <% 
	                } 
	            } else { 
	        %>
	        <tr>
	            <td colspan="6" class="text-center">No se encontraron movimientos.</td>
	        </tr>
	        <% 
	            } 
	        %>
            </tbody>
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
					lengthMenu : "Mostrar _MENU_ registros por página",
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
        <a href="Inicio.jsp" class="btn btn-danger">Ir al inicio de sesión</a>
    </div>
<%
    }
%>
</body>
</html>