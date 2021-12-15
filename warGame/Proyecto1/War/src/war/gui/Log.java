/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.gui;

import war.interfaces.Observable;
import war.interfaces.Observador;

/**
 *
 * @author da9ni5el
 */
public class Log implements Observable {

    private Observador observador;
    
    public Log() {
    }

    public void suscribir(Observador o) {
        this.observador = o;
    }
    
    @Override
    public void notificar(Object q) {
        observador.update(q, true);
    }   
}
