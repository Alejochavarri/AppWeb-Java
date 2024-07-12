package dao;

import java.sql.SQLException;
import java.util.List;

import entities.Cliente;
import entities.Usuario;
import exceptions.ValidationException;

public interface ClienteDAO extends GenericDAO<Cliente> {

	Cliente getClientByUser() throws SQLException;
	
	Boolean updateClientAndUser(Cliente cliente) throws SQLException;
	
	public List<Cliente> list() throws SQLException;
	
	Boolean createClient(Cliente cliente, Usuario usuario) throws SQLException, ValidationException;
	
	Boolean updateClient(Cliente cliente, int id) throws SQLException;

}
