package br.ada.sayajins.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import br.ada.sayajins.model.Pagamentos;
import br.ada.sayajins.model.TipoPagamentoEnum;

public class VerificaValidadePagamento {

    public static Long calculoDeMesesDeAtraso(Pagamentos pagamento){        
        if(pagamentoEstaAtrasado(pagamento)){
            LocalDate dataAtual = LocalDate.now();
            LocalDate dataDeVencimento = pagamento.getDtVencto();
            return ChronoUnit.MONTHS.between(dataDeVencimento,dataAtual);
        }
        return 0L;
    }

    public static boolean pagamentoEstaAtrasado(Pagamentos pagamento){
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataDeVencimento = pagamento.getDtVencto();
        return dataDeVencimento.isBefore(dataAtual);
    }

    public static Long calculoDiasAdiantado(Pagamentos pagamento) {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataDeVencimento = pagamento.getDtVencto();
        if(dataDeVencimento.isAfter(dataAtual)) {
            return (-1)*ChronoUnit.DAYS.between(dataDeVencimento,dataAtual);
        }
        return 0L;
    }

    public static Pagamentos calculaDesconto(Pagamentos pagamento) {
        if(pagamento.getTipoPagamentoEnum() != TipoPagamentoEnum.FIDELIDADE) {
            BigDecimal desconto = new BigDecimal(1-0.005*calculoDiasAdiantado(pagamento));
            BigDecimal valorAtual = pagamento.getValor().multiply(desconto);
            pagamento.setValor(valorAtual);
        }
        return pagamento;
    }
    
}
