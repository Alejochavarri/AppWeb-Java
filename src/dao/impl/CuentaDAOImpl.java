package dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.CallableStatement;

import dao.CuentaDAO;
import entities.Cuenta;
import entities.TipoCuenta;
import utils.ResultSetMapper;

public class CuentaDAOImpl extends GenericDAOImpl<Cuenta> implements CuentaDAO {

	
	public CuentaDAOImpl(String createSelectQuery) {
		super(createSelectQuery);
	}

	@Override
	public List<Cuenta> list() throws SQLException {
		List<Cuenta> cuentas = new ArrayList<Cuenta>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				Cuenta cuenta = ResultSetMapper.cuentaFromResultSet(rs);
				entities.TipoCuenta tipoCuenta = ResultSetMapper.tipoCuentaFromResultSet(rs);
				cuenta.setTipoCuenta(tipoCuenta);
				cuentas.add(cuenta);
			}	
			return cuentas;
		}
	}

	@Override
	public Cuenta getById() throws SQLException {
		Cuenta cuenta = new Cuenta();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				cuenta = ResultSetMapper.cuentaFromResultSet(rs);
				TipoCuenta tipoCuenta = ResultSetMapper.tipoCuentaFromResultSet(rs);
				cuenta.setTipoCuenta(tipoCuenta);
			}
			
			return cuenta;
		}
	}
	
	@Override
    public int actualizarSaldo(int cuentaId, BigDecimal nuevoSaldo) throws SQLException {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBigDecimal(1, nuevoSaldo);
            ps.setInt(2, cuentaId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar el saldo, no existe la cuenta con id: " + cuentaId);
            }
            return rowsAffected;
        }
    }
	
	@Override
	public Cuenta existbycbu() throws SQLException {
		Cuenta cuenta = new Cuenta();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				cuenta = ResultSetMapper.cuentaFromResultSet(rs);
				TipoCuenta tipoCuenta = ResultSetMapper.tipoCuentaFromResultSet(rs);
				cuenta.setTipoCuenta(tipoCuenta);
			}
			
			return cuenta;
		}
	}
	
	@Override
	public int Transferir(int IdCuentaOrigen, int IdCuentaDestino, Long CBUCuentaOrigen, Long CBUCuentaDestino,
			BigDecimal Importe, String Detalle) throws SQLException {
		
		int rowsAffected = 0;
		
		  try (Connection conn = getConnection(); CallableStatement  statement = (CallableStatement) conn.prepareCall(query)) {
			  
			  	statement.setInt(1, IdCuentaOrigen);
	            statement.setInt(2, IdCuentaDestino);
	            statement.setLong(3, CBUCuentaOrigen);
	            statement.setLong(4, CBUCuentaDestino);
	            statement.setString(5, Detalle);
	            statement.setBigDecimal(6, Importe);

	            // Execute the stored procedure
	            rowsAffected = statement.executeUpdate();

	            // Print the number of rows affected
	            System.out.println("Rows affected: " + rowsAffected);

	            // Close the statement and connection
	            statement.close();
	            conn.close();
	        }
		return rowsAffected;
	}


}
