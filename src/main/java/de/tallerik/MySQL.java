package de.tallerik;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.sun.rowset.CachedRowSetImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("Duplicates")
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

            con = dataSource.getConnection();
            System.out.println("Connection established");
            return true;
        }
        catch(SQLException ex)
        {
            System.out.println(ex);
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
    public CachedRowSetImpl rowSelect(String table, String columns, String filter) {
        if(columns == null || columns.equals("")) {
            columns = "*";
        }
        String sql = "SELECT " + columns + " FROM " + table;
        if(filter != null && !filter.equals("")) {
            sql = sql + " WHERE " + filter;
        }
        sql = sql + ";";

        Statement stmt = null;
        ResultSet res = null;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery(sql);
            CachedRowSetImpl crs = new CachedRowSetImpl();
            crs.populate(res);
            stmt.close();
            return crs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
