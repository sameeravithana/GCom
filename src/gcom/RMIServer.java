/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gcom;

import gcom.interfaces.IGroupManagement;
import gcom.modules.group.Member;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samtube405
 */
public class RMIServer {

    Registry registry;
    String host;
    int port;
    boolean serverFlag = false;

    public RMIServer(int port) {
        this.port = port;
        this.serverFlag = true;
    }

    public RMIServer(String host, int port) throws RemoteException {
        this.host=host;
        this.port=port;
        this.serverFlag=false;
    }  
    
    public Registry start()  throws RemoteException{
        if(this.serverFlag)  registry=LocateRegistry.createRegistry(this.port);
        else{
            registry=LocateRegistry.getRegistry(this.host, this.port);

        }
        return registry;
    }
    
    public void stop() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this.registry, true);
    }

    public void bind(String name, RemoteObject ro) throws AccessException, RemoteException, AlreadyBoundException {
        registry.bind(name, UnicastRemoteObject.exportObject(ro, 0));
    }

    public void rebind(String name, RemoteObject ro) throws AccessException, RemoteException {
        registry.rebind(name, UnicastRemoteObject.exportObject(ro, 0));
    }
    
     public void rebind(String name, IGroupManagement ro) throws AccessException, RemoteException {
        registry.rebind(name, UnicastRemoteObject.exportObject(ro, 0));
    }

    public void unbind(String name, RemoteObject ro) throws AccessException, RemoteException, NotBoundException {
        registry.unbind(name);
    }

    public IGroupManagement regLookUp(String name) throws AccessException, RemoteException, NotBoundException {
        return (IGroupManagement) registry.lookup(name);
    }

    public String[] list() throws AccessException, RemoteException {
        return registry.list();
    }

    public RemoteObject getReference(String name) throws AccessException, RemoteException, NotBoundException {
        return (RemoteObject) registry.lookup(name);
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.getRegistry("dthdfgh", 0);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}