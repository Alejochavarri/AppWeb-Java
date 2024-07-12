package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.PaisDAO;
import entities.Pais;
import utils.ResultSetMapper;

public class PaisDAOImpl extends GenericDAOImpl<Pais> implements PaisDAO {

	
	public PaisDAOImpl() {
		super();
	}

	public PaisDAOImpl(String query) {
		super(query);
	}

	@Override
	public List<Pais> list() throws SQLException {
		List<Pais> paises = new ArrayList<Pais>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				Pais pais = ResultSetMapper.paisFromResultSet(rs);
				paises.add(pais);
			}
			return paises;
		}catch(SQLException e) {
			throw e;
		}
		
	}

}
