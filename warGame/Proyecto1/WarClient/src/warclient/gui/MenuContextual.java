package warclient.gui;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class MenuContextual extends JFrame {
    private JPopupMenu menu;
    
    public MenuContextual() throws HeadlessException {
        
        try {
            inicializar();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    private void inicializar() {
        setBounds(100,100,200,200);
        
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.addMouseListener(new MouseAdapter(this));
        
        menu = new JPopupMenu();
        menu.add(new JMenuItem("Mover"));
        menu.add(new JMenuItem("Atacar"));
        menu.add(new JMenuItem("Cancelar"));
    }
    
    public void this_mousePressed(MouseEvent e) {
        mostrarMenu(e);
    }

    public void this_mouseReleased(MouseEvent e) {
        mostrarMenu(e);
    }
    
    private void mostrarMenu(MouseEvent e) {
         menu.show(e.getComponent(), e.getX(), e.getY());
        
    }                        
}

class MouseAdapter extends java.awt.event.MouseAdapter {
    private MenuContextual adapter;

    public MouseAdapter() {
    }

    public MouseAdapter(MenuContextual adapter) {
        this.adapter = adapter;
    }
    
    public void mousePressed(MouseEvent e) {
//        this.adapter
    }
    
    public void mouseReleased(MouseEvent e) {
        
    }
    
}