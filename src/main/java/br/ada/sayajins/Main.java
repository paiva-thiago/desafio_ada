package br.ada.sayajins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.ada.sayajins.enums.TipoPagamento;
import br.ada.sayajins.model.Pagamentos;
import br.ada.sayajins.model.TipoPagamentoEnum;
import br.ada.sayajins.utils.CalcularAcrescimo;
import br.ada.sayajins.utils.MemorySaveUtil;
import br.ada.sayajins.utils.VerificaValidadePagamentoUtil;

public class Main {

    public static void main(String[] args) {
        MemorySaveUtil memory = MemorySaveUtil.getInstance();

        CalcularAcrescimo calcularAcrescimo = new CalcularAcrescimo();

        String file = "src/main/resources/pagamentos.csv";
        List<String> content = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((br.readLine()) != null) {
                content.add(br.readLine());                
            }
        }
        catch(IOException e) {
          e.printStackTrace();
        }    

        List<Pagamentos> listaPagamentos = new ArrayList<>();

        for(int i = 0; i< content.size(); i++){

            String[] arrOfStr = content.get(i).split(";");

            DateTimeFormatter JEFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate data = LocalDate.parse(arrOfStr[2], JEFormatter);
            TipoPagamentoEnum tipo = TipoPagamentoEnum.valueOf(arrOfStr[1]);
            double valor = Double.parseDouble(arrOfStr[3]);
            String nome = arrOfStr[0];
            listaPagamentos.add(new Pagamentos(nome, data, valor, tipo));

        }

        listaPagamentos.stream()
        .forEach(
            p->
            {
                System.out.println(p);
                p.setValor(p.getValor().add(calcularAcrescimo.acrescimo(p.getValor(), enum2Acr(p.getTipoPagamentoEnum()), VerificaValidadePagamentoUtil.calculoDeMesesDeAtraso(p))));
                System.out.println(p);
            }
        );
            
    }

    private static TipoPagamento enum2Acr(TipoPagamentoEnum tp)
    {
        switch (tp)
        {
            case BOLETO:
                return TipoPagamento.BOLETO;
            case CREDITO:
                return TipoPagamento.CREDITO;
            case DEBITO:
                return TipoPagamento.DEBITO;
            case FIDELIDADE:
                return TipoPagamento.PIX;
            default:
                return TipoPagamento.PIX;

        }
} 

}
