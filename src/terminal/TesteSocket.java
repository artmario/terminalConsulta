
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import terminal.servCli;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ernandox86
 */
public class TesteSocket {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(6500);
        System.out.println("ouvindo na porta"+ serverSocket.toString());
        while (true) {
            Socket cliente = serverSocket.accept();
            cliente.setSoTimeout(60000);
            System.out.println(cliente.isConnected() ? "Conectado\t" + cliente.getInetAddress().getHostAddress() : "Desconectado");
            new servCli(cliente).start();
            
        }

    }
    

    

}
