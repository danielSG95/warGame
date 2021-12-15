/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient;

/**
 *
 * @author da9ni5el
 */
public class Tropa {
    private int id;
    private int x;
    private int y;    
    private int alcanceM;
    private int ataque;
    private int alcanceA;
    private float vida;
    private int tipo;
    private double bonus;
    private boolean moviendo;

    private int Team;
    
    public Tropa() {
    }

    public Tropa(int id, int x, int y, int alcanceM, int ataque, int alcanceA, float vida, int tipo, boolean moviendo, int Team, double bonus) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.alcanceM = alcanceM;
        this.ataque = ataque;
        this.alcanceA = alcanceA;
        this.vida = vida;
        this.tipo = tipo;
        this.Team = Team;
        this.moviendo = moviendo;
        this.bonus = bonus;
    }
    
    
    public String escribirReporte(boolean flag) {
        
        StringBuffer buffer = new StringBuffer();
        if(flag)
            buffer.append(",");
        buffer.append("{\n");
        buffer.append("\"id\":").append(id).append(",\n");
        buffer.append("\"tipo\":\"").append(tipo).append("\",\n");
        buffer.append("\"x\":").append(x).append(",\n");
        buffer.append("\"y\":").append(y).append(",\n");
        buffer.append("\"vida\":").append(vida).append(",\n");
        buffer.append("\"alcanceM\":").append(alcanceM).append(",\n");
        buffer.append("\"alcanceA\":").append(alcanceA).append(",\n");
        buffer.append("\"bonus\":").append(bonus).append(",\n");
        buffer.append("\"ataque\":").append(ataque);
        buffer.append("\n}");
        return buffer.toString();
    }
      
    public int getTeam() {
        return Team;
    }

    public void setTeam(int Team) {
        this.Team = Team;
    }
        
    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
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

    public float getVida() {
        return vida;
    }

    public void setVida(float vida) {
        this.vida = vida;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean getMoviendo() {
        return moviendo;
    }

    public void setMoviendo(boolean moviendo) {
        this.moviendo = moviendo;
    }                        
}
