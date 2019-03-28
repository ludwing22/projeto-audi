/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Banco;
import audi.model.item.Item;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class BancoFabrica implements Fabrica{
    
    private ArrayList<Banco> bancos;
    
    Thread thread;

    public BancoFabrica() {
        bancos = new ArrayList<>();
    }

    @Override
    public int getQuantidadePorVeiculo() {
        return 1;
    }

    @Override
    public int getTempoDeProducao() {
        return 12;
    }

    @Override
    public int getEstoqueMaximo() {
        return 10;
    }
    
    public void produzirBanco() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (bancos.size() < getEstoqueMaximo()) {
                            Banco banco = new Banco();
                            bancos.add(banco);
                            System.out.println("Produzi banco");
                        } else {
                            System.out.println("Dormi");
                            thread.wait();
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(BancoFabrica.class.getName()).log(Level.ALL.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void retirarBanco() {
        this.bancos = (ArrayList<Banco>) bancos.subList(0, 5);
        this.thread.notify();
    }

}
