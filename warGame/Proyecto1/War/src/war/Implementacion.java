/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import war.edd.MatrizOrtogonal;
import war.edd.ListaCircular;
import war.edd.NodoCircular;
import war.gui.Log;
import war.interfaces.Observable;
import war.interfaces.Observador;
import war.modelo.pixel;

/**
 *
 * @author da9ni5el
 */
public class Implementacion implements Observable {

    private final int CANT = 2;
    private MatrizOrtogonal<pixel> matrizIntegrada;
    private Log log;
    private ListaCircular<MatrizOrtogonal> lista;

    Observador observador[];

    public Implementacion() {
        lista = new ListaCircular<>();
        matrizIntegrada = new MatrizOrtogonal<>();
        observador = new Observador[CANT];
        log = new Log();
    }

    public void setObservador(Observador o){
        log.suscribir(o);
    }
    
    public Implementacion(MatrizOrtogonal m) {
        this.matrizIntegrada = m;
        lista = new ListaCircular<>();
    }

    public void leerCapas(String path, int capa) throws FileNotFoundException, IOException {
        log.notificar("cargando archivo");
        leerCapasP(path, capa);
    }

    private void leerCapasP(String path, int capa) throws FileNotFoundException, IOException {
        if (lista.buscar(capa) != null)//De esta manera aseguramos que no se cargen dos capas iguales al sistema
        {
            log.notificar("Ya se ha cargado un archivo con esta capa. Imposible procesar");
            return;
        }
        log.notificar("procesando");
        MatrizOrtogonal<pixel> matriz = new MatrizOrtogonal<>();
        String cadena;
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        while ((cadena = br.readLine()) != null) {
            if (!validarCapa(cadena, capa)) {
                log.notificar("Capa invalida");
                return;
            }
            cadena = cadena.replace(';', ',');
            String a[] = cadena.split(",");

            for (int i = 0; i < a.length; i++) {
                a[i] = a[i].replace(" ", "");
            }
            try {
                int x = Integer.parseInt(a[0]);
                int y = Integer.parseInt(a[1]);
                pixel p = new pixel(x, y, capa, a[2]); //Genero el pixel
                matriz.insertar(p, p.getX(), p.getY());              //Inserto en la matriz
            } catch (Exception e) {
                System.out.println("leyendo archivo fail: " + e.toString());
            }
        }
        br.close();
        
        lista.insertar(matriz, capa);
        generarUnificada();
        log.notificar("Archivo Capa"+ capa + " cargado exito");
    }

    private boolean validarCapa(String capa, int capa_) {
        boolean salida = false;
        if (capa.contains("agua") && capa_ == 0) {
            salida = true;
        } else if (capa.contains("grama") && capa_ == 1) {
            salida = true;
        } else if (capa.contains("arbol") && capa_ == 2) {
            salida = true;
        } else if (capa.contains("carretera") && capa_ == 3) {
            salida = true;
        } else if (capa.contains("montania") && capa_ == 4) {
            salida = true;
        }
        return salida;
    }

    private void generarUnificada() {
        NodoCircular pivote = lista.getPrimero();
        do {
            if (pivote != null) {
                MatrizOrtogonal temp = (MatrizOrtogonal) pivote.getDato();
                matrizIntegrada = temp.generarUnificada(matrizIntegrada);
                pivote = pivote.getSiguiente();
            }

        } while (pivote != lista.getPrimero());
        System.out.println("Generar Unificada terminado");

        notificar(matrizIntegrada);
    }

    public boolean eliminarCapa(int tipo) {
        log.notificar("capa a eliminar: " + tipo);
        if (lista.eliminar(tipo)) {
            pixel p = new pixel();
            p.setTipo(tipo);
            matrizIntegrada.eliminarPorCoincidenciaP(p);
            //escribir aqui en la consola de logs
            generarUnificada();
            return true;
        }
        System.out.println("No se elimino de la lista circular");
        return false;
    }

    public void generarReporteLogico(int capa) {
        log.notificar("Generando Reporte Logico");
        if (capa == 5) { //El repote unificado
            matrizIntegrada.graficar("Reporte_Logico_Unificado");
            return;
        }

        NodoCircular buscado = lista.buscar(capa);
        if (buscado != null) {
            MatrizOrtogonal temporal = (MatrizOrtogonal) buscado.getDato();
            if (temporal.graficar("Reporte_Logico_Capa")) {
                System.out.println("Reporte logico de capa generado");
            }
        }
    }

    public void generarReporteGrafico(int capa) {
        log.notificar("Generando Reporte Funcional");
        if (capa == 5) { //El repote unificado
            matrizIntegrada.graficarPixles("Reporte_Grafico_Unificado");
            return;
        }
        NodoCircular buscado = lista.buscar(capa);
        if (buscado != null) {
            MatrizOrtogonal temporal = (MatrizOrtogonal) buscado.getDato();
            if (temporal.graficarPixles("Reporte_Grafico_Capa")) {
                System.out.println("Reporte grafico de capa generado");
            }
        }
    }
    
    public void generarReporteListaCircular(){
        log.notificar("Generando reporte listea Circular");
        lista.graficar("ListaCircular");        
    }

    public void enlazarObservador(Observador o) {
        for (int i = 0; i < CANT; i++) {
            observador[i] = o;
        }
    }

    @Override
    public void notificar(Object q) {
        MatrizOrtogonal m = (MatrizOrtogonal) q;
        for (int i = 0; i < CANT; i++) {
            observador[i].update(m, false);
        }
    }

}
