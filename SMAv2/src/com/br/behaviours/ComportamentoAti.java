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

public class ComportamentoAti extends SimpleBehaviour {
	private static final long serialVersionUID = 1L;
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	public ACLMessage msgAgente;
	public int contAti = 0;
	private boolean resp = false;
	private int countLine = 0;
	private String line;
	private String retornoFinal = new String();
	public boolean isFinished = false;

	public ComportamentoAti(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		if (contAti == 0) {
			enviaPrimeiraMsg();
		} else {
			msgAgente = myAgent.receive(mt);
			if (msgAgente != null) {
				if (msgAgente.getContent().contains("Ciente")) {
					System.out.println(msgAgente.getContent());
					try {
						evaluateAti();
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
		priMsgAgente.setContent(myAgent.getLocalName() + ": Iniciando avaliação.");
		myAgent.send(priMsgAgente);
		contAti++;
	}

	private void evaluateAti() throws IOException {
		setDespesaComArquivos();

		setDespesaComArquivosExtensao();

		setConveniosExtensoes();

		setAcoesProgramasDocumentos();

		setContratosDocumentos();

		setServidoresComArquivos();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(msgAgente.getSender());
		msg.setContent(retornoFinal);
		myAgent.send(msg);
		isFinished = true;
	}

	private void setServidoresComArquivos() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		String link;
		String linkVar;
		resp = false;
		url = new URL("http://www.lai.pe.gov.br/web/ati/servidores-com-arquivos");
		is = url.openStream(); // throws an IOException
								// http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia
		br = new BufferedReader(new InputStreamReader(is));
		link = "http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia/despesa2";
		linkVar = "href=" + "\"" + link + "\"";
		String quantServidores = "<b>Quantitativo Servidores</b>";
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
						"http://www.lai.pe.gov.br/web/ati/servidores-com-arquivos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=162334");
				is = url.openStream();
				br = new BufferedReader(new InputStreamReader(is));

				while ((line = br.readLine()) != null) {
					countLine++;
					// System.out.println("Procurando...");
					if (line.contains(".ods")) {
						// resp = true;
						// this.line = line;
						temPastaQtServ = true;
						break;
					}
				}
				// break;
			}

			if (line.contains(planoCargosCarreiras)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/ati/servidores-com-arquivos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=161785");
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
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Servidores com Arquivos': Link encontrado. \n";
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'Servidores com Arquivos': Link encontrado e Arquivos inseridos corretamente. \n" + "\n";
		} else {
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'Servidores com Arquivos': Link e documentos não foram encontrados. \n" + "\n";
		}
	}

	private void setContratosDocumentos() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/ati/contratos");
		is = url.openStream(); // throws an IOException
								// http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia
		br = new BufferedReader(new InputStreamReader(is));
		String folderContratos = "<b>Contratos</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderContratos)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/ati/contratos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=23562");
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
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Contratos': Documentos inseridos corretamente."
					+ "\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Contratos': Documentos inseridos erroneamente."
					+ "\n";
		}
	}

	private void setAcoesProgramasDocumentos() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/ati/acoes-e-programas");
		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		String folderAcoesProgramasName = "<b>Relatórios de Desempenho da Gestão</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderAcoesProgramasName)) {
				url = new URL(
						"http://www.lai.pe.gov.br/web/ati/acoes-e-programas?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=192187");
				is = url.openStream();
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
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'RDG': Documentos inseridos corretamente." + "\n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'RDG': Documentos inseridos erroneamente." + "\n";
		}
	}

	private void setConveniosExtensoes() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/ati/convenios");
		is = url.openStream(); // throws an IOException
								// http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia
		br = new BufferedReader(new InputStreamReader(is));
		String folderConvenioName = "<b>Terceirizados</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderConvenioName)) {
				// via navegacao, nao ha pastas! via url existe.
				url = new URL(
						"http://www.lai.pe.gov.br/web/ati/convenios?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=129488");
				is = url.openStream(); // throws an IOException
										// http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia
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
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Convenios': Extensões válidas." + "\n";
			// System.out.println(this.getAgent().getLocalName() + ": Tudo certo
			// nas extensoes de Convernios!");
			// System.out.println("Resultado: " + this.line);
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Convenios': Extensões inválidas." + "\n";
			// System.out.println(this.getAgent().getLocalName() + ": Convenios
			// não está correta!");
		}
	}

	private void setDespesaComArquivosExtensao() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		url = new URL("http://www.lai.pe.gov.br/web/ati/despesa-com-arquivos");
		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		String folderName = "<b>Terceirizados</b>";
		while ((line = br.readLine()) != null) {
			countLine++;
			if (line.contains(folderName)) {

				url = new URL(
						"http://www.lai.pe.gov.br/web/ati/despesa-com-arquivos?p_p_id=publicador_repositorio_documento&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-11&p_p_col_pos=1&p_p_col_count=2&_publicador_repositorio_documento_struts_action=%2Fpublicador_repositorio_documento%2Fview_pub&_publicador_repositorio_documento_folderId=230468");
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
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'Execução Orçamentária': Arquivos com extensão válidas.\n";
		} else {
			retornoFinal += this.getAgent().getLocalName()
					+ ": Aba 'Execução Orçamentária': Arquivos com extensão inválidas. \n";
		}
	}

	private void setDespesaComArquivos() throws MalformedURLException, IOException {
		URL url;
		InputStream is;
		BufferedReader br;
		String line;
		String link;
		String linkVar;
		url = new URL("http://www.lai.pe.gov.br/web/ati/despesa-com-arquivos");
		is = url.openStream(); // throws an IOException
								// http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia
		br = new BufferedReader(new InputStreamReader(is));
		link = "http://www2.portaldatransparencia.pe.gov.br/web/portal-da-transparencia/despesa2";
		linkVar = "href=" + "\"" + link + "\"";
		while ((line = br.readLine()) != null) {
			countLine++;

			if (line.contains(linkVar)) {
				resp = true;
				this.line = line;
				break;
			}
		}

		if (resp) {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Execução Orçamentária': Link encontrado. \n";
		} else {
			retornoFinal += this.getAgent().getLocalName() + ": Aba 'Execução Orçamentária': Link não encontrado \n";
		}
	}

	@Override
	public boolean done() {
		if (isFinished) {
			System.out.println("Fim execução agente ATI.");
			myAgent.doDelete();
		} else {
		}
		return false;
	}
}