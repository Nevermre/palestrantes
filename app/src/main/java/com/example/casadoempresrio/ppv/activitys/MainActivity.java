package com.example.casadoempresrio.ppv.activitys;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.Tools;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configuraToolBar();
        Tools.getInstance().configuraListaPalestras(this);
        Tools.getInstance().configuraFiltroCategoriaMain(this);
    }

    void configuraToolBar(){
        // adiciona a barra de tarefas na tela
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_main);
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("PPV");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, my_toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void cadastrarPalestra(View view) {
        Intent it = new Intent(this, CadastrarPalestra.class);
        startActivity(it);
    }

    public void palestrasPendentes(View view) {
        Intent it = new Intent(this, PalestrasPendentes.class);
        startActivity(it);
    }

    // adiciona os icones na barra de tarefa
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_itens, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i("ATUALIZAME", "aaaa");
        Tools.getInstance().atualizaToolBarMenus(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    // trata os cliques da barra de tarefas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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

    // sempre atualiza os dados da tela


    @Override
    protected void onStart() {
        super.onStart();

        // atualiza os dados da tela
        Tools.getInstance().atualizaTelaMain(this, null);

        // fecha o navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();

        Tools.getInstance().atualizaComponentesPorUsuario(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //realm.close();
    }

    public void sair(View view) {
    }

    public void configuracoes(View view) {
    }
}
