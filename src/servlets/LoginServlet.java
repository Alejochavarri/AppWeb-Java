package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.impl.LinksDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.Cliente;
import entities.Cuenta;
import entities.Link;
import entities.Prestamo;
import entities.Usuario;
import exceptions.CustomSecurityException;
import negocio.ClienteNegocio;
import negocio.LinksNegocio;
import negocio.PrestamoNegocio;
import negocio.impl.ClienteNegocioImpl;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.LinksNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;
import negocio.impl.UsuarioNegocioImpl;
import utils.sql.EqualsCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String user = request.getParameter("user").trim();
		String pass = request.getParameter("password").trim();
		HttpSession session = request.getSession();

		HashMap<String, String> filters = new HashMap<>();
		filters.put("usuario", user);
		filters.put("contrasena", pass);
		UsuarioNegocioImpl negocio = new UsuarioNegocioImpl(null);
		RequestDispatcher rd = null;
		try {
			Usuario usuario = negocio.findUsuario(filters);

			if (usuario == null) {
				request.setAttribute("login-fail", "true");
				rd = request.getRequestDispatcher("/Inicio.jsp");
				throw new CustomSecurityException("Usuario no autenticado");
			} else {
				request.setAttribute("usuario", usuario);
				session.setAttribute("usuario", usuario);
				request.setAttribute("nombreUsuario", user);
				Cliente cliente = getClient(session, usuario, request);
				getPrestamos(session, cliente, request);
				getLinks(request, session, usuario);
				getCuentas(session, cliente);
				if (usuario.getTipoUsuario().getNombre().equals("ADMINISTRADOR")) {
					rd = request.getRequestDispatcher("/AdminDashboardServlet?load=1");
				} else {
					rd = request.getRequestDispatcher("/ClienteDashboardServlet?load=1");
				}

			}

			rd.forward(request, response);
		} catch (Exception e) {
			request.setAttribute("login-fail", "true");
			rd = request.getRequestDispatcher("Inicio.jsp");
			rd.forward(request, response);

		}

	}

	private Cliente getClient(HttpSession session, Usuario usuario, HttpServletRequest request) throws SQLException {
		ClienteNegocio clienteNegocio = new ClienteNegocioImpl(null);

		Cliente cliente = clienteNegocio.getClientByUser(usuario.getId());
		if (cliente != null) {
			session.setAttribute("cliente", cliente);
		}
		return cliente;
	}

	private void getLinks(HttpServletRequest request, HttpSession session, Usuario usuario) {
		try {
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder.select("links.id", "links.nombre_recurso", "links.redirige", "links.deleted", 
					"tipo_usuario.id", "tipo_usuario.nombre", "tipo_usuario.deleted")
					.where(new EqualsCriteria("id_tipo_usuario", usuario.getTipoUsuario().getId(), Boolean.FALSE))
					.innerJoin("tipo_usuario on tipo_usuario.id = links.id_tipo_usuario")
					.from("links").build();
			LinksNegocio linkNegocio = new LinksNegocioImpl(new LinksDAOImpl(query));
			List<Link> links = linkNegocio.list();
			session.setAttribute("links", links);

		} catch (SQLException e) {
			request.setAttribute("login-fail", "true");
		}
	}

	private void getPrestamos(HttpSession session, Cliente cliente, HttpServletRequest request) {
		List<Prestamo> prestamos = null;
		try {
			PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
			prestamos = prestamoNegocio.getSoloPrestamoPorCliente(cliente.getId());
			session.setAttribute("prestamos", prestamos);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getCuentas(HttpSession session, Cliente cliente) {
		try {
			CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);
			List<Cuenta> cuentas = cuentaNegocio.listAccountsByClient(cliente.getId());
			session.setAttribute("cuentas", cuentas);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
