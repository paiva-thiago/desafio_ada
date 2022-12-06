package br.ada.sayajins.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
     */
    public static void EscreverProcessamento(MemorySaveUtil dadosProcessados) {

        dadosProcessados.getData()
                .stream()
                .forEach(tipoPagamento -> escreverProcessamentoPorTipo(
                        tipoPagamento.getKey(),
                        tipoPagamento.getValue()));

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

        } catch (Exception e) {
            System.out.printf("\nErro ao acessar arquivo \"%s\": \"%s\"\n", nomeArquivo, e.getMessage());
        }

    }

}
