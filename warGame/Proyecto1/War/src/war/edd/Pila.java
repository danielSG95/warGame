/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;

import war.interfaces.IComparador;

/**
 *
 * @author da9ni5el
 * @param <T>
 */

public class Pila<T> extends ListaSimple<T> {

    public Pila() {
        this.primero = null;
        this.ultimo = null;        
    }
    
    
    
    public void push(T dato)
    {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);        
        insertar(nuevo);
    }
        
    public T pop()
    {                
        return eliminar(null);
    }
    
    public T peek()
    {
        if(esVacio())
        {
            throw new Error("La  PILA se encuentra vacia");
        }
        return primero.getDato();
    }
       
    
    
    public void recorrer()
    {
        System.out.println("\tRecorriendo Pila");
        NodoSimple pivote = primero;
        while(pivote != null)
        {
            System.out.println("Dato: " + pivote.dato);
            pivote = pivote.getSiguiente();
        }
    }
    
    @Override
    protected void insertar(NodoSimple<T> nuevo)
    {        
        if(esVacio())
            primero = nuevo;
        else
        {
            nuevo.setSiguiente(primero);
            primero = nuevo;
        }
    }
    
    @Override
    protected T eliminar(IComparador dato)
    {        
        if(esVacio())
            return null;
        NodoSimple<T>pivote = primero;
        T auxiliar = pivote.getDato();
        primero = primero.getSiguiente();
        pivote.numerico--;
        pivote = null;        
        return auxiliar;
    }
    
}
