package dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import entities.Cuenta;

public interface CuentaDAO extends GenericDAO<Cuenta> {

	public Cuenta getById() throws SQLException;
	public List<Cuenta> list() throws SQLException;
	public int actualizarSaldo(int cuentaId, BigDecimal nuevoSaldo) throws SQLException;
	public Cuenta existbycbu() throws SQLException;
	public int Transferir(int IdCuentaOrigen, int IdCuentaDestino, Long CBUCuentaOrigen, Long CBUCuentaDestino, BigDecimal Importe,String Detalle) throws SQLException;
}
