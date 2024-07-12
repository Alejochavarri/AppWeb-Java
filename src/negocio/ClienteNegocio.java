package negocio;

import java.sql.SQLException;
import java.util.HashMap;

import entities.Cliente;
import entities.Usuario;
import exceptions.ValidationException;

public interface ClienteNegocio extends GenericNegocio<Cliente> {

	public boolean createClient(Usuario usuario, Cliente cliente) throws SQLException, ValidationException;

	Cliente getClientByUser(int userId) throws SQLException;
		
	public Cliente findCliente(int dni) throws SQLException;

	Boolean updateClient(Cliente cliente, int id) throws SQLException;
}
