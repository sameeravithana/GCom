/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom;

import gcom.interfaces.IGroupManagement;
import gcom.interfaces.IMember;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;
import pgcom.cassandra.DBConnect;

/**
 *
 * @author samtube405
 */
public class RMIServer implements Serializable {

    Registry registry;
    String host;
    int port;
    DBConnect cdb;

    public RMIServer(int port, DBConnect cdb) {
        this.port = port;
        this.cdb = cdb;
        initiateDB();
    }

    public RMIServer(String host, int port, DBConnect cdb) throws RemoteException {
        this.host = host;
        this.port = port;
        this.cdb = cdb;
        initiateDB();
    }

    public void createRegistry() throws RemoteException {
        registry = LocateRegistry.createRegistry(this.port);
        loadRMIEntries();
    }

    public Registry getRegistry() throws RemoteException {
        return registry = LocateRegistry.getRegistry(this.host, this.port);
    }

    public void loadRMIEntries() throws RemoteException {

        HashMap<String, byte[]> rmiEntries = cdb.getRMIEntries();

        for (String key : rmiEntries.keySet()) {
            try {

                IGroupManagement obj = (IGroupManagement) SerializationUtils.deserialize(rmiEntries.get(key));

                rebindRO(key, obj);
            } catch (IOException ex) {
                Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initiateDB() {
        String query = "CREATE TABLE IF NOT EXISTS rmiserver (\n"
                + "group_name text PRIMARY KEY,\n"
                + "mem_stub text\n"
                + ")";

        cdb.getSession().execute(query);
    }

    public void stop() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this.registry, true);
    }

    public void bind(String name, RemoteObject ro) throws AccessException, RemoteException, AlreadyBoundException {
        Remote exportObject = UnicastRemoteObject.exportObject(ro, 0);
        registry.bind(name, exportObject);
        try {
            //byte[] serialize = SerializeObjects.serialize(exportObject);
            byte[] serialize = SerializationUtils.serialize(ro);

            cdb.addRMIEntry(name, serialize);

        } catch (Exception ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void rebind(String name, IGroupManagement ro) throws AccessException, RemoteException {
        registry.rebind(name, ro);
        try {
            byte[] serialize = SerializationUtils.serialize(ro);

            cdb.addRMIEntry(name, serialize);

        } catch (Exception ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void rebindRO(String name, Remote ro) throws AccessException, RemoteException {
        registry.rebind(name, ro);
    }

    public void rebind(String name, IMember ro) throws AccessException, RemoteException {

        registry.rebind(name, ro);
        try {
            byte[] serialize = SerializationUtils.serialize(ro);

            cdb.addRMIEntry(name, serialize);

        } catch (Exception ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void unbind(String name) throws AccessException, RemoteException, NotBoundException {
        registry.unbind(name);
    }

    public IGroupManagement regLookUp(String name) throws AccessException, RemoteException, NotBoundException {
        return (IGroupManagement) registry.lookup(name);
    }

    public IMember regMemLookUp(String name) throws AccessException, RemoteException, NotBoundException {
        return (IMember) registry.lookup(name);
    }

    public String[] list() throws AccessException, RemoteException {
        return registry.list();
    }

    public RemoteObject getReference(String name) throws AccessException, RemoteException, NotBoundException {
        return (RemoteObject) registry.lookup(name);
    }
}
