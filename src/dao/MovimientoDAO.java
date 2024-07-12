package dao;

import java.sql.SQLException;
import java.util.List;

import entities.Movimiento;

public interface MovimientoDAO extends GenericDAO<Movimiento> {
	public List<Movimiento> getMovimientoConCuentas() throws SQLException;
	public List<Movimiento> list() throws SQLException;
	public boolean guardarMovimiento(Movimiento movimiento, String query) throws SQLException;
	public List<Movimiento> getMovimientosConFiltros(String query, List<Object> parameters) throws SQLException;
}
