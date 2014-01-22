/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pgcom.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {

    private Cluster cluster;
    private Session session;

    public DBConnect() {
        String[] hosts = new String[]{"127.0.1.1", "127.0.1.2", "127.0.1.3"};
        addContactPoint(hosts);
        connectKeySpace("mykeyspace");

        //session.execute("CREATE TABLE log(id int PRIMARY KEY, log_text text)");

        SimpleStatement toPrepare = prepareStatement("SELECT * FROM users WHERE user_id=1");
        toPrepare.setConsistencyLevel(ConsistencyLevel.ONE);
        ResultSet resultSet = executeQeuery(toPrepare);

        // Print results
        Row result = resultSet.one();
        System.out.println(String.format(
                "Users with ID 1: %s %s",
                result.getString("firstName"),
                result.getString("lastName")));

        // Execute another statement
        resultSet = session.execute("SELECT * FROM users");

        // Print results
        System.out.println("All users:");
        for (Row row : resultSet) {
            System.out.println(String.format("%d %s %s",
                    row.getInt("user_id"),
                    row.getString("firstName"),
                    row.getString("lastName")));
        }

    }

    public DBConnect(String[] hosts) {
        addContactPoint(hosts);
    }

    public void addContactPoint(String[] host) {
        Builder builder = Cluster.builder();
        builder.addContactPoints(host);
        this.cluster = builder.build();
    }

    public void connectKeySpace(String keySpace) {
        try {
            this.session = this.cluster.connect(keySpace);
        } catch (InvalidQueryException iqex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.WARNING, "{0} : KeySpace not found!", keySpace);
        }
    }

    public SimpleStatement prepareStatement(String query) {
        SimpleStatement toPrepare = new SimpleStatement(query);
        return toPrepare;
    }

    public SimpleStatement setConsistencyLevel(SimpleStatement toPrepare, ConsistencyLevel level) {
        toPrepare.setConsistencyLevel(level);
        return toPrepare;
    }

    public ResultSet executeQeuery(SimpleStatement toPrepare) {
        PreparedStatement prepared = this.getSession().prepare(toPrepare);
        ResultSet resultSet = this.getSession().execute(prepared.bind());
        return resultSet;
    }

    public ResultSet executeQeuery(String query) {
        ResultSet resultSet = this.getSession().execute(query);
        return resultSet;
    }

    public static void main(String[] args) {
        DBConnect db = new DBConnect();
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }
}
