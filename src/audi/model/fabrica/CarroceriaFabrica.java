/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Carroceria;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class CarroceriaFabrica implements Fabrica{
    
    Thread thread;
    
    ArrayList<Carroceria> carrocerias;

    @Override
    public int getQuantidadePorVeiculo() {
        return 1;
    }

    @Override
    public int getTempoDeProducao() {
        return 3;
    }

    @Override
    public int getEstoqueMaximo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void produzirCarroceria() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (carrocerias.size() < getEstoqueMaximo()) {
                            Carroceria carro = new Carroceria();
                            carrocerias.add(carro);
                            System.out.println("Produzi carro");
                        } else {
                            System.out.println("Dormi");
                            thread.wait();
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(CarroceriaFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void retirarCarroceria() {
        this.carrocerias = (ArrayList<Carroceria>) carrocerias.subList(0, 5);
        this.thread.notify();
    }
    
}
