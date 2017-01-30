package com.br.agent;

import com.br.behaviours.ComportamentoCoordenador;

import jade.core.Agent;

public class AgenteCoordenador extends Agent {
	private static final long serialVersionUID = 1L;
	public static final String LOCAL_NAME="coord";

	@Override
	protected void setup() {
		System.out.println("Inicia agente Coordenador");
		addBehaviour(new ComportamentoCoordenador(this));
	}

}
