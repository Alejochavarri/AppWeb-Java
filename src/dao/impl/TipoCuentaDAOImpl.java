package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.TipoCuentaDAO;
import entities.TipoCuenta;
import utils.ResultSetMapper;

public class TipoCuentaDAOImpl extends GenericDAOImpl<TipoCuenta> implements TipoCuentaDAO{
	
	public TipoCuentaDAOImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoCuentaDAOImpl(String query) {
		super(query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TipoCuenta> list() throws SQLException {
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(query)){
			List<TipoCuenta> tipos = new ArrayList<TipoCuenta>();
			while(rs.next()) {
				tipos.add(ResultSetMapper.tipoCuentaFromResultSet(rs));
			}
			return tipos;
		}
		catch (SQLException e) {
			throw e;
		}
	}
	
}
