package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.TipoUsuarioDAO;
import entities.TipoUsuario;
import utils.ResultSetMapper;

public class TipoUsuarioDAOImpl extends GenericDAOImpl<TipoUsuario> implements TipoUsuarioDAO {

	
	public TipoUsuarioDAOImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoUsuarioDAOImpl(String query) {
		super(query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<TipoUsuario> list() throws SQLException {
		try (Connection conn = getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(query)) {
			List<TipoUsuario> tipos = new ArrayList<TipoUsuario>();
			while(rs.next()) {
				tipos.add(ResultSetMapper.tipoUsuarioFromResultSet(rs));
			}
			return tipos;
		} catch (SQLException e) {
			throw e;
		}
	}

}
