/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient;

/**
 *
 * @author da9ni5el
 */
import warclient.gui.tablero;


public class WarClient {
    

    public static void main(String[] argv) throws Exception {
        
        //invocando el tablero gui
        tablero tb = new tablero();     
        tb.setVisible(true);
        
    }
    
}
