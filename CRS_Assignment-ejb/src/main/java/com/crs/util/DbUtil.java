package com.crs.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    // Optional fallback (only if you want). You can remove fallback if you rely on Payara resources only.
    private static final String FALLBACK_URL  = System.getProperty("crs.db.url", "jdbc:mysql://localhost:3306/crs_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String FALLBACK_USER = System.getProperty("crs.db.user", "root");
    private static final String FALLBACK_PASS = System.getProperty("crs.db.pass", "");

    public static Connection getConnection() throws SQLException {
        // ✅ Payara standard JNDI resource name
        String[] jndiCandidates = {
                "jdbc/crsDB",                 // Payara JDBC Resource
                "java:comp/env/jdbc/crsDB"     // if mapped via web.xml resource-ref
        };

        for (String jndi : jndiCandidates) {
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(jndi);
                if (ds != null) return ds.getConnection();
            } catch (Exception ignored) {
                // try next
            }
        }

        // Fallback
        return DriverManager.getConnection(FALLBACK_URL, FALLBACK_USER, FALLBACK_PASS);
    }
}