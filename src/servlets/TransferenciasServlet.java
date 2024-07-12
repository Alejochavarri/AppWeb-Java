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
import javax.servlet.http.HttpSession;

import entities.Cliente;
import entities.Cuenta;
import entities.Usuario;
import negocio.ClienteNegocio;
import negocio.CuentaNegocio;
import negocio.impl.ClienteNegocioImpl;
import negocio.impl.CuentaNegocioImpl;

/**
 * Servlet implementation class TransferenciasServlet
 */
@WebServlet("/TransferenciasServlet")
public class TransferenciasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransferenciasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("IdCuenta") != null) {
			HttpSession session = request.getSession();
			
			// Request Submit btn Seleccionar Cuenta
			int Selected = Integer.parseInt(request.getParameter("IdCuenta"));
			//Cuenta cuenta = getCuenta(request,Selected);
			Cuenta cuenta = null;
			List<Cuenta> cuentas = (List<Cuenta> )session.getAttribute("cuentas");
			for(Cuenta c: cuentas) {
				if(c.getId() == Selected){
					cuenta = c;
				}
			}
			session.setAttribute("cuentaSeleccionada", cuenta);
			String Load = "1";
			request.setAttribute("Load", Load);			
			
			RequestDispatcher rd = request.getRequestDispatcher("MisTransferencias.jsp");
			rd.forward(request, response);
		}
		
		if (request.getParameter("btn-confirmar") != null) {
			
			HttpSession session = request.getSession();
			
			String Importe = request.getParameter("importe");
			String CBU_Usuario = request.getParameter("cbu-user");
			String CBU_Destino = request.getParameter("cbu-destino");
			String Detalle = request.getParameter("detalle");
			String Load = "2";
			Long cbu;
			request.setAttribute("Load", Load);
			
			Cuenta Destino = null;
			Cuenta Origen = null;
			BigDecimal ImporteBD = new BigDecimal(Importe);
			Origen = (Cuenta) session.getAttribute("cuentaSeleccionada");
			
			try {
			    cbu = Long.parseLong(CBU_Destino);
			    Destino = existeCuenta(cbu);
			    if(Destino.getCbu()!= null) {
			    	request.setAttribute("Find", "Ok");
			    	Transferir(Origen, Destino, ImporteBD, Detalle);
				}	
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			}			

			Cliente cliente = (Cliente) session.getAttribute("cliente");
			updateCuentas(session,cliente);
			request.setAttribute("Importe", Importe);
			request.setAttribute("CBU_Usuario", CBU_Usuario);
			request.setAttribute("CBU_Destino", CBU_Destino);
			request.setAttribute("Detalle", Detalle);
			
			RequestDispatcher rd = request.getRequestDispatcher("MisTransferencias.jsp");
			rd.forward(request, response);
		}

		if (request.getParameter("btn-finalizar") != null) {
			RequestDispatcher rd = request.getRequestDispatcher("MisTransferencias.jsp");
			rd.forward(request, response);
		}
		
		request.setAttribute("cuenta", "null");
		RequestDispatcher rd = request.getRequestDispatcher("MisTransferencias.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private Cuenta existeCuenta(Long cbu) {
		
		Cuenta cuenta = null;
		try {
			CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);
			cuenta = cuentaNegocio.findCuentabycbu(cbu);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cuenta;
	}
	
	private void Transferir (Cuenta Origen, Cuenta Destino,  BigDecimal Importe,String Detalle) {
		int resultado = 0;
		try {
			CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(null);
			resultado = cuentaNegocio.transferir(Origen, Destino, Importe, Detalle);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateCuentas(HttpSession session, Cliente cliente) {
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
