package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.CuentaDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.Cuenta;
import entities.Prestamo;
import enums.PrestamoEstado;
import negocio.CuentaNegocio;
import negocio.PrestamoNegocio;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;
import utils.sql.EqualsCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;

/**
 * Servlet implementation class AutorizarPrestamoServlet
 */
@WebServlet("/AutorizarPrestamoServlet")
public class AutorizarPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutorizarPrestamoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("AutorizarPrestamo.jsp");
		try {
			if (request.getParameter("load") != null) {
				cargarCuentas(request);
			}

			if (request.getParameter("aprobar") != null) {
				cambiarPrestamoEstado(request, PrestamoEstado.APROBADO);
				cargarCuentas(request);

			}
			if (request.getParameter("rechazar") != null) {
				cambiarPrestamoEstado(request, PrestamoEstado.DESAPROBADO);
				cargarCuentas(request);
			}
		} catch (SQLException e) {
			request.setAttribute("error", Boolean.TRUE);
		} catch (Exception e) {
			request.setAttribute("error", Boolean.TRUE);
		} finally {
			try {
				cargarPrestamos(request);
			} catch (SQLException e) {
				request.setAttribute("error", Boolean.TRUE);
				e.printStackTrace();
			}
			rd.forward(request, response);
		}
	}

	private void cargarCuentas(HttpServletRequest request) throws SQLException {
		try {
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder
					.select("cuenta.id", "cuenta.fecha_creacion", "cuenta.cbu", "cuenta.saldo", "cuenta.deleted",
							"tipo_cuenta.id", "tipo_cuenta.nombre", "tipo_cuenta.deleted")
					.innerJoin("tipo_cuenta on tipo_cuenta.id = cuenta.id_tipo_cuenta").from("cuenta").build();
			CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDAOImpl(query));
			List<Cuenta> cuentas = cuentaNegocio.list();
			request.setAttribute("cuentas", cuentas);

		} catch (SQLException e) {
			throw e;
		}
	}

	private void cambiarPrestamoEstado(HttpServletRequest request, PrestamoEstado estado) throws SQLException {
		try {
			Integer prestamoId = new Integer(request.getParameter("idPrestamo"));
			PrestamoNegocio negocio = new PrestamoNegocioImpl(null);
			if (negocio.cambiarEstadoPrestamo(prestamoId, estado)) {
				request.setAttribute("cambiarEstado", Boolean.TRUE);
			}

		} catch (SQLException e) {
			throw e;
		}
	}

	private void cargarPrestamos(HttpServletRequest request) throws SQLException {
		try {
			SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
			String query = selectbuilder
					.select("prestamo.id", "prestamo.fecha_contratacion", "prestamo.importe_con_intereses",
							"prestamo.importe_pedido", "prestamo.plazo_pago_mes", "prestamo.monto_por_mes",
							"prestamo.cuotas", "prestamo.interes_anual", "prestamo.deleted", "prestamo.estado")
					.where(new EqualsCriteria("estado", PrestamoEstado.BAJO_REVISION.toString(), Boolean.TRUE))
					.from("prestamo").build();
			PrestamoNegocio negocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(query));
			request.setAttribute("prestamos", negocio.list());
		} catch (SQLException e) {
			throw e;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("AutorizarPrestamo.jsp");

		try {
			if (request.getParameter("buscar") != null) {
				LocalDate desde = null;
				LocalDate hasta = null;
				Integer cuentaId = null;
				BigDecimal importeDesde = null;
				BigDecimal importeHasta = null;
				Integer plazoPagoDesde = null;
				Integer plazoPagoHasta = null;
				if ((request.getParameter("fecha_contratacion_desde") != null
						&& !request.getParameter("fecha_contratacion_desde").isEmpty())
						&& (request.getParameter("fecha_contratacion_hasta") != null
								&& !request.getParameter("fecha_contratacion_hasta").isEmpty())) {
					desde = LocalDate.parse(request.getParameter("fecha_contratacion_desde"));
					hasta = LocalDate.parse(request.getParameter("fecha_contratacion_hasta"));
				}
				if (request.getParameter("cuenta") != null && !request.getParameter("cuenta").isEmpty()) {
					cuentaId = new Integer(request.getParameter("cuenta"));
				}
				if ((request.getParameter("importe_pedido_desde") != null && !request.getParameter("importe_pedido_desde").isEmpty())
						&& (request.getParameter("importe_pedido_hasta") != null && !request.getParameter("importe_pedido_hasta").isEmpty())) {
					importeDesde = new BigDecimal(request.getParameter("importe_pedido_desde"));
					importeHasta = new BigDecimal(request.getParameter("importe_pedido_hasta"));

				}
				if ((request.getParameter("plazo_pago_mes_desde") != null && !request.getParameter("plazo_pago_mes_desde").isEmpty())
						&& (request.getParameter("plazo_pago_mes_hasta") != null && !request.getParameter("plazo_pago_mes_hasta").isEmpty())) {
					plazoPagoDesde = new Integer(request.getParameter("plazo_pago_mes_desde"));
					plazoPagoHasta = new Integer(request.getParameter("plazo_pago_mes_hasta"));
				}

				PrestamoNegocio negocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
				List<Prestamo> prestamos = negocio.getPrestamosFilter(desde, hasta, importeDesde, importeHasta,
						plazoPagoDesde, plazoPagoHasta, cuentaId);
				request.setAttribute("prestamos", prestamos);
			}

		} catch (SQLException e) {
			request.setAttribute("error", Boolean.TRUE);
		} catch (Exception e) {
			request.setAttribute("error", Boolean.TRUE);
		} finally {
			rd.forward(request, response);
		}
	}

}
