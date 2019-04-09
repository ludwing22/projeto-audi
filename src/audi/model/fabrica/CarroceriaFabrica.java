/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.interfaces.ObservadorDeProducao;
import audi.model.item.Carroceria;
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
public class CarroceriaFabrica implements Fabrica{
    
    Thread thread;
    
    LinkedList<Carroceria> carrocerias;
    private final List<ObservadorDeProducao> observadores = new ArrayList<ObservadorDeProducao>();

    public CarroceriaFabrica() {
        carrocerias = new LinkedList<>();
    }
    
    @Override
    public int getQuantidadePorVeiculo() {
        return 1;
    }

    @Override
    public int getTempoDeProducao() {
        return 15;
    }

    @Override
    public int getEstoqueMaximo() {
        return 20;
    }
    
    public synchronized void produzirCarroceria() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {
                        
                        if (carrocerias.size() < getEstoqueMaximo()) {
                            TimeUnit.SECONDS.sleep(getTempoDeProducao());
                            Carroceria carro = new Carroceria();
                            carrocerias.add(carro);
                            System.out.println("Produzi carroceria:"+ carrocerias.size() + " /" + getEstoqueMaximo());
                            notificarObservadores();
                        } else {
                            System.out.println("Dormi Fabrica Carroceria");
                            synchronized(this){
                                try{
                                    thread.wait();
                                } catch(InterruptedException ie){}
                            } 
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(CarroceriaFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public synchronized void retirarCarroceria() throws InterruptedException  {
        System.out.println("Retirei Carroceria");
        this.carrocerias.removeFirst();
        
        if (thread.getState() == Thread.State.WAITING){
            synchronized(thread){
                thread.notify(); 
            }
        }
    }

    
    @Override
    public void notificarObservadores() {
        for (ObservadorDeProducao odp: observadores) {
            odp.notificarProducaoCarroceria(this);
        }
    }
    
    public void adicionarObservador(ObservadorDeProducao obs) {
        observadores.add(obs);
    }
    
}
