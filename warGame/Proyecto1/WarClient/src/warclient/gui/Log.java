/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient.gui;

import javax.swing.JTextArea;
import warclient.Interfaces.Observable;
import warclient.Interfaces.Observador;

/**
 *
 * @author da9ni5el
 */
public class Log implements Observable {
    private Observador observador;        
    public Log() {        
    }
        
    public void registrar(Observador b) {
        this.observador = b;                
    }
    
    @Override
    public void notificar(Object q) {
        if(q != null) {
            observador.update(q);
        } else {
            System.out.println("q vacio >:V");
        }
    }  
}
