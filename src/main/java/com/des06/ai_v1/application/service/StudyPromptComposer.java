package com.des06.ai_v1.application.service;

import com.des06.ai_v1.application.model.StudyCommand;
import com.des06.ai_v1.application.port.out.StudyAssistant;
import com.des06.ai_v1.domain.model.Book;

import java.util.List;

import static javax.management.Query.value;

public final class StudyPromptComposer {

    private final String system;

    public StudyPromptComposer(String system) {
        this.system = system;
    }

    public StudyAssistant.Request compose(StudyCommand c, Book base,
                                          List<Book> candidates) {
        String task = switch (c.task()) {
            case EXPLAIN -> """
                    Explique o trecho fornecido, identificando os conceitos.
                    Separe o que o trecho afirma da sua interpretacao.
                    Use um exemplo e finalize com duas perguntas de compreensao.
                    Nao alegue ter acesso ao livro completo.
                    """;
            case RECOMMEND -> """
                    Sugira uma sequencia de ate cinco obras do CATALOGO.
                    Cite id e titulo exatamente como fornecidos.
                    Explique a relacao com o assunto e com o livro-base.
                    Nao recomende titulos ausentes do CATALOGO.
                    """;
            case PLAN -> """
                    Monte um plano semanal usando somente obras do CATALOGO.
                    Respeite a quantidade de semanas e minutos por dia.
                    Inclua objetivo semanal e uma atividade de compreensao.
                    Nao invente capitulos ou paginas de edicoes nao fornecidas.
                    """;
        };

        var data = new StringBuilder();
        data.append("TAREFA\n").append(task);
        data.append("\nLIVRO-BASE\n").append(base.title())
                .append(" | autor: ").append(base.author());
        data.append("\nPERGUNTA\n").append(value(c.question()));
        data.append("\nTRECHO DO LEITOR\n").append(value(c.excerpt()));

        if(c.topic() != null) {
            data.append("\nASSUNTO\n").append(value(c.topic()));
        }


        if(c.weeks() != null) {
            data.append("\nSEMANAS: ").append(c.weeks());
        }

        if(c.minutesPerDay() != null) {
            data.append("\nMINUTOS POR DIA: ").append(c.minutesPerDay());
        }

        data.append("\nCATALOGO\n");
        for (Book b : candidates) {
            data.append("id=").append(b.id()).append(" | titulo=")
                    .append(b.title()).append(" | autor=").append(b.author())

                    .append(" | assuntos=").append(b.topics()).append("\n");
        }
        return new StudyAssistant.Request(system, data.toString());

    }

}