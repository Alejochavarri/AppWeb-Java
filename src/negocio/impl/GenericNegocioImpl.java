package negocio.impl;

import java.sql.SQLException;
import java.util.List;

import dao.GenericDAO;
import entities.Prestamo;
import negocio.GenericNegocio;

public class GenericNegocioImpl<PERSISTENTE> implements GenericNegocio<PERSISTENTE> {

    protected final GenericDAO<PERSISTENTE> genericDAO;
    
    public GenericNegocioImpl(GenericDAO<PERSISTENTE> dao) {
    	this.genericDAO = dao;
	}

	@Override
	public boolean create(PERSISTENTE persistente, Object[] properties) throws SQLException {
		return this.genericDAO.create(persistente, properties);
	}

	@Override
	public boolean update(PERSISTENTE nuevoPersistente, int idPersistente, Object[] properties) throws SQLException {
		return genericDAO.update(nuevoPersistente, idPersistente, properties);
	}

	@Override
	public boolean delete(int idPersistente) throws SQLException {
		// TODO Auto-generated method stub
		return genericDAO.delete(idPersistente);
	}

	@Override
	public List<PERSISTENTE> list() throws SQLException {
		return genericDAO.list();
	}

	
}
