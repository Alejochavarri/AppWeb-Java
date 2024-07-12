package utils.sql;

import utils.SearchCriteria;

public class BetweenCriteria implements SearchCriteria {
	private String field;
	private Object startValue;
	private Object endValue;
	private Boolean isStringOrDate;

	public BetweenCriteria(String field, Object startValue, Object endValue, Boolean isStringOrDate) {
		this.field = field;
		this.startValue = startValue;
		this.endValue = endValue;
		this.isStringOrDate = isStringOrDate;
	}

	@Override
    public String toSql() {
		if(startValue != null && endValue != null) {
			if(this.isStringOrDate) {
				return field + " BETWEEN '" + startValue + "' AND '" + endValue + "'";
			}else {
				return field + " BETWEEN " + startValue + " AND " + endValue;
				
			}
		}
		return "";
    }

}
