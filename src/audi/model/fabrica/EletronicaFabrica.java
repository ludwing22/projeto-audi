/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.interfaces.ObservadorDeProducao;
import audi.model.item.Eletronica;
import audi.model.item.Eletronica;
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
public class EletronicaFabrica implements Fabrica {

    LinkedList<Eletronica> eletronicas;
    private final List<ObservadorDeProducao> observadores = new ArrayList<ObservadorDeProducao>();

    Thread thread;

    public EletronicaFabrica() {
        eletronicas = new LinkedList<>();
    }

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
        return 8;
    }

    public synchronized void produzirEletronica() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {
                        TimeUnit.SECONDS.sleep(getTempoDeProducao());
                        if (eletronicas.size() < getEstoqueMaximo()) {
                            Eletronica eletronica = new Eletronica();
                            eletronicas.add(eletronica);
                            System.out.println("Produzi eletronica");
                            notificarObservadores();
                        } else {
                            System.out.println("Dormi");
                            synchronized (this) {
                                try {
                                    thread.wait();
                                } catch (InterruptedException ie) {
                                }
                            }
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(EletronicaFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public synchronized void retirarEletronica() throws InterruptedException {
        System.out.println("Retirei eletronica");
        this.eletronicas.removeFirst();
        thread.notifyAll();

    }

    @Override
    public void notificarObservadores() {
        for (ObservadorDeProducao odp : observadores) {
            odp.notificarProducaoEletronica(this);
        }
    }

    public void adicionarObservador(ObservadorDeProducao obs) {
        observadores.add(obs);
    }

}
