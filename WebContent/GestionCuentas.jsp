<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="entities.Usuario"%>
<%@page import="entities.Cuenta"%>
<%@page import="entities.Cliente"%>
<%@page import="entities.TipoCuenta"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gestion de cuentas</title>
<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" charset="utf8"
	src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#table_id').DataTable({
			searching : false,
			paging : false,
			info : false,
			language : {
				emptyTable : 'El cliente no posee ninguna cuenta activa'
			}
		});

		$('#table_inactive').DataTable({
			searching : false,
			paging : false,
			info : false,
			language : {
				emptyTable : 'El cliente no posee ninguna cuenta inactiva'
			}
		});
	});
</script>
<style>
input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
	-webkit-appearance: none;
	margin: 0;
}

;
input[type=number] {
	-moz-appearance: textfield;
}
</style>
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

		<%
			List<Cuenta> listaC = new ArrayList<Cuenta>();
			if (request.getAttribute("cuentas") != null) {
				listaC = (List<Cuenta>) request.getAttribute("cuentas");
			}
			int cuentasActivas = 0;
			for (Cuenta cuenta : listaC) {
				if (cuenta.isDeleted() == false) {
					cuentasActivas++;
				}
			}
		%>
		<div class="container m-4 vh-100">
			<%
                if (request.getAttribute("operacion") != null && request.getAttribute("operacion").equals("OK")) {
            %>
            <div class="alert alert-success" role="alert">Cuenta creada
                correctamente</div>
            <%
                } else if (request.getAttribute("operacion") != null
                        && request.getAttribute("operacion").equals("NOT OK")) {
            %>
            <div class="alert alert-danger" role="alert">Error al crear el
                cuenta</div>
            <%
                }
            %>
			<%
                if (request.getAttribute("deleteResult") != null
                        && Boolean.TRUE.equals(request.getAttribute("deleteResult"))) {
            %>
            <div class="alert alert-success" role="alert">Cuenta eliminada
                correctamente</div>
            <%
                } else if (request.getAttribute("deleteResult") != null
                        && Boolean.FALSE.equals(request.getAttribute("deleteResult"))) {
            %>
            <div class="alert alert-danger" role="alert">Error al eliminar
                la cuenta</div>
            <%
                }
            %>
			<div class="col-lg-12 text-center mt-3">
				<h1>Gestión de cuentas</h1>
			</div>
			<div class="row row justify-content-md-center">
				<div class="col-12 mt-2 shadow-lg p-3 mb-3 bg-white rounded">
					<form method="post" action="CuentaServlet">
						<div class="input-group mb-3">
						<input id="txtDNI" type="text" class="form-control" placeholder="Ingrese DNI ....." name="txtDNI" required maxlength="8" oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 8);">
						
							<!--  
							<input id="txtDNI" type="number" class="form-control"
								placeholder="Ingrese DNI ....." name="txtDNI" required>							
							-->
							<div class="input-group-append">
								<button class="btn btn-secondary" type="submit" name="btnBuscar"
									value="Buscar cliente">
									<svg xmlns="http://www.w3.org/2000/svg" height="24px"
										viewBox="0 -960 960 960" width="24px" fill="#ffffff	"> <path
										d="M440-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47Zm0-80q33 0 56.5-23.5T520-640q0-33-23.5-56.5T440-720q-33 0-56.5 23.5T360-640q0 33 23.5 56.5T440-560ZM884-20 756-148q-21 12-45 20t-51 8q-75 0-127.5-52.5T480-300q0-75 52.5-127.5T660-480q75 0 127.5 52.5T840-300q0 27-8 51t-20 45L940-76l-56 56ZM660-200q42 0 71-29t29-71q0-42-29-71t-71-29q-42 0-71 29t-29 71q0 42 29 71t71 29Zm-540 40v-111q0-34 17-63t47-44q51-26 115-44t142-18q-12 18-20.5 38.5T407-359q-60 5-107 20.5T221-306q-10 5-15.5 14.5T200-271v31h207q5 22 13.5 42t20.5 38H120Zm320-480Zm-33 400Z" /></svg>
									Buscar cliente
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			<hr>
			<%
				if (request.getAttribute("SelectedClient") != null) {
					Cliente SelectedClient = (Cliente) request.getAttribute("SelectedClient");
			%>
			<div class="container">
				<div class="row justify-content-center">
					<div class="col-12 mt-2 shadow-lg p-3 mb-3 bg-white rounded">
						<div class="card">
							<div class="card-body">
								<div class="row">
									<div class="col-sm-5">
										<p class="mb-0">Nombre completo</p>
									</div>
									<div class="col-sm-6">
										<p class="text-muted mb-0"><%=SelectedClient.getNombre() + " " + SelectedClient.getApellido()%></p>
									</div>
								</div>
								<hr>
								<div class="row">
									<div class="col-sm-5">
										<p class="mb-0">E-mail</p>
									</div>
									<div class="col-sm-6">
										<p class="text-muted mb-0"><%=SelectedClient.getCorreoElectronico()%></p>
									</div>
								</div>
								<hr>
								<div class="row">
									<div class="col-sm-5">
										<p class="mb-0">Teléfono</p>
									</div>
									<div class="col-sm-6">
										<p class="text-muted mb-0"><%=SelectedClient.getTelefono()%></p>
									</div>
								</div>
								<hr>
								<div class="row">
									<div class="col-sm-5">
										<p class="mb-0">CUIL</p>
									</div>
									<div class="col-sm-6">
										<p class="text-muted mb-0"><%=SelectedClient.getCuil()%></p>
									</div>
								</div>
								<hr>
							</div>
						</div>
					</div>
				</div>
			</div>
			<hr>
			<div class="row mt-1 shadow-lg p-3">
				<div class="row justify-content-center">
					<h6>CUENTAS ACTIVAS</h6>
				</div>
				<table id="table_id" class="table rounded table-stripped"
					style="width: 100%">
					<thead>
						<tr class="text-center">
							<td><b>N° CUENTA</b></td>
							<td><b>CBU</b></td>
							<td><b>TIPO DE CUENTA</b></td>
							<td><b>SALDO</b></td>
							<td><b>FECHA DE CREACIÓN</b></td>
							<td><b></b></td>
						</tr>
					</thead>
					<tbody>
						<%
							if (listaC != null && !listaC.isEmpty()) {
									for (Cuenta c : listaC) {
										if (c.isDeleted() == false) {
						%>
						<tr class="text-center">
							<td><%=c.getId()%></td>
							<td><%=c.getCbu()%></td>
							<td><%=c.getTipoCuenta().getNombre()%></td>
							<td><%=c.getSaldo().toString().format("$ %.2f", c.getSaldo())%></td>
							<td><%=c.getFechaCreacion()%></td>
							<td>
								<button type="button"
									class="btn btn-link bi bi-trash3-fill trash-button"
									data-bs-toggle="modal" data-bs-target="#deleteModal"
									data-bs-id="<%=c.getId()%>"
									data-bs-saldo="<%=c.getSaldo() %>"
									style="color: black; cursor: pointer;"></button>
							</td>
						</tr>

						<%
							}
									}
								}
						%>
					</tbody>
				</table>
			</div>
			<div class="modal fade" id="deleteModal" tabindex="-1"
				aria-labelledby="deleteModal" aria-hidden="true">
				<div class="modal-dialog">
					<form method="get" action="CuentaServlet">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Eliminar cuenta</h4>
								<button type="button" class="btn-close" data-bs-dismiss="modal"
									aria-label="Cerrar"></button>
							</div>
							<div class="modal-body">
								<div class="d-flex flex-column">
									<div>
										<p>
											¿Está seguro que quiere eliminar la cuenta N°: <span
												id="idToDelete"></span>?
										</p>
										<input type="hidden" id="idToDeleteInput" name="idToDelete">
									
									</div>
									<div class="alert alert-info" role="alert">
										Las cuentas que tengan saldo no pueden darse de baja
									</div>
								
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-bs-dismiss="modal">Cancelar</button>
								<button type="submit" name="delete" class="btn btn-danger"
									id="confirmDelete" value="delete">Eliminar</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			<%
				if (cuentasActivas < 3) {
			%>
			<hr>
			<div class="container">
				<div class="row justify-content-center py-1 mt-1">
					<div class="col-auto">
						<button type="button" class="btn btn-secondary"
							data-bs-toggle="modal" data-bs-target="#exampleModal">Asignar
							nueva cuenta</button>
					</div>
				</div>
			</div>
			<hr>
			<div class="modal fade" id="exampleModal" tabindex="-1"
				aria-hidden="true">
				<form method="post" action="CuentaServlet">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="exampleModalLabel1">Nueva
									cuenta</h5>
								<button type="button" class="btn-close" data-bs-dismiss="modal"
									aria-label="Close"></button>
							</div>
							<div class="modal-body">
								<div class="row g-2">
									<div class="col mb-0">
										<label for="dni" class="form-label">DNI</label> <input
											type="text" id="dni" name="dni" class="form-control" required
											value="<%=SelectedClient.getDni()%>" readonly />
											<input
											type="hidden" id="id" name="id" class="form-control" required
											value="<%=SelectedClient.getId()%>" readonly />
									</div>
									<div class="col mb-0">
										<label for="tipocuenta" class="form-label">Tipo de
											cuenta</label> <select id="tipocuenta" name="tipocuenta"
											class="form-control" required>
											<% if(request.getAttribute("tipoCuentas") != null){
                                				List<TipoCuenta> tipos = (List<TipoCuenta>) request.getAttribute("tipoCuentas");
                                				for(TipoCuenta tipo : tipos){
                                				%>
                                				<option value="<%=tipo.getId()%>"> <%=tipo.getNombre() %> </option>
                                			<% }}%>
										</select>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-outline-secondary"
									data-bs-dismiss="modal">Cerrar</button>
								<button type="submit" name="save" class="btn btn-primary">Asignar</button>
							</div>
						</div>
					</div>
				</form>
			</div>
			<%
				}
			%>
			<div>
				<div class="row mt-3 shadow-lg p-3 text-muted mb-3">
					<h6>CUENTAS INACTIVAS</h6>
					<table id="table_inactive" class="table rounded table-stripped"
						style="width: 100%">
						<thead>
							<tr class="text-center">
								<td><b>N° CUENTA</b></td>
								<td><b>CBU</b></td>
								<td><b>TIPO DE CUENTA</b></td>
								<td><b>SALDO</b></td>
								<td><b>FECHA DE CREACIÓN</b></td>
							</tr>
						</thead>
						<tbody>
							<%
								if (listaC != null && !listaC.isEmpty()) {
										for (Cuenta c : listaC) {
											if (c.isDeleted()) {
							%>
							<tr class="text-center">
								<td><%=c.getId()%></td>
								<td><%=c.getCbu()%></td>
								<td><%=c.getTipoCuenta().getNombre()%></td>
								<td><%=c.getSaldo().toString().format("$ %.2f", c.getSaldo())%></td>
								<td><%=c.getFechaCreacion()%></td>
							</tr>
							<%
								}
										}
									}
							%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
</div>
		<%
			} else if (request.getAttribute("client-not-found") == "true") {
		%>
		<div>
			<div class="alert alert-danger" role="alert">
				<strong>Error:</strong> cliente no encontrado. Por favor, intente de
				nuevo.
			</div>
			<%
				}
			%>
		</div>
		<script>
			$('#deleteModal').on('show.bs.modal', function(event) {
				var button = $(event.relatedTarget);
				var accountId = button.data('bs-id');
				var accountSaldo = button.data('bs-saldo');

				var modal = $(this);
				modal.find('#idToDelete').text(accountId);
				modal.find('#idToDeleteInput').val(accountId);
				if (accountSaldo > 0) {
		            $('#confirmDelete').prop('disabled', true);
		        } else {
		            $('#confirmDelete').prop('disabled', false);
		        }
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