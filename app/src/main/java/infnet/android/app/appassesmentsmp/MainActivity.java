package infnet.android.app.appassesmentsmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    public static final String AD_UNIT_ID = "ca-app-pub-2446788647018391/2226429123";
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
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    doSomeThing();
                }
            }
        });


    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void doSomeThing() {
        Toast.makeText(this, "Show Interstitial Ad", Toast.LENGTH_SHORT);
    }
}
