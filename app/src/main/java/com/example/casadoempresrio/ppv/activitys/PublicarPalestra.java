package com.example.casadoempresrio.ppv.activitys;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.MyApp;
import com.example.casadoempresrio.ppv.ferramentas.RealmControl;
import com.example.casadoempresrio.ppv.ferramentas.Tools;
import com.example.casadoempresrio.ppv.objetos.Palestra;
import com.example.casadoempresrio.ppv.objetos.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PublicarPalestra extends AppCompatActivity {
    private String palestraId;
    private Palestra p;
    private String palestranteNome;
    private Usuario palestrante;

    private EditText campoData;
    Calendar calendar;
    int my_dayOfMonth;
    int my_month;
    int my_year;
    Date data;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_palestra);

        campoData = findViewById(R.id.campoData_publicar);

        Intent it = getIntent();
        palestraId = it.getStringExtra("palestra_id");

        configuraToolBar();
        configuraSelectCategoria();


        p = RealmControl.getRealm().where(Palestra.class).equalTo("id", palestraId).findFirst();

        // quem postou a palestra se tornara o palestrante
        palestrante = p.getDonoDaPalestra();
        palestranteNome = palestrante.getPrimeiroNome();

        ((EditText) findViewById(R.id.campoTitulo_publicar)).setText(p.getTitulo());
        ((EditText) findViewById(R.id.campoDescricao_publicar)).setText(p.getDescricao());
        ((EditText) findViewById(R.id.campoPubAlvo_publicar)).setText(p.getPublicoAlvo());

        List<String> aux = Palestra.getCATEGORIAS();
        int indexCat = aux.indexOf(p.getCategoria());
        ((Spinner) findViewById(R.id.campoCategoria_publicar)).setSelection(indexCat);

        ((TextView) findViewById(R.id.palestrante_publicar)).setText(palestranteNome);
    }

    void configuraToolBar(){
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_publicar_pal);
        setSupportActionBar(my_toolbar);

        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MyApp.getAppContext(), VisualizaPalestra.class);
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                it.putExtra("id", palestraId);
                startActivity(it);
                finish();
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setTitle("Publicar Palestra");
        // ativa a seta de voltar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    void configuraSelectCategoria(){
        Spinner cat =  findViewById(R.id.campoCategoria_publicar);
        String[] opcoes = Palestra.getCATEGORIAS().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        cat.setAdapter(adapter);
    }

    // adiciona os icones na barra de tarefa
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_itens, menu);
        return true;
    }

    // trata os cliques da barra de tarefas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miRefresh:
                return true;

            case R.id.miDeslogar:
                Tools.getInstance().logOut(this);
                return true;

            case R.id.miMinhaConta:
                Tools.getInstance().cliqueMinhaConta(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // atualiza a barra de tarefas
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Tools.getInstance().atualizaToolBarMenus(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //realm.close();
    }

    public void publicar(View view) {
        EditText campoTitulo, campoConteudo, campoDescricao, campoDuracao, campoPubAlvo, campoWebsite;
        Spinner campoCategoria;
        campoTitulo =  findViewById(R.id.campoTitulo_publicar);
        campoConteudo =  findViewById(R.id.campoConteudo_publicar);
        campoDescricao = findViewById(R.id.campoDescricao_publicar);
        campoDuracao = findViewById(R.id.campoDuracao_publicar);
        campoPubAlvo = findViewById(R.id.campoPubAlvo_publicar);
        campoCategoria = findViewById(R.id.campoCategoria_publicar);
        campoWebsite = findViewById(R.id.campoWebsite_publicar);
        //campoData = findViewById(R.id.campoData_publicar);

        if(TextUtils.isEmpty(campoTitulo.getText()) || TextUtils.isEmpty(campoDescricao.getText())
                || TextUtils.isEmpty(campoConteudo.getText()) || TextUtils.isEmpty(campoData.getText())
                || TextUtils.isEmpty(campoDuracao.getText()) || TextUtils.isEmpty(campoPubAlvo.getText())
                || TextUtils.isEmpty(campoCategoria.getSelectedItem().toString())
                || TextUtils.isEmpty(campoWebsite.getText())){
            // aviso de campo vazio
            return;
        }

        SimpleDateFormat  df = new SimpleDateFormat("dd/MM/yyyy");

        Palestra paux = new Palestra();
        paux.setTitulo(campoTitulo.getText().toString());
        paux.setConteudo(campoConteudo.getText().toString());
        paux.setDescricao(campoDescricao.getText().toString());
        paux.setDuracao(campoDuracao.getText().toString());
        paux.setPublicoAlvo(campoPubAlvo.getText().toString());
        paux.setCategoria(campoCategoria.getSelectedItem().toString());
        paux.setDonoDaPalestra(Tools.getInstance().getUsuarioLogado(this));
        paux.setPalestrante(palestrante);
        paux.setWebsite("http://"+campoWebsite.getText().toString());
        try {
            paux.setDataDaPalestra(df.parse(campoData.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paux.setAprovada(true);

        // persiste no banco
        RealmControl.getRealm().beginTransaction();
        RealmControl.getRealm().copyToRealm(paux);
        RealmControl.getRealm().commitTransaction();

        p.removeEntidadeOrganizando(Tools.getInstance().getUsuarioLogado(this));

        Toast toast = Toast.makeText(getApplicationContext(), "Palestra publicada!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toast.show();

        Intent it = new Intent(this, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);

        finish();
    }

    @Override
    protected void onStart() {
        calendar = Calendar.getInstance();
        my_dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        my_month = calendar.get(Calendar.MONTH);
        my_year = calendar.get(Calendar.YEAR);

        super.onStart();
    }

    public void abreCalendario(View view) {

        datePickerDialog = new DatePickerDialog(this, R.style.DatePicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                my_dayOfMonth = dayOfMonth;
                my_month = month;
                my_year = year;

                data = calendar.getTime();

                campoData.setText(DateFormat.format("dd/MM/yyyy", data));
            }
        }, my_year, my_month, my_dayOfMonth);

        datePickerDialog.show();

    }
}