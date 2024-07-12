package negocio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import entities.Movimiento;

public interface MovimientoNegocio extends GenericNegocio<Movimiento> {

	List<Movimiento> getMovimientoPorCliente(int id) throws SQLException;
	public List<Movimiento> getMovimientosConCuenta(HashMap<String, String> filters) throws SQLException;
	public List<Movimiento> getMovimientos() throws SQLException;
	public List<Movimiento> getMovimientosFechas(String startDate, String endDate) throws SQLException;
	public boolean guardarMovimiento(Movimiento movimiento) throws SQLException;
	public List<Movimiento> getMovimientosPorCuentaOrigen(List<Object> ids) throws SQLException;
	public List<Movimiento> getMovimientosFiltrados(LocalDate startDate, LocalDate endDate, String tipoMovimiento, String tipoCuenta) throws SQLException;
}
