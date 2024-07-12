package negocio.impl;

import dao.PaisDAO;
import entities.Pais;
import negocio.PaisNegocio;

public class PaisNegocioImpl extends GenericNegocioImpl<Pais> implements PaisNegocio {

	public PaisNegocioImpl(PaisDAO dao) {
		super(dao);
	}

}
