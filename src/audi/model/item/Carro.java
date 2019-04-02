/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.item;

import audi.model.fabrica.*;
import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author paulo
 */
public class Carro {
    public ArrayList<Banco> bancos;
    public ArrayList<Carroceria> carrocerias;
    public ArrayList<Eletronica> eletronica;
    public ArrayList<Motor> motores;
    public ArrayList<Pneu> pneus;

    public Carro() {
        bancos = new ArrayList<>();
        carrocerias = new ArrayList<>();
        eletronica = new ArrayList<>();
        motores = new ArrayList<>();
        pneus = new ArrayList<>();
    }

    public Carro(ArrayList<Banco> bancos, ArrayList<Carroceria> carrocerias, ArrayList<Motor> motores, ArrayList<Pneu> pneus) {
        this.bancos = bancos;
        this.carrocerias = carrocerias;
        this.motores = motores;
        this.pneus = pneus;
    }

    public void setBancos(ArrayList<Banco> bancos) {
        this.bancos = bancos;
    }

    public void setCarrocerias(ArrayList<Carroceria> carrocerias) {
        this.carrocerias = carrocerias;
    }

    public void setEletronica(ArrayList<Eletronica> eletronica) {
        this.eletronica = eletronica;
    }

    public void setMotores(ArrayList<Motor> motores) {
        this.motores = motores;
    }

    public void setPneus(ArrayList<Pneu> pneus) {
        this.pneus = pneus;
    }
    
    public boolean ePossivelProduzirCarro (){
        BancoFabrica bf = new BancoFabrica();
        CarroceriaFabrica cf = new CarroceriaFabrica();
        EletronicaFabrica ef = new EletronicaFabrica();
        MotorFabrica mf = new MotorFabrica();
        PneuFabrica pf = new PneuFabrica();
        
        return this.bancos.size() >= bf.getQuantidadePorVeiculo() &&
                this.carrocerias.size() >= cf.getQuantidadePorVeiculo() &&
                this.eletronica.size() >= ef.getQuantidadePorVeiculo() &&
                this.motores.size() >= mf.getQuantidadePorVeiculo() &&
                this.pneus.size() >= pf.getQuantidadePorVeiculo();
    }
    
}
