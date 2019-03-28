/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Motor;
import audi.model.item.Motor;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class MotorFabrica implements Fabrica{
    
    ArrayList<Motor> motores;
    Thread thread;

    public MotorFabrica() {
        motores = new ArrayList<>();
    }
    
    @Override
    public int getQuantidadePorVeiculo() {
        return 1;
    }

    @Override
    public int getTempoDeProducao() {
        return 5;
    }

    @Override
    public int getEstoqueMaximo() {
        return 10;
    }
    
        public void produzirMotor() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (motores.size() < getEstoqueMaximo()) {
                            Motor motor = new Motor();
                            motores.add(motor);
                            System.out.println("Produzi motor");
                        } else {
                            System.out.println("Dormi");
                            thread.wait();
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(MotorFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void retirarMotor() {
        this.motores = (ArrayList<Motor>) motores.subList(0, 5);
        this.thread.notify();
    }
}
