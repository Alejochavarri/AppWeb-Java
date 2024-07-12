package negocio.impl;

import java.sql.SQLException;

import dao.ProvinciaDAO;
import dao.impl.ProvinciaDAOImpl;
import entities.Provincia;
import negocio.ProvinciaNegocio;
import utils.sql.EqualsCriteria;
import utils.sql.SQLSelectBuilder;

public class ProvinciaNegocioImpl extends GenericNegocioImpl<Provincia> implements ProvinciaNegocio {

	public ProvinciaNegocioImpl(ProvinciaDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean correspondePais(int idPais, int idProvincia) throws SQLException {
		try {
			SQLSelectBuilder builder = new SQLSelectBuilder();
			String query = builder.select("1")
					.where(new EqualsCriteria("id_pais", idPais, Boolean.FALSE))
					.where(new EqualsCriteria("id", idProvincia, Boolean.FALSE))
					.from("provincia")
					.build();
			ProvinciaDAO dao = new ProvinciaDAOImpl(query);
			
			Boolean result = dao.correspondePais();
			return result;
		}catch(SQLException e) {
			throw e;
		}
		
	}


}
