package de.tallerik.utils;

import java.util.HashMap;
import java.util.Set;


@SuppressWarnings("Duplicates unused")
public class Row {
    private HashMap<String, Object> content = new HashMap<>();
    public Row(){}
    public void addcolumn(String name, Object content) {
        this.content.put(name, content);
    }

    public HashMap<String, Object> getColumns() {
        return content;
    }
    public Object get(String key) {
        return content.get(key);
    }
    public Set<String> getKeys() {
        return content.keySet();
    }
}
