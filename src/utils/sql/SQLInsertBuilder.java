package utils.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SQLInsertBuilder extends SQLQueryBuilder {
    private List<String> columns;
    private List<String> placeholders;

    public SQLInsertBuilder() {
        super();
        this.columns = new ArrayList<>();
        this.placeholders = new ArrayList<>();
    }

    public SQLInsertBuilder into(String table) {
        this.table = table;
        return this;
    }

    public SQLInsertBuilder values(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            columns.add(entry.getKey());
            placeholders.add("?");
        }
        return this;
    }

    @Override
    public String build() {
        StringJoiner query = new StringJoiner(" ");
        query.add("INSERT INTO");
        query.add(table);

        query.add("(" + String.join(", ", columns) + ")");
        query.add("VALUES");
        query.add("(" + String.join(", ", placeholders) + ")");

        return query.toString();
    }
}


