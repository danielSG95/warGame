/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;

/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class NodoSimple <T>{
    protected int numerico;
    protected T dato;
    protected NodoSimple<T> siguiente;
    
    public NodoSimple(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.numerico = 0;        
    }   

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoSimple<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoSimple<T> siguiente) {
        this.siguiente = siguiente;
    }

    public int getNumerico() {
        return numerico;
    }

    public void setNumerico(int numerico) {
        this.numerico = numerico;
    }

}
