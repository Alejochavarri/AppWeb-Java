package negocio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import entities.Cuenta;

public interface CuentaNegocio extends GenericNegocio<Cuenta> {
	
	public boolean createAccount(Cuenta cuenta) throws SQLException;
	Cuenta getById(int id) throws SQLException;
	public List<Cuenta> listAccountsByClient(Integer idCliente) throws SQLException;
	public Cuenta findCuenta(HashMap<String, String> filters) throws SQLException;
	public Cuenta findCuentaById(int idCuenta) throws SQLException;
	public int updateSaldoAccount(int id, BigDecimal nuevoSaldo) throws SQLException;
	public Cuenta findCuentabycbu(Long cbu) throws SQLException;
	public int transferir(Cuenta Origen, Cuenta Destino, BigDecimal Importe, String Detalle) throws SQLException;
}
