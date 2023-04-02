package de.tallerik.utils;

import java.util.List;


@SuppressWarnings("Duplicates unused")
public class Insert {
    private String table = "";
    private String columns = "";
    private Object[] data = null;
    public Insert(String table, String columns, Object... data) {
        this.table = table;
        this.columns = columns;
        this.data = data;
    }
    public Insert() {}
    public void setColumns(String columns) {
        this.columns = columns;
    }
    public void setData(String... data) {
        this.data = data;
    }
    public void setData(List<Object> data) {
        this.data = data.toArray();
    }
    public void setTable(String table) {
        this.table = table;
    }
    public String getColumns() {
        return columns;
    }
    public String getTable() {
        return table;
    }
    public Object[] getData() {
        return data;
    }
}
