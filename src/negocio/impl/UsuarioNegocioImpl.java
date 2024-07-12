package negocio.impl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import dao.GenericDAO;
import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import entities.Usuario;
import negocio.UsuarioNegocio;
import utils.sql.EqualsCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLQueryBuilder;
import utils.sql.SQLSelectBuilder;
import utils.sql.SQLUpdateBuilder;

public class UsuarioNegocioImpl extends GenericNegocioImpl<Usuario> implements UsuarioNegocio {

	public UsuarioNegocioImpl(GenericDAO<Usuario> dao) {
		super(dao);
	}
	
	@Override
	public Usuario findUsuario(HashMap<String, String> filters) throws SQLException {
		Usuario usuario = null;
        try {
        	SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
        	
	    	String query = selectBuilder.select("usuario.id", "usuario.usuario", "usuario.contrasena", "tipo_usuario.id" ,"tipo_usuario.nombre", "tipo_usuario.deleted", "usuario.deleted")
	    			.where(Arrays.asList(new EqualsCriteria("usuario.usuario", filters.get("usuario"), Boolean.TRUE), new EqualsCriteria("contrasena", filters.get("contrasena"), Boolean.TRUE)))
	    			.innerJoin("tipo_usuario on tipo_usuario.id = usuario.id_tipo_usuario")
	    			.from("usuario")
	    			.build();
	    	System.out.println("Query: " + query);
	    	UsuarioDAOImpl userDao = new UsuarioDAOImpl(query);
	    	usuario = userDao.findUser();
		} catch (SQLException e) {
			throw e;
		} 
        return usuario;
	}
	
	@Override
	public Usuario getByUsername(String username) throws SQLException {
		try {
			SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
			String query = selectBuilder.select("usuario.id","usuario.usuario", "usuario.contrasena", "usuario.deleted", "tipo_usuario.id", "tipo_usuario.nombre", "tipo_usuario.deleted")
					.where(new EqualsCriteria("usuario.usuario", username, Boolean.TRUE))
					.innerJoin("tipo_usuario on tipo_usuario.id = usuario.id_tipo_usuario")
					.from("usuario")
					.build();

			UsuarioDAO usuarioDAO = new UsuarioDAOImpl(query);
			
			return usuarioDAO.getByUsername();
		
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public Boolean updateUsuario(Usuario usuario) throws SQLException {
		try {
			SQLUpdateBuilder updateBuilder = new SQLUpdateBuilder();
			HashMap<String, Object> updates = new HashMap<>();
			updates.put("contrasena", usuario.getContrasena());
			updates.put("id_tipo_usuario", usuario.getTipoUsuario().getId());
			String query = updateBuilder.set(updates).where(new EqualsCriteria("id", usuario.getId(), Boolean.FALSE)).from("usuario").build();
			UsuarioDAO usuarioDAO = new UsuarioDAOImpl(query);
			return usuarioDAO.updateUsuario(usuario);
		}catch(SQLException e) {
			throw e;
		}
	}
	
	

}
