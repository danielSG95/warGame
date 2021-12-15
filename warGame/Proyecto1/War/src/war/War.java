/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import war.conexion.RPCServer;
import war.gui.Inicio;
import war.edd.ABB;
import war.edd.MatrizOrtogonal;
import war.modelo.pixel;
import war.modelo.tropa;
/**
 *
 * @author da9ni5el
 */
public class War {

    /**
     * @param args the command line arguments
     */
    private static ABB<tropa> j1;
    private static ABB<tropa> j2;
    private static MatrizOrtogonal<pixel> unificada;
    
    public static void main(String[] args) throws IOException, TimeoutException, Exception {
        // TODO code application logic here
        
        //declarando estructuras.
        
        
        j1 = new ABB<>();
                       
        j2 = new ABB<>();
               
        unificada = new MatrizOrtogonal<>();
        //Invocando la interfaz 
        
        RPCServer rpcserver = new RPCServer();
        Inicio in = new Inicio( rpcserver );
        in.setABB(j1, j2);        
        in.setVisible(true);                 
                       
        rpcserver.setArboles(j1, j2);        
        rpcserver.listener();
    }
    
}
