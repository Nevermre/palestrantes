package com.example.casadoempresrio.ppv.activitys;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.RealmControl;
import com.example.casadoempresrio.ppv.ferramentas.Tools;
import com.example.casadoempresrio.ppv.objetos.Palestra;

import java.util.Objects;

public class CadastrarPalestra extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_palestra);

        configuraToolBar();
        configuraSelectCategoria();
    }

    void configuraToolBar(){
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_cadast_pal);
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Cadastrar Palestra");
        // ativa a seta de voltar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    void configuraSelectCategoria(){
        Spinner cat =  findViewById(R.id.campoCategoria);
        String[] opcoes = Palestra.getCATEGORIAS().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        cat.setAdapter(adapter);
    }

    public void cadastrar(View v){
        EditText campoTitulo, campoConteudo, campoDescricao, campoDuracao, campoMatNecessario, campoPubAlvo;
        Spinner campoCategoria;
        campoTitulo =  findViewById(R.id.campoAssunto);
        campoDescricao = findViewById(R.id.campoDescricao);
        campoMatNecessario = findViewById(R.id.campoMatNecessario);
        campoPubAlvo = findViewById(R.id.campoPubAlvo);
        campoCategoria = findViewById(R.id.campoCategoria);

        if(TextUtils.isEmpty(campoTitulo.getText()) || TextUtils.isEmpty(campoDescricao.getText())
                || TextUtils.isEmpty(campoMatNecessario.getText()) || TextUtils.isEmpty(campoPubAlvo.getText()) || TextUtils.isEmpty(campoCategoria.getSelectedItem().toString())){
            // aviso de campo vazio
            return;
        }

        Palestra p = new Palestra();
        p.setTitulo(campoTitulo.getText().toString());
        p.setDescricao(campoDescricao.getText().toString());
        p.setMaterialNecessario(campoMatNecessario.getText().toString());
        p.setPublicoAlvo(campoPubAlvo.getText().toString());
        p.setCategoria(campoCategoria.getSelectedItem().toString());
        p.setDonoDaPalestra(Tools.getInstance().getUsuarioLogado(this));

        // persiste no banco
        RealmControl.getRealm().beginTransaction();
        RealmControl.getRealm().copyToRealm(p);
        RealmControl.getRealm().commitTransaction();

        Toast toast = Toast.makeText(getApplicationContext(), "Proposta cadastrada!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toast.show();

        Intent it = new Intent(this, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);

        finish();
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
}
