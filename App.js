import React, { Component } from 'react';
import {
	Platform,
	StyleSheet,
	Text,
	Button,
	View,
	TextInput,
	DeviceEventEmitter
} from 'react-native';

import ToastModule from './ToastJsModule'
import PhoneInfoModule from './PhoneInfoJsModule'
import RNDtefmobilernModule from './RNDtefmobilernJsModule'


export default class App extends Component {
	state = {
		msg: "", // mensagem/label que deve ser apresentada durante a transacao
		emTransacao: false,  // se esta em transacao
		transacaoCancelada: false, // se o usuário selecionou a opcao de cancela a transacao
		opcoes: [], // lista de opcoes que deve apresentar ao usuario para escolher uma
		transacaoObj: {}, // dados da transacao pendente
		input: "", // armazena os dados que eh solicitado digitacao do usuario
		inputConfig: {}, // configuracao do input/texto que deve ser apresentado (como tipo de entrada, mascara, tamanho maximo/minimo etc.)
	};


	iniciarTransacao = async () => {
		this.setState({ emTransacao: true });
		// TODO ao iniciar uma transacao, deve impedir que inicie outra, pois caso inicia uma outra transacao, ocorre erro
		// Parametros enviados em uma string separados por ;
		// Ver documentacao do DTEFMobile para detalhes dos paramentros aceitos
		// verificar os tipos de transacao como debito, credito, consulta/validacao de dados do PDV e outros tipos tambem na documentacao
		try {
			const promise = await RNDtefmobilernModule.executeTransaction("CupomFiscal=00001;IPServidor=172.16.138.68;PortaServidor=6850;CNPJAutomacao=17551152519415;PinPadAtivo=1;NUMLOJA=0014;NUMPDV=0312;NUMESTAB=0001;CNPJESTAB=17551152519415;TipoServidorTEF=0;TipoInterface=0;CodigoTransacao=1");
			console.log("Promise: [" + JSON.stringify(promise) + "]");
			RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
			this.setState({ transacaoObj: promise })
			// No transacaoObj contem todos os dados da transacao como NSU, comprovantes, bandeira etc.
		} catch (e) {
			console.log(e);
			this.setState({ msg: "", emTransacao: false, transacaoCancelada: false, opcoes: [], transacaoObj: {}, input: "", inputConfig: {} })
		}
	}

	cancelarTransacao = () => {
		this.setState({ transacaoCancelada: true });
	}

	selecionarOpcao = (ix, opcao) => {
		RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": ix, "stringRetorno": opcao });
		this.setState({ opcoes: [] });
	}

	enviarInput = (input) => {
		RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": input });
		this.setState({ input: "", inputConfig: {} });
	}

	confirmarTransacao = async (nsu) => {
		RNDtefmobilernModule.confirmTransaction(nsu);
		this.setState({ msg: "", emTransacao: false, opcoes: [], transacaoCancelada: false, transacaoObj: {}, input: "", inputConfig: {} });
	}

	desfazerTransacao = async (nsu) => {
		RNDtefmobilernModule.undoTransaction(nsu);
		this.setState({ msg: "", emTransacao: false, opcoes: [], transacaoCancelada: false, transacaoObj: {}, input: "", inputConfig: {} });
	}

	handleDTEFEvent = (e) => {
		console.log(e);
		if (e.action === "InputCallback") {
			switch (e["function"]) {
				case "beep":  // solicita aviso sonoro do app, pode ser desconsiderado em algumas situacaoes...
					// this.setState({msg: "BEEP!"});
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "limpaDisplayTerminal": // limpa mensagem enviada anteriormente
					this.setState({ msg: "" });
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "displayTerminal": // mensagem deve ser apresentada ao usuario
					console.log("respondendo");
					this.setState({ msg: e.mensagem });
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "displayErro": // apresenta mensagem de erro (geralmente em um dialog)
					this.setState({ msg: e.mensagem });
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "mensagem": // mensagem deve ser apresentada ao usuario
					this.setState({ msg: e.mensagem });
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "solicitaConfirmacao": // dialog sim/nao
					this.setState({ msg: e.mensagem });
					// deve apresentar um dialog e enviar a resposta apenas quando o usuario clicar no botao...
					// RNDtefmobilern.sendResponse({"resultado": "0", "intRetorno": "0", "stringRetorno": ""});
					break;

				case "mensagemAlerta": // apresenta mensagem de alerta (geralmente em um dialog)
					this.setState({ msg: e.mensagem });
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "entraCartao": // solicita usuario digitar um numero de cartao
					this.setState({ msg: e.label, inputConfig: { tipo: "entraCartao" } });
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "entraDataValidade": // solicita usuario digitar data de validade de cartao (geralmente MM/AA)
					this.setState({ msg: e.label, inputConfig: { tipo: "entraDataValidade" } });
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "entraData": // solicita usuario digitar uma data (geralmente DD/MM/AA)
					this.setState({ msg: e.label, inputConfig: { tipo: "entraData" } });
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "entraCodigoSeguranca": // solicita usuario digitar o codigo de segunranca de um cartao
					this.setState({ msg: e.label, inputConfig: { tipo: "entraCodigoSeguranca", tamanhoMax: e.tamanhoMax } });
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "selecionaOpcao": // solicita usuario escolher uma opcao
					this.setState({ msg: e.label });
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "selecionaOpcaoEx": // solicita usuario escolher uma opcao (versao extendida)
					// adaptado do pista
					/* result.opcoes eh uma string no formato:
					 * <opcoes> := <opt>[#opt]*
					 * <opt>	:= (\d+,"\s+", \s+)
					 * Entao primeiro fazemos o split(#) para pegar cada opt, removemos os () de cada opt com slice
					 * e tambem removemos as aspas do texto da mesma forma.
					 */
					let opcoes = e.opcoes.split("#").map(op => {
						return op.slice(1, -1).split(",");
					});

					this.setState({ msg: e.label, opcoes: opcoes });
					break;

				case "entraValor": // solicita usuario digitar um valor (monetario)
					this.setState({ msg: e.label, inputConfig: { tipo: "entraValor", valorMinimo: e.valorMinimo, valorMaximo: e.valorMaximo } });
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "entraNumero": // solicita usuario digitar um numero (ex:: numero de parcelas)
					this.setState({
						msg: e.label, inputConfig: {
							tipo: "entraNumero",
							numeroMinimo: e.numeroMinimo,
							numeroMaximo: e.numeroMaximo,
							minimoDigitos: e.minimoDigitos,
							maximoDigitos: e.maximoDigitos,
							digitosExatos: e.digitosExatos
						}
					});
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "entraString": // solicita usuario digitar um texto...
					this.setState({
						msg: e.label, inputConfig: {
							tipo: "entraString",
							limpaBuffer: e.limpaBuffer, stringEntrada: e.stringEntrada,
							minimoDigitos: e.minimoDigitos, maximoDigitos: e.maximoDigitos,
							coletaSecreta: e.coletaSecreta
						}
					});
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "entraMascara": // solicita usuario digitar um texto com uma mascara que deve ser aplicada no input
					this.setState({
						msg: e.label, inputConfig: {
							tipo: "entraMascara",
							bLimpaTela: e.bLimpaTela, colLabel: e.colLabel, linLabel: e.linLabel,
							limpaBuffer: e.limpaBuffer, mascara: e.mascara,
							stringEntrada: e.stringEntrada, permiteEntra: e.permiteEntra
						}
					});
					// apenas enviar resposta apos o usuario digitar e confirmar...
					break;

				case "operacaoCancelada": // questiona se a operacao foi cancelada pelo usuario
					// deve verificar se o usuario apertou a opcao de cancelar e enviar...
					// o dtef fica chamando essa funcao varias vezes
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": (this.state.transacaoCancelada) ? "1" : "0", "intRetorno": "0", "stringRetorno": "" });
					break;

				case "setaOperacaoCancelada": // confirma se a operacao deve ser cancelada
					// No pista eh fixo, pois o usuario clicou em cancelar e confirmou ja
					console.log("respondendo");
					RNDtefmobilernModule.sendResponse({ "resultado": "0", "intRetorno": "0", "stringRetorno": "" });
					break;
			}
		}
	}

	componentWillMount = () => {
		this.dtefevent = DeviceEventEmitter.addListener('DTEFMobileInput', this.handleDTEFEvent);
	}

	componentWillUnmount = () => {
		this.dtefevent.remove();
	}

	render() {
		return (
			<View style={styles.container}>
				<Text style={styles.welcome}> Linx </Text>
				<Text style={styles.instructions}> {this.state.msg} </Text>
				{this.state.opcoes.map((op) => {
					return (
						// op[0] = indice, op[1] = label, op[2] = codigo
						// remove o primeiro e ultimo caracter do label, pois são "
						<Button style={styles.button} key={op[0]} title={op[1].slice(1, -1)} onPress={() => { this.selecionarOpcao(op[0], op[2]) }} />
					);
				})}
				{this.state.inputConfig.tipo &&
					// Nesse exemplo, nao esta considerando os tipos de inputs e outras configs...
					[<TextInput key={"input"}
						onChangeText={(input) => this.setState({ input })}
						value={this.state.input}
					/>,
					<Button style={styles.button} key={"enviar"} title="Confirmar" onPress={() => this.enviarInput(this.state.input)} />]
				}
				{this.state.transacaoObj.NSU &&
					/* Se tem transacaoObj eh por que ja foi executado a transacao e agora deve confirmar ou desfazer ela
					 * Nos sistemas, apos fazer a transacao, eh emitido a nota/cupom e depois confirmado a transacao ou caso ocorra
					 * algum erro na emissao, a transacao deve ser desfeita. Para fins de testes, nesse app apresenta as opcoes para
					 * confirmar ou desfazer
					 * */
					[<Button style={styles.button} key={"confirma"} title="Confirmar transação" onPress={() => { this.confirmarTransacao(this.state.transacaoObj.NSU) }} />,
					<Button style={styles.button} key={"desfaz"} title="Desfazer transação" onPress={() => { this.desfazerTransacao(this.state.transacaoObj.NSU) }} />]
				}
				{!this.state.emTransacao && !this.state.inputConfig.tipo ?
					<Button style={styles.button} title="Iniciar transação" onPress={() => { this.iniciarTransacao() }} /> :
					<Button style={styles.button} title="Cancelar" onPress={() => { this.cancelarTransacao() }} />
				}
			</View>
		);
	}
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: 'center',
		alignItems: 'center',
		backgroundColor: '#F5FCFF',
	},
	welcome: {
		fontSize: 20,
		textAlign: 'center',
		margin: 20,
	},
	instructions: {
		textAlign: 'center',
		color: '#333333',
		marginBottom: 15,
	},
	button: {
		color: '#AE1285',
		margin: 60,
	}
});
