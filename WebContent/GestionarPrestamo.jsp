<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="entities.Prestamo" %>
<%@ page import="entities.Cuenta" %>
<%@ page import="entities.CuotaPrestamo" %>
<%@ page import="enums.PrestamoEstado" %>
<%@ page import="enums.CuotaEstado" %>
<%@ page import="entities.Usuario"%>
<%@ page import="exceptions.CustomSecurityException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Gestion Prestamos</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
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
        List<Prestamo> prestamos = (List<Prestamo>) request.getAttribute("prestamos");
        List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentas");
        
        List<Prestamo> prestamosBajoRevision = (List<Prestamo>) request.getAttribute("prestamos_bajo_revision");
        List<Prestamo> prestamosAprobados = (List<Prestamo>) request.getAttribute("prestamos_aprobados");
        List<Prestamo> prestamosDesaprobados = (List<Prestamo>) request.getAttribute("prestamos_desaprobados");
    %>
	<div class="d-flex vh-100">
	<jsp:include page="Sidebar.jsp"></jsp:include>
		<div class="container mt-5">
        <h1 class="text-center">Prestamos</h1>        
            <% if (prestamos != null) { %>
            <h3 class="text-center">Tienes <%= prestamos.size() %> préstamos</h3>
            <div class="accordion" id="prestamosAccordion">
                <!-- Colapsable para Préstamos Bajo Revisión -->
                <div class="card">
                    <div class="card-header" id="headingBajoRevision">
                        <h5 class="mb-0">
                            <div class="d-flex justify-content-center mb-3">
							    <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapseBajoRevision" aria-expanded="true" aria-controls="collapseBajoRevision">
							        Préstamos Bajo Revisión.  N°<%=prestamosBajoRevision.size() %>
							    </button>
							</div>
                        </h5>
                    </div>
                    <div id="collapseBajoRevision" class="collapse" aria-labelledby="headingBajoRevision" data-parent="#prestamosAccordion">
                        <div class="card-body">
                            <% if (!prestamosBajoRevision.isEmpty() || prestamosBajoRevision != null) {
                                for (Prestamo prestamo : prestamosBajoRevision) { %>
                                <div class="card mb-3">
                                    <div class="card-header">
                                        Préstamo <%= prestamo.getId() %>
                                    </div>
                                    <div class="card-body">
					                    <div class="d-flex justify-content-between">
					                        <div>
					                            <h5 class="card-title">Cuota a pagar</h5>
					                            <p class="card-text display-4"><%= prestamo.getMontoPorMes().toString().format("$ %.2f", prestamo.getMontoPorMes())%></p>
					                        </div>
					                        <div>
					                            <h5 class="card-title">Total préstamo</h5>
					                            <p class="card-text display-6"><%= prestamo.getImporteTotal().toString().format("$ %.2f", prestamo.getImporteTotal())%></p>
					                            <p class="card-text">Fecha Peticion de Prestamo - <%= prestamo.getFechaDeContratacion() %></p>
					                        </div>
					                    </div>
					             
					                    <p class="card-text">Cuotas total <%= prestamo.getPlazoEnMeses() %></p>
					                </div>
                                </div>
                            <% }
                            } else { %>
                            <p>No hay préstamos bajo revisión.</p>
                            <% } %>
                        </div>
                    </div>
                </div>

                <!-- Colapsable para Préstamos Aprobados -->
                <div class="card">
                    <div class="card-header" id="headingAprobados">
                        <h5 class="mb-0">
                            <div class="d-flex justify-content-center mb-3">
							    <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapseAprobados" aria-expanded="true" aria-controls="collapseAprobados">
							        Préstamos Aprobados.  N°<%=prestamosAprobados.size() %>
							    </button>
							</div>
                        </h5>
                    </div>
                    <div id="collapseAprobados" class="collapse show" aria-labelledby="headingAprobados" data-parent="#prestamosAccordion">
                        <div class="card-body">
                            <% if (!prestamosAprobados.isEmpty() || prestamosAprobados != null) {
                                for (Prestamo prestamo : prestamosAprobados) { %>
                                <div class="card mb-3">
                                    <div class="card-header">
                                        Préstamo <%= prestamo.getId() %>
                                    </div>
                                    <div class="card-body">
					                    <div class="d-flex justify-content-between">
					                        <div>
					                            <h5 class="card-title">Cuota a pagar</h5>
					                            <p class="card-text display-4"><%= prestamo.getMontoPorMes().toString().format("$ %.2f", prestamo.getMontoPorMes()) %></p>
					                        </div>
					                        <div>
					                            <h5 class="card-title">Total préstamo</h5>
					                            <p class="card-text display-6"><%= prestamo.getImporteTotal().toString().format("$ %.2f", prestamo.getImporteTotal()) %></p>
					                            <p class="card-text">Fecha Contratacion de Prestamo - <%= prestamo.getFechaDeContratacion() %></p>
					                        </div>
					                    </div>
					            
					                    <p class="card-text">Cuotas total <%= prestamo.getPlazoEnMeses() %></p>
					                    <%
					                    	Boolean showPagar = false;
					                    	int count = 0;
					                    	int qtyCuotas = prestamo.getCuotaPrestamos().size();
					                    	for(CuotaPrestamo cuota: prestamo.getCuotaPrestamos()) {
					                    		if(cuota.getEstado() == CuotaEstado.PAGADA) {
					                    			count += 1;
					                    		}
					                    	}
					                    	
					                    	if(count == qtyCuotas) { %>
					                    		<p class="h5 font-italic">El Pago del prestamo ha sido completado</p>
					                    		<form action="CuotaPrestamo" method="get">
												    <input type="hidden" name="prestamoId" value="<%= prestamo.getId() %>">
												    <button type="submit" class="btn btn-primary">Ver Cuotas Pagas</button>
												</form>						                    	
					                    	<%} else { %>
							                    <form action="CuotaPrestamo" method="get">
												    <input type="hidden" name="prestamoId" value="<%= prestamo.getId() %>">
												    <button type="submit" class="btn btn-primary">Pagar cuota préstamo</button>
												</form>					                    	
					                    	<% } %>
			                    	</div>
                                </div>
                            <% }
                            } else { %>
                            <p>No hay préstamos aprobados.</p>
                            <% } %>
                        </div>
                    </div>
                </div>

                <!-- Colapsable para Préstamos Desaprobados -->
                <div class="card">
                    <div class="card-header" id="headingDesaprobados">
                        <h5 class="mb-0">
                            <div class="d-flex justify-content-center mb-3">
							    <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapseDesaprobados" aria-expanded="true" aria-controls="collapseDesaprobados">
							        Préstamos Desaprobados.  N°<%=prestamosDesaprobados.size() %>
							    </button>
							</div>
                        </h5>
                    </div>
                    <div id="collapseDesaprobados" class="collapse" aria-labelledby="headingDesaprobados" data-parent="#prestamosAccordion">
                        <div class="card-body">
                            <% if (!prestamosDesaprobados.isEmpty()|| prestamosDesaprobados != null ) {
                                for (Prestamo prestamo : prestamosDesaprobados) { %>
                                <div class="card mb-3">
                                    <div class="card-header">
                                        Préstamo <%= prestamo.getId() %>
                                    </div>
                                    <div class="card-body">
					                    <div class="d-flex justify-content-between">
					                        <div>
					                            <h5 class="card-title">Primera Cuota</h5>
					                            <p class="card-text display-4"><%= prestamo.getMontoPorMes().toString().format("$ %.2f", prestamo.getMontoPorMes()) %></p>
					                        </div>
					                        <div>
					                            <h5 class="card-title">Total préstamo</h5>
					                            <p class="card-text display-6"><%= prestamo.getImporteTotal().toString().format("$ %.2f", prestamo.getImporteTotal()) %></p>
					                            <p class="card-text">Fecha Peticion de Prestamo - <%= prestamo.getFechaDeContratacion() %></p>
					                        </div>
					                    </div>
					                   
					                    <p class="card-text">Cuotas total <%= prestamo.getPlazoEnMeses() %></p>
					                </div>
                                </div>
                            <% }
                            } else { %>
                            <p>No hay préstamos desaprobados.</p>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>
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