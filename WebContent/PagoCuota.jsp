<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="entities.Prestamo" %>
<%@ page import="entities.Cuenta" %>
<%@ page import="entities.CuotaPrestamo" %>
<%@ page import="enums.CuotaEstado" %>
<%@ page import="entities.Usuario"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cuotas del Préstamo</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
	function redirectToServlet() {
	    document.getElementById("redirectForm").submit();
	}
	
    function validateSelection() {
        const checkboxes = document.querySelectorAll('input[name="cuotasIds"]:checked');
        if (checkboxes.length === 0) {
            alert("Debe seleccionar al menos una cuota para realizar el pago.");
            return false;
        }
        return true;
    }
	
	$(document).ready(function() {
        var saldoInsuficiente = '<%= request.getAttribute("saldoInsuficiente") %>';
        var cuotasPagadas = '<%= request.getAttribute("todasCuotasPagas") %>';
        console.log({saldoInsuficiente});
        console.log({cuotasPagadas});

        if (saldoInsuficiente === 'true') {
            $('#errorModal').modal('show');
        } else if (saldoInsuficiente === 'false') {
            $('#successModal').modal('show');
        }

        if (cuotasPagadas === 'true') {
            $('#cuotasPagasModal').modal('show');
        }
    });
</script>
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
<%
    Prestamo prestamo = (Prestamo) request.getAttribute("prestamo");
    List<CuotaPrestamo> cuotas = (List<CuotaPrestamo>) request.getAttribute("cuotas");
    List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentas");
%>
	<div class="d-flex vh-100">
	<jsp:include page="Sidebar.jsp"></jsp:include>
        <div class="container">
        <h1>Cuotas del Préstamo</h1>
        <%
        Boolean showPagar = false;
    	int count = 0;
    	int qtyCuotasPagas = cuotas.size();
    	for(CuotaPrestamo cuota: cuotas) {
    		if(cuota.getEstado() == CuotaEstado.PAGADA) {
    			count += 1;
    		}
    	}
        if(count != qtyCuotasPagas) {%>
   	        <form action="CuotaPrestamo" method="post" onsubmit="return validateSelection()">
            <div class="form-group">
                <label for="cuentaSelect">Cuentas</label>
                <select class="form-control" id="cuentaSelect" name="cuentaId">
                    <%  
                       if (cuentas != null) {
                           for (Cuenta cuenta : cuentas) {
                    %>
                    <option value="<%= cuenta.getId() %>">CBU: <%= cuenta.getCbu()%> - Saldo:  <%= cuenta.getSaldo() %></option>
                    <% 
                           } 
                        } 
                    %>
                </select>
            </div>

            <div class="form-group">
                <label for="cuotasPendientes">Cuotas del Préstamo</label>
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Seleccionar</th>
                            <th scope="col">Número de Cuota</th>
                            <th scope="col">Monto</th>
                            <th scope="col">Fecha de Vencimiento</th>
                            <th scope="col">Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (cuotas != null) { %>
                            <% for (CuotaPrestamo cuota : cuotas) { %>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="cuotasIds" value="<%= cuota.getId() %>" 
                                               <%= CuotaEstado.PENDIENTE.equals(cuota.getEstado()) ? "" : "disabled" %>>
                                    </td>
                                    <td><%= cuota.getCuota() %></td>
                                    <td><%= cuota.getImporteCuota() %></td>
                                    <td><%= cuota.getFechaVencimiento() %></td>
                                    <td><%= cuota.getEstado() %></td>
                                </tr>
                            <% } %>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <input type="hidden" id="prestamoId" name="prestamoId" value="<%= prestamo.getId() %>">
            <div class="d-flex justify-content-center">
                <button type="submit" class="btn btn-primary">Realizar Pago</button>
            </div>
        </form>
        
        <% } else {%>
        <form action="GestionPrestamos" method="get">
            <div class="form-group">
                <label for="cuentaSelect">Cuentas</label>
                <select class="form-control" id="cuentaSelect" name="cuentaId">
                    <%  
                       if (cuentas != null) {
                           for (Cuenta cuenta : cuentas) {
                    %>
                    <option value="<%= cuenta.getId() %>">CBU: <%= cuenta.getCbu()%> - Saldo:  <%= cuenta.getSaldo() %></option>
                    <% 
                           } 
                        } 
                    %>
                </select>
            </div>

            <div class="form-group">
                <label for="cuotasPendientes">Cuotas del Préstamo</label>
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Seleccionar</th>
                            <th scope="col">Número de Cuota</th>
                            <th scope="col">Monto</th>
                            <th scope="col">Fecha de Vencimiento</th>
                            <th scope="col">Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (cuotas != null) { %>
                            <% for (CuotaPrestamo cuota : cuotas) { %>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="cuotasIds" value="<%= cuota.getId() %>" 
                                               <%= CuotaEstado.PENDIENTE.equals(cuota.getEstado()) ? "" : "disabled" %>>
                                    </td>
                                    <td><%= cuota.getCuota() %></td>
                                    <td><%= cuota.getImporteCuota() %></td>
                                    <td><%= cuota.getFechaVencimiento() %></td>
                                    <td><%= cuota.getEstado() %></td>
                                </tr>
                            <% } %>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <div class="d-flex justify-content-center">
                <button type="submit" class="btn btn-primary">Volver a Prestamos</button>
            </div>
        </form>
        
        
        <%} %>
        
        
        
    </div>
    
    	<div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="errorModalLabel">Error</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    No tiene saldo suficiente para realizar este pago.
                </div>
                <div class="modal-footer">
                       <form id="redirectForm" action="GestionPrestamos" method="get">
					        <input type="hidden" name="load" value="1">
					        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="redirectToServlet()">Cerrar</button>
					    </form>
                </div>
            </div>
        </div>
    </div>
    	<div class="modal fade" id="cuotasPagasModal" tabindex="-1" aria-labelledby="cuotasPagasModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="cuotasPagasModalLabel">Aviso</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Ya ha pagado todas las cuotas de su prestamo
                </div>
                <div class="modal-footer">
                    <form id="redirectForm" action="GestionPrestamos" method="get">
				        <input type="hidden" name="load" value="1">
				        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="redirectToServlet()">Cerrar</button>
				    </form>
                </div>
            </div>
        </div>
    </div>
     <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="successModalLabel">Éxito</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    El pago se realizó con éxito.
                </div>
                <div class="modal-footer">
                   <form id="redirectForm" action="GestionPrestamos" method="get">
			        <input type="hidden" name="load" value="1">
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" onclick="redirectToServlet()">Cerrar</button>
			    </form>
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
</body>
</html>