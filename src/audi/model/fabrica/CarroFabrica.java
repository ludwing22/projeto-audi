/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Carro;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class CarroFabrica implements Fabrica {

    private ArrayList<Carro> carros;

    public CarroFabrica() {
        carros = new ArrayList<>();
    }

    Thread thread;

    @Override
    public int getQuantidadePorVeiculo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTempoDeProducao() {
        return 5;
    }

    @Override
    public int getEstoqueMaximo() {
        return 10;
    }

    public void produzirCarro() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (carros.size() < getEstoqueMaximo()) {
                            Carro carro = new Carro();
                            carros.add(carro);
                            System.out.println("Produzi carro");
                        } else {
                            System.out.println("Dormi");
                            thread.wait();
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void retirarCarro() {
        this.carros = (ArrayList<Carro>) carros.subList(0, 5);
        this.thread.notify();
    }

}
