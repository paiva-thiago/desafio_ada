
package br.ada.sayajins.enums;

public enum TipoPagamento {
    
    CREDITO("1.03"),
    DEBITO("1.01"),
    BOLETO("1.05"),
    PIX("1.00");

    private String acrescimo;

    TipoPagamento(String acrescimo){
        this.acrescimo = acrescimo;
    }

    public String getAcrescimo(){
        return acrescimo;
    }

}