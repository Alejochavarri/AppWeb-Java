package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.LinksDAO;
import entities.Link;
import utils.ResultSetMapper;

public class LinksDAOImpl extends GenericDAOImpl<Link> implements LinksDAO {
	
	public LinksDAOImpl(String query) {
		super(query);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Link> list() throws SQLException {
		List<Link> links = new ArrayList<>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				links.add(ResultSetMapper.linkFromResultSet(rs));
			}
			return links;
		}catch (SQLException e) {
			throw e;
		}
	}

}
