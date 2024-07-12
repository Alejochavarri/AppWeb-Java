package utils.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import utils.SearchCriteria;

public class SQLUpdateBuilder extends SQLQueryBuilder {
    private List<String> updates;
    private List<SearchCriteria> criteria;

    public SQLUpdateBuilder() {
        super();
        this.updates = new ArrayList<>();
        this.criteria = new ArrayList<>();
    }

    public SQLUpdateBuilder update(String table) {
        this.table = table;
        return this;
    }

    public SQLUpdateBuilder set(Map<String, Object> updates) {
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            this.updates.add(entry.getKey() + " = ?");
        }
        return this;
    }

    public SQLUpdateBuilder where(SearchCriteria criterion) {
        this.criteria.add(criterion);
        return this;
    }

    @Override
    public String build() {
        StringJoiner query = new StringJoiner(" ");
        query.add("UPDATE");
        query.add(table);
        query.add("SET");
        query.add(String.join(", ", updates));

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


