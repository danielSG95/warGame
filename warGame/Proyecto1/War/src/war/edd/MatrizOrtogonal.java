/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;


import java.io.IOException;
import java.io.PrintWriter;
import war.interfaces.IComparador;
import war.interfaces.IModificador;

//[dir=both]
/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class MatrizOrtogonal<T> {

    protected ListaCabecera<T> filas;
    protected ListaCabecera<T> columnas;

    public MatrizOrtogonal() {
        this.filas = new ListaCabecera<>();
        this.columnas = new ListaCabecera<>();
    }
    

    public void insertar(T valor, int x, int y)//x columna, y fila
    {
        NodoOrtogonal<T> nuevo = new NodoOrtogonal<>(valor, x, y);

        NodoCabecera<T> fila = filas.getNodoCabecera(y);
        insertarFilas(nuevo, fila);

        NodoCabecera<T> columna = columnas.getNodoCabecera(x);
        insertarColumna(nuevo, columna);

    }

    private void insertarFilas(NodoOrtogonal<T> nuevo, NodoCabecera<T> fila) {
        if (fila == null) {
            fila = new NodoCabecera<>(nuevo.getY());
            filas.insertar(fila);
            fila.setAcceso(nuevo);
        } else {
            if (nuevo.getX() < fila.getAcceso().getX()) {
                nuevo.setDcho(fila.getAcceso());
                fila.getAcceso().setIzdo(nuevo);
                fila.setAcceso(nuevo);
            } else {
                NodoOrtogonal<T> pivote = fila.getAcceso();
                while (nuevo.getX() >= pivote.getX()) {
                    IComparador dato = (IComparador) nuevo.getDato();
                    if (dato.igualQue(pivote.getDato(), 0)) {
//                        System.out.println("Sobreponiendo pixel");
                        pivote.setDato(nuevo.getDato());
                        return;
                    }

                    if (pivote.getDcho() == null) {
                        pivote.setDcho(nuevo);
                        nuevo.setIzdo(pivote);
                        return;
                    }
                    pivote = pivote.getDcho();
                }
                pivote.getIzdo().setDcho(nuevo);
                nuevo.setIzdo(pivote.getIzdo());
                nuevo.setDcho(pivote);
                pivote.setIzdo(nuevo);
            }
        }
//        return fila;
    }

    private void insertarColumna(NodoOrtogonal<T> nuevo, NodoCabecera<T> columna) {
        if (columna == null) {
            columna = new NodoCabecera<>(nuevo.getX());
            columnas.insertar(columna);
            columna.setAcceso(nuevo);
        } else {
            if (nuevo.getY() < columna.getAcceso().getY()) {
                nuevo.setAbjo(columna.getAcceso());
                columna.getAcceso().setArrb(nuevo);
                columna.setAcceso(nuevo);
            } else {
                NodoOrtogonal<T> pivote = columna.getAcceso();
                while (nuevo.getY() >= pivote.getY()) {
                    IComparador dato = (IComparador) nuevo.getDato();
                    if (dato.igualQue(pivote.getDato(), 0)) {
//                        System.out.println("Sobreponiendo pixel");
                        pivote.dato = nuevo.dato;
                        return;
                    }

                    if (pivote.getAbjo() == null) {
                        pivote.setAbjo(nuevo);
                        nuevo.setArrb(pivote);
                        return;
                    }
                    pivote = pivote.getAbjo();
                }
                pivote.getArrb().setAbjo(nuevo);
                nuevo.setArrb(pivote.getArrb());
                nuevo.setAbjo(pivote);
                pivote.setArrb(nuevo);
            }
        }
//        return columna;
    }
    
    public T buscar(int x, int y) {
        NodoOrtogonal buscado = buscarNodoOrtogonal(y, x);
        if(buscado != null) {           
            return (T) buscado.getDato();                        
        }
        return null;
    }

    protected NodoOrtogonal<T> buscarNodoOrtogonal(int y, int x) {
        NodoCabecera<T> efila = filas.getNodoCabecera(y);
        if (efila == null) {
            return null;
        }
        NodoOrtogonal<T> pivote = efila.getAcceso();
        while (pivote != null) {
            if (pivote.getX() == x) {
                return pivote;
            }
            pivote = pivote.getDcho();
        }
        return null;
    }

    //eliminar por coordenada
    private void eliminarNodoOrtogonal(int y, int x) {
        NodoOrtogonal<T> buscado = buscarNodoOrtogonal(y, x);
        if (buscado == null)//DEBERIA CAMBIAR LOS RETURN NULL POR EXCEPTIONS/ERROR?
        {
            System.out.println("La posicion indicada no se encuentra en la matriz");
            return;
        }
        NodoCabecera<T> efila = filas.getNodoCabecera(y);
        NodoCabecera<T> ecolumna = columnas.getNodoCabecera(x);

        //FILAS        
        if (efila.getAcceso() == buscado) {
            if (buscado.getDcho() != null) {
                efila.setAcceso(buscado.getDcho());
            } else {
                efila.setAcceso(null);
                buscado.setIzdo(null);
                filas.eliminarCabecera(y);
            }
        } else {
            if (buscado.getDcho() != null) {
                buscado.getIzdo().setDcho(buscado.getDcho());
                buscado.getDcho().setIzdo(buscado.getIzdo());
            } else {
                buscado.getIzdo().setDcho(null);
                buscado.setIzdo(null);
            }
        }

        //COLUMNAS.
        if (ecolumna.getAcceso() == buscado) {
            if (buscado.getAbjo() != null) {
                ecolumna.setAcceso(buscado.getAbjo());
                buscado.getAbjo().setArrb(ecolumna.getAcceso());
            } else {
                ecolumna.setAcceso(null);
                buscado.setArrb(null);
                columnas.eliminarCabecera(x);
            }
        } else {
            if (buscado.getAbjo() != null) {
                buscado.getArrb().setAbjo(buscado.getAbjo());
                buscado.getAbjo().setArrb(buscado.getArrb());
            } else {
                buscado.getArrb().setAbjo(null);
                buscado.setArrb(null);
            }
        }
        buscado = null; //se elimina el nodo en cuestion
    }

    
    
    public void eliminarPorCoincidenciaP(T tipo)// O(n^3)
    {
        NodoCabecera efila = filas.primero;
        do {            
            if(efila != null){
                NodoOrtogonal actual = efila.getAcceso();
                while (actual != null) {                    
                    
                    IComparador dato = (IComparador)actual.getDato();
                    if(dato.igualQue(tipo, 1)){
                        System.out.println("Nodo de Capa encontrado en:  " + actual.getX() + "," +  actual.getY());
                        eliminarNodoOrtogonal(actual.getY(), actual.getX());
//                        return true;
                    }
                    actual = actual.getDcho();
                }
                efila = efila.getSiguiente();
            }
        } while (efila != filas.primero);
//        return false;
    }
           
    public MatrizOrtogonal generarUnificada(MatrizOrtogonal copiarEn)//La matriz de entrada siempre existira
    {
        System.out.println("Generando unificada...");
        NodoCabecera ecolumna = columnas.primero;
        do {
            if (ecolumna != null) {
                NodoOrtogonal actual = ecolumna.getAcceso();
                while (actual != null) {
                    copiarEn.insertar(actual.getDato(), actual.getX(), actual.getY());
                    actual = actual.getAbjo();
                }
                ecolumna = ecolumna.siguiente;
            }
        } while (ecolumna != columnas.primero);
        return copiarEn;
    }

    
    
    /**
     *      METODOS PARA EL SERVIDOR
    */
    public int MaxX() {        
        return columnas.tam;
    }
    
    public int MaxY() {
        return filas.tam;
    }
    
    public String enviarTexturas() {
        StringBuffer buffer = new StringBuffer();
        //Recorrido por filas
        NodoCabecera ecolumna = columnas.primero;
        do
        {
            if(ecolumna != null) {
                NodoOrtogonal acceso = ecolumna.getAcceso();
                while(acceso != null) {
                    IModificador dato = (IModificador)acceso.getDato();
                    buffer.append("{\n");
                    buffer.append(dato.devolverServer());
                    buffer.append("}");
                    if(acceso.getAbjo()!= null)
                        buffer.append(",\n");                    
                    acceso = acceso.getAbjo();
                }
                if(ecolumna.getSiguiente() != columnas.primero) {
                    buffer.append(",\n");
                }
                ecolumna = ecolumna.getSiguiente();
            }
            
        }while(ecolumna != columnas.primero);
        
//        System.out.println(buffer.toString());
        return buffer.toString();
    }
    
    
    /**
     *      FIN METODOS PARA EL SERVIDOR
    */
    
    //      METODOS DE GRAFICAR
    public boolean graficar(String nombre) {
        return escribirDiscoMatriz(InicioGraph(), nombre, true);
    }

    
    //  GENERA EL MAPA POR CAPAS
    public boolean graficarPixles(String nombre) {
        return escribirDiscoMatriz(generarGraphPix(), nombre, false);
    }
    
    //  GRAFICA LA UNIFICADA
    public boolean graficarPx(String nombre){
        return escribirDiscoMatriz(graphPix(), nombre, true);
    }

    protected boolean escribirDiscoMatriz(String dot, String nombre, boolean type) {
        try {
            PrintWriter dotCode = new PrintWriter(nombre + ".dot");
            dotCode.println(dot);
            dotCode.close();

            Runtime rt = Runtime.getRuntime();
            if(type){
                rt.exec("dot -Tpng " + nombre + ".dot -o src/img/" + nombre + ".png");                
            }else{
                rt.exec("neato -n -Tpng " + nombre + ".dot -o src/img/" + nombre + ".png");
            }
            rt.exec("ristretto src/img/" + nombre + ".png"); 
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    
    public String generarGraphPix()
    {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("digraph Pixeles{\n");
            buffer.append("graph[ranksep=0.2];\n");
            buffer.append("node[shape=box, width=0.6, heigth=0.6,fillcolor=\"white\"");
            buffer.append("color=\"white\" style=\"filled\"];\n");
            buffer.append("edge[style = bold];\n");
//            buffer.append("struct0[label=\"\" pos=\"0,0!\"];");
            buffer.append(nodosPix());
            buffer.append("}");
            return buffer.toString();
            
        } catch (Exception e) {
            System.out.println("Graficando posiciones error: " + e.toString());
            return null;
        }
    }
    
    public String nodosPix()
    {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera pivote = columnas.primero;        
        do
        {
            if(pivote!= null)
            {
                NodoOrtogonal auxiliar = pivote.getAcceso();
                int aux = (auxiliar.getX()+1)*+39;  
                while(auxiliar!= null)
                {     
                    IModificador dato = (IModificador) auxiliar.getDato();
                    buffer.append("\nstruct");
                    buffer.append(auxiliar.getX()).append(auxiliar.hashCode());
                    buffer.append(auxiliar.getY()).append(auxiliar.hashCode());
                    buffer.append("[label=\"").append(auxiliar.getX()).append(",").append(auxiliar.getY()).append("\"");
                    buffer.append(",fillcolor=").append(dato.escribirObjeto(dato, false));
                    buffer.append(" pos = \"");
                    buffer.append(aux).append(",");
                    buffer.append((auxiliar.getY()+1)*-35).append("!\"];");
                                              
                    auxiliar = auxiliar.getAbjo();
                }
                pivote = pivote.getSiguiente();
            }
        }while(pivote!= columnas.primero);
        return buffer.toString();        
    }
    

    private String graphPix() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("digraph matriz{\n");
        buffer.append("graph[nodesep=0.02, ranksep=0.0005, margin=0.05, ratio=\"compress\"];\n");
        buffer.append("node[shape=box, style=filled];\n");
        buffer.append("edge[color=white dir=none];\n");
        buffer.append("rankdir=UD;\n");
        buffer.append(cabeceraYP()).append("\n");
        buffer.append(dotEnlaceX()).append("\n");
        buffer.append(dotEnlaceY()).append("\n");        
        buffer.append("}");
        return buffer.toString();
    }

    private String cabeceraYP() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera efila = filas.primero;
        do {
            if (efila != null) {
                buffer.append("{rank=same; ");                
                NodoOrtogonal actual = efila.acceso;
                while (actual != null) {
                    IModificador dato = (IModificador) actual.getDato();
                    buffer.append("\"xy: ").append(actual.getX()).append(",").append(actual.getY()).append("\"");
                    buffer.append("[label=\"\",style=filled fillcolor=");
                    buffer.append(dato.escribirObjeto(dato, false)).append("];");
                    actual = actual.getDcho();
                }
                buffer.append("}\n");
            }
            efila = efila.getSiguiente();
        } while (efila != null && efila != filas.primero);
        return buffer.toString();
    }
    
    

    private String InicioGraph() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("digraph matriz{\n");
//        buffer.append("graph[nodesep=0.2, ranksep=0.4];\n");
        buffer.append("node[shape=box, style=filled];\n");
        buffer.append("edge[color=black dir=both];\n");
        buffer.append("rankdir=UD;\n");
        buffer.append(cabeceraX()).append("\n");
        buffer.append(cabeceraY()).append("\n");
        buffer.append(dotEnlaceX()).append("\n");
        buffer.append(cabeceraNodoX()).append("\n");
        buffer.append(dotEnlaceY()).append("\n");
        buffer.append(cabeceraNodoY()).append("\n");
        buffer.append(enlaceCabeceraX()).append("\n");
        buffer.append(enlaceCabeceraY()).append("\n");
        buffer.append("}");
        return buffer.toString();
    }

    private String cabeceraX() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera ecolumna = columnas.primero;
        buffer.append("{rank=min; \"Matriz\"; ");
        do {
            if (ecolumna != null) {
                buffer.append("\"x").append(ecolumna.getPosicion()).append("\";");
//                buffer.append(";");
            }
            ecolumna = ecolumna.getSiguiente();
        } while (ecolumna != null && ecolumna != columnas.primero);
        buffer.append("}");
        return buffer.toString();
    }

    private String cabeceraY() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera efila = filas.primero;
        do {
            if (efila != null) {
                buffer.append("{rank=same; ");
                buffer.append("\"y").append(efila.getPosicion()).append("\";");
                NodoOrtogonal actual = efila.acceso;
                while (actual != null) {
                    IModificador dato = (IModificador) actual.getDato();
                    buffer.append("\"xy: ").append(actual.getX()).append(",").append(actual.getY()).append("\"");
                    buffer.append("[label=\"");
                    buffer.append(dato.escribirObjeto(dato, true)).append("\"];");
                    actual = actual.getDcho();
                }
                buffer.append("}\n");
            }
            efila = efila.getSiguiente();
        } while (efila != null && efila != filas.primero);
        return buffer.toString();
    }

    private String dotEnlaceX() {
        StringBuffer buffer = new StringBuffer();
        NodoOrtogonal actual = null;
        NodoCabecera ecolumna = columnas.primero;
        do {
            if (ecolumna != null) {
                if (ecolumna.getAcceso() == null) {
                    ecolumna = ecolumna.getSiguiente();
                    continue;
                }
                actual = ecolumna.getAcceso();
                while (actual != null) {
                    if (actual.getAbjo() != null) {
                        buffer.append("\"xy: ").append(actual.getX()).append(",").append(actual.getY()).append("\"");
                        buffer.append("->");
                        buffer.append("\"xy: ").append(actual.getAbjo().getX()).append(",").append(actual.getAbjo().getY()).append("\"");
                        buffer.append(";\n");
                    } else {
                        break;
                    }
                    actual = actual.getAbjo();
                }
            }
            ecolumna = ecolumna.getSiguiente();
        } while (ecolumna != null && ecolumna != columnas.primero);
        return buffer.toString();
    }

    private String dotEnlaceY() {
        StringBuffer buffer = new StringBuffer();
        NodoOrtogonal actual = null;
        NodoCabecera efila = filas.primero;
        do {
            if (efila != null) {
                if (efila.getAcceso() == null) {
                    efila = efila.getSiguiente();
                    continue;
                }
                actual = efila.getAcceso();
                while (actual != null) {
                    if (actual.getDcho() == null) {
                        break;
                    }
                    buffer.append("\"xy: ").append(actual.getX()).append(",").append(actual.getY()).append("\"");
                    if (actual.getDcho() != null) {
                        buffer.append("->");
                        buffer.append("\"xy: ").append(actual.getDcho().getX()).append(",").append(actual.getDcho().getY()).append("\"");
                        buffer.append("[constraint=false];\n");
                    }
                    actual = actual.getDcho();
                }
            }
            efila = efila.getSiguiente();
        } while (efila != null && efila != filas.primero);
        return buffer.toString();
    }

    private String enlaceCabeceraX() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera ecolumna = columnas.primero;
        buffer.append("\"Matriz\"->x").append(ecolumna.getPosicion());
        buffer.append(";\n");
        do {
            if (ecolumna != null) {
                if (ecolumna.getSiguiente() == null) {
                    break;
                }
                buffer.append("\"x").append(ecolumna.getPosicion()).append("\"");
                buffer.append("-> \"x").append(ecolumna.getSiguiente().getPosicion()).append("\"");
                buffer.append(";\n");
//                if(ecolumna.getSiguiente().getAcceso() == null)
//                    break;
            }
            ecolumna = ecolumna.getSiguiente();
        } while (ecolumna != null && ecolumna != columnas.primero);
        return buffer.toString();
    }

    private String enlaceCabeceraY() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera efila = filas.primero;
        buffer.append("\"Matriz\"-> \"y").append(efila.getPosicion()).append("\"").append("[rankdir=UD];\n");
        do {
            if (efila != null) {
                if (efila.getSiguiente() == filas.primero) {
                    break;
                }
                buffer.append("\"y").append(efila.getPosicion()).append("\"");
                buffer.append("->");
                buffer.append("\"y").append(efila.getSiguiente().getPosicion()).append("\"");
                buffer.append("[rankdir=UD];\n");
            }
            efila = efila.getSiguiente();
        } while (efila != null && efila != filas.primero);
        return buffer.toString();
    }

    private String cabeceraNodoX() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera ecolumna = columnas.primero;
        do {
            if (ecolumna != null) {
                if (ecolumna.getAcceso() == null) {
                    ecolumna = ecolumna.getSiguiente();
                    continue;
                }
                buffer.append("\"x").append(ecolumna.getPosicion()).append("\"");
                buffer.append("->").append("\"xy: ").append(ecolumna.getAcceso().getX()).append(",").append(ecolumna.getAcceso().getY()).append("\"");
                buffer.append(";\n");
            }
            ecolumna = ecolumna.getSiguiente();
        } while (ecolumna != null && ecolumna != columnas.primero);
        return buffer.toString();
    }

    private String cabeceraNodoY() {
        StringBuffer buffer = new StringBuffer();
        NodoCabecera efila = filas.primero;
        do {
            if (efila != null) {
                if (efila.getAcceso() == null) {
                    efila = efila.getSiguiente();
                    continue;
                }
                buffer.append("\"y").append(efila.getPosicion()).append("\"");
                buffer.append("->");
                buffer.append("\"xy: ").append(efila.getAcceso().getX()).append(",").append(efila.getAcceso().getY()).append("\";\n");
            }
            efila = efila.getSiguiente();
        } while (efila != null && efila != filas.primero);
        return buffer.toString();
    }           
}
