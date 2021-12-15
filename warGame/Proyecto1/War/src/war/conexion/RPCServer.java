/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package war.conexion;

/**
 *
 * @author da9ni5el
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import war.edd.ABB;
import war.edd.ABBNodo;
import war.edd.MatrizOrtogonal;
import war.gui.Log;
import war.interfaces.Observador;
import war.modelo.pixel;
import war.modelo.tropa;

public class RPCServer implements Observador {

    ABB<tropa> jugador1;
    ABB<tropa> jugador2;
    MatrizOrtogonal<pixel> unificada;
    Log log;
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public RPCServer() {
        jugador1 = null;
        jugador2 = null;
        unificada = null;
    }

    public void setArboles(ABB j1, ABB j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;
    }
    
    public void setLog(Log l) {
        this.log = l;
    }

    public void listener() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.queuePurge(RPC_QUEUE_NAME);

            channel.basicQos(1);

            log.notificar("Esperando por peticiones");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";

                try {
                    String message = new String(delivery.getBody(), "UTF-8");
//                    int n = Integer.parseInt(message);
                    log.notificar("Peticion Entrante...");
                    System.out.println(" [>>]" + message);
                    response = decodeType(message);

                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
            }));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String decodeType(String request) {
        StringBuffer response = new StringBuffer();

        JsonObject jo = (JsonObject) new JsonParser().parse(request);
        int typeOperation = jo.get("typeOperation").getAsInt();
        switch (typeOperation) {
            case 2: //Envia el tamanio del tablero al cliente
                log.notificar("Id peticion: " + typeOperation + " [Tamanio Tablero]" );
                response.append(enviarTamTablero());
                return response.toString();
            case 3:// Solicitar Texturas
                log.notificar("Id peticion: " + typeOperation + " [Solicita Texturas]" );
                response.append(solicitarTextura());
                return response.toString();
            case 4:
                log.notificar("Id peticion: " + typeOperation + " [Solicita Tropas]" );
                response.append(solicitarTropas());
                return response.toString();
            case 11: //Recibe las tropas del jugador 1
                log.notificar("Id peticion: " + typeOperation + " [Carga Tropas Jugador 1]" );
                jugador1 = crearArbol(jugador1, jo.get("Tropas").getAsJsonArray());
                if (jugador1.raizArbol() != null) {
                    response.append("{\n").append("\t\"status\": 200").append("\n}");
                } else {
                    response.append("{\n").append("\t\"status\": 501").append("\n}");
                }
                return response.toString();
            case 12: //Recibe las tropas del jugador 2
                log.notificar("Id peticion: " + typeOperation + " [Carga Tropas Jugador 2]" );
                jo.get("Tropas").getAsJsonArray();
                jugador2 = crearArbol(jugador2, jo.get("Tropas").getAsJsonArray());
                if (jugador2.raizArbol() != null) {
                    response.append("{\n").append("\t\"status\": 200").append("\n}");
                } else {
                    response.append("{\n").append("\t\"status\": 501").append("\n}");
                }
                return response.toString();
            case 20: //
                log.notificar("Id peticion: " + typeOperation + " [Actualizando informacion del Servidor]" );
                response.append(updateServer(jo.get("turno").getAsInt(), jo));
                return response.toString();
        }
        return null;
    }

    private String updateServer(int turno, JsonObject jo) {
        StringBuffer buffer = new StringBuffer();
        boolean mov = jo.get("Movimientos").getAsJsonArray().isJsonNull();
        if(!mov) {
            JsonArray ja = jo.get("Movimientos").getAsJsonArray();
            if(turno == 1) {
                verficarDatos(jugador1, ja);
            } else {
                verficarDatos(jugador2, ja);
            }            
        }
        buffer.append("{\n");
        buffer.append("\"status\":200");
        buffer.append("\n}");
        return buffer.toString();
    }
    
    void verficarDatos(ABB arbol, JsonArray ja) {
        for (JsonElement jsonElement : ja) {
            JsonObject mov = jsonElement.getAsJsonObject();
            int id = mov.get("id").getAsInt();
            int x = mov.get("x").getAsInt();
            int y = mov.get("y").getAsInt();
            
            ABBNodo buscado = arbol.buscar(new tropa(id));
            if(buscado != null) {
                tropa t = (tropa)buscado.getDato();
                if(t.getX() != x || t.getY() != y) {
                    String mensaje = "Se movio la tropa con id: " + id + " De la posicion: ";
                    mensaje += t.getX() + "," + t.getY() + " A la posicion: " + x + "," + y;
                    t.setX(x);
                    t.setY(y);
                    
                    log.notificar(mensaje);
                }                
            }
        }                
    }
    
    
    private ABB crearArbol(ABB arbol, JsonArray tropas) {
        for (JsonElement tropa : tropas) {
            JsonObject t = tropa.getAsJsonObject();
            int ataque = 0;
            int alcanceA = 0;
            int alcanceM = 0;
            float vida = 0;

            if (t.get("x").getAsInt() > unificada.MaxX() && t.get("y").getAsInt() > unificada.MaxY()) {
                System.out.println("No se inserta en el arbol");
                continue;
            }

            switch (t.get("tipo").getAsString().toLowerCase()) {
                case "infanteria":
                    alcanceM = 3;
                    vida = 50;
                    ataque = 30;
                    alcanceA = 1;
                    break;
                case "infanteriam":
                    alcanceM = 2;
                    vida = 50;
                    ataque = 50;
                    alcanceA = 3;
                    break;
                case "reconocimiento":
                    alcanceM = 6;
                    vida = 100;
                    ataque = 50;
                    alcanceA = 2;
                    break;
                case "tanque":
                    alcanceM = 4;
                    vida = 150;
                    ataque = 80;
                    alcanceA = 2;
                    break;
                case "mtanque":
                    alcanceM = 3;
                    vida = 200;
                    ataque = 100;
                    alcanceA = 2;
                    break;
                case "artilleria":
                    alcanceM = 3;
                    vida = 50;
                    ataque = 150;
                    alcanceA = 6;
                    break;
            }
            
            pixel p = unificada.buscar(t.get("x").getAsInt(), t.get("y").getAsInt());
            int contador = p.getContador();
            p.setContador(contador++);
            
            
            if(p != null) {
                int tipo = p.getTipo();//tipo de textura en esa posicion
                
                tropa trp = new tropa(t.get("id").getAsInt(), t.get("x").getAsInt(), t.get("y").getAsInt(),
                    vida, alcanceM, ataque, alcanceA, transformTipoTropa(t.get("tipo").getAsString()));
                 
                double bonus = otorgarBonus(tipo, trp.getTipo());
                trp.setBonus(bonus);
                
                arbol.insertar(trp);
            }                                              
        }
        return arbol;
    }
    
    private double otorgarBonus(int tipoTextura, int tipoTropa) {
        
        double salida = 0.0;
        switch(tipoTextura) {
            case 0:
                salida = -5;
                break;
            case 1:
                salida = 10;
                break;
            case 2:
                salida = -10;
                break;
            case 3:
                salida = 0;
                break;
            case 4:
                salida = 25;
                break;
        }
        return salida;
    }

    private String enviarTamTablero() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\n");

        if (this.unificada != null) {
            int columnas = unificada.MaxX();
            int filas = unificada.MaxY();
            buffer.append("\"status\": 200,\n");//response
            buffer.append("\"tamColumnas\":").append(columnas).append(",\n");
            /*Body Response*/
            buffer.append("\"tamFilas\":").append(filas).append("\n");
            /*Body Response*/

            System.out.println("Calculado tamanio de la matriz");
        } else {
            buffer.append("\"status\": 501\n");
            System.out.println("Matriz vacia, imposible calcular tamanio");
        }

        buffer.append("\n}");

        return buffer.toString();
    }

    private String solicitarTextura() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\n");
        if (unificada == null) {

            buffer.append("\"status\": 501\n");
        }

        String response = "";

        if (!(response = unificada.enviarTexturas()).equals(null)) {
            buffer.append("\"status\": 200,\n");
            buffer.append("\"pixeles\": [\n");
            buffer.append(response);
            buffer.append("\n]");
        } else {
            buffer.append("\"status\": 501\n");
        }
        buffer.append("\n}");
//        System.out.println(buffer.toString());
        return buffer.toString();
    }

    private String solicitarTropas() {
        StringBuffer buffer = new StringBuffer();
        String j1 = jugador1.obtenerTropas();
        String j2 = jugador2.obtenerTropas();
        buffer.append("{\n");
        if (!j1.equals(null) && !j2.equals(null)) {
            buffer.append("\"status\": 200,\n");
            buffer.append("\"jugador1\": [\n ");
            buffer.append(j1);
            buffer.append("\n],\n");

            buffer.append("\"jugador2\": [\n");
            buffer.append(j2);
            buffer.append("\n]");
        } else {
            buffer.append("\"status\": 501");
        }

        buffer.append("\n}");

        System.out.println(buffer.toString());
        return buffer.toString();
    }
    
    
    
    private int transformTipoTropa(String tipo) {
        int thistipo = 0;
        switch (tipo) {
            case "infanteria":
                thistipo = 5;
                break;
            case "infanteriam":
                thistipo = 6;
                break;
            case "reconocimiento":
                thistipo = 7;
                break;
            case "tanque":
                thistipo = 8;
                break;
            case "mtanque":
                thistipo = 9;
                break;
            case "artilleria":
                thistipo = 10;
                break;
        }
        return thistipo;
    }

    @Override
    public void update(Object q, boolean flag) {
        if (!flag) {
            MatrizOrtogonal m = (MatrizOrtogonal) q;
            this.unificada = m;            
        }

    }

}
