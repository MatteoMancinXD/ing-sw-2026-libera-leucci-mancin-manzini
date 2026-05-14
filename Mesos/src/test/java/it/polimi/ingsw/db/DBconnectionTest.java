package it.polimi.ingsw.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBconnectionTest {

    public static void main(String[] args) {
        Properties props = new Properties();

        try (InputStream input = DBconnectionTest.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (input == null) {
                System.out.println("db.properties file not found");
                System.out.println("Be sure that db.properties file is in resources directory");
                return;
            }

            props.load(input);
            System.out.println("MySQL connection....");

            Connection conn = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );

            if (conn != null) {
                System.out.println("Success! Database 'mesos' ready to be used");
                conn.close();
            }

        } catch (Exception e) {
            System.out.println("Connection error");
            e.printStackTrace();
        }
    }
}
