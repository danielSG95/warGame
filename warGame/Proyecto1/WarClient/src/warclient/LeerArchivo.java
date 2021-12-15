/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import warclient.conexion.RPCClient;

/**
 *
 * @author da9ni5el
 */
public class LeerArchivo {
    
    private RPCClient rpc;
    public LeerArchivo() throws IOException, TimeoutException {        
        rpc = new RPCClient();
    }
    
    
    
    public void leer(String file, int type) throws IOException, Exception {
        leerP(file, type);
    }
    
    private int countLinesFile(String file) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);        
        String cadena;
        int cont = 0;
        while ((cadena = br.readLine()) != null) {                        
            ++cont;
        }
        br.close();
        return cont;
    }

    private void leerP(String file, int type) throws FileNotFoundException, IOException, Exception {
        System.out.println("procesando");
        String cadena;
        int lines = countLinesFile(file);       
        StringBuffer data = new StringBuffer();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);        
        data.append("{\n");
        data.append("\"typeOperation\":").append(type).append(",\n");//11 para jugador 1 || 12 para jugador 2
        data.append("\"Tropas\": [\n");
        while ((cadena = br.readLine()) != null) {                        
            lines--;
            cadena = cadena.replace(';', ',');
            String a[] = cadena.split(",");
            data.append("{\n");
            data.append(parseReadingToJsonObject(a[0], a[1], a[2], a[3]));
                
            data.append("\n}");
            if(lines >= 1){
                data.append(",\n");
            }
            
        }
        br.close();
        data.append("\n]");
        data.append("\n}");
        
        System.out.println(data.toString());
        try {
            rpc.EnviarArbol(data.toString());
        } catch (TimeoutException ex) {
            Logger.getLogger(LeerArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private String parseReadingToJsonObject(String x, String y, String id, String tipo) {
        StringBuffer buf = new StringBuffer();
        
        buf.append("\"x\":").append(x).append(",\n");
        buf.append("\"y\":").append(y).append(",\n");
        buf.append("\"id\":").append(id).append(",\n");
        buf.append("\"tipo\":\"").append(tipo).append("\"");
        return buf.toString();
    }

}
