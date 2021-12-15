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
public class pixel implements IModificador, IComparador{
    private int x;
    private int y;
    private int tipo;
    private String descripcion;
    private int contador;

    public pixel() {
    }

    public pixel(int x, int y, int tipo, String descripcion) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.contador = 0;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void modificarObjeto(Object nuevoValor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String escribirObjeto(Object objeto, boolean type) {
        String retorno = "";
        if(type)
        {
            retorno = descripcion;
        }
        else
        {
            switch(tipo)
            {
                case 0:
                    retorno = "\"#2e86c1\"";
                    break;
                case 1:
                    retorno = "\"#229954\"";
                    break;
                case 2:
                    retorno = "\"#145a32\"";
                    break;
                case 3:
                    retorno = "\"#919191\"";
                    break;
                case 4:
                    retorno = "\"#271400\"";
                    break;
            }
        }
        
        return retorno;
    }

    @Override
    public boolean igualQue(Object q, int type) {
        pixel p = (pixel)q;
        boolean salida;
        if(type == 0){
            salida = p.x == x && p.y == y;
        } else{
            salida = p.tipo == tipo;
        }            
        return salida;
    }

    @Override
    public boolean menorQue(Object q, int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean mayorQue(Object q, int type) {
        pixel p = (pixel)q;
        return p.tipo > tipo;
    }

    @Override
    public boolean mayorIgualQue(Object q, int type) {
        pixel p = (pixel)q;
        
        return p.tipo >= tipo;
    }

    @Override
    public String devolverServer() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"tipo\":").append(tipo).append(",\n");
        buffer.append("\"x\":").append(x).append(",\n");
        buffer.append("\"y\":").append(y);
        return buffer.toString();
    }
    
    
}
