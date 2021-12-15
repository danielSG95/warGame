/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import warclient.Interfaces.Observador;
import warclient.Tropa;
import warclient.Tropas;

/**
 *
 * @author da9ni5el
 */
public class Matriz extends JFrame {

    private String pathImageTropa = "/img/tropas/";
    JButton matriz[][];
    JPanel ancestor;

    private boolean esLlena;

    private int tamButtonX;
    private int tamButtonY;
    private int longX;
    private int longY;

    private Observador ventana;
    private JButton preSelected;
    private int turno;
    private Log log;
    boolean JUEGOINICIADO;
    boolean CARGACORRECTA;    
    Tropas tropas;
    boolean primeroTurno;
    public Matriz() throws HeadlessException {
        tamButtonX = 0;
        tamButtonY = 0;
        esLlena = false;
        log = new Log();
        tropas = new Tropas();
    }

    public Matriz(JPanel padre, Observador v) {
        this.ancestor = padre;
        this.tamButtonX = 0;
        this.tamButtonY = 0;
        log = new Log();
        this.ventana = v;
        log.registrar(ventana);
        turno = 1;
        JUEGOINICIADO = false;
        tropas = new Tropas();
        primeroTurno = true;
    }

    public void setTurno(int turno) {
        tropas.generarReporte(this.turno);
        this.turno = turno;        
        if(!primeroTurno){            
            tropas.reiniciarContadores();            
        }        
        primeroTurno = false;        
    }

    public void setJUEGOINICIADO(boolean JUEGOINICIADO) {
        this.JUEGOINICIADO = JUEGOINICIADO;
    }

    public boolean getCARGACORRECTA() {
        return CARGACORRECTA;
    }

    public JPanel generarMatriz(int x, int y) {
        return crearMatriz(x, y);
    }

    private JPanel crearMatriz(int x, int y) {
        if (esLlena) {
            return ancestor;
        }

        longX = x;
        longY = y;
        matriz = new JButton[y + 1][x + 1];

        GridLayout g = new GridLayout(longY, longX);
        g.preferredLayoutSize(ancestor);
        ancestor.setLayout(g);
        calcularTamButton(x, y);

        int i, j;
        for (i = 1; i <= y; i++) {
            for (j = 1; j <= x; j++) {
                JButton btn = new JButton();
                btn.setSize(tamButtonX, tamButtonY);
                btn.setToolTipText(Integer.toString(i) + "," + Integer.toString(j));
                matriz[i][j] = btn;
                matriz[i][j].putClientProperty("x", i);
                matriz[i][j].putClientProperty("y", j);
                matriz[i][j].putClientProperty("tipoT", -1);
                matriz[i][j].putClientProperty("tipoTp", -1);
                matriz[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        int boton = me.getButton();
                        if (JUEGOINICIADO) {
                            preMovimiento((JButton) me.getSource(), boton);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent me) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                    }

                });

                ancestor.add(matriz[i][j]);
                repaint_();
            }
        }
        esLlena = true;
        return ancestor;
    }

    void preMovimiento(JButton btn, int typeBtn) {
        /**
         * PROPIEDADES DEL BOTON id, tipo
         */
        //1 boton primario 3 boton secundario
        Point posactual = new Point();
        Point posant;
        Tropa buscado = null;
        int tipoelemento = -1;
        int tipoTropa = -1;
        try {
            posactual.x = (int) btn.getClientProperty("x");
            posactual.y = (int) btn.getClientProperty("y");

        } catch (Exception e) {
            System.out.println("prueba: " + e.toString());
        }

        if (typeBtn == 1) {
            buscado = tropas.buscarPorCoordenada(posactual.y, posactual.x);
            if (buscado != null) {
                preSelected = (buscado.getTeam() == turno) ? btn : null;
            }
        } else if (typeBtn == 3) {
            if (preSelected != null && preSelected != btn) {
                posant = new Point();
                posant.x = (int) preSelected.getClientProperty("x");
                posant.y = (int) preSelected.getClientProperty("y");

                buscado = tropas.buscarPorCoordenada(posant.y, posant.x);
                if (buscado != null) {
                    tipoelemento = (int) btn.getClientProperty("tipoT");
                    tipoTropa = (int) btn.getClientProperty("tipoTp");
                    validarMovimiento(posant, posactual, buscado, tipoelemento, tipoTropa);
                }

            }
        }
    }

    private void validarMovimiento(Point posant, Point posactual, Tropa tropa, int tipoelemento, int tipotropa) {
        if (tipotropa >= 5) {
            log.notificar("No se puede ejecutar ese movimiento. Tropa enemiga/alida ocupa esa casilla");
            return;
        }
        
        
        boolean distancia = validarDistancia((int) posactual.distance(posant), tropa.getTipo());
        
        boolean elemento = false;
        if (distancia) {
            elemento = validarElemento(tropa.getTipo(), tipoelemento);
            if (elemento) {                
                double distanciaN = posactual.distance(posant);
                int movTotal = tropa.getAlcanceM();
                if(movTotal<1) {
                    log.notificar("La tropa seleccionada llego al limite de movimientos posibles");
                    return;
                }
                
                
                Movimiento movimiento = new Movimiento(matriz, posant, posactual);
                movimiento.setCompoenentes(tamButtonX, tamButtonY, devolverPathImage(tropa.getTipo()));

                Thread mover = new Thread(movimiento);
                mover.start();

                //se actualiza el nuevo valor de la tropa
                tropa.setX(posactual.y);
                tropa.setY(posactual.x);
                double resultado = movTotal - distanciaN;
                tropa.setAlcanceM((int)resultado);
                tropa.setMoviendo(true);
                cambiarEstadoTextura(posant, tropa);
            } else {
                log.notificar("La tropa especificada, no es capaz de moverse sobre ese terreno X(");
            }
        } else {
            log.notificar("La tropa seleccionada no es capaz de moverse esa distancia");
        }

    }

    private void cambiarEstadoTextura(Point txt, Tropa t) {
        try {

            if (txt.x != t.getX() || txt.y != t.getY()) {
                matriz[txt.x][txt.y].putClientProperty("tipoTp", -1);
            }

            matriz[t.getY()][t.getX()].putClientProperty("tipoTp", t.getTipo());

        } catch (Exception e) {
        }
    }

    private boolean validarDistancia(int dist, int tipo) {
        boolean salida = false;
        switch (tipo) {
            case 5:
                salida = (dist <= 3) ? true : false;
                break;
            case 6:
                salida = (dist <= 2) ? true : false;
                break;
            case 7:
                salida = (dist <= 6) ? true : false;
                break;
            case 8:
                salida = (dist <= 4) ? true : false;
                break;
            case 9:
                salida = (dist <= 3) ? true : false;
                break;
            case 10:
                salida = (dist <= 3) ? true : false;
                break;
        }

        return salida;
    }

    private boolean validarElemento(int tipotropa, int tipoelemento) {
        boolean salida = false;
        switch (tipotropa) {
            case 6: //infanteria mecanizada
                salida = (tipoelemento == 0) ? false : true;
                break;
            case 7: //reconocimiento
                salida = (tipoelemento == 0) ? false : true;
                salida = (tipoelemento == 4) ? false : true;
                break;
            case 8: //tanque
                salida = (tipoelemento == 1) ? true : false;
                salida = (tipoelemento == 3) ? true : false;
                break;
            case 9: //mega tanque
                salida = (tipoelemento == 3) ? true : false;
                break;
            case 10: //artilleria
                salida = (tipoelemento == 3) ? true : false;
                break;
            default: //infanteria
                salida = true;
        }
        return salida;
    }

    private void repaint_() {
        ancestor.validate();
        ancestor.repaint();
    }

    private void calcularTamButton(int cantx, int canty) {
        tamButtonX = (int) (ancestor.getSize().getWidth() / cantx + 1);
        tamButtonY = (int) (ancestor.getSize().getHeight() / canty + 1);
    }

    //En este momento el tablero debe existir.
    public void cargarTexturas(String json) {
        int x, y, tipo;
        JsonObject jo = (JsonObject) new JsonParser().parse(json);
        if (jo.get("status").getAsInt() == 200) {
            JsonArray ja = jo.get("pixeles").getAsJsonArray();
            for (JsonElement jsonElement : ja) {
                if (jsonElement.isJsonObject()) {
                    JsonObject pixel = jsonElement.getAsJsonObject();

                    x = pixel.get("x").getAsInt();
                    y = pixel.get("y").getAsInt();
                    tipo = pixel.get("tipo").getAsInt();
                    if (x <= longX && y <= longY) {
                        matriz[y][x].setBackground(seleccionarColor(tipo));
                        matriz[y][x].putClientProperty("tipoT", tipo);
                    } else {
                        System.out.println("Esta posicion esta fuera del arreglo: " + x + "," + y + " Tipo: " + tipo);
                    }
                }
            }
            CARGACORRECTA = true;
            log.notificar("Se han cargado las texturas del mapa correctamente");
        } else {
            CARGACORRECTA = false;
            log.notificar("No se cargaron las texturas");
        }
    }

    void cargarTropas(String json) {
        CARGACORRECTA = true;
        JsonObject jo = (JsonObject) new JsonParser().parse(json);
        if (jo.get("status").getAsInt() == 200) {

            //Procesando jugador 1
            procesarTropas(jo.get("jugador1").getAsJsonArray(), 1);

            //procesandojugdor 2
            procesarTropas(jo.get("jugador2").getAsJsonArray(), 2);

            log.notificar("Se han cargado las Tropas de ambos jugadores al mapa");
        } else {
            log.notificar("no se cargaron las tropas");
            CARGACORRECTA = false;
        }
    }

    private void procesarTropas(JsonArray ja, int team) {
        if (ja.isJsonNull()) {
            CARGACORRECTA = false;
            return;
        }
        for (JsonElement jsonElement : ja) {
            if (jsonElement.isJsonObject()) {
                JsonObject tropa = jsonElement.getAsJsonObject();

                int tipo = tropa.get("tipo").getAsInt();
                int x = tropa.get("x").getAsInt();
                int y = tropa.get("y").getAsInt();
                if (x <= longX && y <= longY) {
                    try {
                        Image img = ImageIO.read(getClass().getResource(devolverPathImage(tipo)));
                        ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(tamButtonX - 10, tamButtonY - 10, java.awt.Image.SCALE_SMOOTH));
                        matriz[y][x].setIcon(iconoEscala);
                        matriz[y][x].putClientProperty("tipoTp", tipo);

                        int id = tropa.get("id").getAsInt();
                        int alcancea = tropa.get("alcanceA").getAsInt();
                        int alcancem = tropa.get("alcanceM").getAsInt();
                        int bonus = tropa.get("bonus").getAsInt();
                        float vida = tropa.get("vida").getAsFloat();
                        int ataque = tropa.get("ataque").getAsInt();

                        Tropa nuevo = new Tropa(id, x, y, alcancem, ataque, alcancea, vida, tipo, false, team, bonus);
                        tropas.agregarTropa(nuevo);
                    } catch (Exception ex) {
                        CARGACORRECTA = false;
                        System.out.println("Cargando Tropas: " + ex);
                        return;
                    }
                }
            }
            CARGACORRECTA = true;
        }
    }

    private String devolverPathImage(int tipo) {
        String path = "";
        switch (tipo) {
            case 5:
                path = pathImageTropa + "infanteria.png";
                break;
            case 6:
                path = pathImageTropa + "infanteria_mecanizada.png";
                break;
            case 7:
                path = pathImageTropa + "reconocimiento.png";
                break;
            case 8:
                path = pathImageTropa + "tanque.png";
                break;
            case 9:
                path = pathImageTropa + "mega_tanque.png";
                break;
            case 10:
                path = pathImageTropa + "artilleria.png";
                break;
        }
        return path;
    }

    private Color seleccionarColor(int tipo) {
        Color color = null;

        switch (tipo) {
            case 0:
                color = new Color(46, 134, 193);
                break;
            case 1:
                color = new Color(34, 153, 84);
                break;
            case 2:
                color = new Color(20, 90, 50);
                break;
            case 3:
                color = new Color(145, 145, 145);
                break;
            case 4:
                color = new Color(39, 20, 0);
                break;
        }

        return color;
    }

    protected boolean getEsllena() {
        return this.esLlena;
    }

}

class Movimiento extends Thread {

    JButton matriz[][];
    Point A;
    Point B;
    int xtam;
    int ytam;
    String path;

    int xfinal;
    int yfinal;

    public Movimiento(JButton matriz[][], Point A, Point B) {
        this.matriz = matriz;
        this.A = A;
        this.B = B;
    }

    public void setCompoenentes(int xtam, int ytam, String path) {
        this.xtam = xtam;
        this.ytam = ytam;
        this.path = path;
    }

    @Override
    public void run() {
        calcularMovimiento(A.y, A.x, B.y, B.x);
    }

    private void calcularMovimiento(int x1, int y1, int x2, int y2) {
        int xr = x2 - x1;
        int yr = y2 - y1;
        try {
            if (xr == 0) {//movimiento lineal en y

                if (yr > 0) {//se desplaza hacia abajo
                    movimientoLinealY(y1, y2, x1);
                } else { //se desplaza hacia arriba
                    movimientoLinealInvY(y1, y2, x1);
                }
            } else if (yr == 0) { //movimiento linea en x

                if (xr > 0) {//se desplaza a la derecha
                    movimientoLinealX(x1, x2, y1);
                } else { //se desplaza a la izquierda
                    movimientoLinealInvX(x1, x2, y1);
                }
            } else {
                //ninguno es igual que cero
                if (yr > 0) {
                    movimientoEscuadra(x1, y1, x2, y2, xr);
                } else {
                    movimientoEscuadraInv(x1, y1, x2, y2, xr);
                }
            }
        } catch (Exception e) {
            System.out.println("Movimiento Error: " + e.toString());
        }
    }

    private void movimientoEscuadra(int xi, int yi, int xf, int yf, int xr) throws IOException, InterruptedException {
        int i;
        for (i = yi; i <= yf; i++) {
            Image img = ImageIO.read(getClass().getResource(path));
            ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
            matriz[i][xi].setIcon(iconoEscala);
            Thread.sleep(100);
            matriz[i][xi].setIcon(null);
        }
        yfinal = i - 1;
        if (xr > 0) {
            for (i = xi; i <= xf; i++) {
                Image img = ImageIO.read(getClass().getResource(path));
                ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
                matriz[yfinal][i].setIcon(iconoEscala);
                Thread.sleep(100);
                if (i != xf) {
                    matriz[yfinal][i].setIcon(null);
                }
            }            
        } else {
            for (i = xi; i >= xf; i--) {
                Image img = ImageIO.read(getClass().getResource(path));
                ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
                matriz[yfinal][i].setIcon(iconoEscala);
                Thread.sleep(100);
                if (i != xf) {
                    matriz[yfinal][i].setIcon(null);
                }
            }            
        }
    }
    
    private void movimientoEscuadraInv(int xi, int yi, int xf, int yf, int xr) throws IOException, InterruptedException {
        int i;
        for (i = yi; i >= yf; i--) {
            Image img = ImageIO.read(getClass().getResource(path));
            ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
            matriz[i][xi].setIcon(iconoEscala);
            Thread.sleep(100);
            matriz[i][xi].setIcon(null);
        }
        yfinal = i + 1;
        if (xr > 0) {
            for (i = xi; i <= xf; i++) {
                Image img = ImageIO.read(getClass().getResource(path));
                ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
                matriz[yfinal][i].setIcon(iconoEscala);
                Thread.sleep(100);
                if (i != xf) {
                    matriz[yfinal][i].setIcon(null);
                }
            }            
        } else {
            for (i = xi; i >= xf; i--) {
                Image img = ImageIO.read(getClass().getResource(path));
                ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
                matriz[yfinal][i].setIcon(iconoEscala);
                Thread.sleep(100);
                if (i != xf) {
                    matriz[yfinal][i].setIcon(null);
                }
            }            
        }
    }

    private void movimientoLinealX(int xi, int xf, int y) throws IOException, InterruptedException {
        System.out.println("mov lineal x");
        int i;
        for (i = xi; i <= xf; i++) {
            Image img = ImageIO.read(getClass().getResource(path));
            ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
            matriz[y][i].setIcon(iconoEscala);
            Thread.sleep(100);
            if (i != xf) {
                matriz[y][i].setIcon(null);
            }
        }
        xfinal = i;
    }

    private void movimientoLinealInvX(int xi, int xf, int y) throws IOException, InterruptedException {
        int i;
        for (i = xi; i >= xf; i--) {
            Image img = ImageIO.read(getClass().getResource(path));
            ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
            matriz[y][i].setIcon(iconoEscala);
            Thread.sleep(100);
            if (i != xf) {
                matriz[y][i].setIcon(null);
            }
        }
        xfinal = i;
    }

    private void movimientoLinealY(int yi, int yf, int x) throws IOException, InterruptedException {
        int i;
        for (i = yi; i <= yf; i++) {
            Image img = ImageIO.read(getClass().getResource(path));
            ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
            matriz[i][x].setIcon(iconoEscala);
            Thread.sleep(100);
            if (i != yf) {
                matriz[i][x].setIcon(null);
            }
        }
        yfinal = i;
    }

    private void movimientoLinealInvY(int yi, int yf, int x) throws IOException, InterruptedException {
        int i;
        for (i = yi; i >= yf; i--) {
            Image img = ImageIO.read(getClass().getResource(path));
            ImageIcon iconoEscala = new ImageIcon(img.getScaledInstance(xtam - 10, ytam - 10, java.awt.Image.SCALE_SMOOTH));
            matriz[i][x].setIcon(iconoEscala);
            Thread.sleep(100);
            if (i != yf) {
                matriz[i][x].setIcon(null);
            }
        }
        yfinal = i;
    }

}
