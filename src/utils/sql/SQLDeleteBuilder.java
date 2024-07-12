package utils.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import utils.SearchCriteria;

public class SQLDeleteBuilder extends SQLQueryBuilder {
    private List<SearchCriteria> criteria;

    public SQLDeleteBuilder() {
        super();
        this.criteria = new ArrayList<>();
    }

    public SQLDeleteBuilder deleteFrom(String table) {
        this.table = table;
        return this;
    }

    public SQLDeleteBuilder where(SearchCriteria criterion) {
        this.criteria.add(criterion);
        return this;
    }

    @Override
    public String build() {
        StringJoiner query = new StringJoiner(" ");
        query.add("UPDATE");
        query.add(table);
        query.add("SET deleted = true");

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
}

