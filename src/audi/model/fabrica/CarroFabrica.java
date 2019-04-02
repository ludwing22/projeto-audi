/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.interfaces.ObservadorDeProducao;
import audi.model.item.Banco;
import audi.model.item.Carro;
import audi.model.item.Carroceria;
import audi.model.item.Eletronica;
import audi.model.item.Motor;
import audi.model.item.Pneu;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class CarroFabrica implements Fabrica, ObservadorDeProducao {

    private ArrayList<Carro> carros;

    private Carro carro = new Carro();

    private BancoFabrica bf;
    private CarroceriaFabrica cf;
    private EletronicaFabrica ef;
    private MotorFabrica mf;
    private PneuFabrica pf;

    public CarroFabrica() {
        carros = new ArrayList<>();
        carro = new Carro();

        bf = new BancoFabrica();
        bf.adicionarObservador(this);
        cf = new CarroceriaFabrica();
        cf.adicionarObservador(this);
        ef = new EletronicaFabrica();
        ef.adicionarObservador(this);
        mf = new MotorFabrica();
        mf.adicionarObservador(this);
        pf = new PneuFabrica();
        pf.adicionarObservador(this);
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

    public synchronized void iniciarFabricaCarro() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                try {
                    bf.produzirBanco();
                    cf.produzirCarroceria();
                    ef.produzirEletronica();
                    mf.produzirMotor();
                    pf.produzirPneu();

                    while (true) {
                        produzirCarro(thread);

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public synchronized void produzirCarro(Thread thread) throws InterruptedException {
        if (carro.ePossivelProduzirCarro()) {
            imprimirCoisasCarro();
            TimeUnit.SECONDS.sleep(this.getTempoDeProducao());
            if (carros.size() < getEstoqueMaximo()) {
                carros.add(this.carro);
                this.carro = new Carro();
                System.out.println("*** Produzi carro:" + this.carros.size() + " /" + getEstoqueMaximo());
                obterItensOutrasFabricas();
            } else {
                System.out.println("Dormi fabrica carro");
                thread.wait();
            }
        }

    }

    public synchronized void obterItensOutrasFabricas() throws InterruptedException {
        if (carro.bancos.size() < bf.getQuantidadePorVeiculo() && bf.bancos.size() > 0) {
            bf.retirarBanco();
            carro.bancos.add(new Banco());
        }
        if (carro.carrocerias.size() < cf.getQuantidadePorVeiculo() && cf.carrocerias.size() > 0) {
            cf.retirarCarroceria();
            carro.carrocerias.add(new Carroceria());
        }
        if (carro.eletronica.size() < ef.getQuantidadePorVeiculo() && ef.eletronicas.size() > 0) {
            ef.retirarEletronica();
            carro.eletronica.add(new Eletronica());
        }
        if (carro.motores.size() < mf.getQuantidadePorVeiculo() && mf.motores.size() > 0) {
            mf.retirarMotor();
            carro.motores.add(new Motor());
        }
        if (carro.pneus.size() < pf.getQuantidadePorVeiculo() && pf.pneus.size() > 0) {
            pf.retirarPneu();
            carro.pneus.add(new Pneu());
        }

        produzirCarro(this.thread);
    }

    public synchronized void retirarCarro() {
        this.carros = (ArrayList<Carro>) carros.subList(0, 5);
        this.thread.notify();
    }

    @Override
    public void notificarObservadores() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void notificarProducaoBanco(BancoFabrica bf) {

        if (carro.bancos.size() < bf.getQuantidadePorVeiculo()) {
            try {
                bf.retirarBanco();
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.bancos.add(new Banco());
        }
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoCarroceria(CarroceriaFabrica cf) {

        if (carro.carrocerias.size() < cf.getQuantidadePorVeiculo()) {
            try {
                cf.retirarCarroceria();
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.carrocerias.add(new Carroceria());
        }
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoEletronica(EletronicaFabrica ef) {

        if (carro.eletronica.size() < ef.getQuantidadePorVeiculo()) {
            try {
                ef.retirarEletronica();
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.eletronica.add(new Eletronica());
        }
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoMotor(MotorFabrica mf) {

        if (carro.motores.size() < mf.getQuantidadePorVeiculo()) {
            try {
                mf.retirarMotor();
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.motores.add(new Motor());
        }
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoPneu(PneuFabrica pf) {

        if (carro.pneus.size() < pf.getQuantidadePorVeiculo()) {
            try {
                pf.retirarPneu();
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.pneus.add(new Pneu());
        }
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void adicionarObservador(ObservadorDeProducao obs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void imprimirCoisasCarro(){
        System.out.println(carro.bancos.size() + " /"+
                carro.carrocerias.size() + " /"+
                        carro.eletronica.size() + " /"+
                                carro.motores.size() + " /"+
                                        carro.pneus.size() + " /"+
                carro.bancos.size() + " /"
        );
    }

}
