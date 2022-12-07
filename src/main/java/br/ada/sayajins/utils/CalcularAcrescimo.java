package br.ada.sayajins.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.ada.sayajins.enums.TipoPagamento;

public class CalcularAcrescimo {

    public BigDecimal acrescimo(BigDecimal valor, TipoPagamento tipoPagamento, Long mesesAtrasado){

        BigDecimal novoValor = new BigDecimal("0");
        BigDecimal acrescimo = new BigDecimal(tipoPagamento.getAcrescimo());
        novoValor = novoValor.add(valor);

        if (mesesAtrasado < 1 && mesesAtrasado > 0){
            return novoValor.multiply(acrescimo);
        }

        novoValor = novoValor.multiply(acrescimo.multiply(new BigDecimal(mesesAtrasado).setScale(2, RoundingMode.HALF_EVEN)));

        return novoValor;
    }
}