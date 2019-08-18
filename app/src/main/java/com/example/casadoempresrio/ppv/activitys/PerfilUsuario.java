package com.example.casadoempresrio.ppv.activitys;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.Tools;
import com.example.casadoempresrio.ppv.objetos.Usuario;

import java.util.Objects;

public class PerfilUsuario extends AppCompatActivity {
    private String nomeLogado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        configuraToolBar();

        TextView textView = findViewById(R.id.perfil_usuario_nome);
        textView.setText(nomeLogado);
    }

    void configuraToolBar(){
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_perfil_usuario);
        setSupportActionBar(my_toolbar);
        nomeLogado = "";
        Usuario u = Tools.getInstance().getUsuarioLogado(this);
        if(u != null){
            if(u.getTipo() == Usuario.ENTIDADE){
                nomeLogado = u.getEntidadeNome();
            }
            else{
                nomeLogado = u.getPrimeiroNome();
            }
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("Perfil: "+nomeLogado);
        // ativa a seta de voltar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
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

    public void minhasPalestras(View view) {
        Intent it = new Intent(this, MinhasPalestras.class);
        startActivity(it);
    }

    public void ajuda(View view) {
    }

    public void configuracoes(View view) {
    }
}
