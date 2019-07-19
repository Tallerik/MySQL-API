[![](https://jitpack.io/v/Tallerik/MySQL-API.svg)](https://jitpack.io/#Tallerik/MySQL-API)
# MySQL-API
Simple Java API for MySQL

## Installation
**Notice** Version can be changed. You see the last version in the badge on top of this site  
### Maven
```XML
<repositories>  
 <repository>  
   <id>jitpack.io</id>  
   <url>https://jitpack.io</url>  
 </repository>  
</repositories>  

<dependencies>
   <dependency>
    <groupId>com.github.Tallerik</groupId>
    <artifactId>MySQL-API</artifactId>
    <version>2.0.1</version>
   </dependency>
</dependencies>
```

### Gradle
```Gradle
allprojects {  
 repositories {  
  maven { url 'https://jitpack.io' }  
 }  
}  
 
dependencies {  
   implementation 'com.github.Tallerik:MySQL-API:2.0.1'  
}  
```

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
**Default way**
```Java
boolean: sql.tableInsert("myTable", "name, age", "Robert", "32"); // INSERT Statement
boolean: sql.rowUpdate("myTable", new UpdateValue("age", "45"), "name = 'Robert'"); // UPDATE Statement
Result: sql.rowSelect("myTable", "*", "name = 'Robert'"); // SELECT Statement
boolean: sql.custom("DELETE * FROM myTable;"); // Custom SQL Statement
```

**Request builder**
```java
// sql.tableInsert();
Insert ins = new Insert();
ins.setTable("myTable");
ins.setColumns("column1, column2");
ins.setData("value1", "value2");
boolean: sql.tableInsert(ins);  // Multiple Builders accepted (sql.tableInsert(ins, ins2, ins3)


// sql.rowUpdate();
Update up = new Update();
up.setTable("myTable");
up.setValue(new UpdateValue("column1",  "value3"));
up.setFilter("column2 = 'value2'");
boolean: sql.rowUpdate(up);  // Multiple Builders accepted (sql.rowUpdate(up, up2, up3)


// sql.rowSelect();
Select select = new Select();
select.setTable("myTable");
select.setColumns("*"); // Optional default '*'
select.setFilter(""); // Optional default ''
Result: sql.rowSelect(select); // Only one Builder accepted!
```


#### Result
 
 ```Java
 List<Row> rowList = res.getRows();
 for(Row r : rowList) {
     System.out.println(r.get("column1") + "   " + r.get("column2"));
 }
 ```

### Close Connection
```Java
sql.close();
```

