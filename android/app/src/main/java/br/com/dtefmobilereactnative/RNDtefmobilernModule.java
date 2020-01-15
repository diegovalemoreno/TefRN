package br.com.linx.dtefmobilereactnative;

import java.io.Console;
import android.util.Base64;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/*
import com.linx.dtefmobile.CDTEFMobile;
import com.linx.dtefmobile.CDTEFMobilePinpadVerifone;
import com.linx.dtefmobile.CDTEFMobilePromptX;
import com.linx.dtefmobile.CRetorno;
*/
import com.linx.dtefmobile.*;

//------------------------
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.Intent;

import android.graphics.Color;

import android.media.AudioManager;
import android.media.ToneGenerator;

import android.net.Uri;

import android.preference.PreferenceManager;

import android.text.InputFilter;
import android.text.TextWatcher;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.util.Log;

import android.os.Bundle;

import android.support.annotation.Nullable;

import org.json.JSONObject;

public class RNDtefmobilernModule extends ReactContextBaseJavaModule implements CDTEFMobilePromptX {
    private static final String TAG = "DTEFMobile";
    private static final String EVENTNAME = "DTEFMobileInput";
    private static final String BEEP = "beep";
    private final static String LIMPADISPLAYTERMINAL = "limpaDisplayTerminal";
    private final static String DISPLAYTERMINAL = "displayTerminal";
    private final static String DISPLAYERRO = "displayErro";
    private final static String MENSAGEM = "mensagem";
    private final static String SOLICITACONFIRMACAO = "solicitaConfirmacao";
    private final static String MENSAGEMALERTA = "mensagemAlerta";
    private final static String ENTRACARTAO = "entraCartao";
    private final static String ENTRADATAVALIDADE = "entraDataValidade";
    private final static String ENTRADATA = "entraData";
    private final static String ENTRACODIGOSEGURANCA = "entraCodigoSeguranca";
    private final static String SELECIONAOPCAO = "selecionaOpcao";
    private final static String SELECIONAOPCAOEX = "selecionaOpcaoEx";
    private final static String ENTRAVALOR = "entraValor";
    private final static String ENTRANUMERO = "entraNumero";
    private final static String ENTRASTRING = "entraString";
    private final static String ENTRAMASCARA = "entraMascara";
    private final static String OPERACAOCANCELADA = "operacaoCancelada";
    private final static String SETAOPERACAOCANCELADA = "setaOperacaoCancelada";
    private CDTEFMobile oDTEFMobile = null;
    private boolean bRespostaRecebida; // Adaptado do plugin cordova
    private CRetorno oRetornoRespostaRecebida; // Adaptado do plugin cordova
    private final ReactApplicationContext reactContext;

    public RNDtefmobilernModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNDtefmobilernModule";
    }

    @ReactMethod
    public void executeTransaction(String parameters, final Promise promise) {
        Log.v(TAG, "executeTransaction - init");
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        final CDTEFMobilePromptX oTransacaoActivity = this;
        oDTEFMobile.setActivity(this.getCurrentActivity());
        oDTEFMobile.setContext(reactContext.getApplicationContext());
        
        oRetornoRespostaRecebida = new CRetorno();

        Log.v(TAG, "executeTransaction - defineParametros");
        this.defineParametrosDTEFMobile(oDTEFMobile, parameters);
        Thread thread = new Thread() {
            @Override
            public void run() {
                Log.v(TAG, "Execute transaction");

                final int iResultado = oDTEFMobile.executeTransactionX(1, oTransacaoActivity);
                Log.v(TAG, "Executed transaction: " + iResultado);

                if (iResultado == 0) {
                    try {
                        Log.v(TAG, "Tentando gerar reolver a promise");
                        WritableMap res = Arguments.createMap();
                        res.putString("action", "");
                        res.putString("Comprovante", oDTEFMobile.getParameter("Comprovante"));
                        res.putString("NSU", oDTEFMobile.getParameter("NSU"));
                        res.putString("CodigoRede", oDTEFMobile.getParameter("CodigoRede"));
                        res.putString("CodigoBandeira", oDTEFMobile.getParameter("CodigoBandeira"));
                        res.putString("IndiceCodigoBandeira", oDTEFMobile.getParameter("IndiceCodigoBandeira"));
                        res.putString("IndiceCodigoRede", oDTEFMobile.getParameter("IndiceCodigoRede"));
                        res.putString("TipoOperacao", oDTEFMobile.getParameter("TipoOperacao"));
                        res.putString("ComprovanteCliente", oDTEFMobile.getParameter("ComprovanteCliente"));
                        res.putString("ComprovanteEstabelecimento", oDTEFMobile.getParameter("ComprovanteEstabelecimento"));
                        res.putString("ComprovanteReduzido", oDTEFMobile.getParameter("ComprovanteReduzido"));
                        res.putString("NSURede", oDTEFMobile.getParameter("NSURede"));
                        res.putString("CodigoResposta", oDTEFMobile.getParameter("CodigoResposta"));
                        res.putString("CodigoAutorizacao", oDTEFMobile.getParameter("CodigoAutorizacao"));
                        res.putString("NumeroParcelas", oDTEFMobile.getParameter("NumeroParcelas"));
                        res.putString("NomeBandeira", oDTEFMobile.getParameter("NomeBandeira"));
                        res.putString("NumeroCartao", oDTEFMobile.getParameter("NumeroCartao"));
                        res.putString("TipoFinanciamento", oDTEFMobile.getParameter("TipoFinanciamento"));
                        res.putString("DataFiscal", oDTEFMobile.getParameter("DataFiscal"));
                        res.putString("HoraFiscal", oDTEFMobile.getParameter("HoraFiscal"));
                        res.putString("CNPJRede", oDTEFMobile.getParameter("CNPJRede"));
                        res.putString("ValorDesconto", oDTEFMobile.getParameter("ValorDesconto"));
                        res.putString("ValorPago", oDTEFMobile.getParameter("ValorPago"));
                        res.putString("NUMPDV", oDTEFMobile.getParameter("NUMPDV"));
                        res.putString("NUMLOJA", oDTEFMobile.getParameter("NUMLOJA"));
                        res.putString("NUMESTAB", oDTEFMobile.getParameter("NUMESTAB"));
                        res.putString("NumeroPDV", oDTEFMobile.getParameter("NumeroPDV"));
                        res.putString("NumeroLoja", oDTEFMobile.getParameter("NumeroLoja"));
                        res.putString("NumeroEmpresa", oDTEFMobile.getParameter("NumeroEmpresa"));
                        Log.v(TAG, "Retornando promise");
                        promise.resolve(res);
                    } catch (Exception e) {
                        Log.v(TAG, "executaTransacao exception = " + e.getMessage());
                        Log.v(TAG, "Retornando promise");
                        promise.reject(e.getMessage());
                    }
                } else if (iResultado == 1) {
                    // necessario esta condicao para esperar retorno do modulo de pagamento da Rede no Poynt
                    Log.v(TAG, "onActivityResult Activity (processando pagamento...)");
                    Log.v(TAG, "Retornando promise");
                    promise.reject("NaoImplementado", "result deu 1");
                } else {
                    Log.v(TAG, "onActivityResult Activity.RESULT_CANCELED 2 MensagemErro");
                    Log.v(TAG, "onActivityResult Activity.RESULT_CANCELED 2 MensagemErro = " + oDTEFMobile.getParameter("MensagemErro"));
                    Log.v(TAG, "Retornando promise");
                    promise.reject(oDTEFMobile.getParameter("MensagemErro"), oDTEFMobile.getParameter("MensagemErro"));
                }
            }
        };
        thread.start();
    }

    @ReactMethod
    public void executeTransactionCPF(String parameters, final Promise promise) {
        Log.v(TAG, "executeTransaction - init CPF");
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        final CDTEFMobilePromptX oTransacaoActivity = this;
        oDTEFMobile.setActivity(this.getCurrentActivity());
        oDTEFMobile.setContext(reactContext.getApplicationContext());

        oRetornoRespostaRecebida = new CRetorno();

        Log.v(TAG, "executeTransaction - defineParametros CPF");
        this.defineParametrosDTEFMobile(oDTEFMobile, parameters);
        Thread thread = new Thread() {
            @Override
            public void run() {
        Log.v(TAG, "Execute transaction");

        final int iResultado = oDTEFMobile.executeTransactionX(1,  oTransacaoActivity);
        Log.v(TAG, "Executed transaction CPF: " + iResultado);

        if (iResultado == 0) {
            try {
                Log.v(TAG, "Tentando gerar resolver a promise CPF");
                WritableMap res = Arguments.createMap();
                res.putString("action", "");
                res.putString("bLimpaTela", oDTEFMobile.getParameter("bLimpaTela"));
                res.putString("colLabel", oDTEFMobile.getParameter("colLabel"));
                res.putString("linLabel", oDTEFMobile.getParameter("linLabel"));
                res.putString("linColeta", oDTEFMobile.getParameter("linColeta"));
                res.putString("limpaBuffer", oDTEFMobile.getParameter("limpaBuffer"));
                res.putString("label", oDTEFMobile.getParameter("label"));
                res.putString("mascara", oDTEFMobile.getParameter("mascara"));
                res.putString("stringEntrada", oDTEFMobile.getParameter("stringEntrada"));
                res.putString("permiteEntra", oDTEFMobile.getParameter("permiteEntra"));

                Log.v(TAG, "Retornando promise CPF");
                promise.resolve(res);
            } catch (Exception e) {
                Log.v(TAG, "executaTransacao exception = CPF " + e.getMessage());
                Log.v(TAG, "Retornando promise CPF");
                promise.reject(e.getMessage());
            }
        } else if (iResultado == 1) {
            // necessario esta condicao para esperar retorno do modulo de pagamento da Rede no Poynt
            Log.v(TAG, "onActivityResult Activity (processando pagamento...)");
            Log.v(TAG, "Retornando promise");
            promise.reject("NaoImplementado", "result deu 1");
        } else {
            Log.v(TAG, "onActivityResult Activity.RESULT_CANCELED 2 MensagemErro");
            Log.v(TAG, "onActivityResult Activity.RESULT_CANCELED 2 MensagemErro = " + oDTEFMobile.getParameter("MensagemErro"));
            Log.v(TAG, "Retornando promise");
            promise.reject(oDTEFMobile.getParameter("MensagemErro"), oDTEFMobile.getParameter("MensagemErro"));
        }
            }
        };
        thread.start();
    }
    @ReactMethod
    public void undoTransaction(final String nsu, final Promise promise) {
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        final CDTEFMobilePromptX promptX = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                oDTEFMobile.setPromptX(promptX);
                oDTEFMobile.undoTransaction(Integer.parseInt(nsu));
                oDTEFMobile.finalizeTransaction();
                WritableMap res = Arguments.createMap();
                promise.resolve(res);
            }
        };
        thread.start();
    }

    @ReactMethod
    public void confirmTransaction(final String nsu, final Promise promise) {
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        final CDTEFMobilePromptX promptX = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                oDTEFMobile.setPromptX(promptX);
                oDTEFMobile.confirmTransaction(Integer.parseInt(nsu));
                oDTEFMobile.finalizeTransaction();
                WritableMap res = Arguments.createMap();
                promise.resolve(res);
            }
        };
        thread.start();
    }

    @ReactMethod
    public void finalizeTransaction(final Promise promise) {
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        final CDTEFMobilePromptX promptX = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                oDTEFMobile.setPromptX(promptX);
                oDTEFMobile.finalizeTransaction();
                WritableMap res = Arguments.createMap();
                promise.resolve(res);
            }
        };
        thread.start();
    }

    @ReactMethod
    public void sendResponse(ReadableMap data) {
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        // Recebe retorno do JS
        Log.v(TAG, "setRespostaRecebida");

        int resultado;
        try {
            resultado = Integer.parseInt(data.getString("resultado"));
        } catch (Exception e) {
            resultado = 0;
        }
        int intRetorno;
        try {
            intRetorno = Integer.parseInt(data.getString("intRetorno"));
        } catch (Exception e) {
            intRetorno = 0;
        }
        String stringRetorno = data.getString("stringRetorno");
        oRetornoRespostaRecebida.setResultado(resultado);
        oRetornoRespostaRecebida.setIntRetorno(intRetorno);
        oRetornoRespostaRecebida.setStringRetorno(stringRetorno);
        setRespostaRecebida(true);
        oDTEFMobile.setRespostaRecebida(true);
    }

    private void defineParametrosDTEFMobile(CDTEFMobile oDTEFMobile, String parameters)  {
        if(oDTEFMobile == null)
            oDTEFMobile = new CDTEFMobile();
        // Adaptado do plugin cordova
        String[] pars = parameters.split(";");
        for (String par : pars) {

            Log.v(TAG, "DefineParametrosDTEFMobile par=" + par);

            String[] keyvalues = par.split("=");
            String key = keyvalues[0];
            String value = keyvalues[1];

            Log.v(TAG, "DefineParametrosDTEFMobile key=" + key + " - value=" + value);
            oDTEFMobile.setParameter(key, value);
        }
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        setRespostaRecebida(false);
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    private WritableMap createJSONResponse(String functionName) {
        // cria um objeto que sera enviado para o RN (JS), semelhante ao formato do cordova
        WritableMap resp = Arguments.createMap();
        resp.putString("action", "InputCallback");
        resp.putString("function", functionName);
        return resp;
    }

    private CRetorno aguardaRecebeResposta(String nomeFuncao) {
        // Adaptado do plugin cordova
        Log.v(TAG, "aguardaRecebeResposta - " + nomeFuncao + " inicio bRespostaRecebida = " + bRespostaRecebida);
        while (!bRespostaRecebida) {
            try {
                // Log.v(TAG, "aguardando resposta - " + nomeFuncao + " resposta recebida = " + bRespostaRecebida);
                Thread.sleep(20);
            } catch (Exception e) {
            }
        }

        CRetorno oRetorno = new CRetorno();
        oRetorno.setResultado(oRetornoRespostaRecebida.getResultado());
        oRetorno.setStringRetorno(oRetornoRespostaRecebida.getStringRetorno());
        oRetorno.setIntRetorno(oRetornoRespostaRecebida.getIntRetorno());

        Log.v(TAG, "aguardaRecebeResposta = resultado = " + oRetorno.getResultado());
        Log.v(TAG, "aguardaRecebeResposta = stringRetorno = " + oRetorno.getStringRetorno());
        Log.v(TAG, "aguardaRecebeResposta = intRetorno = " + oRetorno.getIntRetorno());

        return oRetorno;
    }

    private synchronized void setRespostaRecebida(boolean value) {
        // Adaptado do plugin cordova
        Log.v(TAG, "setRespostaRecebida = " + value);
        bRespostaRecebida = value;
    }

    @Override
    public void beep() {
        Log.v(TAG, BEEP);
        WritableMap params = createJSONResponse(BEEP);
        sendEvent(EVENTNAME, params);
        aguardaRecebeResposta(BEEP);
    }

    @Override
    public void limpaDisplayTerminal() {
        Log.v(TAG, LIMPADISPLAYTERMINAL);
        WritableMap params = createJSONResponse(LIMPADISPLAYTERMINAL);
        sendEvent(EVENTNAME, params);
        aguardaRecebeResposta(LIMPADISPLAYTERMINAL);
    }

    @Override
    public void displayTerminal(String mensagem) {
        Log.v(TAG, DISPLAYTERMINAL + mensagem);
        WritableMap params = createJSONResponse(DISPLAYTERMINAL);
        params.putString("mensagem", mensagem);
        sendEvent(EVENTNAME, params);
        aguardaRecebeResposta(DISPLAYTERMINAL);
    }

    @Override
    public void displayErro(String mensagem) {
        Log.v(TAG, DISPLAYERRO + mensagem);
        WritableMap params = createJSONResponse(DISPLAYERRO);
        params.putString("mensagem", mensagem);
        sendEvent(EVENTNAME, params);
        aguardaRecebeResposta(DISPLAYERRO);
    }

    @Override
    public void mensagem(String mensagem) {
        Log.v(TAG, MENSAGEM + mensagem);
        WritableMap params = createJSONResponse(MENSAGEM);
        params.putString("mensagem", mensagem);
        sendEvent(EVENTNAME, params);
        aguardaRecebeResposta(MENSAGEM);
    }

    @Override
    public int solicitaConfirmacao(String mensagem) {
        Log.v(TAG, SOLICITACONFIRMACAO + mensagem);
        WritableMap params = createJSONResponse(SOLICITACONFIRMACAO);
        params.putString("mensagem", mensagem);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(SOLICITACONFIRMACAO).getResultado();
    }

    @Override
    public void mensagemAlerta(String mensagem) {
        Log.v(TAG, MENSAGEMALERTA + mensagem);
        WritableMap params = createJSONResponse(MENSAGEMALERTA);
        params.putString("mensagem", mensagem);
        sendEvent(EVENTNAME, params);
        aguardaRecebeResposta(MENSAGEMALERTA);
    }

    @Override
    public CRetorno entraCartao(String label, String numeroCartao) {
        Log.v(TAG, ENTRACARTAO + label);
        WritableMap params = createJSONResponse(ENTRACARTAO);
        params.putString("label", label);
        params.putString("numeroCartao", numeroCartao);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRACARTAO);
    }

    @Override
    public CRetorno entraDataValidade(String label, String dataValidade) {
        Log.v(TAG, ENTRADATAVALIDADE + label);
        WritableMap params = createJSONResponse(ENTRADATAVALIDADE);
        params.putString("label", label);
        params.putString("dataValidade", dataValidade);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRADATAVALIDADE);
    }

    @Override
    public CRetorno entraData(String label, String data) {
        Log.v(TAG, ENTRADATA + label);
        WritableMap params = createJSONResponse(ENTRADATA);
        params.putString("label", label);
        params.putString("data", data);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRADATA);
    }

    @Override
    public CRetorno entraCodigoSeguranca(String label, String codigoSeguranca, int tamanhoMax) {
        Log.v(TAG, ENTRACODIGOSEGURANCA + label);
        WritableMap params = createJSONResponse(ENTRACODIGOSEGURANCA);
        params.putString("label", label);
        params.putString("codigoSeguranca", codigoSeguranca);
        params.putInt("tamanhoMax", tamanhoMax);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRACODIGOSEGURANCA);
    }

    @Override
    public CRetorno selecionaOpcao(String label, String opcoes, int opcao) {
        Log.v(TAG, SELECIONAOPCAO + label);
        WritableMap params = createJSONResponse(SELECIONAOPCAO);
        params.putString("label", label);
        params.putString("opcoes", opcoes);
        params.putInt("opcao", opcao);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(SELECIONAOPCAO);
    }

    @Override
    public CRetorno selecionaOpcaoEx(String label, String opcoes, int opcaoSelecionada, String identificador) {
        Log.v(TAG, SELECIONAOPCAOEX + label);
        WritableMap params = createJSONResponse(SELECIONAOPCAOEX);
        params.putString("label", label);
        params.putString("opcoes", opcoes);
        params.putInt("opcaoSelecionada", opcaoSelecionada);
        params.putString("identificador", identificador);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(SELECIONAOPCAOEX);
    }

    @Override
    public CRetorno entraValor(String label, String valor, String valorMinimo, String valorMaximo) {
        Log.v(TAG, ENTRAVALOR + label);
        WritableMap params = createJSONResponse(ENTRAVALOR);
        params.putString("label", label);
        params.putString("valor", valor);
        params.putString("valorMinimo", valorMinimo);
        params.putString("valorMaximo", valorMaximo);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRAVALOR);
    }

    @Override
    public CRetorno entraNumero(String label, String numero, String numeroMinimo, String numeroMaximo, int minimoDigitos, int maximoDigitos, int digitosExatos) {
        Log.v(TAG, ENTRANUMERO + label);
        WritableMap params = createJSONResponse(ENTRANUMERO);
        params.putString("label", label);
        params.putString("numero", numero);
        params.putString("numeroMinimo", numeroMinimo);
        params.putString("numeroMaximo", numeroMaximo);
        params.putInt("minimoDigitos", minimoDigitos);
        params.putInt("maximoDigitos", maximoDigitos);
        params.putInt("digitosExatos", digitosExatos);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRANUMERO);
    }

    @Override
    public CRetorno entraString(String label, int limpaBuffer, String stringEntrada, int minimoDigitos, int maximoDigitos, int coletaSecreta) {
        Log.v(TAG, ENTRASTRING + label);
        WritableMap params = createJSONResponse(ENTRASTRING);
        params.putString("label", label);
        params.putInt("limpaBuffer", limpaBuffer);
        params.putString("stringEntrada", stringEntrada);
        params.putInt("minimoDigitos", minimoDigitos);
        params.putInt("maximoDigitos", maximoDigitos);
        params.putInt("coletaSecreta", coletaSecreta);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRASTRING);
    }

    @Override
    public CRetorno entraCartao(String arg1, String arg2, int arg3, int arg4) {
        // metodo não utilizado necessáriamente
        return null;
    }


    @Override
    public CRetorno entraMascara(boolean bLimpaTela, int colLabel, int linLabel, int linColeta, boolean limpaBuffer, String label, String mascara, String stringEntrada, boolean permiteEntra) {
        Log.v(TAG, ENTRAMASCARA );
        WritableMap params = createJSONResponse(ENTRAMASCARA);
        params.putBoolean("bLimpaTela", bLimpaTela);
        params.putInt("colLabel", colLabel);
        params.putInt("linLabel", linLabel);
        params.putBoolean("limpaBuffer", limpaBuffer);
        params.putString("label", label);
        params.putString("mascara", mascara);
        params.putString("stringEntrada", stringEntrada);
        params.putBoolean("permiteEntra", permiteEntra);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(ENTRAMASCARA);
    }

    @Override
    public int operacaoCancelada() {
        Log.v(TAG, OPERACAOCANCELADA);
        WritableMap params = createJSONResponse(OPERACAOCANCELADA);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(OPERACAOCANCELADA).getResultado();
    }

    @Override
    public int setaOperacaoCancelada(boolean cancelada) {
        Log.v(TAG, SETAOPERACAOCANCELADA);
        WritableMap params = createJSONResponse(SETAOPERACAOCANCELADA);
        params.putBoolean("cancelada", cancelada);
        sendEvent(EVENTNAME, params);
        return aguardaRecebeResposta(SETAOPERACAOCANCELADA).getResultado();
    }
}