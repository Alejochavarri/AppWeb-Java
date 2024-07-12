package negocio.impl;

import dao.LinksDAO;
import entities.Link;
import negocio.LinksNegocio;

public class LinksNegocioImpl extends GenericNegocioImpl<Link> implements LinksNegocio {

	public LinksNegocioImpl(LinksDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

}
