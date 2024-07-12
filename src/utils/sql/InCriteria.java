package utils.sql;

import java.util.List;
import java.util.stream.Collectors;

import utils.SearchCriteria;

public class InCriteria implements SearchCriteria {

    private List<Object> criteria;
    private String field;

    public InCriteria(List<Object> criteria, String field) {
        this.criteria = criteria;
        this.field = field;
    }


	@Override
    public String toSql() {
        String criteriaString = criteria.stream()
                .map(value -> {
                    if (value instanceof String) {
                        return "'" + value + "'";
                    } else {
                        return String.valueOf(value);
                    }
                })
                .collect(Collectors.joining(", "));
        
        return field + " IN (" + criteriaString + ")";
    }
}

