<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="entities.Usuario"%>
<%@page import="entities.TipoUsuario"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.time.YearMonth"%>
<%@ page import="java.time.Year"%>
<%@ page import="java.util.List"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" charset="utf8"
	src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						var tableConfig = {
							language : {
								emptyTable : '"No hay datos para mostrar"',
								lengthMenu : "Mostrar _MENU_ registros por página",
								search : "Buscar:",
								info : "Mostrando del _START_ al _END_, de un total de _TOTAL_ entradas",
								paginate : {
									previous : "Anterior",
									next : "Siguiente",
								},
							}
						};
						$('.datatable').each(function() {
							$(this).DataTable(tableConfig);
						});
					});
	function tipoDeCalendario() {
		const startDate = document.getElementById('startDate');
		const endDate = document.getElementById('endDate');
		const selectedType = document
				.querySelector('input[name="type"]:checked').value;

		if (selectedType === 'Daily') {
			startDate.type = 'date';
			endDate.type = 'date';
		} else if (selectedType === 'Monthly') {
			startDate.type = 'month';
			endDate.type = 'month';
		} else if (selectedType === 'Yearly') {
			startDate.type = 'number';
			endDate.type = 'number';
			startDate.min = '1900';
			startDate.max = new Date().getFullYear().toString();
			endDate.min = '1900';
			endDate.max = new Date().getFullYear().toString();
		}
	}

	function validateDates(event) {
		const startDate = document.getElementById('startDate').value;
		const endDate = document.getElementById('endDate').value;
		const reportType = document.querySelector('input[name="type"]:checked');

		if (!reportType) {
			alert('Debes seleccionar al menos un tipo de reporte (Diario, Mensual o Anual).');
			event.preventDefault();
		} else if (new Date(startDate) > new Date(endDate)) {
			alert('La fecha de fin debe ser mayor o igual a la fecha de inicio.');
			event.preventDefault();
		}
	}
</script>
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

		<div class="container">
			<div class="col-lg-12 text-center mt-3">
				<h1>Reportes</h1>
			</div>
			<div
				class="col-md-6 offset-md-3 mt-5 bg-ligth rounded shadow-lg pt-3">
				<form method="get" action="ReporteServlet"
					onsubmit="validateDates(event)">
					<div class="form-group">
						<div class="row">
							<div class="text-center mb-4">
								<h4 style="color: gray;">Seleccione fechas:</h4>
							</div>
						</div>
						<div class="row justify-content-center">
							<div class="col-md-6 px-4">
								<label for="startDate">Fecha de inicio:</label> <input
									type="date" class="form-control" id="startDate"
									name="startDate" required
									onfocus="this.max=new Date().toISOString().split('T')[0]">
							</div>
							<div class="col-md-6 px-4">
								<label for="endDate">Fecha de fin:</label> <input type="date"
									class="form-control" id="endDate" name="endDate" required
									onfocus="this.max=new Date().toISOString().split('T')[0]">
							</div>
						</div>
						<div class="row mt-3 justify-content-center">
							<div class="col-md-9">
								<h6 class="py-3">
									<label for="Choose Report" style="color: gray">Periodo:</label>
									<div class="form-check form-check-inline mx-2 my-1">
										<input id="a" type="radio" class="form-check-input"
											name="type" value="Daily"> <label
											class="form-check-label" for="a" required>Diario</label>
									</div>
									<div class="form-check form-check-inline mx-2 my-1">
										<input id="c" type="radio" class="form-check-input"
											name="type" value="Monthly"> <label
											class="form-check-label" for="b" required>Mensual</label>
									</div>
									<div class="form-check form-check-inline mx-2 my-1">
										<input id="c" type="radio" class="form-check-input"
											name="type" value="Yearly"> <label
											class="form-check-label" for="c" required>Anual</label>
									</div>
								</h6>
							</div>
						</div>
						<div class="row mt-3 justify-content-center">
							<div class="col-md-6 text-center my-2">
								<button type="submit"
									class="btn btn btn-outline-secondary btn-lg btn-block">
									<svg fill="#000000" width="30px" height="30px" version="1.1"
										id="Layer_1" xmlns="http://www.w3.org/2000/svg"
										xmlns:xlink="http://www.w3.org/1999/xlink"
										viewBox="0 0 490 490" xml:space="preserve"> <g> <g>
									<g> <path
										d="M480,325h-5V45c0-5.523-4.477-10-10-10H300V10c0-5.523-4.477-10-10-10h-90c-5.523,0-10,4.477-10,10v25H25
				c-5.523,0-10,4.477-10,10v280h-5c-5.523,0-10,4.477-10,10v35c0,5.523,4.477,10,10,10h152.338l-50.913,84.855l17.149,10.29
				L185.662,380H235v110h20V380h49.338l57.087,95.145l17.149-10.29L327.662,380H480c5.523,0,10-4.477,10-10v-35
				C490,329.477,485.523,325,480,325z M210,20h70v15h-70V20z M35,55h420v270H35V55z M470,360H20v-15h450V360z" />
									<path
										d="M170,90c-55.14,0-100,44.86-100,100s44.86,100,100,100s100-44.86,100-100S225.14,90,170,90z M170,270
				c-44.112,0-80-35.888-80-80c0-40.724,30.593-74.413,70-79.353V190c0,5.523,4.477,10,10,10h79.353
				C244.413,239.407,210.724,270,170,270z M180,180v-69.353c36.128,4.529,64.824,33.225,69.353,69.353H180z" />
									<rect x="345" y="130" width="70" height="20" /> <rect x="345"
										y="160" width="70" height="20" /> <rect x="345" y="190"
										width="70" height="20" /> <rect x="345" y="100" width="45"
										height="20" /> <path
										d="M324.999,119.999v-20h-45c-2.652,0-5.196,1.054-7.071,2.929l-15,15l14.143,14.143l12.07-12.072H324.999z" />
									<rect x="310" y="235" width="115" height="20" /> <rect x="280"
										y="270" width="145" height="20" /> </g> </g> </g> </svg>
									Generar informe
								</button>
							</div>
						</div>
					</div>
				</form>
			</div>

			<%
			if (request.getAttribute("cuentasPorFecha") != null) {
			%>
			<div class="panel-body py-5">
				<table id="table_cuentas"
					class="table table-striped table-condensed display datatable">
					<h3 class="text-center">Cuentas</h3>
					<thead>
						<tr>
							<th class="text-center">Fecha</th>
							<th class="text-center">Nuevas cuentas</th>
							<th class="text-center">Cajas de ahorro</th>
							<th class="text-center">Cuentas corrientes</th>
						</tr>
					</thead>
					<tbody>
						<%
							for (Map.Entry<?, List<Long>> entry : ((Map<?, List<Long>>) request.getAttribute("cuentasPorFecha"))
										.entrySet()) {
									Object periodo = entry.getKey();
									List<Long> cantidades = entry.getValue();
									Long totalCuentas = cantidades.get(0);
									Long cajaAhorroCuentas = cantidades.get(1);
									Long cuentaCorrienteCuentas = cantidades.get(2);
									if (totalCuentas != 0) {
						%>
						<tr>
							<td class="text-center"><%=periodo%></td>
							<td class="text-center"><%=totalCuentas%></td>
							<td class="text-center"><%=cajaAhorroCuentas%></td>
							<td class="text-center"><%=cuentaCorrienteCuentas%></td>
						</tr>
						<%
							}
								}
						%>

					</tbody>
				</table>
			</div>
			<%
				}
			%>
			<%
			if (request.getAttribute("prestamosPorFecha") != null) {
			%>
			<div class="panel-body py-5">
				<table id="table_prestamos"
					class="table table-striped table-condensed display datatable">
					<h3 class="text-center">Prestamos</h3>
					<thead>
						<tr>
							<th class="text-center">Fecha</th>
							<th class="text-center">Cantidad de prestamos solicitados</th>
							<th class="text-center">Prestamos Aprobados</th>
							<th class="text-center">Prestamos Rechazados</th>
							<th class="text-center">Prestamos en revisión</th>
						</tr>
					</thead>
					<tbody>
						<%
						for (Map.Entry<?, List<Long>> entry : ((Map<?, List<Long>>) request.getAttribute("prestamosPorFecha"))
								.entrySet()) {
							Object periodo = entry.getKey();
							List<Long> cantidades = entry.getValue();
							Long totalPrestamos = cantidades.get(0);
							Long prestamosAprobados = cantidades.get(1);
							Long prestamosDesaprobados = cantidades.get(2);
							Long prestamosRevision = cantidades.get(3);
							if (totalPrestamos != 0) {
						%>
						<tr>
							<td class="text-center"><%=periodo%></td>
							<td class="text-center"><%=totalPrestamos%></td>
							<td class="text-center"><%=prestamosAprobados%></td>
							<td class="text-center"><%=prestamosDesaprobados%></td>
							<td class="text-center"><%=prestamosRevision%></td>
						</tr>
						<%
							}
								}
						%>
					</tbody>
				</table>
			</div>
			<%
				}
			%>
			<%
				if (request.getAttribute("historial") != null) {
			%>
			<div class="panel-body py-5">
				<table id="table_movimientos"
					class="table table-striped table-condensed display datatable">
					<h3 class="text-center">Movimientos</h3>
					<thead>
						<tr>
							<th class="text-center">Fecha</th>
							<th class="text-center">Cantidad de transacciones</th>
							<th class="text-center">Movimientos por Alta de cuentas</th>
							<th class="text-center">Movimientos por Alta de prestamo</th>
							<th class="text-center">Movimientos por Pago de cuota prestamo</th>
							<th class="text-center">Transferencias</th>
						</tr>
					</thead>
					<tbody>
						<%
						for (Map.Entry<?, List<Long>> entry : ((Map<?, List<Long>>) request.getAttribute("historial"))
								.entrySet()) {
							Object periodo = entry.getKey();
							List<Long> cantidades = entry.getValue();
							Long totalMovimientos = cantidades.get(0);
							Long movAltaCuenta = cantidades.get(1);
							Long movAltaPrestamo = cantidades.get(2);
							Long movPagoPrestamo = cantidades.get(3);
							Long movTransferencia = cantidades.get(4);
							if (totalMovimientos != 0) {
						%>
						<tr>
							<td class="text-center"><%=periodo%></td>
							<td class="text-center"><%=totalMovimientos%></td>
							<td class="text-center"><%=movAltaCuenta%></td>
							<td class="text-center"><%=movAltaPrestamo%></td>
							<td class="text-center"><%=movPagoPrestamo%></td>
							<td class="text-center"><%=movTransferencia%></td>
						</tr>
						<%
									}
							}
						%>
					</tbody>
				</table>
			</div>
			<%
				}
			%>
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
</body>
</html>