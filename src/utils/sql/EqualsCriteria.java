package utils.sql;

import utils.SearchCriteria;

public class EqualsCriteria implements SearchCriteria {
	private String field;
    private Object value;
    private Boolean isStringOrLocalDate;

    public EqualsCriteria(String field, Object value, Boolean isStringOrLocalDate) {
        this.field = field;
        this.value = value;
        this.isStringOrLocalDate = isStringOrLocalDate;
    }

    @Override
    public String toSql() {
    	if(this.isStringOrLocalDate) {
    		return field + " = '" + value + "'";
    		
    	}else {
    		return field + " = " + value;
    	}
    }

}
