package com.crs.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private static final String FALLBACK_URL  = System.getProperty(
            "crs.db.url",
            "jdbc:mysql://localhost:3306/crs_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    );
    private static final String FALLBACK_USER = System.getProperty("crs.db.user", "root");
    private static final String FALLBACK_PASS = System.getProperty("crs.db.pass", "");

    public static Connection getConnection() throws SQLException {
        String[] jndiCandidates = {
                "jdbc/crsDB",
                "java:comp/env/jdbc/crsDB"
        };

        for (String jndi : jndiCandidates) {
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(jndi);
                if (ds != null) {
                    Connection con = ds.getConnection();
                    System.out.println("[DbUtil] Using JNDI datasource: " + jndi);
                    System.out.println("[DbUtil] URL = " + con.getMetaData().getURL());
                    System.out.println("[DbUtil] USER = " + con.getMetaData().getUserName());
                    return con;
                }
            } catch (Exception ignored) {
            }
        }

        Connection con = DriverManager.getConnection(FALLBACK_URL, FALLBACK_USER, FALLBACK_PASS);
        System.out.println("[DbUtil] Using FALLBACK JDBC");
        System.out.println("[DbUtil] URL = " + con.getMetaData().getURL());
        System.out.println("[DbUtil] USER = " + con.getMetaData().getUserName());
        return con;
    }
}