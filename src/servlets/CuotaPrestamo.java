package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.PrestamoDAOImpl;
import entities.Cliente;
import entities.Cuenta;
import entities.Movimiento;
//import entities.CuotaPrestamo;
import entities.Prestamo;
import enums.CuotaEstado;
import enums.TipoMovimiento;
import exceptions.PagoCuotaPrestamoException;
import exceptions.SaldoNegativoException;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.MovimientoNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;

/**
 * Servlet implementation class CuotaPrestamo
 */
@WebServlet("/CuotaPrestamo")
public class CuotaPrestamo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CuotaPrestamo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrestamoNegocioImpl prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
		try {
			int prestamoId = Integer.parseInt(request.getParameter("prestamoId"));
			Prestamo prestamo = prestamoNegocio.getPrestamoPorId(prestamoId);
			List<entities.CuotaPrestamo> cuotas = prestamoNegocio.getCuotaPrestamoByPrestamoId(prestamoId);	
			LocalDate fechaContratacion = prestamo.getFechaDeContratacion();
		    
			for (entities.CuotaPrestamo cuota : cuotas) {
		        LocalDate fechaVencimiento = fechaContratacion.plusMonths(cuota.getCuota());
		        cuota.setFechaVencimiento(fechaVencimiento);
		    }
			getCuentas(request);
			request.setAttribute("prestamo", prestamo);
	        request.setAttribute("cuotas", cuotas);
	        	        
	        RequestDispatcher dispatcher = request.getRequestDispatcher("PagoCuota.jsp");
	        dispatcher.forward(request, response);
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int prestamoId = Integer.parseInt(request.getParameter("prestamoId"));
        String[] cuotasIds = request.getParameterValues("cuotasIds");
        int cuentaId = Integer.parseInt(request.getParameter("cuentaId"));
        String movDetalle = "Pago de Cuota del Prestamo";
        CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);
        PrestamoNegocioImpl prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDAOImpl(null));
        int qtyCuotas = cuotasIds.length;
        Cuenta cuenta = null;
        Prestamo prestamo = null;
        BigDecimal qtyMultiply = BigDecimal.valueOf(qtyCuotas);
       
        try {
        	prestamo = prestamoNegocio.getPrestamoPorId(prestamoId);
        	cuenta = cuentaNegocio.findCuentaById(cuentaId);
			BigDecimal montoAPagar = prestamo.getMontoPorMes().multiply(qtyMultiply);
			
			if (montoAPagar != null && cuenta.getSaldo().compareTo(montoAPagar) < 0) {
				throw new SaldoNegativoException(
					"No posee saldo suficiente en la cuenta " + cuenta.getCbu() + ". Moto a pagar " + montoAPagar
				);
			}
			
			for (String cuotaIdStr : cuotasIds) {
				int cuotaId = Integer.parseInt(cuotaIdStr);
				Boolean isPagable = prestamoNegocio.updatePagoCuotaPrestamo(cuotaId, CuotaEstado.PAGADA);
				if(isPagable == false) {
					throw new PagoCuotaPrestamoException("La cuota con el id: " + cuotaId + " ya ha sido pagada");
				}
			}
			
			BigDecimal substractSaldo = cuenta.getSaldo().subtract(montoAPagar);
			try {
				cuentaNegocio.updateSaldoAccount(cuentaId, substractSaldo);
				insertMovimiento(cuenta, montoAPagar, movDetalle);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("saldoInsuficiente", false);
		} catch (SaldoNegativoException e) {
			request.setAttribute("saldoInsuficiente", true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (PagoCuotaPrestamoException e) {
			request.setAttribute("todasCuotasPagas", true);
		} finally {
			this.doGet(request, response);
		}

        // Redirigir a una página de confirmación o mostrar un mensaje de éxito
    }
	
	
	private void getCuentas(HttpServletRequest request) {
		List<Cuenta> cuentas = null;
		Cliente cliente = (Cliente) request.getSession().getAttribute("cliente");
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
	
	private void insertMovimiento(Cuenta cuenta, BigDecimal importe, String movDetalle) {
		Movimiento movimiento = new Movimiento();
		movimiento.setCuentaOrigen(cuenta);
		movimiento.setCuentaDestino(null);
		movimiento.setDetalle(movDetalle);
	    LocalDate fecha = LocalDate.now();
	    DateTimeFormatter formateada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = fecha.format(formateada);
        Date actualFecha = Date.valueOf(formattedDate);
		movimiento.setFecha(actualFecha);
		movimiento.setImporte(importe);
		movimiento.setTipoMovimiento(TipoMovimiento.PAGO_DE_PRESTAMO);
		
		boolean isSave = false;
		MovimientoNegocioImpl negocio = new MovimientoNegocioImpl(null);
		try {
			isSave = negocio.guardarMovimiento(movimiento);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
