<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.util.List"%>
<%@ page import="entities.Cuenta"%>
<%@ page import="entities.Prestamo"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.min.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<title>Pedir prÃ©stamo</title>
</head>
<body>

	<div class="d-flex vh-100">
		<jsp:include page="Sidebar.jsp"></jsp:include>

		<div class="container-fluid m-5">
			<%
				if (request.getAttribute("success") != null && Boolean.TRUE.equals(request.getAttribute("success"))) {
			%>
			<div class="alert alert-success" role="alert">Prestamo
				solicitado con exito!</div>
			<%
				} else if (request.getAttribute("error") != null) {
			%>
			<div class="alert alert-danger" role="alert">Oops...Hubo un
				error!</div>

			<%
				}
			%>


			<div class="row mb-3">
				<div class="d-flex justify-content-start w-100 gap-3">
					<h3>Tus prestamos recientes</h3>
					<button class="btn btn-primary" data-bs-toggle="modal"
						data-bs-target="#solicitarPrestamoModal">Solicitar
						prestamo</button>
				</div>
			</div>

			<div class="row p-3 mb-4 bg-white rounded shadow-lg">

				<table id="table_id" class="table text-center">
					<thead>
						<tr>
							<th>Fecha de contratacion</th>
							<th>Monto por mes</th>
							<th>Importe total</th>
							<th>Cuotas
							<th>
						</tr>
					</thead>
					<tbody>
						<%
							if (request.getAttribute("prestamos") != null) {
								List<Prestamo> prestamos = (List<Prestamo>) request.getAttribute("prestamos");
								for (Prestamo prestamo : prestamos) {
						%>
						<tr>
							<td><%=prestamo.getFechaDeContratacion()%></td>
							<td><%=prestamo.getMontoPorMes().toString().format("$ %.2f", prestamo.getMontoPorMes())%></td>
							<td><%=prestamo.getImporteTotal().toString().format("$ %.2f", prestamo.getImporteTotal())%></td>
							<td><%=prestamo.getCuotas()%></td>
							<td><%=prestamo.getEstado()%></td>
						</tr>
						<%
							}
							}
						%>


					</tbody>
				</table>
			</div>
		</div>

	</div>

	<div class="modal fade" id="solicitarPrestamoModal" tabindex="-1"
		aria-labelledby="solicitarPrestamoModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="solicitarPrestamoModal">Solicitar
						prestamo</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form action="PedirPrestamoServlet" method="post">
					<div class="modal-body">

						<div class="col-md-12 mb-2">
							<label for="fecha_contratacion" class="form-label">Fecha
								contratacion</label> <input readonly="<%=Boolean.TRUE%>" type="date"
								class="form-control" id="fecha_contratacion"
								name="fecha_contratacion" value="<%=LocalDate.now()%>">
						</div>
						<div class="col-md-12 mb-2">
							<label for="cuenta" class="form-label">Cuenta</label> <select
								class="form-control" id="cuenta" name="cuenta" required>
								<%
									if (request.getSession().getAttribute("cuentas") != null) {
										List<Cuenta> cuentas = (List<Cuenta>) request.getSession().getAttribute("cuentas");
										for (Cuenta cuenta : cuentas) {
								%>
								<option value=<%=cuenta.getId()%>><%=cuenta.getCbu()%></option>
								<%
									}
									}
								%>
							</select>
						</div>
						<div class="col-md-12 mb-2">
							<label for="importe_pedido" class="form-label">Importe</label>
							<div class="input-group">
							<span class="input-group-text">$</span> 
							 <input	type="number" class="form-control" id="importe_pedido"
								name="importe_pedido" required>
								</div>
						</div>
						<div class="col-md-12 mb-2">
							<label for="intereses" class="form-label">Intereses</label> <input
								type="text" class="form-control" id="intereses" name="intereses"
								readonly="<%=Boolean.TRUE%>" value="2%">
						</div>
						<div class="col-md-12 mb-2">
							<label for="plazo_pago_mes" class="form-label">Plazo de
								pago (meses)</label> <select id="plazo_pago_mes" class="form-control"
								name="plazo_pago_mes" required>
								<%
									for (int i = 1; i < 13; i++) {
								%>
								<option value=<%=i%>><%=i%></option>
								<%
									}
								%>
							</select>
						</div>
						<div class="col-md-12 mb-2">
							<label for="totalPrestamo" class="form-label">Importe
								Total</label> 
								<div class="input-group">
								<span class="input-group-text">$</span> 
								<input type="text" class="form-control" id="totalPrestamo"
								name="totalPrestamo" readonly="<%=Boolean.TRUE%>">
								</div>
							<script>
								document.addEventListener("DOMContentLoaded",function() {
									var importePedido = document.getElementById('importe_pedido');
									var plazoPagoMes = document.getElementById('plazo_pago_mes');
									var totalPrestamo = document.getElementById('totalPrestamo');
									function calculateTotal() {
										var importe = parseFloat(importePedido.value);
										var plazo = parseInt(plazoPagoMes.value);
										var total = importe * (1 + (0.02 / 12 * plazo));
										totalPrestamo.value = total.toFixed(2);
													}

										importePedido.addEventListener('input',calculateTotal);
										plazoPagoMes.addEventListener('change',calculateTotal);
												});
							</script>
						</div>
						<div class="col-md-12 mb-2">
							<label for="valorCuota" class="form-label">Valor cuota</label>
							<div class="input-group">
								<span class="input-group-text">$</span> <input type="text"
									class="form-control" id="valorCuota" name="valorCuota"
									readonly=<%=Boolean.TRUE%>>
							</div>
							<script>
								document.addEventListener("DOMContentLoaded",function() {
									var importePedido = document.getElementById('importe_pedido');
									var plazoPagoMes = document.getElementById('plazo_pago_mes');
									var valorCuota = document.getElementById('valorCuota');

									function calculateCuota() {
										var importe = parseFloat(importePedido.value);
										var plazo = parseInt(plazoPagoMes.value);
										var interes = 0.02 / 12
										var totalCuota = (importe / plazo) + interes * importe;
										valorCuota.value = totalCuota.toFixed(2);
													}
											importePedido.addEventListener('input',calculateCuota);
											plazoPagoMes.addEventListener('change',calculateCuota);
										});
							</script>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancelar</button>
						<button type="submit" id="btnPedirPrestamo"
							class="btn btn-primary" name="pedir_prestamo">Solicitar</button>
					</div>
				</form>
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
			language: {
				emptyTable : '"No hay datos para mostrar"',
				lengthMenu : "Mostrar _MENU_ registros por página",
				search : "Buscar:",
				info : "Mostrando del _START_ al _END_, de un total de _TOTAL_ entradas",
				paginate : {
					previous : "Anterior",
					next : "Siguiente",
				},
			
			}
		});

		$('#btnPedirPrestamo').on('click', function() {
			// Acciones al hacer clic en el botón
		});
	});
	</script>
</body>
</html>
