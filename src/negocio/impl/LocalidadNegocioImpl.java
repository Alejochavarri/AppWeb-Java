package negocio.impl;

import java.sql.SQLException;

import dao.LocalidadDAO;
import dao.impl.LocalidadDAOImpl;
import entities.Localidad;
import negocio.LocalidadNegocio;
import utils.sql.EqualsCriteria;
import utils.sql.SQLSelectBuilder;

public class LocalidadNegocioImpl extends GenericNegocioImpl<Localidad> implements LocalidadNegocio {

	public LocalidadNegocioImpl(LocalidadDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean correspondeProvincia(int provinciaId, int idLocalidad) throws SQLException {
		try {
			SQLSelectBuilder builder = new SQLSelectBuilder();
			String query = builder.select("true")
					.where(new EqualsCriteria("id_provincia", provinciaId, Boolean.FALSE))
					.where(new EqualsCriteria("id", idLocalidad, Boolean.FALSE))
					.from("localidad")
					.build();
			LocalidadDAO dao = new LocalidadDAOImpl(query);
			return dao.correspondeProvincia();
		}catch(SQLException e) {
			throw e;
		}
	}

	
}
