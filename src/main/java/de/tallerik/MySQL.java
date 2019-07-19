package de.tallerik;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.tallerik.utils.*;

import javax.sql.DataSource;
import java.sql.*;

@SuppressWarnings("Duplicates unused")
public class MySQL {

    // Vars
    private String host, user, password, db;
    private int port = 3306;
    private Connection con;
    private boolean debug = false;

    // Inits
    public MySQL(){}
    public MySQL(String host) {
        this.host = host;
    }
    public MySQL(String host, String user, String pw, String db) {
        this.host = host;
        this.user = user;
        this.password = pw;
        this.db = db;
    }

    // Setter / Getter
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDb(String db) {
        this.db = db;
    }
    public boolean isDebug() {
        return debug;
    }

    // Connection
    public boolean connect() {
        try
        {
            MysqlDataSource dataSource = new MysqlDataSource();
            if( dataSource instanceof DataSource && debug)
                System.out.println("MysqlDataSource implements the javax.sql.DataSource interface");

            dataSource.setServerName(host);
            dataSource.setPort(port);
            dataSource.setDatabaseName(db);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setAllowMultiQueries(true);
            con = dataSource.getConnection();
            System.out.println("Connection established");
            return true;
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    public boolean isConnected() {
        if (con == null) {
            return false;
        }
        try {
            if (con.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean close() {
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Database Interaction
    public boolean tableInsert(String table, String columns, String... data) {
        String sqldata = "";
        int i = 0;
        for (String d : data) {
            sqldata = sqldata + "'" + d + "'";
            i++;
            if(i != data.length) {
                sqldata = sqldata + ", ";
            }
        }


        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + sqldata + ");";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    public boolean tableInsert(Insert... builders) {
        String sql = "";
        for (Insert b : builders) {
            String sqldata = "";
            int i = 0;
            for (String d : b.getData()) {
                sqldata = sqldata + "'" + d + "'";
                i++;
                if(i != b.getData().length) {
                    sqldata = sqldata + ", ";
                }
            }


            sql = sql + "INSERT INTO " + b.getTable() + " (" + b.getColumns() + ") VALUES (" + sqldata + "); ";

        }
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean rowUpdate(String table, UpdateValue value, String filter) {
        String change = "";
        int i = 0;
        for(String key : value.getKeys()) {
            change = change + key + " = '" + value.get(key) + "'";
            i++;
            if(i != value.getKeys().size()) {
                change = change + ", ";
            }
        }
        String sql = "UPDATE " + table + " SET " + change + " WHERE " + filter + ";";
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    public boolean rowUpdate(Update... builders) {
        String sql = "";
        for (Update u : builders) {
            String change = "";
            int i = 0;
            for(String key : u.getValue().getKeys()) {
                change = change + key + " = '" + u.getValue().get(key) + "'";
                i++;
                if(i != u.getValue().getKeys().size()) {
                    change = change + ", ";
                }
            }
            sql = sql + "UPDATE " + u.getTable() + " SET " + change + " WHERE " + u.getFilter() + "; ";
        }
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public Result rowSelect(String table, String columns, String filter) {
        if(columns == null || columns.equals("")) {
            columns = "*";
        }
        String sql = "SELECT " + columns + " FROM " + table;
        if(filter != null && !filter.equals("")) {
            sql = sql + " WHERE " + filter;
        }
        sql = sql + ";";

        Statement stmt;
        ResultSet res;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery(sql);
            ResultSetMetaData resmeta = res.getMetaData();
            Result result = new Result();
            while(res.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        row.addcolumn(resmeta.getColumnName(i), res.getObject(i));
                    } catch (SQLException e) {
                        bound = false;
                    }

                    i++;
                }
                result.addrow(row);
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return new Result();
        }
    }
    public Result rowSelect(Select s) {
        String sql = "";
        String columns;
        String lsql;
        if(s.getColumns() == null || s.getColumns().equals("")) {
            columns = "*";
        } else {
            columns = s.getColumns();
        }
        lsql = "SELECT " + columns + " FROM " + s.getTable();
        if(s.getFilter() != null && !s.getFilter().equals("")) {
            lsql = lsql + " WHERE " + s.getFilter();
        }
        lsql = lsql + "; ";
        sql = sql + lsql;

        Statement stmt;
        ResultSet res;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery(sql);
            ResultSetMetaData resmeta = res.getMetaData();
            Result result = new Result();
            while(res.next()) {
                Row row = new Row();
                int i = 1;
                boolean bound = true;
                while (bound) {
                    try {
                        row.addcolumn(resmeta.getColumnName(i), res.getObject(i));
                    } catch (SQLException e) {
                        bound = false;
                    }
                    i++;
                }
                result.addrow(row);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result();
        }
    }

    public boolean custom(String sql) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
