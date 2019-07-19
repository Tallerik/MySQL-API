package de.tallerik.utils;


@SuppressWarnings("Duplicates unused")
public class Update {
    private String table = "";
    private UpdateValue value = null;
    private String filter = "";

    public Update() {}
    public Update(String table, UpdateValue value, String filter) {
        this.table = table;
        this.value = value;
        this.filter = filter;
    }
    public void setTable(String table) {
        this.table = table;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }
    public void setValue(UpdateValue value) {
        this.value = value;
    }
    public String getTable() {
        return table;
    }
    public String getFilter() {
        return filter;
    }
    public UpdateValue getValue() {
        return value;
    }
}
