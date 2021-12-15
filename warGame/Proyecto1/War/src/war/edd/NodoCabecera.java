/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;

import war.edd.NodoOrtogonal;

/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class NodoCabecera <T>{
    protected int posicion;
    protected NodoCabecera<T> siguiente;
    protected NodoCabecera<T> anterior;
    protected NodoOrtogonal<T> acceso;

    public NodoCabecera(int posicion) {
        this.posicion = posicion;
        this.siguiente = null;
        this.anterior = null;
        this.acceso = null;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public NodoCabecera<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCabecera<T> siguiente) {
        this.siguiente = siguiente;
    }

    public NodoCabecera<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoCabecera<T> anterior) {
        this.anterior = anterior;
    }

    public NodoOrtogonal<T> getAcceso() {
        return acceso;
    }

    public void setAcceso(NodoOrtogonal<T> acceso) {
        this.acceso = acceso;
    }
    
    
    
}
