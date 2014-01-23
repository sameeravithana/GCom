/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pgcom.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {

    private Cluster cluster;
    private Session session;

    public DBConnect() {
        String[] hosts = new String[]{"127.0.1.1", "127.0.1.2", "127.0.1.3"};
        addContactPoint(hosts);
        connectKeySpace("mykeyspace");
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
            Logger.getLogger(DBConnect.class.getName()).log(Level.WARNING, "{0} : KeySpace not found! Default pgcomkeyspace added", keySpace);
//            String cqlStatement = "CREATE KEYSPACE pgcomkeyspace WITH replication = {'class':'SimpleStrategy','replication_factor':3}";
//            session.execute(cqlStatement);
        }
    }

    public void addRMIEntry(String name, byte[] vals) {
        PreparedStatement statement = session.prepare("insert into rmiserver(group_name,mem_stub) values (?, ?)");
        BoundStatement boundStatement = new BoundStatement(statement);
        session.execute(boundStatement.bind(name, ByteBuffer.wrap(vals)));
    }

    public byte[] getRMIServer(String key) {
        String q1 = "SELECT * FROM rmiserver WHERE group_name = '" + key + "';";

        ResultSet results = session.execute(q1);
        for (Row row : results) {
            ByteBuffer data = row.getBytes("mem_stub");
            byte[] result = new byte[data.remaining()];
            data.get(result);
            return result;
        }
        return null;
    }

    public HashMap<String, byte[]> getRMIEntries() {
        String q1 = "SELECT * FROM rmiserver;";
        HashMap<String, byte[]> res = new HashMap<String, byte[]>();
        ResultSet results = session.execute(q1);
        for (Row row : results) {
            ByteBuffer data = row.getBytes("mem_stub");
            byte[] result = new byte[data.remaining()];
            data.get(result);
            res.put(row.getString("group_name"), result);
        }
        return res;
    }

    public SimpleStatement prepareStatement(String query, Object... params) {
        return new SimpleStatement(query, params);
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
