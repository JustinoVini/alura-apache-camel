package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:pedidos?delay=5s&noop=true").
                        split(). // divide o conteudo
                            xpath("/pedido/itens/item").
                        filter().
                            xpath("/item/formato[text()='EBOOK']"). // filtra as mensagens
                        log("${id}").
                        marshal().xmljson().
                        log("${body}").
                        setHeader("CamelFileName", simple("${file:name.noext}.toJson")).
                to("file:saida");
            }
        });

        context.start(); //aqui camel realmente começa a trabalhar
        Thread.sleep(20000);
        context.stop();
    }
}
