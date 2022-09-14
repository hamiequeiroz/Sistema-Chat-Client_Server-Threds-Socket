import java.io.*;
import java.net.*;
import java.util.*;

public class ServerTCP extends Thread {
    
    private ServerSocket socket;

    ArrayList<Socket> array_clientes = new ArrayList<Socket>(10);

    final static int serverPort = 4455;
   
    Thread ThredConexoes;
    Thread ThredReceberMensagens;
    
    public ServerTCP() throws SocketException {
        try {
             socket = new ServerSocket(serverPort);
        } catch (IOException e) {
             System.out.println("Erro na criacao do servidor: " + e.getMessage());
        }
    }

    class ThredReceberConexoes extends Thread {
        Socket cliente_teste;
        private DataInputStream in;
        private DataOutputStream out;

        ThredReceberConexoes(Socket sck){
            cliente_teste = sck;
            array_clientes.add(sck);
        }
        public void run() {
            
            try {
                String IPAddress = cliente_teste.getInetAddress().getHostAddress();
                int ClientPort = cliente_teste.getPort();
                System.out.println("Conexao aceita: " + IPAddress +":"+ClientPort);
                System.out.println(".....");

                if(in == null){
                    in  = new DataInputStream(cliente_teste.getInputStream());
                    out = new DataOutputStream(cliente_teste.getOutputStream());
                }    
                
                String recebido = in.readUTF();

                System.out.println(IPAddress +":"+ClientPort + ": "+recebido);

                System.out.println(".....");

                System.out.println("Clientes Conectados: "+array_clientes.size());  
                for(int i=0;i < array_clientes.size();i++){ 
                        
                    if(array_clientes.get(i) != cliente_teste){
                        DataOutputStream outDestino = new DataOutputStream(array_clientes.get(i).getOutputStream());
                        outDestino.writeUTF(IPAddress +":"+ClientPort + ": "+recebido);
                    }
                        
                }

                run();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    public void run() {
        try { 

            System.out.println("Esperando clientes... " + serverPort);
            Socket cliente = new Socket();
            cliente = socket.accept();
            System.out.println("...");
            

            ThredConexoes = new ThredReceberConexoes(cliente);
            ThredConexoes.start();

            run();

        } catch (IOException e) {
            System.out.println("Erro na escuta: " + e.getMessage());
        }
    }

    public static void main (String args[]) throws Exception {
        
        new ServerTCP().start();

    }
}