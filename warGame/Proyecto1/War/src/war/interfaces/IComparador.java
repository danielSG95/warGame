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
public interface IComparador {  
    boolean igualQue(Object q, int type);
    boolean menorQue(Object q, int type);    
    boolean mayorQue(Object q, int type);
    boolean mayorIgualQue(Object q, int type);
}
