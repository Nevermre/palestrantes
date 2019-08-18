package com.example.casadoempresrio.ppv.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.AdapterPalestrasPersonalizado;
import com.example.casadoempresrio.ppv.ferramentas.RealmControl;
import com.example.casadoempresrio.ppv.ferramentas.Tools;
import com.example.casadoempresrio.ppv.objetos.Palestra;
import com.example.casadoempresrio.ppv.objetos.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class VisualizaPalestra extends AppCompatActivity {
    private Palestra p;
    private String palestraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_palestra);

        Intent it = getIntent();
        // recupera a palestra pelo ID
        palestraId = it.getStringExtra("id");
        p = RealmControl.getRealm().where(Palestra.class).equalTo("id", palestraId).findFirst();

        if(p == null){
            finish();
        }

        ((TextView) findViewById(R.id.palestra_titulo_id)).setText(p.getTitulo());
        ((TextView) findViewById(R.id.palestra_conteudo_id)).setText(p.getConteudo());
        ((TextView) findViewById(R.id.palestra_descricao_id)).setText(p.getDescricao());
        ((TextView) findViewById(R.id.palestra_duracao_id)).setText(p.getDuracao());
        ((TextView) findViewById(R.id.palestra_matNec_id)).setText(p.getMaterialNecessario());
        ((TextView) findViewById(R.id.palestra_pubAlvo_id)).setText(p.getPublicoAlvo());
        ((TextView) findViewById(R.id.palestra_categoria_id)).setText(p.getCategoria());

        if(p.isAprovada() == false){
            findViewById(R.id.linha_conteudo).setVisibility(View.GONE);
            findViewById(R.id.linha_duracao).setVisibility(View.GONE);
            findViewById(R.id.linha_conteudo_view).setVisibility(View.GONE);
            findViewById(R.id.linha_duracao_view).setVisibility(View.GONE);
        }
        // adiciona a barra de tarefas na tela
        configuraToolBar();
    }

    void configuraToolBar(){
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_vis_pal);
        setSupportActionBar(my_toolbar);


        // ativa a seta de voltar
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Palestra: "+p.getTitulo());
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

    public void configuraSpinner(){
        Spinner cat = findViewById(R.id.spin_entidades_interessadas);

        ArrayList<String> aux = new ArrayList<>();
        aux.add("Selecione");

        List<Usuario> entidadesInteressadas = p.getEntidadesInteressadasEmOrganizar();

        for(int i = 0; i<entidadesInteressadas.size(); i++){
            aux.add(entidadesInteressadas.get(i).getEntidadeNome());
        }

        String[] opcoes = aux.toArray(new String[0]);
        ArrayAdapter<String> spn_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        cat.setAdapter(spn_adapter);

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(p.isAprovada()){
            ((TextView)findViewById(R.id.palestra_data_id)).setText(DateFormat.format("dd/MM/yyyy", p.getDataDaPalestra()));
            ((TextView)findViewById(R.id.palestra_website_id)).setText(p.getWebsite());

            findViewById(R.id.campo_mat_necessario).setVisibility(View.GONE);
            findViewById(R.id.linha_campo_mat_necessario).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.campo_visualiza_data).setVisibility(View.GONE);
            findViewById(R.id.linha_campo_visualiza_data).setVisibility(View.GONE);
            findViewById(R.id.campo_visualiza_website).setVisibility(View.GONE);
            findViewById(R.id.linha_campo_visualiza_website).setVisibility(View.GONE);
        }

        findViewById(R.id.campo_organizarPalestra).setVisibility(View.GONE);
        findViewById(R.id.campo_ja_tem_interesse).setVisibility(View.GONE);
        findViewById(R.id.campo_publicar_palestra).setVisibility(View.GONE);
        findViewById(R.id.campo_ja_esta_organizando).setVisibility(View.GONE);

        if(Tools.getInstance().isLogged(this)){
            Usuario u = Tools.getInstance().getUsuarioLogado(this);
            if(p.getDonoDaPalestra().getEmail().equals(u.getEmail()) && !p.isAprovada()){
                configuraSpinner();
                findViewById(R.id.lista_entidades_interessadas).setVisibility(View.VISIBLE);
            }
            else{
                findViewById(R.id.lista_entidades_interessadas).setVisibility(View.GONE);
            }
            if(Tools.getInstance().getUsuarioLogado(this).getTipo() == Usuario.ENTIDADE && p.isAprovada() == false){
                if(p.getEntidadesInteressadasEmOrganizar().contains(Tools.getInstance().getUsuarioLogado(this))){ // essa entidade ja demonstrou interesse em organizar esta palestra
                    findViewById(R.id.campo_organizarPalestra).setVisibility(View.GONE);
                    findViewById(R.id.campo_ja_tem_interesse).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.text_telefone_palestrante)).setText(p.getDonoDaPalestra().getTelefone());
                    ((TextView)findViewById(R.id.text_email_palestrante)).setText(p.getDonoDaPalestra().getEmail());
                }
                else if(p.getEntidadesOrganizando().contains(Tools.getInstance().getUsuarioLogado(this))){
                    findViewById(R.id.campo_publicar_palestra).setVisibility(View.VISIBLE);
                    findViewById(R.id.campo_ja_esta_organizando).setVisibility(View.VISIBLE);
                }
                else {
                    findViewById(R.id.campo_organizarPalestra).setVisibility(View.VISIBLE);
                }
            }
            else{
                findViewById(R.id.campo_organizarPalestra).setVisibility(View.GONE);
            }
        }
        else{
            findViewById(R.id.lista_entidades_interessadas).setVisibility(View.GONE);
            findViewById(R.id.campo_organizarPalestra).setVisibility(View.GONE);
        }

        Log.i("APROVADA", ""+p.isAprovada());
    }

    // atualiza a barra de tarefas
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Tools.getInstance().atualizaToolBarMenus(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i("CLIQUE_TOOLBAR", "tratando clique na seta pelo onSupportNavigateUp");
        onBackPressed();
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //realm.close();
    }

    public void organizarPalestra(View view) {

        RealmControl.getRealm().beginTransaction();
        p.addEntidadeInteressadaEmOrganizar(Tools.getInstance().getUsuarioLogado(this));
        RealmControl.getRealm().commitTransaction();

        Intent it = new Intent(this, VisualizaPalestra.class);
        it.putExtra("id", palestraId);
        startActivity(it);

        finish();
    }

    public void aprovarEntidade(View view) {
        Spinner spn = findViewById(R.id.spin_entidades_interessadas);
        int index = spn.getSelectedItemPosition();

        if(index == 0){
            Toast.makeText(this,"É necessário selecionar uma Entidade que irá patrocinar sua palestra!", Toast.LENGTH_LONG).show();
            return;
        }

        String entidadeNome = spn.getSelectedItem().toString();

        Usuario entidadeOrganizando = RealmControl.getRealm().where(Usuario.class).equalTo("entidadeNome", entidadeNome).findFirst();

        RealmControl.getRealm().beginTransaction();
        p.addEntidadeOrganizando(entidadeOrganizando);
        RealmControl.getRealm().commitTransaction();

        //RealmControl.getRealm().beginTransaction();
        p.removeEntidadeInteressadaEmOrganizar(entidadeOrganizando);
        //RealmControl.getRealm().commitTransaction();

        Toast toast = Toast.makeText(getApplicationContext(), "Entidade aprovada!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toast.show();

        finish();

        /* isso era quando o palestrante que publicava a palestra
        Intent it = new Intent(this, PublicarPalestra.class);
        it.putExtra("entidade_nome", entidadeNome);
        it.putExtra("palestra_id", p.getId());
        startActivity(it);
        */
    }

    public void publicarPalestra(View view) {

        Intent it = new Intent(this, PublicarPalestra.class);
        it.putExtra("palestra_id", p.getId());
        startActivity(it);
    }

    public void abreSite(View view) {
        String url = ((TextView) findViewById(R.id.palestra_website_id)).getText().toString();

        Uri uri = Uri.parse(url);

        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }
}
