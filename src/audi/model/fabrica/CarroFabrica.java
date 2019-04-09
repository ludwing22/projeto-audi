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
import audi.view.AudiInterface;
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
    private AudiInterface audii;
    public CarroFabrica(AudiInterface i) {
        carros = new ArrayList<>();
        carro = new Carro();
        audii = i;
        //audii
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
                audii.setQTDCaminhao(this.carros.size()*100/getEstoqueMaximo(), this.carros.size() + " / " + getEstoqueMaximo());
                obterItensOutrasFabricas();
            } else {
               carros.removeAll(carros);
                
            }
        }

    }

    public synchronized void obterItensOutrasFabricas() throws InterruptedException {
        if (carro.bancos.size() <= bf.getQuantidadePorVeiculo() && bf.bancos.size() > 0) {
            bf.retirarBanco();
            carro.bancos.add(new Banco());
        }
        if (carro.carrocerias.size() <= cf.getQuantidadePorVeiculo() && cf.carrocerias.size() > 0) {
            cf.retirarCarroceria();
            carro.carrocerias.add(new Carroceria());
        }
        if (carro.eletronica.size() <= ef.getQuantidadePorVeiculo() && ef.eletronicas.size() > 0) {
            ef.retirarEletronica();
            carro.eletronica.add(new Eletronica());
        }
        if (carro.motores.size() <= mf.getQuantidadePorVeiculo() && mf.motores.size() > 0) {
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
        audii.setEstoqueBancos(bf.bancos.size()*100/bf.getEstoqueMaximo(), bf.bancos.size() + " / " + bf.getEstoqueMaximo());
        if ( bf.bancos.size() >= bf.getQuantidadePorVeiculo() && carro.bancos.size() < bf.getQuantidadePorVeiculo()) {
            try {
                bf.retirarBanco();
                bf.retirarBanco();
                bf.retirarBanco();
                bf.retirarBanco();
                bf.retirarBanco();
                audii.setEstoqueBancos(bf.bancos.size()*100/bf.getEstoqueMaximo(), bf.bancos.size() + " / " + bf.getEstoqueMaximo());
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.bancos.add(new Banco());
            carro.bancos.add(new Banco());
            carro.bancos.add(new Banco());
            carro.bancos.add(new Banco());
            carro.bancos.add(new Banco());
        }
        
        if (bf.bancos.size() == bf.getEstoqueMaximo()) {
            audii.selectRBBancos(false);
        }
        else
            audii.selectRBBancos(true);
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoCarroceria(CarroceriaFabrica cf) {
        audii.setEstoqueCarroceria(cf.carrocerias.size()*100/cf.getEstoqueMaximo(), cf.carrocerias.size() + " / " + cf.getEstoqueMaximo());
                    
        if (carro.carrocerias.size() < cf.getQuantidadePorVeiculo()) {
            try {
                cf.retirarCarroceria();
                audii.setEstoqueCarroceria(cf.carrocerias.size()*100/cf.getEstoqueMaximo(), cf.carrocerias.size() + " / " + cf.getEstoqueMaximo());
        
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.carrocerias.add(new Carroceria());
        }
        
        
        if (cf.carrocerias.size() == cf.getEstoqueMaximo()) {
            audii.selectRBCarroceria(false);
        }
        else
            audii.selectRBCarroceria(true);
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoEletronica(EletronicaFabrica ef) {
audii.setEstoqueEletronicos(ef.eletronicas.size()*100/ef.getEstoqueMaximo(), ef.eletronicas.size() + " / " + ef.getEstoqueMaximo());
                    
        if (carro.eletronica.size() < ef.getQuantidadePorVeiculo()) {
            try {
                ef.retirarEletronica();
                audii.setEstoqueEletronicos(ef.eletronicas.size()*100/ef.getEstoqueMaximo(), ef.eletronicas.size() + " / " + ef.getEstoqueMaximo());
                    
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.eletronica.add(new Eletronica());
        }
        
        if (ef.eletronicas.size() == ef.getEstoqueMaximo()) {
            audii.selectRBEletronicas(false);
        }
        else
            audii.selectRBEletronicas(true);
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public synchronized void notificarProducaoMotor(MotorFabrica mf) {
        audii.setEstoqueMotores(mf.motores.size()*100/mf.getEstoqueMaximo(), mf.motores.size() + " / " + mf.getEstoqueMaximo());
                    
        if (carro.motores.size() < mf.getQuantidadePorVeiculo()) {
            try {
                mf.retirarMotor();
                audii.setEstoqueMotores(mf.motores.size()*100/mf.getEstoqueMaximo(), mf.motores.size() + " / " + mf.getEstoqueMaximo());
                    
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.motores.add(new Motor());
            
        }
        
        if (mf.motores.size() == mf.getEstoqueMaximo()) {
            audii.selectRBMotores(false);
        }
        else
            audii.selectRBMotores(true);
        try {
            produzirCarro(this.thread);
        } catch (InterruptedException ex) {
            Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    @Override
    public synchronized void notificarProducaoPneu(PneuFabrica pf) {
audii.setEstoquePneus(pf.pneus.size()*100/pf.getEstoqueMaximo(), pf.pneus.size() + " / " + pf.getEstoqueMaximo());
                    
        if ( pf.pneus.size() >= pf.getQuantidadePorVeiculo() && carro.pneus.size() < pf.getQuantidadePorVeiculo()) {
            try {
                pf.retirarPneu();
                pf.retirarPneu();
                pf.retirarPneu();
                pf.retirarPneu();
                audii.setEstoquePneus(pf.pneus.size()*100/pf.getEstoqueMaximo(), pf.pneus.size() + " / " + pf.getEstoqueMaximo());
                    
            } catch (InterruptedException ex) {
                Logger.getLogger(CarroFabrica.class.getName()).log(Level.SEVERE, null, ex);
            }
            carro.pneus.add(new Pneu());
            carro.pneus.add(new Pneu());
            carro.pneus.add(new Pneu());
            carro.pneus.add(new Pneu());
        }
        
        if (pf.pneus.size() == pf.getEstoqueMaximo()) {
            audii.selectRBPneus(false);
        }
        else
            audii.selectRBPneus(true);
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
