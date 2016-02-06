package com.milcomsolutions.wastemanagement.vo.api;

public enum DatabaseType {
    MYSQL("MySql", "jdbc:mysql://", "com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect"), ORACLE("Oracle", "jdbc:oracle:thin:@",
            "oracle.jdbc.driver.OracleDriver", "org.hibernate.dialect.Oracle10gDialect"), POSTGRES("Postgres", "jdbc:postgresql://", "org.postgresql.Driver",
            "org.hibernate.dialect.ProgressDialect"), MSSQL("Microsoft SQL Server", "jdbc:sqlserver://", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "org.hibernate.dialect.SQLServerDialect"), HSQL("Hypersonic QL", "jdbc:hsqldb:hsql://", "org.hsqldb.jdbcDriver",
            "org.hibernate.dialect.HSQLDialect"), H2("h2", "jdbc:h2:", "org.h2.Driver", "org.hibernate.dialect.H2Dialect"), SYBASE("Sybase",
            "jdbc:jtds:sybase://", "net.sourceforge.jtds.jdbc.Driver", "org.hibernate.dialect.SybaseDialect");

    private String description;

    private String urlFormat;

    private String className;

    private String jpaDialect;


    DatabaseType(String description, String urlPrefix, String className, String jpaDialect) {
        this.description = description;
        this.urlFormat = urlPrefix;
        this.className = className;
        this.jpaDialect = jpaDialect;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getUrlFormat() {
        return urlFormat;
    }


    public void setUrlFormat(String urlFormat) {
        this.urlFormat = urlFormat;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public String getJpaDialect() {
        return jpaDialect;
    }


    public void setJpaDialect(String jpaDialect) {
        this.jpaDialect = jpaDialect;
    }
}
