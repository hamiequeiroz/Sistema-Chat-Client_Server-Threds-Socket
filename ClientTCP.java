import java.io.*;
import java.net.*;
import java.util.*;


public class ClientTCP extends Thread {
    
    private String nome;
    private Socket socket;
    final static int port = 4455;
    final static String server = "localhost";
    
    private DataInputStream in;
    Thread threadReceberMensagem;
    Thread threadEnviarMensagem;
    private  DataOutputStream out;

    public String getNome(){
        return this.nome;
    }
    
    public ClientTCP(String nome_) throws SocketException, UnknownHostException {
        try {
            this.nome = nome_;
            socket = new Socket(server, port);
            in     = new DataInputStream(socket.getInputStream());
            out    = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Erro na criacao do cliente: " + e.getMessage());
        }
    }

    class receberMensagem extends Thread {

        public void run() {
            try {
                System.out.println(getMessage());
                run();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    class enviarMensagem extends Thread {

        public void run() {
            try {
                
                Scanner leitor = new Scanner(System.in);
                System.out.print("Eu: ");
                String echo = leitor.nextLine();

                if(echo.equals("sair")){ 
                    close();
                }else{
                    sendEcho(echo);
                }
                
                run();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    public void sendEcho(String msg)throws IOException {
        
        out.writeUTF(msg);
        
    }

    public void setReceberMessagem()throws IOException {
        
        threadReceberMensagem = new receberMensagem();
        threadReceberMensagem.start();
        
    }

    public void setEnviarMessagem()throws IOException {
        System.out.println("Conexao estabelecida. Bem vindo ao Chat!!");
        threadEnviarMensagem = new enviarMensagem();
        threadEnviarMensagem.start();
        
    }

    public String getMessage()throws IOException {
        String recebido = "";
        try {
            recebido = in.readUTF();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return recebido;
    }

    public void close() throws SocketException {
        try {
            in.close();
            out.close();
            socket.close();
            try {
                threadEnviarMensagem.interrupt();
                threadReceberMensagem.interrupt();
            } catch (Exception e) {
                //TODO: handle exception
            }
            System.out.println("Chat Encerrado!!"); 
            
        } catch (IOException e) {
            System.out.println("Erro ao fechar conexao: " + e.getMessage());
        }
        
    }
}