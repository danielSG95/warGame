/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.interfaces;

/**
 *
 * @author da9ni5el
 */
public interface IModificador {  
    void modificarObjeto(Object nuevoValor);
    //se utiliza para escribir en el dot el objeto
    String escribirObjeto(Object objeto, boolean type);
    
    String devolverServer();
    //implementar para el nombre.dot y nombre.png
}
