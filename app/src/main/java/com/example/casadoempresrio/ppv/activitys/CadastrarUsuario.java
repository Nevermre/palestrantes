package com.example.casadoempresrio.ppv.activitys;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.ferramentas.RealmControl;
import com.example.casadoempresrio.ppv.ferramentas.Tools;
import com.example.casadoempresrio.ppv.objetos.Usuario;

import java.util.Objects;

public class CadastrarUsuario extends AppCompatActivity {
    private final String[] tipoContaOpcoes = new String[]{"Palestrante", "Entidade"};

    private AutoCompleteTextView email;
    private EditText senha;
    private AutoCompleteTextView primeiro_nome;
    private AutoCompleteTextView ultimo_nome;
    private AutoCompleteTextView entidade_nome;
    private AutoCompleteTextView telefone;
    private Spinner tipoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        // adiciona a barra de tarefas na tela
        configuraToolBar();
        configuraSelectTipoUsuario();
    }

    void configuraToolBar(){
        Toolbar my_toolbar = findViewById(R.id.my_toolbar_cadast_usuario);
        setSupportActionBar(my_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Criar Conta");
        // ativa a seta de voltar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    void configuraSelectTipoUsuario(){
        Spinner tipoConta = findViewById(R.id.cadastrar_tipoConta);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipoContaOpcoes);
        tipoConta.setAdapter(adapter);

        tipoConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoContaSelecionada = parent.getSelectedItem().toString();

                if(tipoContaSelecionada.equals(tipoContaOpcoes[0])){ // selecionado Palestrante
                    (findViewById(R.id.cadastrar_row_entidade_nome)).setVisibility(View.GONE);
                    ((AutoCompleteTextView) findViewById(R.id.cadastrar_entidadeNome)).setText("");
                    (findViewById(R.id.cadastrar_row_primeiro_nome)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.cadastrar_row_ultimo_nome)).setVisibility(View.VISIBLE);
                }
                else if(tipoContaSelecionada.equals(tipoContaOpcoes[1])){ // selecionado Entidade
                    (findViewById(R.id.cadastrar_row_entidade_nome)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.cadastrar_row_primeiro_nome)).setVisibility(View.GONE);
                    ((AutoCompleteTextView) findViewById(R.id.cadastrar_primeiroNome)).setText("");
                    (findViewById(R.id.cadastrar_row_ultimo_nome)).setVisibility(View.GONE);
                    ((AutoCompleteTextView) findViewById(R.id.cadastrar_UltimoNome)).setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                (findViewById(R.id.cadastrar_row_entidade_nome)).setVisibility(View.GONE);
                (findViewById(R.id.cadastrar_row_primeiro_nome)).setVisibility(View.VISIBLE);
                (findViewById(R.id.cadastrar_row_ultimo_nome)).setVisibility(View.VISIBLE);
            }
        });
    }

    public void cadastrar(View view) {
        Usuario u;

        email = findViewById(R.id.cadastrar_email);
        senha =  findViewById(R.id.cadastrar_senha);
        primeiro_nome = findViewById(R.id.cadastrar_primeiroNome);
        ultimo_nome = findViewById(R.id.cadastrar_UltimoNome);
        entidade_nome = findViewById(R.id.cadastrar_entidadeNome);
        telefone = findViewById(R.id.cadastrar_telefone);
        tipoConta =  findViewById(R.id.cadastrar_tipoConta);

        try{
            validaDadosCadastro();
        }catch (Exception e){

            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.getView().setBackgroundColor(Color.parseColor("#ff7575"));
            toast.show();
            return;
        }

        if(tipoConta.getSelectedItem().toString().equals(tipoContaOpcoes[1])){ // entidade
            u = new Usuario(Usuario.ENTIDADE);
            u.setEmail(email.getText().toString());
            u.setSenha(senha.getText().toString());
            u.setEntidadeNome(entidade_nome.getText().toString());
            u.setTelefone(telefone.getText().toString());
        }
        else{ // palestrante
            u = new Usuario(Usuario.PALESTRANTE);
            u.setEmail(email.getText().toString());
            u.setSenha(senha.getText().toString());
            u.setPrimeiroNome(primeiro_nome.getText().toString());
            u.setUltimoNome(ultimo_nome.getText().toString());
            u.setTelefone(telefone.getText().toString());
        }

        // persiste no banco
        RealmControl.getRealm().beginTransaction();
        RealmControl.getRealm().copyToRealm(u);
        RealmControl.getRealm().commitTransaction();

        Tools.getInstance().logIn(email.getText().toString(), senha.getText().toString(), this);

        finish();
    }

    private void validaDadosCadastro() throws Exception {

        if( RealmControl.getRealm().where(Usuario.class).equalTo("email", email.getText().toString()).findAll().size() > 0 ){ // email ja cadastrado
            throw new Exception("Email já cadastrado!");
        }
        if( TextUtils.isEmpty(email.getText()) ){
            throw new Exception("O campo 'Email' não pode ser vazio.");
        }
        if( senha.getText().toString().length() < 3 || senha.getText().toString().length() > 31 ){
            throw new Exception("A senha precisa ter no mínimo 3 e no máximo 31 caracteres.");
        }
        if( TextUtils.isEmpty(telefone.getText()) ){
            throw new Exception("O campo 'Telefone' não pode ser vazio.");
        }

        if(tipoConta.getSelectedItem().toString().equals(tipoContaOpcoes[1])) { // entidade
            if( TextUtils.isEmpty(entidade_nome.getText()) ){
                throw new Exception("O campo 'Nome da Entidade' não pode ser vazio.");
            }
        }
        else{ // palestrante
            if( TextUtils.isEmpty(primeiro_nome.getText()) ){
                throw new Exception("O campo 'Primeiro Nome' não pode ser vazio.");
            }
            if( TextUtils.isEmpty(ultimo_nome.getText()) ){
                throw new Exception("O campo 'Último Nome' não pode ser vazio.");
            }
        }
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
