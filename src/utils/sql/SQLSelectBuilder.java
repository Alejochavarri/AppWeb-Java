package utils.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import utils.SearchCriteria;

public class SQLSelectBuilder extends SQLQueryBuilder {
    private List<String> joins;
    private List<SearchCriteria> criteria;

    public SQLSelectBuilder() {
        super();
        this.joins = new ArrayList<>();
        this.criteria = new ArrayList<>();
    }

    public SQLSelectBuilder select(String... fields) {
        this.fields.addAll(Arrays.asList(fields));
        return this;
    }

    public SQLSelectBuilder innerJoin(String joinClause) {
        this.joins.add("INNER JOIN " + joinClause);
        return this;
    }

    public SQLSelectBuilder where(SearchCriteria criterion) {
    	if(Objects.nonNull(criterion)) {
    		this.criteria.add(criterion);
    	}
        return this;
    }
    
    public SQLSelectBuilder where(List<SearchCriteria> criterias) {
    	if(criterias != null && !criterias.isEmpty()) {
    		this.criteria.addAll(criterias);
    	}
		return this;
    }

    @Override
    public String build() {
        StringJoiner query = new StringJoiner(" ");
        query.add("SELECT");

        if (fields.isEmpty()) {
            query.add("*");
        } else {
            query.add(String.join(", ", fields));
        }

        query.add("FROM");
        query.add(table);

        for (String join : joins) {
            query.add(join);
        }

        if (!criteria.isEmpty()) {
            query.add("WHERE");
            List<String> whereClauses = new ArrayList<>();
            for (SearchCriteria criterion : criteria) {
                whereClauses.add(criterion.toSql());
            }
            query.add(String.join(" AND ", whereClauses));
        }

        return query.toString();
    }

    public SQLSelectBuilder leftJoin(String joinClause) {
        this.joins.add("LEFT JOIN " + joinClause);
        return this;
    }
}

