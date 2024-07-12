package servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.MovimientoDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.Cuenta;
import entities.Movimiento;
import negocio.MovimientoNegocio;
import negocio.PrestamoNegocio;
import negocio.impl.MovimientoNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;
import utils.sql.InCriteria;
import utils.sql.SQLSelectBuilder;

/**
 * Servlet implementation class ClienteDashboardServlet
 */
@WebServlet("/ClienteDashboardServlet")
public class ClienteDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClienteDashboardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("ClienteDashboard.jsp");
        try {
        	if(request.getParameter("load")!= null) {
        		List<Cuenta> cuentas = (List<Cuenta>) request.getSession().getAttribute("cuentas");
        		if (cuentas != null && !cuentas.isEmpty()) {
        			List<Object> ids = cuentas.stream().map(Cuenta::getId).collect(Collectors.toList());
        			MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(null);
        			List<Movimiento> movimientos = movimientoNegocio.getMovimientosPorCuentaOrigen(ids);
        			request.setAttribute("movimientos", movimientos);
        		}
        		
        	}
        } catch (Exception e) {
            request.setAttribute("error", Boolean.TRUE);
        } finally {
            rd.forward(request, response);
        }
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
