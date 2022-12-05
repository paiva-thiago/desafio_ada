package br.ada.sayajins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.ada.sayajins.model.Pagamentos;
import br.ada.sayajins.model.TipoPagamentoEnum;

public class Main {
    public static void main(String[] args) {
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

        for(Pagamentos a : listaPagamentos){
            System.out.println(a);
        }
    }

}
