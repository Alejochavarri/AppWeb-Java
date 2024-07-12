<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.util.List"%>
<%@ page import="entities.Cuenta"%>
<%@ page import="entities.Prestamo"%>
<%@page import="entities.Usuario"%>
<%@page import="entities.TipoUsuario"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
                || !(usuarioLog.getTipoUsuario().getNombre().equals("ADMINISTRADOR"))) {
            throw new CustomSecurityException("El usuario no tiene los permisos necesarios por favor Ingrese sus credenciales");
        }
%>
	<div class="d-flex">
		<jsp:include page="Sidebar.jsp"></jsp:include>

		<div class="container-fluid m-4">
			<div class="row mb-3">
				<%
					if (request.getAttribute("error") != null) {
				%>
				<div class="alert alert-danger" role="alert">Oops!...Hubo un
					error</div>
				<%
					}
				%>
			</div>
			<%if(request.getAttribute("cambiarEstado") != null && request.getAttribute("cambiarEstado") == Boolean.TRUE){
				%>
				<div class="alert alert-success">
					Prestamo actualizado correctamente!
				</div>
			<% }%>
			<div class="row mb-3">
				<h3>Prestamos a aprobar</h3>
				<h5>Utilice los filtros para encontrar los prestamos</h5>
			</div>
			<form id="formularioBusqueda" action="AutorizarPrestamoServlet" method="post"
				class="mb-3 g-3 p-3 needs-validation" novalidate>
				<div class="row mb-3">

					<div class="col-md-2">
						<label for="fecha_contratacion_desde">Fecha desde</label> <input
							type="date" class="form-control" id="fecha_contratacion_desde"
							name="fecha_contratacion_desde">
					</div>
					<div class="col-md-2">
						<label for="fecha_contratacion_hasta">Fecha hasta</label> <input
							type="date" class="form-control" id="fecha_contratacion_hasta"
							name="fecha_contratacion_hasta">

					</div>
					<div class="col-md-2">
						<label for="cuenta">Cuenta</label> <select class="form-control"
							id="cuenta" name="cuenta">
							<%
								if (request.getAttribute("cuentas") != null) {
									List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentas");
									for (Cuenta cuenta : cuentas) {
							%>
							<option></option>
							<option value=<%=cuenta.getId()%>><%=cuenta.getCbu()%></option>
							<%
								}
								}
							%>
						</select>
					</div>
					<div class="col-md-2">
						<label for="importe_pedido_desde">Importe desde</label> <input
							type="number" class="form-control" id="importe_pedido_desde"
							name="importe_pedido_desde">
					</div>
					<div class="col-md-2">
						<label for="importe_pedido_hasta">Importe hasta</label> <input
							type="number" class="form-control" id="importe_pedido_hasta"
							name="importe_pedido_hasta">
					</div>
					<div class="col-md-2">
						<label class="d-inline-block text-truncate" style="max-width: 150px;" data-toggle="tooltip" data-html="true" title="Plazo de pago hasta" style="max-width: 150px;" for="plazo_pago_mes_desde">Plazo de pago desde
							(meses)</label> <select id="plazo_pago_mes_desde" class="form-control"
							name="plazo_pago_mes_desde">
							<%
								for (int i = 1; i < 24; i++) {
							%>
							<option value=<%=i%>>
								<%=i%></option>

							<%
								}
							%>
						</select>
					</div>
					<div class="col-md-2">
						<label class="d-inline-block text-truncate" data-toggle="tooltip" data-html="true" title="Plazo de pago hasta" style="max-width: 250px;" for="plazo_pago_mes_hasta">Plazo de pago hasta
							(meses)</label> <select id="plazo_pago_mes_hasta" class="form-control"
							name="plazo_pago_mes_hasta">
							<%
								for (int i = 1; i < 24; i++) {
							%>
							<option value=<%=i%>>
								<%=i%></option>

							<%
								}
							%>
						</select>
					</div>
					<div class="col-md-2 d-flex align-items-end">
						<button class="btn btn-primary mt-3" type="submit" name="buscar">Buscar</button>
					</div>
				</div>
			</form>
			<div class="row mb-2">
		        <form id="resetBusqueda" action="AutorizarPrestamoServlet?load=1" method="get">
		            <button class="btn btn-secondary" type="submit" name="resetFiltros">Reset</button>
		        </form>
			</div>
			<% if (request.getAttribute("prestamos") != null){
				%>
				
				<div class="row mt-3 shadow-lg p-3 overflow-x-scroll">
					<div class="col-12">
						<table id="table_id" class="table table-stripped text-center align-middle">
							<thead>
								<tr>
									<th>Fecha de contratacion</th>
									<th>Monto por mes</th>
									<th>Importe total</th>
									<th>Plazo de pago</th>
									<th>Importe pedido</th>
									<th>Monto por mes</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
						<%
							
								List<Prestamo> prestamos = (List<Prestamo>) request.getAttribute("prestamos");
								for (Prestamo prestamo : prestamos) {
						%>
								<tr>
									<td><%=prestamo.getFechaDeContratacion()%></td>
									<td><%=prestamo.getMontoPorMes().toString().format("$ %.2f", prestamo.getMontoPorMes())%></td>
									<td><%=prestamo.getImporteTotal().toString().format("$ %.2f", prestamo.getImporteTotal())%></td>
									<td><%=prestamo.getPlazoEnMeses() %></td>
									<td><%=prestamo.getImportePedido().toString().format("$ %.2f", prestamo.getImportePedido()) %></td>
									<td><%=prestamo.getMontoPorMes() %></td>
									<td>
										<button class="btn btn-success" data-id="<%=prestamo.getId() %>" data-bs-toggle="modal" data-bs-target="#confirmarPrestamoModal">Aprobar</button>
										<button class="btn btn-danger" data-id="<%=prestamo.getId()%>" data-bs-toggle="modal" data-bs-target="#rechazarPrestamoModal">Rechazar</button>
									</td>
								</tr>
	
	
						<%
							}
						%>
							</tbody>
						</table>
	
					</div>
				</div>
			</div>
				
			 <% }%>
	</div>
	
	<div class="modal fade" id="confirmarPrestamoModal" tabindex="-1" aria-labelledby="confirmarPrestamoModal" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h1 class="modal-title fs-5" id="confirmarPrestamoModalLabel">Aprobar prestamo</h1>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	        <p>Esta seguro que desea confimar el prestamo?</p>
	        <input type="hidden" id="idPrestamo" name="idPrestamo">
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
	        <button type="button" id="btnAprobarPrestamo" class="btn btn-success">Confirmar</button>
	      </div>
	    </div>
	  </div>
	</div>
	<div class="modal fade" id="rechazarPrestamoModal" tabindex="-1" aria-labelledby="rechazarPrestamoModal" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h1 class="modal-title fs-5" id="confirmarPrestamoModalLabel">Rechazar prestamo</h1>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	        <p>Esta seguro que desea rechazar el prestamo?</p>
	        <input type="hidden" id="idPrestamoRechazar" name="idPrestamoRechazar">
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
	        <button type="button" id="btnRechazarPrestamo" class="btn btn-danger">Confirmar</button>
	      </div>
	    </div>
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
					lengthMenu : "Mostrar _MENU_ registros",
					search : "Buscar:",
					info : "Mostrando del _START_ al _END_, de un total de _TOTAL_ entradas",
					paginate : {
						previous : "Anterior",
						next : "Siguiente",
					},
				
				},
			});
			$('#confirmarPrestamoModal').on('show.bs.modal', function(event){
				
				var button = $(event.relatedTarget);
				var id = button.data('id');
				var modal = $(this)
				modal.find('.modal-body #idPrestamo').val(id);
				
			})
			$('#btnAprobarPrestamo').on('click', function(){
				var id = $('#idPrestamo').val();
				window.location.href = 'AutorizarPrestamoServlet?aprobar=1&idPrestamo=' + id;
			})
			$('#rechazarPrestamoModal').on('show.bs.modal', function(event){
				var button = $(event.relatedTarget);
				var id = button.data('id')
				var modal = $(this)
				modal.find('.modal-body #idPrestamoRechazar').val(id);
			})
			$('#btnRechazarPrestamo').on('click', function(){
				var id = $('#idPrestamoRechazar').val();
				window.location.href = 'AutorizarPrestamoServlet?rechazar=1&idPrestamo=' + id;
			})
			$('button[name="resetFiltros"]').click(function() {
                $('#formularioBusqueda')[0].reset();
            });
			$('#formularioBusqueda').on('submit', function(e){
				
				
				if(this.checkValidity() === false){
					
					event.preventDefault();
					event.stopPropagation();
				}
				$(this).addClass("was-validated");
			})
			
		});
		$(function () {
			  $('[data-toggle="tooltip"]').tooltip()
			})
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