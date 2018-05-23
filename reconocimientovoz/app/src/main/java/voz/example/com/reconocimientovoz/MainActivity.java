package voz.example.com.reconocimientovoz;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecognitionListener{

    SpeechRecognizer spr;
    Intent mVozIntent;

    //tiempo limite: https://stackoverflow.com/questions/36868054/android-speech-recognition-limiting-listening-time

    List<String> mListaResultados;

    @BindView(R.id.tvResultados) TextView tvResultados;
    @BindView(R.id.btnEscuchar) Button btnEscuchar;
    @BindView(R.id.pbTiempo) ProgressBar pbTiempo;
    @BindView(R.id.tvTiempo) TextView tvTiempo;

    static final String TAG = "PruebaVoz";
    static final String TAG_R = "RESULTADO";
    final int pSegundos = 100;
    Handler handler = new Handler();
    boolean isListening;
    CountDownTimer ct;


    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
    public void dejardeEscuchar(){
        spr.stopListening();
        pbTiempo.setProgress(0);
        tvResultados.setText("no se escucho");
        btnEscuchar.setEnabled(true);
    }
    public void comenzarEscuchar(View view){
        spr.startListening(mVozIntent);
        isListening = true;
        btnEscuchar.setEnabled(false);

        //https://developer.android.com/reference/android/os/CountDownTimer
        ct = new CountDownTimer(6000,100){
            long fin;
            @Override
            public void onTick(long millisUntilFinished) {
                tvTiempo.setText(String.valueOf(millisUntilFinished /1000));
                pbTiempo.setProgress((int) millisUntilFinished);
                fin = millisUntilFinished;
            }
            @Override
            public void onFinish() {
                spr.stopListening();
                tvTiempo.setText("Termino");
                btnEscuchar.setEnabled(true);
                pbTiempo.setProgress(0);
                Log.i(TAG,"TerminoCounter");
            }
        };
        ct.start();
//        new Thread(new Runnable() {
//            int conteo = 0;
//            @Override
//            public void run() {
//                while (conteo <= pSegundos && isListening) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            pbTiempo.setProgress(conteo);
//                        }
//                    });
//                    try {Thread.sleep(50);} catch (InterruptedException ie) {ie.printStackTrace();}
//                    conteo++;
//                }
//                isListening =false;
//            }
//        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mListaResultados = new ArrayList<>();

        //iniciar speech2text
        spr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        spr.setRecognitionListener(this);
        mVozIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mVozIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        mVozIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mVozIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mVozIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 0);
        //mVozIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        //mVozIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        //mVozIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Comenzar Hablar");
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

//Reconocimiento de voz
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG,"onReadyForSpeech");
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG,"onBeginningOfSpeech");
    }
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG,"onRmsChanged - " + rmsdB);
    }
    @Override
    public void onEndOfSpeech() {
        Log.i(TAG,"onEndOfSpeech");
    }
    @Override
    public void onError(int error) {
        getErrorText(error);
    }
    @Override
    public void onResults(Bundle results) {
        Log.i(TAG,"onResults");
        mListaResultados = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String resultado = String.valueOf(mListaResultados.size()) + " onResults";
        for(String x : mListaResultados) {
            System.out.println(TAG_R + " : " + x);
            resultado = resultado + "\n" + x;
            tvResultados.setText(resultado);
        }
        btnEscuchar.setEnabled(true);
        ct.cancel();
    }
    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(TAG,"onPartialResults");
        mListaResultados = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String resultado = String.valueOf(mListaResultados.size()) + " onPartialResults";
        for(String x : mListaResultados) {
            System.out.println(TAG_R + " : " + x);
            resultado = resultado + "\n" + x;
            tvResultados.setText(resultado);
        }
        btnEscuchar.setEnabled(true);
        pbTiempo.setProgress(0);
    }
    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG,"onEvent");
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG,"onBufferReceived");
    }


}
