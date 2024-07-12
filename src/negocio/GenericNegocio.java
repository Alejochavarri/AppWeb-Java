package negocio;

import java.sql.SQLException;
import java.util.List;

import entities.Prestamo;

public interface GenericNegocio<PERSISTENTE> {

	public boolean create(PERSISTENTE persistente, Object[] properties) throws SQLException;
	public boolean update(PERSISTENTE nuevoPersistente, int idPersistente, Object[] properties) throws SQLException;
	public boolean delete(int idPersistente) throws SQLException;
	public List<PERSISTENTE> list() throws SQLException;
}
