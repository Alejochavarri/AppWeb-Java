package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.PrestamoDAOImpl;
import entities.Cliente;
import entities.Cuenta;
import entities.CuotaPrestamo;
import entities.Movimiento;
import entities.Prestamo;
import enums.CuotaEstado;
import enums.PrestamoEstado;
import enums.TipoMovimiento;
import exceptions.PagoCuotaPrestamoException;
import exceptions.SaldoNegativoException;
import negocio.PrestamoNegocio;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.MovimientoNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;

/**
 * Servlet implementation class GestionPrestamos
 */
@WebServlet("/GestionPrestamos")
public class GestionPrestamos extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GestionPrestamos() {
		super();
		prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
	}

	private PrestamoNegocio prestamoNegocio = null;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (this.prestamoNegocio == null) {
			prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
		}
		try {
			Cliente logguedCliente = (Cliente) request.getSession().getAttribute("cliente");
			List<Prestamo> prestamos;
			prestamos = this.prestamoNegocio.getSoloPrestamoPorCliente(logguedCliente.getId());
	        for (Prestamo prestamo : prestamos) {
	            List<CuotaPrestamo> cuotas = this.prestamoNegocio.getCuotaPrestamoByPrestamoId(prestamo.getId());
	            prestamo.setCuotaPrestamos(cuotas);
	        }
	        
			request.setAttribute("prestamos", prestamos);
			getCuentas(request, logguedCliente);
			separatePrestamosByEstados(prestamos, request);
			RequestDispatcher rd = request.getRequestDispatcher("/GestionarPrestamo.jsp");
			rd.forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// RequestDispatcher rd =
	// request.getRequestDispatcher("/GestionarPrestamo.jsp");
	// rd.forward(request, response);
	// }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {}

	private void separatePrestamosByEstados(List<Prestamo> prestamos, HttpServletRequest request) {
		List<Prestamo> prestamosBajoRevision = prestamos.stream()
				.filter(p -> p.getEstado() == PrestamoEstado.BAJO_REVISION).collect(Collectors.toList());
		List<Prestamo> prestamosAprobados = prestamos.stream().filter(p -> p.getEstado() == PrestamoEstado.APROBADO)
				.collect(Collectors.toList());
		List<Prestamo> prestamosDesaprobados = prestamos.stream()
				.filter(p -> p.getEstado() == PrestamoEstado.DESAPROBADO).collect(Collectors.toList());

		request.setAttribute("prestamos_aprobados", prestamosAprobados);
		request.setAttribute("prestamos_bajo_revision", prestamosBajoRevision);
		request.setAttribute("prestamos_desaprobados", prestamosDesaprobados);
	}

	private void getCuentas(HttpServletRequest request, Cliente cliente) {
		List<Cuenta> cuentas = null;
//		cuentas = (List<Cuenta>) request.getSession().getAttribute("cuentas");
		CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);
		try {
			cuentas = cuentaNegocio.listAccountsByClient(cliente.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		session.setAttribute("cuentas", cuentas);
		request.setAttribute("cuentas", cuentas);

	}
}
