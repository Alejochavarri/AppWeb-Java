package negocio.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import dao.ClienteDAO;
import dao.GenericDAO;
import dao.impl.ClienteDAOImpl;
import dao.impl.UsuarioDAOImpl;
import entities.Cliente;
import entities.Usuario;
import exceptions.ValidationException;
import negocio.ClienteNegocio;
import negocio.UsuarioNegocio;
import utils.SQLQueryCreator;
import utils.sql.EqualsCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;
import utils.sql.SQLUpdateBuilder;

public class ClienteNegocioImpl extends GenericNegocioImpl<Cliente> implements ClienteNegocio {

	public ClienteNegocioImpl(GenericDAO<Cliente> dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean createClient(Usuario usuario, Cliente cliente) throws SQLException,ValidationException {
		try {
			ClienteDAO dao = new ClienteDAOImpl();
			return dao.createClient(cliente, usuario);
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public Cliente getClientByUser(int userId) throws SQLException {
		try {
			String query = SQLQueryCreator.createClientSQLQuery(Arrays.asList(new EqualsCriteria("id_usuario", userId, Boolean.FALSE)));
			ClienteDAO clienteDAO = new ClienteDAOImpl(query);
			return clienteDAO.getClientByUser();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public Cliente findCliente(int dni) throws SQLException {
		try {
			String query = SQLQueryCreator.createClientSQLQuery(Arrays.asList(new EqualsCriteria("dni", dni, Boolean.FALSE)));
			ClienteDAO clienteDAO = new ClienteDAOImpl(query);
			return clienteDAO.getClientByUser();
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public Boolean updateClient(Cliente cliente, int id) throws SQLException {
		try {
			ClienteDAO dao = new ClienteDAOImpl();
			return dao.updateClient(cliente, id);
		} catch (SQLException e) {
			throw e;
		}
	}

}
