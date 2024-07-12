package utils.sql;

import utils.SearchCriteria;

public class LikeCriteria implements SearchCriteria {

	private String field;
    private Object value;
    
	public LikeCriteria(String field, Object value) {
		super();
		this.field = field;
		this.value = value;
	}

	@Override
	public String toSql() {
		return field + " LIKE '%" + value + "%'";
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
