/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient;

import warclient.conexion.RPCClient;

/**
 *
 * @author da9ni5el
 */
public class Tropas {

    Nodo primero;
    private RPCClient rpclient;
    public Tropas() {
        primero = null;
        
    }
    
    
    public void agregarTropa(Tropa t){
        insertar(t);
    }
                
    public Tropa buscarPorCoordenada(int x, int y) {
        Nodo pivote = primero;
        while(pivote != null) {
            if(pivote.tropa.getX() == x && pivote.tropa.getY() == y)
                return pivote.tropa;
            pivote = pivote.siguiente;
        }        
        return null;
    }
    
    void insertar(Tropa t) {
        Nodo nuevo = new Nodo(t, null);
        if(primero == null) {
            primero = nuevo;            
        } else {
            nuevo.siguiente = primero;
            primero = nuevo;
        }        
    }
    
    public void reiniciarContadores() {
        Nodo pivote = primero;
        while(pivote != null) {
            pivote.tropa.setAlcanceM(restablecerContadores(pivote.tropa.getTipo()));
            pivote = pivote.siguiente;
        }                                
    }
    
    int restablecerContadores(int tipoT) {
        int salida = 0;
        switch(tipoT) {
            case 5:
                salida = 3;
                break;
            case 6:
                salida = 2;
                break;
            case 7:
                salida = 6;
                break;
            case 8:
                salida = 4;
                break;
            case 9:
                salida = 3;
                break;
            case 10:
                salida = 3;
                break;
        }        
        return salida;
    }
    
    //Escribir metodo de elminar
    
    
    public void generarReporte(int turno) {
        
        Nodo pivote = primero;
        if(pivote == null)
            return;
        StringBuffer buffer = new StringBuffer();
        
        
        buffer.append("{\n");
        buffer.append("\"typeOperation\": 20").append(",\n");
        buffer.append("\"turno\":").append(turno).append(",\n");
        buffer.append("\"Movimientos\":[").append("\n");        
        boolean flag = false;
        while(pivote != null) {
            if(pivote.tropa.getTeam() == turno) {
                //aqui se procesan solo las tropas del jugador en turno actual
                if(pivote.tropa.getMoviendo()) {
                    //la tropa actual sufrio un cambio de posicion. Debe reportarse al servidor
                    buffer.append(pivote.tropa.escribirReporte(flag));
                    flag = true;
                }
                
            }
            pivote = pivote.siguiente;
        }
        
        buffer.append("\n]");
        buffer.append("\n}");
        if(flag) {
            try {
                rpclient = new RPCClient(); 
                rpclient.actualizarServidor(buffer.toString());
            } catch (Exception e) {
                
            }
           
           
        }
        System.out.println(buffer.toString());
    }            
    
}

class Nodo {
    Tropa tropa;
    Nodo siguiente;

    public Nodo() {
        siguiente = null;
    }

    public Nodo(Tropa t, Nodo siguiente) {
        this.tropa = t;
        this.siguiente = siguiente;
    }
   
    
}
