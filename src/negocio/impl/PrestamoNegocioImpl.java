package negocio.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.CuotaPrestamoDAO;
import dao.GenericDAO;
import dao.PrestamoDAO;
import dao.impl.CuotaPrestamoDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.CuotaPrestamo;
import entities.Prestamo;
import enums.CuotaEstado;
import enums.PrestamoEstado;
import negocio.PrestamoNegocio;
import utils.SearchCriteria;
import utils.sql.*;

public class PrestamoNegocioImpl extends GenericNegocioImpl<Prestamo> implements PrestamoNegocio {

	public PrestamoNegocioImpl(GenericDAO<Prestamo> dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Prestamo> getPrestamoPorCliente(int id) throws SQLException {
		try {
			HashMap<String, String> filters = new HashMap<>();
			filters.put("id_cliente", String.valueOf(id));
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder
					.select("p.id", "p.fecha_contratacion", "p.importe_con_intereses", "p.importe_pedido",
					"p.plazo_pago_mes", "p.monto_por_mes", "p.cuotas", "p.interes_anual", "p.deleted", "p.estado", "p.monto_por_mes")
					.where(new EqualsCriteria("p.id_cliente", id, Boolean.FALSE))
					.from("prestamo p")
					.build();
			System.out.println("query pedir prestamos: " + query);
			PrestamoDAO dao = new PrestamoDAOImpl(query);
			return dao.getPrestamosPorCliente();
		} catch (SQLException e) {
			throw e;
		}

	}

	@Override
	public List<Prestamo> getSoloPrestamoPorCliente(int id) throws SQLException {
		try {
			HashMap<String, String> filters = new HashMap<>();
			filters.put("id_cliente", String.valueOf(id));
			String query = QueryHelper.createSelectQuery("prestamo", filters);
			System.out.println("query prestamos login: " + query);
			PrestamoDAO dao = new PrestamoDAOImpl(QueryHelper.createSelectQuery("prestamo", filters));
			return dao.getSoloPrestamosPorCliente();
		} catch (SQLException e) {
			throw e;
		}

	}
	
	@Override
	public Prestamo getPrestamoPorId(int id_prestamo) throws SQLException {
		try {
			HashMap<String, String> filters = new HashMap<>();
			filters.put("id", String.valueOf(id_prestamo));
			PrestamoDAO dao = new PrestamoDAOImpl(QueryHelper.createSelectQuery("prestamo", filters));
			return dao.getPrestamoPorId(id_prestamo);
		} catch (SQLException e) {
			throw e;
		}

	}

	@Override
	public List<Prestamo> getPrestamosFilter(LocalDate desde, LocalDate hasta, BigDecimal importeDesde,
			BigDecimal importeHasta, Integer plazoPagoDesde, Integer plazoPagoHasta, Integer cuentaId)
			throws SQLException {
		try {
			List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
			if(desde != null && hasta != null) {
				criterias.add(new BetweenCriteria("prestamo.fecha_contratacion", desde, hasta, Boolean.TRUE));
			}
			if(importeDesde != null && importeHasta != null) {
				criterias.add(new BetweenCriteria("prestamo.importe_con_intereses", importeDesde, importeHasta, Boolean.FALSE));
			}
			if(plazoPagoDesde != null && plazoPagoHasta != null) {
				criterias.add(new BetweenCriteria("prestamo.plazo_pago_mes", plazoPagoDesde, plazoPagoHasta, Boolean.FALSE));
			}
			if(cuentaId != null) {
				criterias.add(new EqualsCriteria("prestamo.estado", PrestamoEstado.BAJO_REVISION.toString(), Boolean.TRUE));
			}
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder
					.select("prestamo.id", "prestamo.fecha_contratacion", "prestamo.importe_con_intereses", "prestamo.importe_pedido",
							"prestamo.plazo_pago_mes", "prestamo.monto_por_mes", "prestamo.cuotas", "prestamo.interes_anual", "prestamo.deleted", "prestamo.estado")
					.where(criterias)
					.from("prestamo")
					.build();
			PrestamoDAO dao = new PrestamoDAOImpl(query);
			return dao.list();

		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public Boolean cambiarEstadoPrestamo(Integer idPrestamo, PrestamoEstado estado) throws SQLException {
		try {
			SQLUpdateBuilder updateQuery = new SQLUpdateBuilder();
			Map<String, Object> propertiesToUpdate = new HashMap<>();
			propertiesToUpdate.put("estado", estado.toString());
			String query = updateQuery.set(propertiesToUpdate)
					.where(new EqualsCriteria("id", idPrestamo , Boolean.TRUE))
					.from("prestamo")
					.build();
			PrestamoDAO dao = new PrestamoDAOImpl(query);
			return dao.cambiarEstado(estado);
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public List<CuotaPrestamo> getCuotaPrestamoByPrestamoId(int id_prestamo) throws SQLException {
		HashMap<String, String> filters = new HashMap<>();
		filters.put("cuota.id_prestamo", String.valueOf(id_prestamo));
		try {
			String query = utils.QueryHelper.findCuotasPrestamos("cuota_prestamo", filters);
			CuotaPrestamoDAOImpl dao = new CuotaPrestamoDAOImpl(query);
			return dao.list();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	@Override
	public CuotaPrestamo findCuotaPrestamoById(int idCuota) throws SQLException {
		try {
			HashMap<String, String> filters = new HashMap<>();
			filters.put("id", String.valueOf(idCuota));
			String query = utils.QueryHelper.genericFinder("cuota_prestamo", filters);
			CuotaPrestamoDAO dao = new CuotaPrestamoDAOImpl(query);
			return dao.findCuotaPrestamo();			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Boolean updatePagoCuotaPrestamo(int idCuota, CuotaEstado estado) throws SQLException {
		try {
			CuotaPrestamo cuota = this.findCuotaPrestamoById(idCuota);
			Integer cuota_id = cuota.getId();
			SQLUpdateBuilder updateQuery = new SQLUpdateBuilder();
			Map<String, Object> propertiesToUpdate = new HashMap<>();
			propertiesToUpdate.put("estado", estado.toString());
			String query = updateQuery.set(propertiesToUpdate)
					.where(new EqualsCriteria("id", cuota_id , Boolean.TRUE))
					.from("cuota_prestamo")
					.build();
			CuotaPrestamoDAO dao = new CuotaPrestamoDAOImpl(query);
			return dao.cambiarEstado(estado);
		}catch(SQLException e) {
			throw e;
		}
	}

}
