package com.br.behaviours;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.br.agent.AgenteCoordenador;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ComportamentoDetran extends SimpleBehaviour {
	private static final long serialVersionUID = 1L;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	public ACLMessage msgAgente;
	public int contJoao = 0;
	private boolean finished = false;
	private boolean resp = false;
	private int countLine = 0;
	private String line;
	private String retornoFinal = new String();
	private boolean isFinished = false;

	public ComportamentoDetran(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		if (contJoao == 0) {
			enviaPrimeiraMsg();
		} else {
			msgAgente = myAgent.receive(mt);
			if (msgAgente != null) {
				if (msgAgente.getContent().contains("Ciente")) {
					try {
						evaluateDetran();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				this.block();
			}
		}
	}

	private void enviaPrimeiraMsg() {
		AID agenteRecebedor = new AID(AgenteCoordenador.LOCAL_NAME, AID.ISLOCALNAME);
		ACLMessage priMsgAgente = new ACLMessage(ACLMessage.REQUEST);
		priMsgAgente.addReceiver(agenteRecebedor);
		priMsgAgente.setContent(myAgent.getLocalName() + ": Iniciando avalia��o.");
		myAgent.send(priMsgAgente);
		contJoao++;
	}

	private void evaluateDetran() throws IOException {
		setExecucaoOrcamentaria();
		setExecucaoOrcamentariaExtensao();
		setConveniosArquivos();
		setAcoesProgramas();
		setContratos();
		setServidores();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(msgAgente.getSender());
		msg.setContent(retornoFinal);
		myAgent.send(msg);
		isFinished = true;
	}

	private void setServidores() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		String link;
		String linkVar;
		url = new URL("http://www.lai.pe.gov.br/web/detran/servidores-com-arquivos");
		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		link = "http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia/despesa2";
		linkVar = "href=" + "\"" + link + "\"";
		String quantServidores = "<b>Quantitdetranvo Servidores</b>";
		String planoCargosCarreiras = "<b>Plano de Cargos e Carreiras - Controle Interno</b>";
		boolean temLink = false;
		boolean temPastaQtServ = false;
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(linkVar)) {
				temLink = true;
			}

			if (line.contains(quantServidores)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/detran/servidores-com-arquivos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=162334");
				is = url.openStream();
				br = new BufferedReader(new InputStreamReader(is));

				while ((line = br.readLine()) != null) {
					countLine++;
					if (line.contains(".ods")) {
						temPastaQtServ = true;
						break;
					}
				}
				// break;
			}

			if (line.contains(planoCargosCarreiras)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/detran/servidores-com-arquivos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=161785");
				is = url.openStream();
				br = new BufferedReader(new InputStreamReader(is));

				while ((line = br.readLine()) != null) {
					countLine++;
					if (line.contains(".ods") && temPastaQtServ && temLink) {
						resp = true;
						this.line = line;
						break;
					}
				}

				break;
			}

		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Servidores': Link e arquivos v�lidos.\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Servidores': Link e/ou arquivos inv�lidos.\n";
		}
	}

	private void setContratos() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/detran/contratos");
		is = url.openStream(); // throws an IOException
		br = new BufferedReader(new InputStreamReader(is));
		String folderContratos = "<b>Contratos</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderContratos)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/detran/contratos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=23562");
				is = url.openStream(); // throws an IOException
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					countLine++;
					if (line.contains(".xlsx") || line.contains(".ods")) {
						resp = true;
						this.line = line;
						break;
					}
				}
				break;
			}
		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'Contratos': Pasta existente e documentos inseridos corretamente." + "\n";
		} else {
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'Contratos': Documentos inseridos incorretamente.\n";
		}
	}

	private void setAcoesProgramas() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/detran/acoes-e-programas");
		is = url.openStream(); // throws an IOException
		br = new BufferedReader(new InputStreamReader(is));
		String folderAcoesProgramasName = "<b>Relat�rios de Desempenho da Gest�o</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderAcoesProgramasName)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/detran/acoes-e-programas?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=192187");
				is = url.openStream(); // throws an IOException
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					countLine++;
					if (line.contains("2015.pdf")) {
						if (line.contains("2014.pdf")) {
							if (line.contains("2013.pdf")) {
								resp = true;
								this.line = line;
								break;
							}
						}
					}
				}
				break;
			}
		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'RDG': Pasta existente e arquivos inseridos corretamente.\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Acoes e Programas' n�o est� correta.\n";
		}
	}

	private void setConveniosArquivos() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/detran/convenios");
		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		String folderConvenioName = "<b>Terceirizados</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderConvenioName)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/detran/convenios?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=129488");
				is = url.openStream();
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					countLine++;
					if (line.contains(".xlsx") || line.contains(".ods")) {
						resp = true;
						this.line = line;
						break;
					}
				}
				break;
			}
		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Convenios: Arquivos inseridos corretamente.\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Convenios': Arquivos inseridos incorretamente.\n";
		}
	}

	private void setExecucaoOrcamentariaExtensao() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/detran/despesa-com-arquivos");
		is = url.openStream(); // throws an IOException
		br = new BufferedReader(new InputStreamReader(is));
		String folderName = "<b>Terceirizados</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderName)) {

				url = new URL(
						"http://www.lai.pe.gov.br/web/detran/despesa-com-arquivos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=230468");
				is = url.openStream(); // throws an IOException
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					countLine++;
					// System.out.println("Procurando...");
					if (line.contains(".xlsx") || line.contains(".ods")) {
						resp = true;
						this.line = line;
						break;
					}
				}
				break;
			}
		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Execu��o Or�ament�ria: Extens�es corretas.\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Execu��o Or�ament�ria: Extens�es incorretas.\n";
		}
	}

	private void setExecucaoOrcamentaria() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		String link;
		String linkVar;
		url = new URL("http://www.lai.pe.gov.br/web/detran/despesa-com-arquivos/despesa2");
		is = url.openStream(); // throws an IOException
		br = new BufferedReader(new InputStreamReader(is));
		link = "http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia";
		linkVar = "href=" + "\"" + link;
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(linkVar)) {
				resp = true;
				this.line = line;
				break;
			}
		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Execu��o Or�ament�ria': Link encontrado.\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Execu��o Or�ament�ria': Link n�o encontrado.\n";
		}
	}

	@Override
	public boolean done() {
		if (isFinished) {
			System.out.println("Fim execu��o agente Detran");
			myAgent.doDelete();
		} else {
		}
		return false;
	}
}