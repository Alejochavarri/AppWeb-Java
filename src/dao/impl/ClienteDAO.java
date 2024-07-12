package dao.impl;

import dao.GenericDAO;
import entities.Cliente;

public class ClienteDAO extends GenericDAOImpl<Cliente> implements GenericDAO<Cliente> {

	public ClienteDAO(String query) {
		super(query);
	}

}
