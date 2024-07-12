<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
</head>
<body class="bg-body-tertiary">
	<%
		if (request.getAttribute("login-fail") == "true") {
	%>
	<div class="container mt-5">
		<div class="alert alert-danger" role="alert">
			<strong>Error:</strong> Usuario no encontrado. Por favor, intente de
			nuevo.
		</div>
	
	</div>
	<%
		}
	%>
	<section class="h-100 mt-5">
	<div class="container h-100">
		<div class="row justify-content-sm-center h-100">
			<div class="col-xxl-4 col-xl-5 col-lg-5 col-md-7 col-sm-9">
				<div class="card shadow-lg">
					<div class="card-body p-5">
						<h1 class="fs-4 card-title fw-bold mb-4">Login</h1>
						<form method="post" action="LoginServlet" class="needs-validation"
							novalidate="" autocomplete="off">
							<div class="mb-3">
								<label class="mb-2 text-muted" for="user">Usuario</label> <input
									id="user" type="text" class="form-control" name="user" value=""
									required autofocus>
								<div class="invalid-feedback">Por favor ingrese un
									usuario.</div>
							</div>

							<div class="mb-3">
								<div class="mb-2 w-100">
									<label class="text-muted" for="password">Contraseña</label>
								</div>
								<input id="password" type="password" class="form-control"
									name="password" required>
								<div class="invalid-feedback">Por favor ingrese una
									contraseña.</div>
							</div>

							<div class="d-flex align-items-center">
								<button type="submit" class="btn btn-primary ms-auto">
									Login</button>
							</div>
						</form>
					</div>
				</div>
				<div class="text-center mt-5 text-muted">Trabajo práctico
					integrador &mdash; Grupo 12</div>
			</div>
		</div>
	</div>
	</section>
	<script>
		// Activa la validación de Bootstrap
		(function() {
			'use strict'

			// Obtiene todos los formularios a los que se les debe aplicar la validación de Bootstrap
			var forms = document.querySelectorAll('.needs-validation')

			// Itera sobre ellos y previene el envío si están inválidos
			Array.prototype.slice.call(forms).forEach(function(form) {
				form.addEventListener('submit', function(event) {
					if (!form.checkValidity()) {
						event.preventDefault()
						event.stopPropagation()
					}

					form.classList.add('was-validated')
				}, false)
			})
		})()
	</script>
</body>
</html>