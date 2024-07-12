package negocio;

import java.sql.SQLException;
import java.util.HashMap;

import dao.UsuarioDAO;
import entities.Usuario;

public interface UsuarioNegocio extends GenericNegocio<Usuario> {

	public Usuario getByUsername(String username) throws SQLException;
	
	Boolean updateUsuario(Usuario usuario) throws SQLException;
	
	public Usuario findUsuario(HashMap<String, String> filters) throws SQLException;

}
