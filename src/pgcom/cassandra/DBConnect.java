/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pgcom.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.Cluster.Builder;

public class DBConnect {

    private Cluster cluster;
    private Session session;

    private DBConnect() {
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

    private void addContactPoint(String[] host) {
        Builder builder = Cluster.builder();
        builder.addContactPoints(host);
        this.cluster = builder.build();
    }

    private void connectKeySpace(String keySpace) {
        this.session = this.cluster.connect(keySpace);
    }

    private SimpleStatement prepareStatement(String query) {
        SimpleStatement toPrepare = new SimpleStatement(query);
        return toPrepare;
    }

    private SimpleStatement setConsistencyLevel(SimpleStatement toPrepare, ConsistencyLevel level) {
        toPrepare.setConsistencyLevel(level);
        return toPrepare;
    }

    private ResultSet executeQeuery(SimpleStatement toPrepare) {
        PreparedStatement prepared = this.session.prepare(toPrepare);
        ResultSet resultSet = this.session.execute(prepared.bind());
        return resultSet;
    }

    private ResultSet executeQeuery(String query) {
        ResultSet resultSet = this.session.execute(query);
        return resultSet;
    }

    public static void main(String[] args) {
        DBConnect db = new DBConnect();
    }
}
