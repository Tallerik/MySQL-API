# MySQL-API
Simple Java API for MySQL

## Usage

### Init
```Java
MySQL sql = new MySQL();
```

### Set Credentials
```Java
sql.setHost("localhost");
sql.setUser("sqluser");
sql.setPassword("sqlpassword");
sql.setDb("database");

sql.setPort(3306); // Optional. Default: 3306
sql.setDebug(false); // Optional. Default: false
```


### Connect to MySQL
```Java
boolean: sql.connect();
```

### Checks
```Java
boolean: isConnected();
boolean: isDebug();
```

### MySQL Data interaction
```Java
boolean: sql.tableInsert("myTable", "name, age", "Robert", "32"); // INSERT Statement
boolean: sql.rowUpdate("myTable", new UpdateValue("age", "45"), "name = 'Robert'"); // UPDATE Statement
CachedRowSetImpl: sql.rowSelect("myTable", "*", "name = 'Robert'"); // SELECT Statement
boolean: sql.custom("DELETE * FROM myTable;"); // Custom SQL Statement
```
 **Tip**
 `CachedRowSetImpl` is like ResultSet
 ```Java
 CachedRowSetImpl res = sql.rowSelect(...);
 while(res.next()) {
    System.out.PrintLn(res.getString("myValue"));
 }
 
 ```

### Close Connection
```Java
sql.close();
```
