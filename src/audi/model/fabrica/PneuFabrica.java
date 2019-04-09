/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.interfaces.ObservadorDeProducao;
import audi.model.item.Pneu;
import audi.model.item.Pneu;
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
public class PneuFabrica implements Fabrica {

    LinkedList<Pneu> pneus;
    private final List<ObservadorDeProducao> observadores = new ArrayList<>();
    Thread thread;

    public PneuFabrica() {
        pneus = new LinkedList<>();
    }

    @Override
    public int getQuantidadePorVeiculo() {
        return 4;
    }

    @Override
    public int getTempoDeProducao() {
        return 9;
    }

    @Override
    public int getEstoqueMaximo() {
        return 100;
    }

    public synchronized void produzirPneu() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {

                        if (pneus.size() < getEstoqueMaximo()) {

                            for (int i = 0; i < getQuantidadePorVeiculo(); i++) {
                                TimeUnit.SECONDS.sleep(getTempoDeProducao());
                                Pneu pneu = new Pneu();
                                pneus.add(pneu);
                            }

                            System.out.println("Produzi pneu:" + pneus.size() + " /" + getEstoqueMaximo());
                            notificarObservadores();
                        } else {
                            System.out.println("Dormi fabrica de pneu");
                            synchronized (this) {
                                try {
                                    thread.wait();
                                } catch (InterruptedException ie) {
                                }
                            }
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(PneuFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public synchronized void retirarPneu() throws InterruptedException {

        System.out.println("Retirei pneu");
        this.pneus.removeFirst();

        if (thread.getState() == Thread.State.WAITING) {
            synchronized (thread) {
                thread.notify();
            }
        }
    }

    @Override
    public void notificarObservadores() {
        for (ObservadorDeProducao odp : observadores) {
            odp.notificarProducaoPneu(this);
        }
    }

    public void adicionarObservador(ObservadorDeProducao obs) {
        observadores.add(obs);
    }
}
