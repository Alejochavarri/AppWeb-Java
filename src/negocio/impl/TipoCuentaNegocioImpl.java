package negocio.impl;

import dao.GenericDAO;
import entities.TipoCuenta;
import negocio.TipoCuentaNegocio;

public class TipoCuentaNegocioImpl extends GenericNegocioImpl<TipoCuenta> implements TipoCuentaNegocio{

	public TipoCuentaNegocioImpl(GenericDAO<TipoCuenta> dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

}
