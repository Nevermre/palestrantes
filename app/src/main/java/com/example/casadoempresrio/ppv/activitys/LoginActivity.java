package com.example.casadoempresrio.ppv.activitys;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.Tools;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    //private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // adiciona a barra de tarefas na tela
        configuraToolbar();
    }

    void configuraToolbar(){
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_login);
        setSupportActionBar(my_toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        // ativa a seta de voltar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void criarConta(View view) {
        Intent it = new Intent(this, CadastrarUsuario.class);
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

    public void login(View view) {
        AutoCompleteTextView email = findViewById(R.id.login_email);
        EditText senha = findViewById(R.id.login_senha);
        Tools.getInstance().logIn(email.getText().toString(), senha.getText().toString(), this);
    }
}
