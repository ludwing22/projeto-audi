/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Eletronica;
import audi.model.item.Eletronica;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class EletronicaFabrica implements Fabrica{
    
    ArrayList<Eletronica> eletronicas;
    
    Thread thread;
    

    @Override
    public int getQuantidadePorVeiculo() {
        return 4;
    }

    @Override
    public int getTempoDeProducao() {
        return 3;
    }

    @Override
    public int getEstoqueMaximo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        public void produzirEletronica() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (eletronicas.size() < getEstoqueMaximo()) {
                            Eletronica eletronica = new Eletronica();
                            eletronicas.add(eletronica);
                            System.out.println("Produzi eletronica");
                        } else {
                            System.out.println("Dormi");
                            thread.wait();
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(EletronicaFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void retirarEletronica() {
        this.eletronicas = (ArrayList<Eletronica>) eletronicas.subList(0, 5);
        this.thread.notify();
    }
    
    
}
