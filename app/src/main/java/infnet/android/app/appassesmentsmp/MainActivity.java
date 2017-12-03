package infnet.android.app.appassesmentsmp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import infnet.android.app.appassesmentsmp.utils.ValidaCPF;

public class MainActivity extends AppCompatActivity {

    public static final String AD_UNIT_ID = "ca-app-pub-2446788647018391/2226429123";
    public static final String FILE_NAME = "cadastro";
    public static final String SUCCESS_MSG = "Cadastro efetuado com sucesso";
    public static final String ERROR_MSG = "Ocorreu um erro, tente novamente";


    EditText edtNome, edtEmail, edtSenha, edtConfirmaSenha, edtCpf;
    Button btnCadastrar;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNome = (EditText) findViewById(R.id.input_nome);
        edtEmail = (EditText) findViewById(R.id.input_email);
        edtSenha = (EditText) findViewById(R.id.input_senha);
        edtConfirmaSenha = (EditText) findViewById(R.id.input_c_senha);
        edtCpf = (EditText) findViewById(R.id.input_cpf);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastrar);

        getSupportActionBar().setTitle("Cadastre-se");

        // Anuncio
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AD_UNIT_ID);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()) {

                    salvarDados();

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
            }
        });


    }

    private void salvarDados() {
        String lstrNomeArq;
        byte[] dados;
        try {
            dados = montarDados();

            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(dados);
            fos.flush();
            fos.close();

            sendMessenger(SUCCESS_MSG);

        } catch (Exception e) {
            sendMessenger(ERROR_MSG);
        }
    }

    private byte[] montarDados() {
        StringBuilder dados = new StringBuilder();
        dados.append("nome: ").append(edtNome.getText().toString()).append("\n");
        dados.append("email: ").append(edtEmail.getText().toString()).append("\n");
        dados.append("senha: ").append(edtSenha.getText().toString()).append("\n");
        dados.append("cpf: ").append(ValidaCPF.imprimeCPF(edtCpf.getText().toString()));
        return dados.toString().getBytes();
    }

    private boolean validarCampos() {
        boolean result = true;
        List<EditText> campos = getCampos();
        for (EditText campo : campos) {
            if (campo.getText().toString().trim().equals("") || campo.getText().toString() == null) {
                campo.setError("Este campo é obrigatório");
                result = false;
                break;
            } else {
                // valida nome
                if (Pattern.compile("[^a-z0-9._]\\\\").matcher(edtNome.getText().toString()).find()) {
                    edtNome.setError("Caracteres especiais não são permitidos");
                    result = false;
                }
                if (!validarEmail()) {
                    edtEmail.setError("Formato correto: usuario@email.com");
                    result = false;
                }
                if (!edtSenha.getText().toString().equals(edtConfirmaSenha.getText().toString())) {
                    edtConfirmaSenha.setError("Senha e Confirmar Senha são diferentes");
                    result = false;
                }
                if (!ValidaCPF.isCPF(edtCpf.getText().toString())) {
                    edtCpf.setError("CPF deve conter 11 digitos");
                    result = false;
                }
            }
        }

        return result;
    }

    private boolean validarEmail() {
        String email = edtEmail.getText().toString();

        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }

    private List<EditText> getCampos() {
        List<EditText> campos = new ArrayList<EditText>();
        campos.add(edtNome);
        campos.add(edtEmail);
        campos.add(edtSenha);
        campos.add(edtConfirmaSenha);
        campos.add(edtCpf);

        return campos;
    }

    private void sendMessenger(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

}
