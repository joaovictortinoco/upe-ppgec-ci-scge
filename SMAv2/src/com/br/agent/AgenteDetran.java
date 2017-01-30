package com.br.agent;

import com.br.behaviours.ComportamentoDetran;

import jade.core.Agent;

public class AgenteDetran extends Agent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		System.out.println("Inicia agente Detran");
		addBehaviour(new ComportamentoDetran(this));
	}

}
