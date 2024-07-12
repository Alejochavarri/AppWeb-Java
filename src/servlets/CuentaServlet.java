package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.CuentaDAOImpl;
import dao.impl.TipoCuentaDAOImpl;
import dao.impl.TipoUsuarioDAOImpl;
import entities.Cliente;
import entities.Cuenta;
import entities.TipoCuenta;
import entities.TipoUsuario;
import negocio.CuentaNegocio;
import negocio.TipoCuentaNegocio;
import negocio.TipoUsuarioNegocio;
import negocio.impl.ClienteNegocioImpl;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.TipoCuentaNegocioImpl;
import negocio.impl.TipoUsuarioNegocioImpl;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;

/**
 * Servlet implementation class CuentaServlet
 */
@WebServlet("/CuentaServlet")
public class CuentaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CuentaServlet() {
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
		RequestDispatcher rd = request.getRequestDispatcher("GestionCuentas.jsp");
		try {
			if (request.getParameter("delete") != null) {
				Integer id = new Integer(request.getParameter("idToDelete"));
				String query = QueryHelper.createDeleteQuery("cuenta");
				CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDAOImpl(query));
				String mensaje_a_enviar = "Cuenta a eliminar: " + id;
				Boolean result = cuentaNegocio.delete(id);
				request.setAttribute("deleteResult", result);
				rd.forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("error", Boolean.TRUE);
			rd.forward(request, response);
		} catch (Exception e) {
			request.setAttribute("error", Boolean.TRUE);
			rd.forward(request, response);
		}
		if (request.getParameter("load") != null) {
			List<Cuenta> cuentas = null;
			HashMap<String, String> accountFilters = new HashMap<>();
			Cliente logguedCliente = (Cliente) request.getSession().getAttribute("cliente");
			accountFilters.put("id_cliente", String.valueOf(logguedCliente.getId()));
			CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDAOImpl(null));
			try {
				cuentas = cuentaNegocio.listAccountsByClient(logguedCliente.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("cuentas", cuentas);
			rd = request.getRequestDispatcher("MisCuentas.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("txtDNI") != null) {
			Integer dni = new Integer(request.getParameter("txtDNI"));
			ClienteNegocioImpl negocio = new ClienteNegocioImpl(null);
			try {
				loadTipoCuenta(request);
				Cliente cliente = negocio.findCliente(dni);
				if (cliente != null && cliente.getId() != 0) {
					RequestDispatcher rd = null;
					CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);
					try {
						List<Cuenta> cuentas = null;
						HashMap<String, String> accountFilters = new HashMap<>();
						accountFilters.put("id_cliente", String.valueOf(cliente.getId()));
						cuentas = cuentaNegocio.listAccountsByClient(cliente.getId());
						request.setAttribute("SelectedClient", cliente);
						request.setAttribute("cuentas", cuentas);
						rd = request.getRequestDispatcher("/GestionCuentas.jsp");
					} catch (Exception e) {
						// Manejo de excepciones
					}
					rd.forward(request, response);
				} else {
					request.setAttribute("client-not-found", "true");
					RequestDispatcher rd = request.getRequestDispatcher("/GestionCuentas.jsp");
					rd.forward(request, response);
				}
			} catch (Exception e) {
				request.setAttribute("client-not-found", "true");
			}
		}

		if (request.getParameter("save") != null) {
			Integer dni = Integer.valueOf(request.getParameter("dni"));
			entities.TipoCuenta tipoCuenta = new entities.TipoCuenta(
					Integer.parseInt(request.getParameter("tipocuenta")));
			Integer idCliente = Integer.valueOf(request.getParameter("id"));
			Date fechaActual = new Date(System.currentTimeMillis());
			BigDecimal saldoInicial = new BigDecimal("10000.00");
			Cliente cliente = new Cliente();
			ClienteNegocioImpl negocio = new ClienteNegocioImpl(null);
			try {
				cliente = negocio.findCliente(dni);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Long cbu = null;
			CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);

			while (cbu == null) {
				try {
					String cbuToFind = randomCBU().toString();
					HashMap<String, String> accountFilters = new HashMap<>();
					accountFilters.put("cbu", cbuToFind);
					Cuenta cuenta = cuentaNegocio.findCuenta(accountFilters);

					if (cuenta.getCbu() == null) {
						cbu = Long.parseLong(cbuToFind);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

			Cuenta newAccount = new Cuenta(tipoCuenta, dni, fechaActual, cbu, saldoInicial, cliente);
			String query = QueryHelper.createInsertInto("cuenta", getCuentaProperties());
			cuentaNegocio = new CuentaNegocioImpl(new CuentaDAOImpl(query));
			RequestDispatcher rd = request.getRequestDispatcher("GestionCuentas.jsp");
			try {
				cuentaNegocio.createAccount(newAccount);
				request.setAttribute("operacion", "OK");
			} catch (SQLException e) {
				request.setAttribute("operacion", "NOT OK");
				e.printStackTrace();
			} catch (Exception e) {
				request.setAttribute("operacion", "NOT OK");
				e.printStackTrace();
			} finally {
				rd.forward(request, response);
			}
		}
	}

	private Long randomCBU() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		int firstDigit = 1 + random.nextInt(9);
		sb.append(firstDigit);
		for (int i = 1; i < 18; i++) {
			int digit = random.nextInt(10);
			sb.append(digit);
		}

		Long randomNumber = new Long(sb.toString());
		return randomNumber;
	}

	private List<String> getCuentaProperties() {
		return Arrays.asList("id_tipo_cuenta", "id_cliente", "fecha_creacion", "cbu", "saldo", "deleted");
	}

	private void loadTipoCuenta(HttpServletRequest request) throws SQLException {
		SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
		String query = selectBuilder.select("tipo_cuenta.id", "tipo_cuenta.nombre", "tipo_cuenta.deleted")
				.from("tipo_cuenta").build();
		TipoCuentaNegocio tipoCuentaNegocio = new TipoCuentaNegocioImpl(new TipoCuentaDAOImpl(query));
		List<TipoCuenta> tipos = tipoCuentaNegocio.list();
		request.setAttribute("tipoCuentas", tipos);
	}
}
