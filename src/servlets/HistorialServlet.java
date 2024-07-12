package servlets;

import java.io.IOException;
//import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entities.Cliente;
import entities.Movimiento;
import enums.TipoCuenta;
import enums.TipoMovimiento;
import negocio.impl.MovimientoNegocioImpl;

/**
 * Servlet implementation class HistorialServlet
 */
@WebServlet("/HistorialServlet")
public class HistorialServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistorialServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("cuenta") != null) {
			int cuentaId = Integer.parseInt(request.getParameter("cuenta"));
			MovimientoNegocioImpl movimientoNegocio = new MovimientoNegocioImpl(null);
			HashMap<String, String> filters = new HashMap<>();
			filters.put("idcuenta_origen", String.valueOf(cuentaId));
			try {
				List<Movimiento> movimientos = movimientoNegocio.getMovimientosConCuenta(filters);
				request.setAttribute("movimientos", movimientos);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} else {
			Cliente logguedCliente = (Cliente) request.getSession().getAttribute("cliente");
			MovimientoNegocioImpl movimientoNegocio = new MovimientoNegocioImpl(null);
			List<Movimiento> movimientos = null;
			HashMap<String, String> filters = new HashMap<>();
			filters.put("cuenta_origen.id_cliente", String.valueOf(logguedCliente.getId()));
			try {
				movimientos = movimientoNegocio.getMovimientosConCuenta(filters);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("movimientos", movimientos);			
		}
		RequestDispatcher rd = null;
		rd = request.getRequestDispatcher("/MiHistorial.jsp");
		rd.forward(request, response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String periodo = request.getParameter("periodo");
        String operacion = request.getParameter("operacion");
        String estado = request.getParameter("estado");

        if(!periodo.equals("Todo") && !operacion.equals("Todo") && estado.equals("Todo")) {
        	doGet(request, response);
        }

        // Lógica para convertir los filtros a valores utilizables
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.now();

        if (periodo != null) {
            switch (periodo) {
                case "Ultima semana":
                    startDate = endDate.minus(1, ChronoUnit.WEEKS);
                    break;
                case "Ultimo mes":
                    startDate = endDate.minus(1, ChronoUnit.MONTHS);
                    break;
                case "Ultimo ano":
                    startDate = endDate.minus(1, ChronoUnit.YEARS);
                    break;
                default:
                    startDate = null;
                    break;
            }
        }

        // Convierte las selecciones de operacion y estado a formatos utilizables
        String tipoMovimiento = null;
        if (operacion != null) {
            switch (operacion) {
                case "Alta de Cuenta":
                    tipoMovimiento = TipoMovimiento.ALTA_DE_CUENTA.toString();
                    break;
                case "Alta de Prestamo":
                    tipoMovimiento = TipoMovimiento.ALTA_DE_PRESTAMO.toString();
                    break;
                case "Pago Prestamo":
                    tipoMovimiento = TipoMovimiento.PAGO_DE_PRESTAMO.toString();
                    break;
                case "Transferencia":
                    tipoMovimiento = TipoMovimiento.TRANSFERENCIA.toString();
                    break;
                default:
                    tipoMovimiento = null;
                    break;
            }
        }

        String tipoCuenta = null;
        if (estado != null) {
            switch (estado) {
                case "Corriente":
                    tipoCuenta = "CUENTA CORRIENTE";
                    break;
                case "Ahorro":
                    tipoCuenta = TipoCuenta.CAJA_DE_AHORRO.toString();
                    break;
                default:
                    tipoCuenta = null;
                    break;
            }
        }
        MovimientoNegocioImpl movimientoNegocio = new MovimientoNegocioImpl(null);
        List<Movimiento> movimientos = null; 
        try {
			movimientos = movimientoNegocio.getMovimientosFiltrados(startDate, endDate, tipoMovimiento, tipoCuenta);
			request.setAttribute("movimientos", movimientos);
			RequestDispatcher rd = null;
			rd = request.getRequestDispatcher("/MiHistorial.jsp");
			rd.forward(request, response);		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		doGet(request, response);
	}

}
