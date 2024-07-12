<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="entities.Link"%>
<%@ page import="java.util.List"%>
<%@ page import="entities.Usuario" %>
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

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Istok+Web:ital,wght@0,400;0,700;1,400;1,700&display=swap"
	rel="stylesheet">
</head>
<style>
	<%@include file="/Css/GlobalStyles.css" %>
</style>
<body>
	<%
		List<Link> links = (List<Link>) session.getAttribute("links");
		Usuario logguedUser = (Usuario) session.getAttribute("usuario");
	%>
	<div class="sidebar">
	
		<div class="button-container m-1">
			<button class="btn btn-outline-secondary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasExample" aria-controls="offcanvasExample">
				<i class="bi bi-list" style="font-size: 1.5rem"></i>		
			</button>
		
		</div>
		<div class="offcanvas offcanvas-start" tabindex="-1" id="offcanvasExample" aria-labelledby="offcanvasExampleLabel">
			<div class="offcanvas-header d-flex justify-content-between align-items-center w-100">
				<a 
					class="offcanvas-title mt-2 link-dark text-decoration-none"
					>
					<strong><%= logguedUser != null ? logguedUser.getUsuario() : null%></strong>
				</a>
				<img src="Resources/avatar-profile-pic.png" alt="profile-icon" width="32" height="32"
				class="rounded-circle me-2"> 
				
			</div>
			<hr>
			<div class="offcanvas-body d-flex flex-column">
				<ul class="nav nav-pills flex-column mb-auto">
					<%
						if (links != null) {
							for (Link link : links) {
					%>
					<li class="nav-item m-2"><a
						class="text-decoration-none link-dark" href=<%=link.getRedirige()%>><%=link.getNombreRecurso()%></a>
					</li>
					<%
						}
						}
					%>
				</ul>
				<hr>
				<ul class="nav text-end">
					<li class="nav-item"><a class="text-decoration-none link-dark" href="LogoutServlet">Cerrar sesión</a></li>
				</ul>
			
			</div>
		</div>
	
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
		integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
		integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
		crossorigin="anonymous"></script>
</body>
</html>