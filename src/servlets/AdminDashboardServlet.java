package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.CuentaDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.Prestamo;
import enums.PrestamoEstado;
import negocio.CuentaNegocio;
import negocio.PrestamoNegocio;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;
import utils.sql.EqualsCriteria;
import utils.sql.SQLSelectBuilder;

/**
 * Servlet implementation class AdminDashboardServlet
 */
@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminDashboardServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("AdminDashboard.jsp");
		try {
			if (request.getParameter("load") != null) {
				cargarPrestamos(request);
				cargarCuentas(request);
			}
		} catch (SQLException e) {
			request.setAttribute("error", Boolean.TRUE);
		} catch (Exception e) {
			request.setAttribute("error", Boolean.TRUE);
		} finally {
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void cargarPrestamos(HttpServletRequest request) throws SQLException {
		try {
			SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
			String query = selectbuilder
					.select("p.id", "p.fecha_contratacion", "p.importe_con_intereses", "p.importe_pedido",
							"p.plazo_pago_mes", "p.monto_por_mes", "p.cuotas", "p.interes_anual", "p.deleted",
							"p.estado")
					.where(new EqualsCriteria("estado", PrestamoEstado.BAJO_REVISION.toString(), Boolean.TRUE))
					.from("prestamo p").build();
			selectbuilder = new SQLSelectBuilder();
			String query2 = selectbuilder
					.select("p.id", "p.fecha_contratacion", "p.importe_con_intereses", "p.importe_pedido",
							"p.plazo_pago_mes", "p.monto_por_mes", "p.cuotas", "p.interes_anual", "p.deleted",
							"p.estado")
					.where(new EqualsCriteria("MONTH(fecha_contratacion)", "MONTH(CURDATE())", Boolean.FALSE))
					.from("prestamo p").build();
			PrestamoNegocio negocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(query));
			
			int prestamosPendientes = negocio.list().size();
			request.setAttribute("prestamos", negocio.list().size());
			
			negocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(query2));
			List<Prestamo> prestamos = negocio.list();
			BigDecimal totalAprobado = BigDecimal.ZERO;
	        for (Prestamo prestamo : prestamos) {
	            if (prestamo.getEstado() == PrestamoEstado.APROBADO) {
	            	totalAprobado = totalAprobado.add(prestamo.getImportePedido());
	            }
	        }
	        request.setAttribute("TotalAprobadoprestamos", totalAprobado);
			int prestamosSolicitados = negocio.list().size();
			request.setAttribute("prestamosSolicitados", prestamosSolicitados);
		} catch (SQLException e) {
			throw e;
		}
	}
	private void cargarCuentas(HttpServletRequest request) throws SQLException {
		try {
			SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
			String query = selectbuilder
					.select("cuenta.id", "cuenta.id_tipo_cuenta", "cuenta.id_cliente", "cuenta.fecha_creacion",
							"cuenta.cbu", "cuenta.saldo", "cuenta.deleted", "tipo_cuenta.id", "tipo_cuenta.nombre", "tipo_cuenta.deleted")
					.innerJoin("tipo_cuenta ON cuenta.id_tipo_cuenta = tipo_cuenta.id")
					.where(new EqualsCriteria("cuenta.deleted", "0", Boolean.TRUE))
					.from("cuenta").build();
			CuentaNegocio negocio = new CuentaNegocioImpl(new CuentaDAOImpl(query));
			request.setAttribute("cuentasOperativas", negocio.list().size());
		} catch (SQLException e) {
			throw e;
		}
	}
}
