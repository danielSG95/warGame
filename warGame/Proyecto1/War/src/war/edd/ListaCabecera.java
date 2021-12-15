/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;

import war.edd.NodoCabecera;

/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class ListaCabecera <T> {
    protected NodoCabecera<T> primero;
    protected NodoCabecera<T> ultimo;
    protected int tam;

    public ListaCabecera() {
        this.primero = null;
        this.ultimo = null;
        this.tam = 0;        
    }
    
    public void insertar(NodoCabecera<T> nuevo)
    {
        this.tam++;
        if(primero == null)
            primero = ultimo = nuevo;
        else
        {
            if(nuevo.getPosicion() < primero.getPosicion())
            {
                nuevo.setSiguiente(primero);
                nuevo.setAnterior(ultimo);
                ultimo.setSiguiente(nuevo);
                primero.setAnterior(nuevo);
                primero = nuevo;
            }
            else
            {
                NodoCabecera<T> pivote = primero;
                while(nuevo.getPosicion() > pivote.getPosicion())
                {
                    if(pivote == ultimo)
                    {
                        ultimo.siguiente = nuevo;
                        nuevo.anterior = ultimo;
                        nuevo.siguiente = primero;
                        primero.anterior = nuevo;
                        ultimo = nuevo;
                        return;
                    }
                    pivote = pivote.getSiguiente();                    
                }
                pivote.getAnterior().setSiguiente(nuevo);
                nuevo.setAnterior(pivote.getAnterior());
                nuevo.setSiguiente(pivote);
                pivote.setAnterior(nuevo);
            }
        }
    }
    
    protected NodoCabecera<T> getNodoCabecera(int posicion)
    {
        if(primero == null) return null;
        NodoCabecera<T> pivote = primero;
        do
        {
            if(pivote != null)
            {
                if(pivote.getPosicion() == posicion)
                    return pivote;
                pivote = pivote.getSiguiente();
            }
        }while(pivote!= null && pivote!= ultimo.getSiguiente());
        return null;
    }        
    
    protected void eliminarCabecera(int posicion)
    {
        NodoCabecera<T>auxiliar = getNodoCabecera(posicion);
        if(auxiliar!= null)
        {
            auxiliar.getSiguiente().setAnterior(auxiliar.getAnterior());
            auxiliar.getAnterior().setSiguiente(auxiliar.getSiguiente());
            if(auxiliar == primero)
                primero = auxiliar.getSiguiente();
            if(auxiliar == ultimo)
                ultimo = auxiliar.getAnterior();
            auxiliar = null;
            this.tam--;
            System.out.println("Probando el eliminar");            
        }        
    }
    
    protected void recorrer()
    {
        NodoCabecera pivte = primero;
        do
        {
            if(pivte != null)
            {
                System.out.println("Dato cabecera: " +  pivte.posicion);
                pivte = pivte.siguiente;
            }            
        }while(pivte != null && pivte != primero);
        
    }
}
