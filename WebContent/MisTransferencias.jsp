<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="entities.Usuario"%>
<%@page import="entities.Cuenta"%>
<%@ page import="java.util.List"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transferencias</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    
    
</head>

<body class="bg-body">

	<%
		Usuario usuarioLog = (Usuario) session.getAttribute("usuario");
		
		String CBU = "";
		String Saldo = "";
		String Id = "";
		String Auxid = "null";
		String Auxcbu = "null";
		
		String Estado = "";
		String CBU_Resumen = "";
		String Monto = "";
		String Detalle = "";
		String Destino = "";
		
		List<Cuenta> cuentas = (List<Cuenta>)session.getAttribute("cuentas");
		
		Cuenta cuentaSeleccionada = null;
		
		if(session.getAttribute("cuentaSeleccionada")!= null && (request.getAttribute("Load") == "1") ){
			cuentaSeleccionada = (Cuenta) session.getAttribute("cuentaSeleccionada");
			CBU = Long.toString(cuentaSeleccionada.getCbu());
			Saldo = cuentaSeleccionada.getSaldo().toString();
			Id = Integer.toString(cuentaSeleccionada.getId());
			
		}
		
		if (session.getAttribute("cuentaSeleccionada")!= null && (request.getAttribute("Load") == "2") ) {
			
			if(request.getAttribute("Find") == "Ok"){
				Estado = "La Operacion Se ha realizado con Exito!" ;
				CBU_Resumen = (String)request.getAttribute("CBU_Usuario");
				Monto = (String)request.getAttribute("Importe");
				Detalle = (String)request.getAttribute("Detalle");
				Destino = (String)request.getAttribute("CBU_Destino");
				
				cuentaSeleccionada = (Cuenta) session.getAttribute("cuentaSeleccionada");
				CBU = Long.toString(cuentaSeleccionada.getCbu());
				Saldo = cuentaSeleccionada.getSaldo().toString();
				Id = Integer.toString(cuentaSeleccionada.getId());
			}
			
			if(request.getAttribute("Find") == null){
				Estado = "Error: No se encontro el CBU Ingresado!" ;
				CBU_Resumen = (String)request.getAttribute("CBU_Usuario");
				Monto = (String)request.getAttribute("Importe");
				Detalle = (String)request.getAttribute("Detalle");
				Destino = (String)request.getAttribute("CBU_Destino");
				
				cuentaSeleccionada = (Cuenta) session.getAttribute("cuentaSeleccionada");
				CBU = Long.toString(cuentaSeleccionada.getCbu());
				Saldo = cuentaSeleccionada.getSaldo().toString();
				Id = Integer.toString(cuentaSeleccionada.getId());
			}
			
		}
		
		
	%>


	<div class="d-flex vh-100">
		<%// Div NavBar %>
		<jsp:include page="Sidebar.jsp"></jsp:include>
		
		<%// Container Gral %>
		<div class="container-fluid justify-content-start align-items-start">
			
		<%// Div Titulo %>
		<div class="container-fluid d-flex justify-content-start align-items-start" style="margin:10px; ">
			<h3>Transferir</h3>
		</div>
		
		<%// Container Cuerpo %>
		<div class="container justify-content-center align-items-center">
		
		<%// Tabla Seleccionar Cuenta %>
			<div class="row row-cols-2 g-12 d-flex justify-content-center align-items-center" style="margin-top: 10px;border-radius:10px;border:1px solid black; ">
			
				<div class="col d-flex justify-content-center align-items-center" style="margin-top:10px;">
					<h5>Seleccionar Cuenta</h5>
				</div>
				
				<div class="col d-flex justify-content-center align-items-center" style="margin:10px;">
					<div class="card text-dark mb-12 rounded-3" style="width: 38rem; margin:10px;">
						<div class="card-body">
							<div style="margin-bottom: 10px">
								<label>Numero de Cuenta</label>
							</div>
							<form action="TransferenciasServlet" method="get">
								<select class="form-select" name="IdCuenta" aria-label="Default select example" style="margin-bottom: 10px">
								  <%								
								  if (cuentas != null) {								     
									 for (Cuenta cuenta : cuentas) {
										 Auxid = Integer.toString(cuenta.getId());
										 Auxcbu = Long.toString(cuenta.getCbu());
									         %>
									  <option value="<%=Auxid %>"><%=Auxid%> - <%=Auxcbu%></option>
									         <%
									     }
									 }
								  
								  %>
								</select>
								<input type="submit" value="Aceptar" name="btnAceptar" class="btn btn-primary" style="margin-top: 10px;"></input>
							</form>					
						</div>
						
					</div>
				</div>
			
			</div>
		</div>	
		
		<!--Form Inferior -->
		<div class="container justify-content-center align-items-center" style="margin-top: 30px;border-radius:10px;border:1px solid black; ">
			
		<div class="row" style="margin-bottom:30px;">
			<div class="col-6">
				<div class="container justify-content-start align-items-start" >	
			 
		<div class="row row-cols-1 g-12 d-flex justify-content-start align-items-start" style="margin-top: 50px;border-radius:10px; ">			
			
			 
			
			<div class="col d-flex justify-content-start align-items-start" style="margin-left:10px;margin-top: 10px;">
				<h5>Cuenta origen</h5>
			</div>	
			
			<div class="col d-flex justify-content-start align-items-start">
				<div class="card text-dark mb-12 rounded-3" style="width: 38rem; margin:10px;">
					<div class="card-body">
						<div class="row">
							<div class="col-6">
								<label>Cuenta N°</label>
							</div>
							<div class="col-6" style="text-align: end;">
								<label id="label-Id"><%=Id %></label>
							</div>
							<div class="col-6">
								<label>CBU N°</label>
							</div>
							<div class="col-6" style="text-align: end;">
								<label id="CBU-Usuario"><%=CBU %></label>
							</div>
							<div class="col-6">
								<label>Saldo $</label>							
							</div>
							<div class="col-6" style="text-align: end;">
								<label id="label-Saldo"><%=Saldo %></label>
							</div>
						</div>
						
						
					</div>			 
				</div>
			</div>
			
			<div class="col d-flex justify-content-start align-items-start" style="margin-left:10px">
				<h5>Monto</h5>
			</div>			
			
			<div class="col d-flex justify-content-start align-items-start">
				<div class="card text-dark mb-12 rounded-3" style="width: 38rem; margin:10px;">
					<div class="card-body">
						<div class="input-group">
						  <span class="input-group-text">Importe </span>
						  <input type="number" id="input-importe" class="form-control" oninput="if(value.length>10)value=value.slice(0,10); if(value<0)value=0" placeholder="(Obligatorio)" >
						  <span class="input-group-text ">$</span>
						</div>
					</div>			 
				</div>
			</div>
			
			<div class="col d-flex justify-content-start align-items-start" style="margin-left:10px">
				<h5>Destinatario</h5>
			</div>	
			<div class="col d-flex justify-content-start align-items-start">
				<div class="card text-dark mb-12 rounded-3" style="width: 38rem; margin:10px;border-radius:10px;">
					<div class="card-body">
						  <div class="input-group">
						   <input type="number" id="input-CBU" class="form-control" oninput="if(value.length>18)value=value.slice(0,18); if(value<0)value=0" placeholder="CBU / CVU (Obligatorio)">
						  </div>
					</div>			 
				</div>
			</div>
			
			<div class="col d-flex justify-content-start align-items-start" style="margin-left:10px">
				<h5>Detalle</h5>
			</div>			
			
			<div class="col d-flex justify-content-start align-items-start">
				<div class="card text-dark mb-12 rounded-3" style="width: 38rem; margin:10px;">
					<div class="card-body">
						<div class="input-group">						  
						  <input type="text" id="input-detalle" class="form-control" placeholder="Detalle (Max. 20 caracteres)" maxlength="20">
						</div>
					</div>			 
				</div>
			</div>
			
			<div class="col-12 d-flex justify-content-start align-items-start">
				<button type="button" id="btn-Transferir" class="btn btn-primary"  style="margin:10px">Transferir</button>
				<form action="TransferenciasServlet" method="get">
					<input type="submit" value="Finalizar" id="btn-Finalizar" name="btn-finalizar" class="btn btn-primary" style="display: none;margin:10px"></input>
				</form>			
			</div>

			<label style="margin:10px; color:red;" id="msj-error">Mensaje</label>
			
		</div>
		
		</div>
		</div>
		
		
		
		<!-- Resumen de Operacion -->
		<div class="col-6">
			<div class="container justify-content-start align-items-start" style="margin-top: 50px;border-radius:10px; ">
				<div class="col d-flex justify-content-start align-items-start" style="margin-left:10px;margin-top: 10px;">
				<h5>Resumen de Operación</h5>
			</div>	
			
			<div class="col d-flex justify-content-start align-items-start">
				<div class="card text-dark mb-12 rounded-3" style="width: 38rem; margin:10px;">
					<div class="card-body">
						<div class="row">
							<div class="col-3" style="margin-bottom:10px">
								<label>Estado :</label>
							</div>
							<div class="col-9" style="text-align: end;margin-bottom:10px">
								<label id="label-Estado"><%=Estado %></label>
							</div>
							<div class="col-6" style="margin-bottom:10px">
								<label >CBU N°</label>
							</div>
							<div class="col-6" style="text-align: end;margin-bottom:10px">
								<label id="label-CBU"><%=CBU_Resumen %></label>
							</div>
							<div class="col-6" style="margin-bottom:10px" >
								<label>Monto $</label>
							</div>							
							<div class="col-6" style="text-align: end;margin-bottom:10px">
								<label id="label-Monto"><%=Monto %></label>
							</div>
							<div class="col-6" style="margin-bottom:10px">
								<label>CBU Destinatario N°</label>
							</div>
							<div class="col-6" style="text-align: end;margin-bottom:10px">
								<label id="label-Destino"><%=Destino %></label>
							</div>
							<div class="col-6" style="margin-bottom:10px">
								<label>Detalle: </label>
							</div>
							<div class="col-6" style="text-align: end;margin-bottom:10px">
								 <p class="card-text" id="label-Detalle"><%=Detalle %></p>
							</div>
						</div>
						
						
					</div>			 
				</div>
			</div>
				
			</div>
		</div>	
		<!-- Fin Resumen de Operacion -->
		<!-- Modal -->
		<form action="TransferenciasServlet" method="get">
		<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog">

		    <div class="modal-content">
		    
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel">Confirmar Operacion</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      
		      <div class="modal-body">	
			        <div class="row">
			        	<div class="col-12">
			        		<label>CBU : </label>
             				<input type="text" id="modal-cbu" name="cbu-user" readonly></input>
			        	</div>
			        	<div class="col-12">
			        		<label>Importe : </label>
			        		<input type="text" id="modal-importe" name="importe" readonly></input>
			        	</div>
			        	<div class="col-12">
			        		<label>Cuenta Destino :</label>
			        		<input type="text" id="modal-cd" name="cbu-destino" readonly></input>
			        	</div>
			        	<div class="col-12">
			        		<label>Detalle : </label>
			        		<input type="text" id="m-detalle" name="detalle" readonly></input>
			        	</div>
			        </div>
		       		        
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>		        
			        <input type="submit" value="Confirmar" name="btn-confirmar" class="btn btn-primary"></input>
			      </div>
			      
			   </div>      
		  </div>
		</div>
		</form>
		<!--Fin Modal -->
		
		</div>
		
		</div>
		
		
		
	</div>
		
	</div>
	
	

	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
		
	<%if ( (request.getAttribute("Load") == null))
	{
		
	%>
	<script>
		$(document).ready(function(){
			var Default = "No Disponible";
			   
            $("#label-Estado").text(Default);
            $("#label-CBU").text(Default);
            $("#label-Monto").text(Default);
            $("#label-Detalle").text(Default);
            $("#label-Destino").text(Default);
            
            $("#label-Id").text(Default);
            $("#label-CBU-Form1").text(Default);
            $("#label-Saldo").text(Default);
            $("#CBU-Usuario").text(Default);
           

		})
		</script>
	<%} 
	%>
	
	<%if (request.getAttribute("Load") == "1") 
	{
		
	%>
	<script>
		$(document).ready(function(){
			var Default = "No Disponible";

            
            $("#label-Estado").text(Default);
            $("#label-CBU").text(Default);
            $("#label-Monto").text(Default);
            $("#label-Detalle").text(Default);
            $("#label-Destino").text(Default);

		})
		</script>
	<%} 
	%>
	
		<%if (request.getAttribute("Load") == "2") 
	{
		
	%>
	<script>
		$(document).ready(function(){
			
			$("#btn-Transferir").prop("disabled", true);
			$("#label-Estado").css("color", "green");
			$("#btn-Finalizar").show();
			
		})
		</script>
	<%} 
	%>
		<%if ((request.getAttribute("Load") == "2") && (request.getAttribute("Find") == null) )
	{
		
	%>
	<script>
		$(document).ready(function(){
			
			$("#btn-Transferir").prop("disabled", true);
			$("#label-Estado").css("color", "red");
			
		})
		</script>
	<%} 
	%>

	<script>
	$(document).ready(function(){	
		$("#msj-error").hide();
		
		
	        $("#btn-Transferir").click(function(){
	        	var Importe = $("#input-importe").val();
	        	var CBU_Destinatario = $("#input-CBU").val();
	        	var CBU_Usuario = $("#CBU-Usuario").text();
	        	var Detalle = $("#input-detalle").val();
	        	var Saldo = $("#label-Saldo").text();
	        	 
	        	var Importe_Comp = parseFloat($("#input-importe").val());
	        	var Saldo_Comp = parseFloat($("#label-Saldo").text());
	        	
	        	
        		if (Importe === '') {
	        		$("#msj-error").text("ERROR: Por Favor Ingrese el Importe a Transferir!"); 
	        		$("#msj-error").show(); 	        	
	        		
	        	} else if (CBU_Destinatario === ''){
	        		$("#msj-error").text("ERROR: Por Favor Ingrese el Destinatario!"); 
	        		$("#msj-error").show();
	        		
	        	} else if (Importe_Comp > Saldo_Comp) {
	        		$("#msj-error").text("ERROR: Fondos Insuficientes!"); 
	        		$("#msj-error").show(); 
	        	    
	        	}else if ($("#input-CBU").val().length < 18) {
	        		$("#msj-error").text("ERROR: CBU no válido!"); 
	        		$("#msj-error").show(); 
	        		
	        	}else if (Importe_Comp == 0) {
	        		$("#msj-error").text("ERROR: Importe invalido"); 
	        		$("#msj-error").show();   
	        		
	        	}else if (CBU_Destinatario === CBU_Usuario) {
	        		$("#msj-error").text("ERROR: La cuenta de origen es Igual a la Cuenta Destino"); 
	        		$("#msj-error").show();         		
	        	}
	        	else {
	        	    $("#exampleModal").modal("show");
	        	    $('.modal-content input').css('border', 'none');
	        	    $("#modal-cbu").val(CBU_Usuario);
	        	    $("#modal-importe").val(Importe);
	        	    $("#modal-cd").val(CBU_Destinatario);
	        	    $("#m-detalle").val(Detalle);
	        	    $("#msj-error").hide();
	        	}

	        	
		        	
	        	
	        })
	      
	      
	})
	
	</script>
	
</body>
</html>