/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.modelo;

import war.interfaces.IComparador;
import war.interfaces.IModificador;

/**
 *
 * @author da9ni5el
 */
public class tropa implements IComparador, IModificador {
    private int id;
    private int x;
    private int y;    
    private int alcanceM;
    private int ataque;
    private int alcanceA;
    private float vida;
    private int tipo;
    private double bonus;
    public tropa() {
    }

    public tropa(int id, int x, int y, int tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.vida = 0;
        this.alcanceA = 0;
        this.alcanceM = 0;
        this.ataque = 0;
        this.bonus = 0;
    }
    
    public tropa(int id, int x, int y, float vida, int alcanceM, int ataque, int alcanceA, int tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.alcanceM = alcanceM;
        this.ataque = ataque;
        this.alcanceA = alcanceA;
        this.tipo = tipo;
        this.bonus = 0;
    }
    
    public tropa(int id) {
        this.id = id;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }
           
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public float getVida() {
        return vida;
    }

    public void setVida(float vida) {
        this.vida = vida;
    }

    public int getAlcanceM() {
        return alcanceM;
    }

    public void setAlcanceM(int alcanceM) {
        this.alcanceM = alcanceM;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getAlcanceA() {
        return alcanceA;
    }

    public void setAlcanceA(int alcanceA) {
        this.alcanceA = alcanceA;
    }

    @Override
    public boolean igualQue(Object q, int type) {
        tropa t = (tropa )q;
        return t.id == id;
    }

    @Override
    public boolean menorQue(Object q, int type) {
        tropa t = (tropa )q;
        return id < t.id;
    }

    @Override
    public boolean mayorQue(Object q, int type) {
        tropa t = (tropa )q;
        return id > t.id;
    }

    @Override
    public boolean mayorIgualQue(Object q, int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarObjeto(Object nuevoValor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String escribirObjeto(Object objeto, boolean type) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Id:").append(id).append("\\n");
        buffer.append("X:").append(x);
        buffer.append(" Y:").append(y).append("\\n");
        buffer.append("Tipo:").append(tipo);
        return buffer.toString();        
    }

    @Override
    public String devolverServer() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"id\":").append(id).append(",\n");
        buffer.append("\"tipo\":\"").append(tipo).append("\",\n");
        buffer.append("\"x\":").append(x).append(",\n");
        buffer.append("\"y\":").append(y).append(",\n");
        buffer.append("\"vida\":").append(vida).append(",\n");
        buffer.append("\"alcanceM\":").append(alcanceM).append(",\n");
        buffer.append("\"alcanceA\":").append(alcanceA).append(",\n");
        buffer.append("\"bonus\":").append(bonus).append(",\n");
        buffer.append("\"ataque\":").append(ataque).append("\n");
        return buffer.toString();
    }
    
    
    
}
