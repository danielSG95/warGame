package war.edd;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//COMPROBAR FUNCIONALIDAD DE ELMIINAR
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import war.interfaces.*;

/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class ABB<T> {

    protected ABBNodo<T> raiz;

    public ABB() {
        this.raiz = null;
    }

    public ABB(ABBNodo<T> raiz) {
        this.raiz = raiz;
    }

    public ABBNodo<T> raizArbol() {
        return raiz;
    }

    boolean esVacio() {
        return raiz == null;
    }

    public ABBNodo<T> buscar(T buscado) {
        IComparador dato;
        dato = (IComparador) buscado;
        return localizar(raiz, dato);
    }

    protected ABBNodo<T> localizar(ABBNodo<T> raizSub, IComparador buscado) {
        if (raizSub == null) {
            return null;
        } else if (buscado.igualQue(raizSub.getDato(), 0)) {
            return raizSub;
        } else if (buscado.menorQue(raizSub.getDato(), 0)) {
            return localizar(raizSub.izdo, buscado);
        } else {
            return localizar(raizSub.dcho, buscado);
        }
    }

    public boolean insertar(T valor) {
        try {
            IComparador dato;
            dato = (IComparador) valor;
            raiz = insertar(raiz, dato);
            if (raiz != null) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    protected ABBNodo<T> insertar(ABBNodo<T> raizSub, IComparador dato) throws Exception {
        if (raizSub == null) {
            raizSub = new ABBNodo(dato);
        } else if (dato.menorQue(raizSub.getDato(), 0)) {
            ABBNodo<T> izdo;
            izdo = insertar(raizSub.getIzdo(), dato);
            raizSub.setIzdo(izdo);
        } else if (dato.mayorQue(raizSub.getDato(), 0)) {
            ABBNodo<T> dcho;
            dcho = insertar(raizSub.getDcho(), dato);
            raizSub.setDcho(dcho);
        }
        return raizSub;
    }

    public void recorrerInorden() {
        ayudanteInorden(raizArbol());
    }

    public void ayudanteInorden(ABBNodo<T> raiz) {
        if (raiz == null) {
            return;
        }

        ayudanteInorden(raiz.getIzdo());
        IModificador dato = (IModificador) raiz.getDato();
        System.out.println(dato.escribirObjeto(dato, false));
//        System.out.println("Elemento: " + raiz.getDato());
        ayudanteInorden(raiz.getDcho());
    }

    public T modificar(T nuevo) {
        IModificador dato;
        dato = (IModificador) nuevo;
        return modificar(raiz, dato);
    }

    protected T modificar(ABBNodo<T> raiz, IModificador dato) {
        ABBNodo<T> buscado = buscar((T) dato);
        if (buscado != null) {
            dato.modificarObjeto(buscado.getDato());
            return buscado.getDato();
        }
        return null;
    }

    /*CONTIENE ERRORES*/
    public void eliminar(T valor) throws Exception {
        IComparador dato;
        dato = (IComparador) valor;
        raiz = eliminar(raiz, dato);        
    }
//método interno para realizar la operación
   

    protected ABBNodo<T> eliminar(ABBNodo<T> raizSub, IComparador dato) throws Exception {
        if (raizSub == null) {
            throw new Exception("No encontrado el nodo con la clave");
        } else if (dato.menorQue(raizSub.getDato(), 0)) {
            ABBNodo<T> izdo;
            izdo = eliminar(raizSub.getIzdo(), dato);
            raizSub.setIzdo(izdo);
        } else if (dato.mayorQue(raizSub.getDato(), 0)) {
            ABBNodo<T> dcho;
            dcho = eliminar(raizSub.getDcho(), dato);
            raizSub.setDcho(dcho);
        } else // Nodo encontrado
        {
            ABBNodo<T> q;

            q = raizSub; 							 // nodo a quitar del árbol
            if (q.getIzdo() == null) {
                raizSub = q.getDcho();
            } else if (q.getDcho() == null) {
                raizSub = q.getIzdo();
            } else { 											 // tiene rama izquierda y derecha
                q = reemplazar(q);
            }
            q = null;
        }
        return raizSub;
    }
// método interno para susutituir por el mayor de los menores

    private ABBNodo<T> reemplazar(ABBNodo<T> act) {
        ABBNodo<T> a;
        ABBNodo<T> p;
        p = act;
        a = act.getIzdo();					 // rama de nodos menores
        while (a.getDcho() != null) {
            p = a;
            a = a.getDcho();
        }
        act = new ABBNodo(a.getDato());
        if (p == act) {
            p.setIzdo(a.getIzdo());
        } else {
            p.setDcho(a.getIzdo());
        }
        return p;
    }
    
    
    
    
    
    /**
     *      METODO SERVER     
    */
    
    public String obtenerTropas() {
        return devolverTropas(raiz, true);        
    }
    
    private String devolverTropas(ABBNodo raiz, boolean flag) {
        if(raiz == null) {
            return "";
        }
        
        StringBuffer buffer = new StringBuffer();
        IModificador dato = (IModificador)raiz.getDato();
        if(flag == false)
            buffer.append(",");
        buffer.append("{\n");
        buffer.append(dato.devolverServer());
        buffer.append("}\n");        
        buffer.append(devolverTropas(raiz.getIzdo(), false));
        buffer.append(devolverTropas(raiz.getDcho(), false));
        
        return buffer.toString();
    }    
    
    /**
     *      FIN METODOS SERVER
    */
    

    public void graficarABB(String nombre) {
        if (raiz == null) {
            System.out.println("Arbol vacio");
            return;
        }
        try {
            try (BufferedWriter dotcode = new BufferedWriter(new FileWriter(new File(nombre + ".dot")))) {
                dotcode.write("digraph G{\ngraph[overlap=true, fontsize = 0.5];\nnode[shape=ellipse, fontname=Helvetica, fixedsize=true, width=3.5, height=0.9];\n");
                dotcode.write("edge[color = black];\n");
                dotcode.write(toDot(raiz));
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

    private String toDot(ABBNodo<T> raiz) {
        StringBuffer buffer = new StringBuffer();
        IModificador dato;
        if (raiz == null) {
            return null;
        }

        dato = (IModificador) raiz.getDato();
        buffer.append("nodo").append(raiz.hashCode()).append("[label=\"");
        buffer.append(dato.escribirObjeto(dato, false)).append("\"];\n");

        if (raiz.getIzdo() != null) {

            dato = (IModificador) raiz.getIzdo().getDato();
            buffer.append("nodo").append(raiz.getIzdo().hashCode()).append("[label=\"");
            buffer.append(dato.escribirObjeto(dato, false)).append("\"];\n");

            buffer.append("nodo").append(raiz.hashCode()).append(" -> nodo").append(raiz.getIzdo().hashCode());
            buffer.append("[label=\"izdo\"];\n");

            buffer.append(toDot(raiz.getIzdo()));
        }
        if (raiz.getDcho() != null) {

            dato = (IModificador) raiz.getDcho().getDato();
            buffer.append("nodo").append(raiz.getDcho().hashCode()).append("[label=\"");
            buffer.append(dato.escribirObjeto(dato, false)).append("\"];\n");

            buffer.append("nodo").append(raiz.hashCode()).append(" -> nodo").append(raiz.getDcho().hashCode());
            buffer.append("[label=\"dcho\"];\n");

            buffer.append(toDot(raiz.getDcho()));
        }
        return buffer.toString();
    }
}
