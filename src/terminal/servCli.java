/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.System.in;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author artmario
 */
public class servCli extends Thread {

    private List<String> tarefas;
    private final Socket cliente;
    private BufferedInputStream in;
    private BufferedOutputStream out;

    public servCli(Socket cliente) {
        this.cliente = cliente;
        List<String> tarefas = Collections.synchronizedList(new ArrayList<String>());
    }

    @Override
    public void run() {
        try {
            in = new BufferedInputStream(cliente.getInputStream());
            out = new BufferedOutputStream(cliente.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(servCli.class.getName()).log(Level.SEVERE, null, e);
        }
        new Thread(new Runnable() {// enviador
            @Override
            public void run() {
                byte[] cod = null;
                try {
                    write(out, "#wake", null);

                    while (true) {
                        //write(out, "#live?", null);
                        write(out, "#live?", null);
                        write(out, null, mensa("teste", "mesmo", 1));
                        Thread.sleep(10000);
                        synchronized(tarefas)
                        {
                            while(tarefas.isEmpty())
                            {
                                write(out, null, mensa(tarefas.get(0), "mesmo", 1));
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(servCli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(servCli.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();// enviador
        try {
            while (true) {
                //recepção
                byte[] dados = new byte[255];
                int qtd = in.read(dados);
                if (qtd > 0) {
                    String str_retorno = new String(dados);
                    str_retorno = str_retorno.substring(1, qtd).trim();
                    System.out.println(str_retorno);
                    synchronized(tarefas)
                    {
                        tarefas.add(str_retorno);
                    }

                } else {
                    break;
                }
            } //recepção
        } catch (IOException e) {
            Logger.getLogger(servCli.class.getName()).log(Level.SEVERE, null, e);
        }
        //To change body of generated methods, choose Tools | Templates.//To change body of generated methods, choose Tools | Templates.
    }

    public void write(OutputStream stream, String value, byte[] binary) throws IOException, InterruptedException {
        byte[] data;
        if (binary == null) {
            data = value.getBytes();
        } else {
            data = binary;
        }
        stream.flush();
        stream.write(data);
        stream.flush();
        Thread.sleep(1000);
    }

    public byte[] mensa(String msg1, String msg2, int time) {

        String msg = "#mesgx@msg1X@msg2XX";
        String buff = "";
        buff = msg;
        buff = buff.replace("@msg1", msg1);
        buff = buff.replace("@msg2", msg2);
        byte[] cod = buff.getBytes();
        cod[5] = (byte) (msg1.length() + 48);
        cod[5 + msg1.length() + 1] = (byte) (msg2.length() + 48);
        cod[buff.length() - 1] = 48;
        cod[buff.length() - 2] = (byte) time;
        return cod;
    }

}
