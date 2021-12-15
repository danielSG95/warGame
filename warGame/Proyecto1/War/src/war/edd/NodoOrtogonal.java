/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.edd;

import war.edd.Pila;

/**
 *
 * @author da9ni5el
 * @param <T>
 */
public class NodoOrtogonal <T>{
    protected T dato;
    protected int x;
    protected int y;
    protected NodoOrtogonal<T> dcho;
    protected NodoOrtogonal<T> izdo;
    protected NodoOrtogonal<T> abjo;
    protected NodoOrtogonal<T> arrb;
    protected Pila capas;//solo para matriz de horarios

    public NodoOrtogonal(T dato, int x, int y) {
        this.dato = dato;
        this.dcho = null;
        this.izdo = null;
        this.abjo = null;
        this.arrb = null;
        this.x = x;
        this.y = y;
        this.capas = null;
    }

    public Pila getCapas() {
        return capas;
    }

    public void setCapas(Pila capas) {
        this.capas = capas;
    }
        
    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoOrtogonal<T> getDcho() {
        return dcho;
    }

    public void setDcho(NodoOrtogonal<T> dcho) {
        this.dcho = dcho;
    }

    public NodoOrtogonal<T> getIzdo() {
        return izdo;
    }

    public void setIzdo(NodoOrtogonal<T> izdo) {
        this.izdo = izdo;
    }

    public NodoOrtogonal<T> getAbjo() {
        return abjo;
    }

    public void setAbjo(NodoOrtogonal<T> abjo) {
        this.abjo = abjo;
    }

    public NodoOrtogonal<T> getArrb() {
        return arrb;
    }

    public void setArrb(NodoOrtogonal<T> arrb) {
        this.arrb = arrb;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }     

//    public Pila getLaboratoro() {
//        return laboratoro;
//    }
//
//    public void setLaboratoro(Pila laboratoro) {
//        this.laboratoro = laboratoro;
//    }
    
}
