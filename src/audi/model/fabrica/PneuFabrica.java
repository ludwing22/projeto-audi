/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Pneu;
import audi.model.item.Pneu;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class PneuFabrica implements Fabrica{
    
    ArrayList<Pneu> pneus;
    
    Thread thread;

    @Override
    public int getQuantidadePorVeiculo() {
        return 4;
    }

    @Override
    public int getTempoDeProducao() {
        return 4;
    }

    @Override
    public int getEstoqueMaximo() {
        return 10;
    }
    
        public void produzirPneu() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (pneus.size() < getEstoqueMaximo()) {
                            Pneu pneu = new Pneu();
                            pneus.add(pneu);
                            System.out.println("Produzi pneu");
                        } else {
                            System.out.println("Dormi");
                            thread.wait();
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(PneuFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void retirarPneu() {
        this.pneus = (ArrayList<Pneu>) pneus.subList(0, 5);
        this.thread.notify();
    }
    
}
