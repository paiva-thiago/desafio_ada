package br.ada.sayajins.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.ada.sayajins.model.Pagamentos;

public class MemorySaveUtil {
    /**
     * @author Kawe Takamoto Siqueira
     * @version 1.0.0
     * 
     * A helper class that allows easy data 
     * persistence of processed payments
     * 
     * Usage:
     * 1. Get a singleton instance via the getInstance
     * static method
     * 
     * 2. After processing a Pagamento object save it via
     * the save method
     * 
     * 3. Data can be easily writen via the getData method
     * @see MemorySaveUtil#getData()
     */


    private Map<String, List<Pagamentos>> memory = new HashMap<>();
    private static MemorySaveUtil instance;

    public static MemorySaveUtil getInstance()
    {
        if(MemorySaveUtil.instance == null)
        {
            MemorySaveUtil.instance = new MemorySaveUtil();
        }
        return MemorySaveUtil.instance;
    }

    public void save(Pagamentos pagamento)
    {
        /**
         * If the payment method has not yet been added to
         * meory add it
         * then add Pagamento object to the list
         */
        String paymentMethod = pagamento.getTipoPagamentoEnum().name();
        if (!this.memory.keySet().contains(paymentMethod))
        {
            this.memory.put(paymentMethod, new ArrayList<Pagamentos>());
        }
        this.memory.get(paymentMethod).add(pagamento);
    }

    public Set<Entry<String, List<Pagamentos>>> getData()
    {
        /**
         * This entry set contains both
         * the name of the payment method
         * and the Pagamentos objects 
         * related to this payment method
         * 
         * Each entry.getKey() will return the payment method
         * whilst each entry.getValue() will return a List
         * containing all Pagamentos referring to the payment method. 
         */
        return this.memory.entrySet();

    }

}
