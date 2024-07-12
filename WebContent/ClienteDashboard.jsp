<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="entities.Usuario"%>
<%@page import="entities.Cuenta"%>
<%@page import="java.util.*"%>
<%@page import="entities.Movimiento"%>
<%@page import="entities.Prestamo"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTM
L 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Panel de Gestion</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<style>
.centered-btn {
	text-align: center;
	margin-top: 20px;
}
</style>
</head>
<body class="bg-body">

	<%
		try {
			Usuario usuarioLog = (Usuario) session.getAttribute("usuario");
			if ((usuarioLog == null || usuarioLog.getUsuario().isEmpty())) {
				throw new CustomSecurityException(
						"El usuario no tiene los permisos necesarios por favor Ingrese sus credenciales");
			}
	%>

	<%
		String User = usuarioLog.getUsuario();
	%>

	<div class="d-flex vh-100">
		<jsp:include page="Sidebar.jsp"></jsp:include>

		<div class="container-fluid mt-5">
			<div
				class="d-sm-flex align-items-center justify-content-between mb-4">
				<h1 class="h3 mb-0 text-gray-800">Bienvenido!</h1>

			</div>
			<!-- Ultimos movimientos (5 ultimos movimientos) -->
			<div class="row">

				<div class="col-xl-7 col-lg-5">
					<div class="card shadow mb-4 h-75 w-100">
						<div
							class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
							<h6 class="m-0 font-weight-bold text-primary">Movimientos</h6>
							<a href="HistorialServlet?load=1	"> <i
								class="bi bi-box-arrow-in-up-right"> </i>
							</a>

						</div>
						<div class="card-body overflow-auto">
							<div class="chart-area">
								<%
									if (request.getAttribute("movimientos") != null) {
											List<Movimiento> movimientos = (List<Movimiento>) request.getAttribute("movimientos");
											if (movimientos != null && !movimientos.isEmpty()) {
								%>
								<table class="table">
									<thead>
										<tr>
											<th>Destinatario</th>
											<th>Fecha</th>
											<th>Detalle</th>
											<th>Importe</th>
											<th>Tipo de movimiento</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (Movimiento movimiento : movimientos) {
										%>
										<tr>
											<td><%=movimiento.getCuentaDestino().getCbu()%></td>
											<td><%=movimiento.getFecha()%></td>
											<td><%=movimiento.getDetalle()%></td>
											<td><%=movimiento.getImporte().toString().format("$ %.2f", movimiento.getImporte())%></td>
											<td><%=movimiento.getTipoMovimiento()%></td>
										</tr>
										<%
											}
										%>
									</tbody>
								</table>
								<%
									}
										}
								%>
							</div>
						</div>
					</div>
				</div>
				<!-- Ultima cuota de cada prestamo -->
				<div class="col-xl-5 col-lg-12">
					<div class="card shadow mb-4 h-75">
						<div
							class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
							<h6 class="m-0 font-weight-bold text-primary">Prestamos</h6>
							<a href="PedirPrestamoServlet?load=1"> <i
								class="bi bi-box-arrow-in-up-right"> </i>
							</a>
						</div>
						<!-- Card Body -->
						<div class="card-body overflow-auto">
							<%
								if (request.getSession().getAttribute("prestamos") != null) {
							%>
							<table class="table text-center" style="font-size: 0.8rem;">
								<thead>
									<tr>
										<td>Fecha de contratacion</td>
										<td>Importe total</td>
										<td>Plazo de pago</td>
										<td>Monto por mes</td>
										<td>Estado</td>
									</tr>

								</thead>
								<tbody>
									<%
										List<Prestamo> prestamos = (List<Prestamo>) request.getSession().getAttribute("prestamos");
												for (Prestamo prestamo : prestamos) {
									%>
									<tr>
										<td><%=prestamo.getFechaDeContratacion()%></td>
										<td><%=prestamo.getImporteTotal().toString().format("$ %.2f", prestamo.getImporteTotal())%></td>
										<td><%=prestamo.getPlazoEnMeses()%></td>
										<td><%=prestamo.getMontoPorMes().toString().format("$ %.2f", prestamo.getMontoPorMes())%></td>
										<td><%=prestamo.getEstado()%></td>
									</tr>
									<%
										}
									%>
								</tbody>

							</table>
							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-lg-6 mb-4">
					<!-- Project Card Example -->
					<div class="card shadow mb-4">
						<div
							class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
							<h6 class="m-0 font-weight-bold text-primary">Cuentas</h6>
							<a href="CuentaServlet?load=1"> <i
								class="bi bi-box-arrow-in-up-right"> </i>
							</a>

						</div>
						<div class="card-body">
							<%
								if (request.getSession().getAttribute("cuentas") != null) {
										List<Cuenta> cuentas = (List<Cuenta>) request.getSession().getAttribute("cuentas");
							%>
							<table class="table">
								<thead>
									<tr>
										<td>CBU</td>
										<td>Fecha de creaci�n</td>
										<td>Tipo de cuenta</td>
										<td>Saldo</td>
									</tr>

								</thead>
								<%
									if (!cuentas.isEmpty()) {
												for (Cuenta cuenta : cuentas) {
								%>
								<tbody>
									<tr>
										<td><%=cuenta.getCbu()%></td>
										<td><%=cuenta.getFechaCreacion()%></td>
										<td><%=cuenta.getTipoCuenta().getNombre()%></td>
										<td><%=cuenta.getSaldo().toString().format("$ %.2f", cuenta.getSaldo())%></td>
									</tr>


								</tbody>
								<%
									}
											}
								%>
							</table>
							<%
								}
							%>
						</div>
					</div>
				</div>
				<div class="col-lg-6 mb-4">
					<div class="card shadow mb-4">
						<div class="card-header py-3">
							<h6 class="m-0 font-weight-bold text-primary"></h6>
						</div>
						<div class="card-body">
							<div class="text-center">
								<img class="img-fluid px-3 px-sm-4 mt-3 mb-4"
									style="width: 15rem;" src="Resources/cliente-dashboard.svg">
							</div>
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
		<strong>Error:</strong>
		<%=ex.getMessage()%>
	</div>

	<div class="centered-btn">
		<a href="Inicio.jsp" class="btn btn-danger">Ir al inicio de sesi�n</a>
	</div>
	<%
		}
	%>
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>