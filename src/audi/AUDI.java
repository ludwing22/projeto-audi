/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi;

import audi.model.fabrica.CarroFabrica;
import audi.view.AudiInterface;
/**
 *
 * @author paulo
 */
public class AUDI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AudiInterface i = new AudiInterface();
        
        CarroFabrica cf = new CarroFabrica(i);
        cf.iniciarFabricaCarro();
    }
    
}
