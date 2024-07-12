package negocio.impl;

import dao.TipoUsuarioDAO;
import entities.TipoUsuario;
import negocio.TipoUsuarioNegocio;

public class TipoUsuarioNegocioImpl extends GenericNegocioImpl<TipoUsuario> implements TipoUsuarioNegocio {

	public TipoUsuarioNegocioImpl(TipoUsuarioDAO dao) {
		super(dao);
	}

	

}
