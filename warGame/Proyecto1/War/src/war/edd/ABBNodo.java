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
public class ABBNodo <T> {
    private T dato;
    protected ABBNodo<T> izdo;
    protected ABBNodo<T> dcho;
    
    public ABBNodo(T dato)
    {
        this.dato = dato;
        izdo = null;
        dcho = null;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public void setIzdo(ABBNodo<T> izdo) {
        this.izdo = izdo;
    }

    public ABBNodo<T> getIzdo() {
        return izdo;
    }

    public void setDcho(ABBNodo<T> dcho) {
        this.dcho = dcho;
    }

    public ABBNodo<T> getDcho() {
        return dcho;
    }                    
}
