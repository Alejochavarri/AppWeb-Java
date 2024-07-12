package dao;

import java.sql.SQLException;

import entities.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario> {

	public Usuario getByUsername() throws SQLException;
	
	Boolean updateUsuario(Usuario usuario) throws SQLException;
	
	public Usuario findUser() throws SQLException;
	
}
