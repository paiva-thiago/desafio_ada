package br.ada.sayajins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.ada.sayajins.model.Pagamentos;
import br.ada.sayajins.model.TipoPagamentoEnum;
import br.ada.sayajins.utils.EscritaEmArquivo;
import br.ada.sayajins.utils.MemorySaveUtil;
import br.ada.sayajins.utils.VerificaValidadePagamentoUtil;

public class Main {

    private static BigDecimal operationCosts = new BigDecimal("10");   
    
    
    public static void main(String[] args) {
        System.out.println("Begining main");

        MemorySaveUtil memory = MemorySaveUtil.getInstance();

        String file = "src/main/resources/pagamentos.csv";
        List<String> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((br.readLine()) != null) {
                content.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Pagamentos> listaPagamentos = new ArrayList<>();

        for (int i = 0; i < content.size(); i++) {

            String[] arrOfStr = content.get(i).split(";");

            DateTimeFormatter JEFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate data = LocalDate.parse(arrOfStr[2], JEFormatter);
            TipoPagamentoEnum tipo = TipoPagamentoEnum.valueOf(arrOfStr[1]);
            double valor = Double.parseDouble(arrOfStr[3]);
            String nome = arrOfStr[0];
            listaPagamentos.add(new Pagamentos(nome, data, valor, tipo));

        }



        Consumer<Pagamentos> addOperationCosts = (p) -> 
        {
            //Checks if still late, by a couple days, if so add operation costs
            if (VerificaValidadePagamentoUtil.pagamentoEstaAtrasado(p))
            {
                p.setValor(p.getValor().add(operationCosts));
                //Sets payment day to today
                p.setDtVencto(LocalDate.now());
            }
        };

        Function<Pagamentos, Pagamentos> addTax = p ->
        {

            long monthsLate = VerificaValidadePagamentoUtil.calculoDeMesesDeAtraso(p);
            if (monthsLate>0)
            {
                VerificaValidadePagamentoUtil.calculaAcrescimo(p);
                //makes payment month this month
                p.setDtVencto(p.getDtVencto().plusMonths(monthsLate));
                addOperationCosts.accept(p);
            }
            //is not early
            else
            {
                addOperationCosts.accept(p);
                VerificaValidadePagamentoUtil.calculaDesconto(p);
            }

            return p;
        };
        listaPagamentos.stream()
            .map(addTax)
            .forEach(p -> memory.save(p));


        System.out.println("Finished main");

    }
}