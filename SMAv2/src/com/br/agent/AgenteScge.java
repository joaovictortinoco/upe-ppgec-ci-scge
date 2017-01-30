package com.br.agent;

import com.br.behaviours.ComportamentoScge;

import jade.core.Agent;

public class AgenteScge extends Agent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		System.out.println("Inicia agente SCGE");
		addBehaviour(new ComportamentoScge(this));
	}

}
