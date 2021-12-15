/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package warclient.conexion;

/**
 *
 * @author da9ni5el
 */
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

    }

    
    public void EnviarArbol(String data) throws Exception {
        String response = call(data);
//        System.out.println("[>>] " + response);
    }
    
    public String solicitarTamTablero(String request) throws Exception {
        String response = call(request);
//        System.out.println(response);
        return response;
    }
    
    public String solicitarTextura() throws IOException, InterruptedException {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("{\n");
        buffer.append("\"typeOperation\": 3\n");
        buffer.append("}");
        
        String response = call(buffer.toString());
        return response;        
    } 
    
    public String solicitarTropas() throws IOException, InterruptedException {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("{\n");
        buffer.append("\"typeOperation\": 4\n");
        buffer.append("}");
        String response = call(buffer.toString());
        return response;
    }
    
    public String actualizarServidor(String request) throws IOException, InterruptedException {
        String response = call(request);
//        System.out.println(response);
        return response;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    private String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

//        String replyQueueName = channel.queueDeclare().getQueue();
        String replyQueueName = "amq.rabbitmq.reply-to";
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();                

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        
        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
                
        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

}
