package negocio.impl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.GenericDAO;
import dao.MovimientoDAO;
import dao.impl.MovimientoDAOImpl;
import entities.Movimiento;
import enums.TipoCuenta;
import negocio.MovimientoNegocio;
import utils.QueryHelper;
import utils.sql.BetweenCriteria;
import utils.sql.InCriteria;
import utils.sql.SQLInsertBuilder;
import utils.sql.SQLSelectBuilder;

public class MovimientoNegocioImpl extends GenericNegocioImpl<Movimiento> implements MovimientoNegocio {

	public MovimientoNegocioImpl(GenericDAO<Movimiento> dao) {
		super(dao);
	}
	
	@Override
	public List<Movimiento> getMovimientoPorCliente(int id) throws SQLException {
		try {
			HashMap<String, String> filters = new HashMap<>();
			filters.put("cuenta_origen.id_cliente", String.valueOf(id));
			String query = QueryHelper.findMovimientosCuentas("movimiento", filters);
			MovimientoDAO dao = new MovimientoDAOImpl(query);
			return dao.list();
		} catch (SQLException e) {
			throw e;
		}

	}
	
	@Override
	public List<Movimiento> getMovimientosConCuenta(HashMap<String, String> filters) throws SQLException {
		try {			
			String query = QueryHelper.findMovimientosCuentas("movimiento", filters);
			MovimientoDAO dao = new MovimientoDAOImpl(query);
			return dao.getMovimientoConCuentas();
		} catch (SQLException e) {
			throw e;
		}

	}
	
	@Override
	public List<Movimiento> getMovimientos() throws SQLException {
		try {
			HashMap<String, String> filters = new HashMap<>();			
			String query = QueryHelper.findMovimientosCuentas("movimiento", filters);
			MovimientoDAO dao = new MovimientoDAOImpl(query);
			return dao.getMovimientoConCuentas();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	@Override
	public List<Movimiento> getMovimientosFechas(String startDate, String endDate) throws SQLException {
		try {
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder
					.select("movimiento.*, cuenta_origen.cbu as cbu_origen, cuenta_origen.saldo as saldo_origen, tcuenta.nombre as tipocuenta, cuenta_origen.fecha_creacion as fecha_creacion_origen, cuenta_origen.deleted as deleted_origen, "
				               + "cuenta_destino.cbu as cbu_destino, cuenta_destino.saldo as saldo_destino, cuenta_destino.fecha_creacion as fecha_creacion_destino, cuenta_destino.deleted as deleted_destino, tcuenta.id as id_cuenta_origen, tcuenta.deleted as deleted_cuenta_origen ")
					.leftJoin("cuenta AS cuenta_origen ON movimiento.idcuenta_origen = cuenta_origen.id")
					.leftJoin("tipo_cuenta AS tcuenta ON cuenta_origen.id_tipo_cuenta = tcuenta.id")
					.leftJoin("cuenta AS cuenta_destino ON movimiento.idcuenta_destino = cuenta_destino.id ")
					.where(new BetweenCriteria("movimiento.fecha", startDate, endDate, true))
					.from("movimiento")
					.build();
			MovimientoDAO dao = new MovimientoDAOImpl(query);
			System.out.println(query);
			return dao.getMovimientoConCuentas();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	@Override
	public boolean guardarMovimiento(Movimiento movimiento) throws SQLException {
		try {
			
			HashMap<String, Object> values = new HashMap<String, Object>();
			values.put("fecha", movimiento.getFecha());
	        values.put("idcuenta_origen", movimiento.getCuentaOrigen().getId());
	        values.put("importe", movimiento.getImporte());
	        values.put("tipo_movimiento", movimiento.getTipoMovimiento());
	        values.put("idcuenta_destino", null);
	        values.put("detalle", movimiento.getDetalle());

	        SQLInsertBuilder builder = new SQLInsertBuilder();
	        String query = builder.into("movimiento").values(values).build();
			MovimientoDAO dao = new MovimientoDAOImpl(query);
			return dao.guardarMovimiento(movimiento, query);
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public List<Movimiento> getMovimientosPorCuentaOrigen(List<Object> ids) throws SQLException {
		try {
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder
					.select("movimiento.*, cuenta_origen.cbu as cbu_origen, cuenta_origen.saldo as saldo_origen, tcuenta.nombre as tipocuenta, cuenta_origen.fecha_creacion as fecha_creacion_origen, cuenta_origen.deleted as deleted_origen, "
				               + "cuenta_destino.cbu as cbu_destino, cuenta_destino.saldo as saldo_destino, cuenta_destino.fecha_creacion as fecha_creacion_destino, cuenta_destino.deleted as deleted_destino, tcuenta.id as id_cuenta_origen, tcuenta.deleted as deleted_cuenta_origen ")
					.innerJoin("cuenta AS cuenta_origen ON movimiento.idcuenta_origen = cuenta_origen.id")
					.innerJoin("tipo_cuenta AS tcuenta ON cuenta_origen.id_tipo_cuenta = tcuenta.id")
					.leftJoin("cuenta AS cuenta_destino ON movimiento.idcuenta_destino = cuenta_destino.id ")
					.where(new InCriteria(ids, "movimiento.idcuenta_origen"))
					.from("movimiento")
					.build();
			MovimientoDAO dao = new MovimientoDAOImpl(query);
			return dao.getMovimientoConCuentas();
		}catch(SQLException e) {
			throw e;
		}
	}
	
	@Override
	public List<Movimiento> getMovimientosFiltrados(LocalDate startDate, LocalDate endDate, String tipoMovimiento, String tipoCuenta) throws SQLException {
//		List<Movimiento> movimientos = null;
		StringBuilder query = new StringBuilder(
				"SELECT movimiento.*, " +
				"cuenta_origen.cbu as cbu_origen, cuenta_origen.saldo as saldo_origen, tcuenta.nombre as tipocuenta, cuenta_origen.fecha_creacion as fecha_creacion_origen, cuenta_origen.deleted as deleted_origen, " +
			    "cuenta_destino.cbu as cbu_destino, cuenta_destino.saldo as saldo_destino, cuenta_destino.fecha_creacion as fecha_creacion_destino, cuenta_destino.deleted as deleted_destino, tcuenta.id as id_cuenta_origen, tcuenta.deleted as deleted_cuenta_origen " +
				"FROM movimiento " +
                "INNER JOIN Cuenta cuenta_origen ON movimiento.idcuenta_origen = cuenta_origen.id " +
                "LEFT JOIN Cuenta cuenta_destino ON movimiento.idcuenta_destino = cuenta_destino.id " +
                "INNER JOIN tipo_cuenta AS tcuenta ON cuenta_origen.id_tipo_cuenta = tcuenta.id  " +
                "WHERE movimiento.id <> 0"
                );
        List<Object> parameters = new ArrayList<>();
//		HashMap<String, String> filters = new HashMap<>();
//		String query = QueryHelper.findMovimientosCuentas("movimiento", filters);

        if (startDate != null) {
            query.append(" AND movimiento.fecha >= ?");
            parameters.add(startDate);
        }
        if (endDate != null) {
            query.append(" AND movimiento.fecha <= ?");
            parameters.add(endDate);
        }
        if (tipoMovimiento != null) {
            query.append(" AND movimiento.tipo_movimiento = ?");
            parameters.add(tipoMovimiento);
        }
        if (tipoCuenta != null) {
            query.append(" AND tcuenta.nombre = ?");
            parameters.add(tipoCuenta);
        }
        
//        System.out.println("Query movimientos: " + query);
        MovimientoDAO dao = new MovimientoDAOImpl(query.toString());
		return dao.getMovimientosConFiltros(query.toString(), parameters);
	}
	
}
