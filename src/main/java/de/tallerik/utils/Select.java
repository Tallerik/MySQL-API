package de.tallerik.utils;


@SuppressWarnings("Duplicates unused")
public class Select {
    private String table = "";
    private String columns = "";
    private String filter = "";

    public Select() {}
    public Select(String table, String columns, String filter) {
        this.table = table;
        this.columns = columns;
        this.filter = filter;
    }
    public String getFilter() {
        return filter;
    }
    public String getTable() {
        return table;
    }
    public String getColumns() {
        if(columns == null) {
            return "*";
        } else {
            return columns;
        }
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }
    public void setTable(String table) {
        this.table = table;
    }
    public void setColumns(String columns) {
        this.columns = columns;
    }
}
