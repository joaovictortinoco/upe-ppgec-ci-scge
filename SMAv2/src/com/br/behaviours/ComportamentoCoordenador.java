package com.br.behaviours;

import com.br.dashboard.Dashboard;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ComportamentoCoordenador extends SimpleBehaviour {
	private static final long serialVersionUID = 1L;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	ACLMessage msgAgente;
	int contCoordenador = 0; 
	
	public ComportamentoCoordenador(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		msgAgente = myAgent.receive(mt);
		if (msgAgente != null) {
			System.out.println(msgAgente.getContent());
			enviaMsg();
		} else {
			this.block();
		}
	}

	private void enviaMsg() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(msgAgente.getSender());
		msg.setContent(myAgent.getLocalName() + ": Ciente.");
		myAgent.send(msg);
	}

	@Override
	public boolean done() {
		if (contCoordenador == 12) {
			myAgent.doDelete();
		} else {
			contCoordenador++;
		}

		return false;
	}
}