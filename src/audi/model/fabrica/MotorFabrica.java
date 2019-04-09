/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.interfaces.ObservadorDeProducao;
import audi.model.item.Motor;
import audi.model.item.Motor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class MotorFabrica implements Fabrica {

    LinkedList<Motor> motores;
    private final List<ObservadorDeProducao> observadores = new ArrayList<ObservadorDeProducao>();
    Thread thread;

    public MotorFabrica() {
        motores = new LinkedList<>();
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

    public synchronized void produzirMotor() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {
                        
                        if (motores.size() < getEstoqueMaximo()) {
                            TimeUnit.SECONDS.sleep(getTempoDeProducao());
                            Motor motor = new Motor();
                            motores.add(motor);
                            System.out.println("Produzi motor:"+ motores.size() + " /" + getEstoqueMaximo());
                            notificarObservadores();
                        } else {
                            System.out.println("Dormi fabrica motores");
                            synchronized (this) {
                                try {
                                    thread.wait();
                                } catch (InterruptedException ie) {
                                }
                            }
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(MotorFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public synchronized void retirarMotor() throws InterruptedException {
        
        this.motores.removeFirst();
        System.out.println("Retirei motor: " + this.motores.size() + this.getEstoqueMaximo());
        
        if (thread.getState() == Thread.State.WAITING){
            synchronized(thread){
                thread.notify(); 
            }
        }
    }

    @Override
    public void notificarObservadores() {
        for (ObservadorDeProducao odp : observadores) {
            odp.notificarProducaoMotor(this);
        }
    }

    public void adicionarObservador(ObservadorDeProducao obs) {
        observadores.add(obs);
    }
}
