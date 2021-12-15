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
public class NodoCircular <T> {
    protected T dato;
    protected NodoCircular<T> siguiente;
    protected NodoCircular<T> anterior;
    protected int id;
    
    public NodoCircular(T dato)
    {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
        this.id = 0;
    }

    public NodoCircular(T dato, int id) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
       
    public void setDato(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public void setAnterior(NodoCircular<T> anterior) {
        this.anterior = anterior;
    }

    public NodoCircular<T> getAnterior() {
        return anterior;
    }

    public void setSiguiente(NodoCircular<T> siguiente) {
        this.siguiente = siguiente;
    }

    public NodoCircular<T> getSiguiente() {
        return siguiente;
    }  
}
