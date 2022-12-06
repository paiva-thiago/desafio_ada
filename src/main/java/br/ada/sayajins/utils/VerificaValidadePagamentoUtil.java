package br.ada.sayajins.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import br.ada.sayajins.model.Pagamentos;

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
    
}
