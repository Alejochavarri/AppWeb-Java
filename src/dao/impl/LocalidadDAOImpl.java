package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.LocalidadDAO;
import entities.Localidad;
import entities.Pais;
import entities.Provincia;
import utils.ResultSetMapper;

public class LocalidadDAOImpl extends GenericDAOImpl<Localidad> implements LocalidadDAO {

	
	
	public LocalidadDAOImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocalidadDAOImpl(String query) {
		super(query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Localidad> list() throws SQLException {
		List<Localidad> localidades = new ArrayList<Localidad>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
			while(rs.next()) {
				Localidad localidad = ResultSetMapper.localidadFromResultSet(rs);
				Provincia provincia = ResultSetMapper.provinciaFromResultSet(rs);
				Pais pais = ResultSetMapper.paisFromResultSet(rs);
				provincia.setPais(pais);
				localidad.setProvincia(provincia);
				localidades.add(localidad);
			}
			return localidades;
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public Boolean correspondeProvincia() throws SQLException {
		try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
			return rs.next();
		}catch(SQLException e) {
			throw e;
		}
	}

}
