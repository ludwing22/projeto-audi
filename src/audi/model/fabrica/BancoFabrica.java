/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.interfaces.ObservadorDeProducao;
import audi.model.item.Banco;
import audi.model.item.Item;
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
public class BancoFabrica implements Fabrica {

    public LinkedList<Banco> bancos;
    private final List<ObservadorDeProducao> observadores = new ArrayList<ObservadorDeProducao>();

    Thread thread;

    public BancoFabrica() {
        bancos = new LinkedList<>();
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

    public synchronized void produzirBanco() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {
                        
                        if (bancos.size() < getEstoqueMaximo()) {
                            TimeUnit.SECONDS.sleep(getTempoDeProducao());
                            Banco banco = new Banco();
                            bancos.add(banco);
                            System.out.println("Produzi banco :"+ bancos.size() + " /" + getEstoqueMaximo());
                            notificarObservadores();
                        } else {
                            System.out.println("Dormi fabrica banco");
                            synchronized (this) {
                                try {
                                    thread.wait();
                                } catch (InterruptedException ie) {
                                }
                            }
                        }

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(BancoFabrica.class.getName()).log(Level.ALL.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public synchronized void retirarBanco() throws InterruptedException {
        System.out.println("Retirei Banco");
        this.bancos.removeFirst();
        if(thread.isInterrupted()){
            thread.notifyAll();   
        }
    }

    @Override
    public void notificarObservadores() {

        for (ObservadorDeProducao observadorDeProducao : observadores) {
            observadorDeProducao.notificarProducaoBanco(this);
        }
    }

    public void adicionarObservador(ObservadorDeProducao obs) {
        observadores.add(obs);
    }

}
