package com.br.agent;

import com.br.behaviours.ComportamentoAti;

import jade.core.Agent;

public class AgenteAti extends Agent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		System.out.println("Inicia agente ATI.");
		addBehaviour(new ComportamentoAti(this));
	}

}
