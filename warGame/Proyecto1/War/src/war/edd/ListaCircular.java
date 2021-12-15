/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class ListaCircular<T> {

    protected NodoCircular primero;
    protected NodoCircular ultimo;

    public ListaCircular() {
        primero = null;
        ultimo = null;
    }

    private boolean esVacio() {
        return primero == null;
    }

    private void insertarP(T dato, int id) {
        NodoCircular nuevo = new NodoCircular(dato, id);
        if (esVacio()) {
            primero = ultimo = nuevo;
            primero.anterior = ultimo;
            ultimo.siguiente = primero;
        } else {
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = primero;
            primero.anterior = nuevo;
            ultimo = nuevo;
        }
    }

    private NodoCircular buscarElemento(int id) {
        NodoCircular pivote = primero;
        do {
            if (pivote != null) {
                if (pivote.id == id) {
                    return pivote;
                }
                pivote = pivote.getSiguiente();
            }

        } while (pivote != primero);
        System.out.println("No se ha encontrado el id dentro de la lista");
        return null;
    }

    private boolean eliminarP(int id) {
        NodoCircular auxiliar = buscarElemento(id);
        if (auxiliar != null) {
            
            auxiliar.siguiente.anterior = auxiliar.anterior;
            auxiliar.anterior.siguiente = auxiliar.siguiente;
            if (auxiliar == primero)
		primero = auxiliar.siguiente;
            if (auxiliar == ultimo)
		ultimo = auxiliar.anterior;
            
            if(primero == ultimo ) {
                primero = ultimo = null;
            }
            
            auxiliar = null;
            System.out.println("Nodo eliminado");
            return true;
        }        
        System.out.println("No se encuentra dentro de la lista circular");
        return false;
    }

    public void insertar(T dato, int id) {
        insertarP(dato, id);
    }

    public boolean eliminar(int id)
    {
        return eliminarP(id);
    }
    
    public NodoCircular buscar(int id)
    {
        return buscarElemento(id);
    }
    
    
    public void graficar(String nombre){
        if (primero == null) {
            System.out.println("Arbol vacio");
            return;
        }
        try {
            try (BufferedWriter dotcode = new BufferedWriter(new FileWriter(new File(nombre + ".dot")))) {
                dotcode.write("digraph G{\n");
                dotcode.write("edge[color = black, dit= both];\n");
                dotcode.write(toDot());
                dotcode.write("}");
                dotcode.close();
            }

            Runtime rt = Runtime.getRuntime();
            rt.exec("dot -Tpng " + nombre + ".dot -o src/img/" + nombre + ".png");
            rt.exec("ristretto src/img/" + nombre + ".png");
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
    }
    
    private String toDot(){
        StringBuffer buffer = new StringBuffer();
        NodoCircular pivote = primero;
        do
        {
            if(pivote != null) {
                buffer.append("nodo").append(pivote.hashCode()).append("[");
                buffer.append("label=\"Capa: ").append(pivote.id).append("\"];\n");
                
                if(pivote.getSiguiente() != null) {                    
                    buffer.append("nodo").append(pivote.getSiguiente().hashCode()).append("[");
                    buffer.append("label=\"Capa: ").append(pivote.getSiguiente().id).append("\"];\n");
                                                            
                    buffer.append("nodo").append(pivote.hashCode());
                    buffer.append("->").append("nodo").append(pivote.getSiguiente().hashCode()).append("\n");
                    
                    buffer.append("nodo").append(pivote.getSiguiente().hashCode());
                    buffer.append("->").append("nodo").append(pivote.hashCode()).append("\n");
                } 
                pivote = pivote.getSiguiente();
            }
        }while(pivote != primero);
        return buffer.toString();
    }
    
    public NodoCircular getPrimero()
    {
        return this.primero;
    }
        
    public NodoCircular getUltimo()
    {
        return this.ultimo;
    }    
}
