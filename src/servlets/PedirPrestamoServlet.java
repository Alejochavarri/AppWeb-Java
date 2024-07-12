package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.CuentaDAOImpl;
import dao.impl.GenericDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.Cliente;
import entities.Cuenta;
import entities.Prestamo;
import enums.PrestamoEstado;
import negocio.CuentaNegocio;
import negocio.PrestamoNegocio;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;
import utils.sql.QueryHelper;

/**
 * Servlet implementation class PedirPrestamoServlet
 */
@WebServlet("/PedirPrestamoServlet")
public class PedirPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PedirPrestamoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("load") != null) {
			try {
				Cliente logguedCliente = (Cliente) request.getSession().getAttribute("cliente");
				List<Prestamo> prestamos = getPrestamos(logguedCliente);
				request.setAttribute("prestamos", prestamos);
				RequestDispatcher rd = request.getRequestDispatcher("/PedirPrestamo.jsp");
				rd.forward(request, response);
			} catch (SQLException e) {
				request.setAttribute("error", Boolean.TRUE);
				e.printStackTrace();
			}
		}
	}

	private List<Prestamo> getPrestamos(Cliente logguedCliente) throws SQLException {
		PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
		List<Prestamo> prestamos;
			prestamos = prestamoNegocio.getSoloPrestamoPorCliente(logguedCliente.getId());
		return prestamos;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("pedir_prestamo") != null) {
			RequestDispatcher rd = request.getRequestDispatcher("/PedirPrestamo.jsp");
			Cliente logguedCliente = (Cliente) request.getSession().getAttribute("cliente");
			try {
				LocalDate fechaContratacion = LocalDate.parse(request.getParameter("fecha_contratacion"));
				BigDecimal importePedido = new BigDecimal(request.getParameter("importe_pedido"));
				Integer plazoMes = Integer.valueOf(request.getParameter("plazo_pago_mes"));
				int cuentaId = Integer.valueOf(request.getParameter("cuenta"));

				Cuenta cuenta = new Cuenta(cuentaId);
				Prestamo prestamo = new Prestamo(logguedCliente, fechaContratacion, importePedido, plazoMes, cuenta, PrestamoEstado.BAJO_REVISION);
				List<String> properties = new ArrayList<>();
				properties.add("id_cliente");
				properties.add("fecha_contratacion");
				properties.add("importe_con_intereses");
				properties.add("importe_pedido");
				properties.add("plazo_pago_mes");
				properties.add("monto_por_mes");
				properties.add("cuotas");
				properties.add("id_cuenta");
				properties.add("deleted");
				properties.add("estado");
				PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(
						new GenericDAOImpl<Prestamo>(QueryHelper.createInsertInto("prestamo", properties)));
				Object[] prestamoProperties = new Object[] { prestamo.getCliente().getId(),
						prestamo.getFechaDeContratacion(), prestamo.getImporteTotal(), prestamo.getImportePedido(),
						prestamo.getPlazoEnMeses(), prestamo.getMontoPorMes(), prestamo.getCuotas(),
						prestamo.getCuenta().getId(), prestamo.isDeleted(), prestamo.getEstado().toString() };
				
				Boolean result = prestamoNegocio.create(prestamo, prestamoProperties);
				if(result) {
					request.setAttribute("success", Boolean.TRUE);
				}
			} catch (SQLException e) {
				request.setAttribute("error", Boolean.FALSE);
				e.printStackTrace();
			} finally {
				List<Prestamo> prestamos;
				try {
					prestamos = getPrestamos(logguedCliente);
					request.setAttribute("prestamos", prestamos);
				} catch (SQLException e) {
					request.setAttribute("error", Boolean.TRUE);
				}
				rd.forward(request, response);
			}

		}
	}

	private Cuenta getCuenta(int cuentaId) throws SQLException {
		try {
			CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(null);
			return cuentaNegocio.getById(cuentaId);
		} catch (SQLException e) {
			throw e;
		}
	}

}
