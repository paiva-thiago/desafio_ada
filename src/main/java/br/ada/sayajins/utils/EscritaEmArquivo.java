package br.ada.sayajins.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import br.ada.sayajins.model.Pagamentos;

/**
 * @author Fillipe Couto
 */
public class EscritaEmArquivo {

    /**
     * Escreve os pagamentos processados em arquivos CSV, separados por tipo de
     * pagamento
     * 
     * @param dadosProcessados : Lista em memória com os dados processados
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void EscreverProcessamento(MemorySaveUtil dadosProcessados) throws InterruptedException {
        List<CompletableFuture<Void>> futures = new ArrayList<>(); 
        dadosProcessados.getData()
                .stream()
                .forEach(tipoPagamento -> futures.add(
                    CompletableFuture.supplyAsync(
                            (Supplier<Void>) () -> 
                            {
                                escreverProcessamentoPorTipo(
                                tipoPagamento.getKey(),
                                tipoPagamento.getValue());
                                return null;
                            }  
                        )
                    ));
        
        try
        {
            CompletableFuture.allOf
            (
            futures.toArray(new CompletableFuture[futures.size()])
            ).get();
        }
        catch (ExecutionException e)
        {
            
        }
                
    }

    private static void escreverProcessamentoPorTipo(String tipoPagamento, List<Pagamentos> pagamentos) {

        LocalDate dataProcessamento = LocalDate.now();
        String nomeArquivo = String.format(
                "PAGAMENTOS_%s_%04d-%02d-%02d.csv",
                tipoPagamento,
                dataProcessamento.getYear(),
                dataProcessamento.getMonthValue(),
                dataProcessamento.getDayOfMonth());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {

            pagamentos.stream().forEach(pagamento -> {

                String registro = String.format(
                        "%s;%s;%s",
                        pagamento.getNome(),
                        pagamento.getDtVencto(),
                        pagamento.getValor());

                try {
                    writer.append(registro);
                    writer.newLine();
                } catch (IOException e) {
                    System.out.printf(
                            "\nErro ao gravar dado \"%s\" em arquivo arquivo \"%s\": \"%s\"\n",
                            registro,
                            nomeArquivo,
                            e.getMessage());
                }

            });
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(1,10));
            System.out.println(nomeArquivo + "finalizado");

        } catch (Exception e) {
            System.out.printf("\nErro ao acessar arquivo \"%s\": \"%s\"\n", nomeArquivo, e.getMessage());
        }

    }

}
