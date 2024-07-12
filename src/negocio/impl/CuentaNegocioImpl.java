package negocio.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import dao.CuentaDAO;
import dao.GenericDAO;
import dao.impl.CuentaDAOImpl;
import entities.Cuenta;
import negocio.CuentaNegocio;
import utils.sql.EqualsCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;


public class CuentaNegocioImpl extends GenericNegocioImpl<Cuenta> implements CuentaNegocio {

	public CuentaNegocioImpl(GenericDAO<Cuenta> dao) {
		super(dao);
	}

	@Override
	public Cuenta getById(int id) throws SQLException {
		HashMap<String, String> filters = new HashMap<>();
		filters.put("id", String.valueOf(id));
		CuentaDAO dao = new CuentaDAOImpl(QueryHelper.createSelectQuery("cuenta", filters));
		return dao.getById();
	}
	
	private String queryFindCuenta(int idCuenta) {
		SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
		String query = selectbuilder
				.select("cuenta.id", "cuenta.id_tipo_cuenta", "cuenta.id_cliente", "cuenta.fecha_creacion",
						"cuenta.cbu", "cuenta.saldo", "cuenta.deleted", "tipo_cuenta.id", "tipo_cuenta.nombre", "tipo_cuenta.deleted")
				.innerJoin("tipo_cuenta ON cuenta.id_tipo_cuenta = tipo_cuenta.id")
				.where(new EqualsCriteria("cuenta.id", idCuenta, Boolean.TRUE))
				.from("cuenta").build();
		return query;
	}
	
	private String queryFindCuentabycbu(Long cbuCuenta) {
		SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
		String query = selectbuilder
				.select("cuenta.id", "cuenta.id_tipo_cuenta", "cuenta.id_cliente", "cuenta.fecha_creacion",
						"cuenta.cbu", "cuenta.saldo", "cuenta.deleted", "tipo_cuenta.id", "tipo_cuenta.nombre", "tipo_cuenta.deleted")
				.innerJoin("tipo_cuenta ON cuenta.id_tipo_cuenta = tipo_cuenta.id")
				.where(new EqualsCriteria("cuenta.cbu", cbuCuenta, Boolean.TRUE))
				.from("cuenta").build();
		return query;
	}
	
	@Override
	public List<Cuenta> listAccountsByClient(Integer idCliente) throws SQLException{
		List<Cuenta> cuentas = null;
		SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
		String query = selectbuilder
				.select("cuenta.id", "cuenta.id_tipo_cuenta", "cuenta.id_cliente", "cuenta.fecha_creacion",
						"cuenta.cbu", "cuenta.saldo", "cuenta.deleted", "tipo_cuenta.id", "tipo_cuenta.nombre", "tipo_cuenta.deleted")
				.innerJoin("tipo_cuenta ON cuenta.id_tipo_cuenta = tipo_cuenta.id")
				.where(new EqualsCriteria("cuenta.id_cliente", idCliente, Boolean.TRUE))
				.from("cuenta").build();
		CuentaDAOImpl cuentaDAO = new CuentaDAOImpl(query);
		cuentas = cuentaDAO.list();
		return cuentas;	
	}
	
	@Override
	public Cuenta findCuenta(HashMap<String, String> filters) throws SQLException {
		Cuenta cuenta = null;
		try {
			String query = QueryHelper.findUser("cuenta", filters);
			CuentaDAO cuentaDao = new CuentaDAOImpl(query);
			cuenta = cuentaDao.getById();
		} catch (SQLException e) {
			throw e;
		}
		return cuenta;
	}
	
	@Override
	public Cuenta findCuentaById(int idCuenta) throws SQLException {
		Cuenta cuenta = null;
		String query = queryFindCuenta(idCuenta);
		try {
			CuentaDAO cuentaDao = new CuentaDAOImpl(query);
			cuenta = cuentaDao.getById();
		} catch (SQLException e) {
			throw e;
		}
		return cuenta;
	}
	
	@Override
	public boolean createAccount(Cuenta cuenta) throws SQLException {
		try {
		Object[] accountProperties2 = new Object[] {cuenta.getTipoCuenta().getId(), cuenta.getCliente().getId(), cuenta.getFechaCreacion(), cuenta.getCbu(), cuenta.getSaldo(), cuenta.isDeleted()};
		boolean cuentaCreada = this.create(cuenta, accountProperties2);
		return cuentaCreada;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	@Override
	public int updateSaldoAccount(int id, BigDecimal nuevoSaldo) throws SQLException {
		String query = "UPDATE cuenta SET saldo = ? WHERE id = ?";
		CuentaDAOImpl cuentaDAO = new CuentaDAOImpl(query);
		return cuentaDAO.actualizarSaldo(id, nuevoSaldo);
	}
	
	@Override
	public Cuenta findCuentabycbu(Long cbu) throws SQLException {
		Cuenta cuenta = null;
		String query = queryFindCuentabycbu(cbu);
		try {
			CuentaDAO cuentaDao = new CuentaDAOImpl(query);
			cuenta = cuentaDao.existbycbu();
		} catch (SQLException e) {
			throw e;
		}
		return cuenta;
	}

	@Override
	public int transferir(Cuenta Origen, Cuenta Destino, BigDecimal Importe,String Detalle) throws SQLException {
		int rowsAffected;
		String query = ("{CALL sp_transferencia(?, ?, ?, ?, ?, ?)}");
		CuentaDAO cuentaDao = new CuentaDAOImpl(query);
		rowsAffected = cuentaDao.Transferir(Origen.getId(), Destino.getId(), Origen.getCbu(), Destino.getCbu(), Importe, Detalle);
		return rowsAffected;
	}

	

}
