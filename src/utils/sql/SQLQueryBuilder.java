package utils.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class SQLQueryBuilder {
    protected String table;
    protected List<String> fields;
    
    public SQLQueryBuilder() {
        this.fields = new ArrayList<>();
    }

    public SQLQueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public abstract String build();
}

