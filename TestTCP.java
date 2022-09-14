import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.*;

public class TestTCP {
    public static void main (String args[]) throws IOException{
            
       
       ClientTCP client = new ClientTCP("Hamie");
            client.setReceberMessagem();
            client.setEnviarMessagem();
        
    }
}