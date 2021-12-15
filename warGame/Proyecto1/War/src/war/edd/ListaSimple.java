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
 * EN ESTE MISMO PAQUETE ESCRIBIR LAS PILAS Y COLAS
 * @param <T>
 * 
 * Utilizar lista de rangos (Drag and Drop) de horarios
 */
public class ListaSimple <T>{
    protected NodoSimple<T> primero;
    protected NodoSimple<T> ultimo;
    
    public ListaSimple() {
        this.primero = null;
        this.ultimo = null;
    }      
    
    protected boolean esVacio()
    {
        return primero==null;
    }
            
    public void insertar(T dato)
    {
        NodoSimple<T> nuevo = new NodoSimple<>(dato);
        insertar(nuevo);
    }            
    
    protected void insertar(NodoSimple<T> nuevo)
    {
        if(esVacio())
            primero = ultimo = nuevo;
        else
        {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
    }
   
    public NodoSimple<T> buscar(T valor)
    {
        IComparador dato = (IComparador)valor;
        return buscar(primero, dato);
    }
    
    protected NodoSimple<T> buscar(NodoSimple<T>primero, IComparador dato)
    {
        NodoSimple<T>pivote = primero;
        while(pivote!= null)
        {
            if(dato.igualQue(pivote.getDato(),0))
                return pivote;
            pivote = pivote.getSiguiente();
        }
        return null;
    }            
    
    public T elminar(T valor)
    {
        IComparador dato = (IComparador)valor;
        return eliminar(dato);
    }

    protected T eliminar(IComparador dato) 
    {
        if(esVacio())
        {
            System.out.println("EDD vacia");
            return null;
        }
        NodoSimple<T>pivote = primero;
        NodoSimple<T>auxiliar = null;
        T objeto = null;
        while(pivote!= null)
        {
            if(dato.igualQue(pivote.getDato(),0))
            {
                if(pivote == ultimo)
                {
                    this.ultimo = auxiliar;
                    continue;
                }
                else if(pivote == primero)
                {
                    this.primero = primero.getSiguiente();
                }
                else
                {
                    auxiliar.setSiguiente(pivote.getSiguiente());
                }                
                objeto = pivote.getDato();
                pivote = null;
                return objeto;
            }
            auxiliar = pivote;
            pivote = pivote.getSiguiente();
        }
        return null;
    }              
}
