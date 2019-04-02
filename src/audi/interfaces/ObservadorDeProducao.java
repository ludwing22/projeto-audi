/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.interfaces;

import audi.model.fabrica.BancoFabrica;
import audi.model.fabrica.CarroceriaFabrica;
import audi.model.fabrica.EletronicaFabrica;
import audi.model.fabrica.MotorFabrica;
import audi.model.fabrica.PneuFabrica;

/**
 *
 * @author paulo
 */
public interface ObservadorDeProducao {
    public void notificarProducaoBanco(BancoFabrica bf);
    public void notificarProducaoCarroceria(CarroceriaFabrica cf);
    public void notificarProducaoEletronica(EletronicaFabrica ef);
    public void notificarProducaoMotor(MotorFabrica mf);
    public void notificarProducaoPneu(PneuFabrica pf);
}
