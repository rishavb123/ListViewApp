package io.bhagat.audioproject;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


public class Client extends Thread{

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Socket connection;

    private Object readObject;

    private boolean stop;

    private String host;
    private int port;

    private Function<Client, Object> callback;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
        stop = false;
        try {
            connection = new Socket(host, port);
            setupStreams();
            System.out.println("Connected to " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Could not connect to Server!");
        }

        callback = new Function<Client, Object> () {

            @Override
            public Object f(Client x) {
                System.out.println(x.getHost() + ":" + x.getPort() + " - " + x.read());
                return null;
            }

        };
    }

    public void run()
    {
        try {
            while(!stop)
            {
                readObject = input.readObject();
                send(callback.f(this));
            }
        } catch(EOFException | SocketException e) {
            System.out.println("terminated connnection");
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Object read()
    {
        return readObject;
    }

    private void setupStreams() throws IOException
    {
        input = new ObjectInputStream(connection.getInputStream());
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
    }

    public void close()
    {
        try {
            stop = true;
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(Object obj)
    {
        try {
            if(obj != null)
                output.writeObject(obj);
            output.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the callback
     */
    public Function<Client, Object> getCallback() {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback(Function<Client, Object> callback) {
        this.callback = callback;
    }

    abstract class Function<E, T> {

        /**
         * The function this interface contains
         * @param x the parameter of type specified by Java Generics
         * @return the return of the function specified by Java Generics
         */
        public abstract T f(E x);

        /**
         * function with multiple parameters and multiple returns
         * @param xs the parameters of type specified by Java Generics
         * @return the return of objects of type specified by Java Generics in an array
         */
        public T[] g(@SuppressWarnings("unchecked") E...xs) { return null; }

    }

}

