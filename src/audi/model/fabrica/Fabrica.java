/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audi.model.fabrica;

import audi.model.item.Item;
import java.util.ArrayList;

/**
 *
 * @author paulo
 */
public interface Fabrica {
    public int getQuantidadePorVeiculo();
    public int getTempoDeProducao();
    public int getEstoqueMaximo();
}
