package com.aluracursos.ecomart.Controller;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorizador")
public class CategorizadorDeProductosController {
    private final ChatClient chatClient;

    public CategorizadorDeProductosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptionsBuilder
                        .builder()
                        .withModel("gpt-4o")
                        .build())
                .build();
    }

    @GetMapping
    public String categorizarProductos(String producto){
        var system = """
                Actúa como un categorizador de productos y debes responder solo el nombre de la categoría del producto informado
                                
                Escoge una categoría de la siguiente lista:
                                
                1. Higiene Personal
                2. Electrónicos
                3. Deportes
                4 Otros
                                
                Ejemplo de uso:
                                
                Producto: Pelota de fútbol
                Respuesta: Deportes
                """;
        var tokens = contadorDeTokens(system,producto);
        System.out.println(tokens);

        //implementación de la lógica para la selección del modelo

        return this.chatClient.prompt()
                .system(system)
                .user(producto)
                .options(ChatOptionsBuilder.builder()
                        .withTemperature(0.82f).build())
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }

    private int contadorDeTokens(String system, String user){
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
        return enc.countTokens(system + user);
    }

}


