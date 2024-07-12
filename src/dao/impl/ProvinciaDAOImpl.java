package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.ProvinciaDAO;
import entities.Pais;
import entities.Provincia;
import utils.ResultSetMapper;

public class ProvinciaDAOImpl extends GenericDAOImpl<Provincia> implements ProvinciaDAO {

	
	public ProvinciaDAOImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProvinciaDAOImpl(String query) {
		super(query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Provincia> list() throws SQLException {
		List<Provincia> provincias = new ArrayList<Provincia>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				Pais pais = ResultSetMapper.paisFromResultSet(rs);
				Provincia provincia = ResultSetMapper.provinciaFromResultSet(rs);
				provincia.setPais(pais);
				provincias.add(provincia);
			}
			return provincias;
		}catch(SQLException e) {
			throw e;
		}
		
	}

	@Override
	public Boolean correspondePais() throws SQLException {
		try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
			return rs.next();
		}catch(SQLException e) {
			throw e;
		}
	}

}
