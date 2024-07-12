<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="entities.Cliente"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Mi Perfil</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
	<link rel="stylesheet"
    href="https://cdn.datatables.net/1.10.21/css/jquery.dataTables.min.css">
	<link rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
	
</head>
	<%
			Cliente cliente = (Cliente) session.getAttribute("cliente");
			String nombre = "";
			String apellido = "";
			String mail = "";
			String dni = "";
			String cuil = "";
			String direccion = "";
			String provincia = "";
			String localidad = "";
			String nacionalidad = "";
			String sexo= "";
			String Fnac= "";
			String telefono = "";
			if (session.getAttribute("cliente") != null) {
				telefono = cliente.getTelefono();
				mail = cliente.getCorreoElectronico();
				nombre = cliente.getNombre();
				apellido = cliente.getApellido();
				dni = Integer.toString(cliente.getDni()) ;
				cuil = Long.toString(cliente.getCuil());
				direccion = cliente.getDireccion();
				provincia = cliente.getProvincia().getNombre();
				localidad = cliente.getLocalidad().getNombre();
				nacionalidad = cliente.getNacionalidad().getNombre();
				sexo = cliente.getSexo().name();
				Fnac =cliente.getFechaNacimiento().toString();
				
			}
		%>
<body class="bg-body">

	<div class="d-flex vh-100">
		<jsp:include page="Sidebar.jsp"></jsp:include>
	
	
	
	<div class="container-fluid justify-content-start align-items-start">
	
	<div class="container-fluid d-flex justify-content-start align-items-start" style="margin:10px; ">
			<h3>Mi Perfil</h3>
		</div>
	
	<div class="row d-flex justify-content-center align-items-center" style="border: 0px solid black; border-radius: 10px;margin:20px">
	
		
		<div class="row d-flex justify-content-center align-items-center">
		
			<div class="col-4 d-flex justify-content-center align-items-center" style="margin-top: 20px">
			  <div class="card border-dark mb-3" style="width: 12rem;">
			  	<img src="https://cdn.icon-icons.com/icons2/624/PNG/512/User-80_icon-icons.com_57249.png" class="card-img-top" alt="...">
			  <div class="card-body">
			    <h5 class="card-title"><%=apellido%> <%=nombre%></h5>
			  </div>			
			</div>
			</div>
			
			<div class="col-4" style="margin-bottom:10px;">
				
				<div class="card text-dark mb-12 rounded-3" style="margin:10px;">
					<div class="card-body">
						<div class="row">
							<div class="col-6" style="margin-bottom:10px;">
								<label for="inputNombre" class="form-label">Nombre</label>
				    			<input type="text" class="form-control" id="inputNombre" placeholder=<%=nombre%> readonly>
							</div>
							<div class="col-6" style="margin-bottom:10px;">
								<label for="inputApellido" class="form-label">Apellido</label>
				    			<input type="text" class="form-control" id="inputApellido" placeholder=<%=apellido%> readonly>		
							</div>
							<div class="col-6" style="margin-bottom:10px;">
								<label for="inputDNI" class="form-label">DNI</label>
					    		<input type="text" class="form-control" id="inputDNI" placeholder=<%=dni%>  readonly>
				    		</div>
				    		<div class="col-6" style="margin-bottom:10px;"style="margin-bottom:10px;">
								<label for="inputCUIL" class="form-label">CUIL</label>
				    			<input type="text" class="form-control" id="inputCUIL" placeholder=<%=cuil%> readonly>
				    		</div>
				    		<div class="col-6" style="margin-bottom:10px;">
								<label for="inputNacionalidad" class="form-label">Nacionalidad</label>
				    			<input type="text" class="form-control" id="inputNacionalidad"placeholder=<%=nacionalidad%> readonly>
				    		</div>
				    		<div class="col-6" style="margin-bottom:10px;">
								 <label for="inputSexo" class="form-label">Sexo</label>
				    			 <input type="text" class="form-control" id="inputSexo"placeholder=<%=cliente.getSexo()%> readonly>
				    		</div>
				    		<div class="col-12" style="margin-bottom:10px;">
								 <label for="inputFNac" class="form-label">Fecha Nacimiento</label>
				    			 <input type="date" class="form-control" id="inputFNac" value= <%=cliente.getFechaNacimiento()%> readonly>
				    		</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-8" style="margin-bottom:10px">
				
				<div class="card text-dark mb-12 rounded-3" style="margin:10px;">
					<div class="card-body">
						<div class="row">
							<div class="col-6" style="margin-bottom:10px;">
								<label for="inputEmail" class="form-label">Email</label>
				    			<input type="text" class="form-control" id="inputEmail" placeholder=<%=mail%> readonly>
							</div>
							<div class="col-6" style="margin-bottom:10px;">
								<label for="inputTel" class="form-label">Telefono</label>
				    			<input type="text" class="form-control" id="inputTel"placeholder=<%=telefono%> readonly>		
							</div>
							
				    		<div class="col-6" style="margin-bottom:10px;">
								<label for="inputDir" class="form-label">Direccion</label>
				   			    <input type="text" class="form-control" id="inputDir"placeholder=<%=direccion%> readonly>		
							</div>
							<div class="col-6" style="margin-bottom:10px;">
								 <label for="inputProvincia" class="form-label">Provincia</label>
				    			 <input type="text" class="form-control" id="inputProvincia"placeholder=<%=provincia%> readonly>
				    		</div>
				    		<div class="col-12" style="margin-bottom:10px;">
								 <label for="inputLocalidad" class="form-label">Localidad</label>
				    			<input type="text" class="form-control" id="inputLocalidad"placeholder=<%=localidad%> readonly>
				    		</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>			  			

		</div>
	</div>		
	</div>

	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script>
	$(document).ready(function() {
		  // Selecciona los elementos que deseas modificar
		  var elementos = $(".form-control");

		  // Elimina los bordes de los elementos seleccionados
		  elementos.css("border", "none");
		});
	
	</script>
</body>
</html>