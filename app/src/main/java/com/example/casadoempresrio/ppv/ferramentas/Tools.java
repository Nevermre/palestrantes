package com.example.casadoempresrio.ppv.ferramentas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casadoempresrio.ppv.activitys.LoginActivity;
import com.example.casadoempresrio.ppv.activitys.MainActivity;
import com.example.casadoempresrio.ppv.objetos.Palestra;
import com.example.casadoempresrio.ppv.activitys.PerfilUsuario;
import com.example.casadoempresrio.R;
import com.example.casadoempresrio.ppv.objetos.Usuario;
import com.example.casadoempresrio.ppv.activitys.VisualizaPalestra;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public final class Tools {
    @SuppressLint("StaticFieldLeak")
    private static final Tools ourInstance = new Tools();

    private AdapterPalestrasPersonalizado adapter;
    private Usuario usuarioLogado;
    private List<Integer> visible_only_to_palestrante;
    private List<Integer> visible_only_to_entidade;

    private Tools() {
        // define os componentes visiveis para cada tipo de usuario.
        // itens do menu (toolbar) sao configurados no onPrepareOptionsMenu

        visible_only_to_palestrante = new ArrayList<>();
        visible_only_to_entidade = new ArrayList<>();

        visible_only_to_palestrante.add(R.id.navigation_logado);
        visible_only_to_palestrante.add(R.id.navigation_cadastrar_palestra);

        visible_only_to_entidade.add(R.id.navigation_logado);
        visible_only_to_entidade.add(R.id.navigation_palestras_pendentes);
    }

    public static Tools getInstance() {
        return ourInstance;
    }

    public void configuraFiltroCategoriaMain(final Activity a){
        Spinner cat = a.findViewById(R.id.spn_filtrar);

        ArrayList<String> aux = new ArrayList<>();
        aux.add("Selecione");
        List<String> categ = Palestra.getCATEGORIAS();

        aux.addAll(categ);

        String[] opcoes = aux.toArray(new String[0]);
        ArrayAdapter<String> spn_adapter = new ArrayAdapter<>(a, android.R.layout.simple_spinner_dropdown_item, opcoes);
        cat.setAdapter(spn_adapter);

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaTelaMain(a, parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // criar o query de busca nas palestras
                RealmQuery<Palestra> query = RealmControl.getRealm().where(Palestra.class);

                query = query.equalTo("aprovada", true);
                // executa o query
                RealmResults<Palestra> resultado = query.findAll(); // lista de Palestra

                adapter = new AdapterPalestrasPersonalizado(resultado, a);

                ListView listView = a.findViewById(R.id.lista_palestras_disp);
                listView.setAdapter(adapter);
            }
        });
    }

    public void configuraFiltroCategoriaPendentes(final Activity a){
        Spinner cat = a.findViewById(R.id.spn_filtrar_pendentes);

        ArrayList<String> aux = new ArrayList<>();
        aux.add("Selecione");
        List<String> categ = Palestra.getCATEGORIAS();

        aux.addAll(categ);

        String[] opcoes = aux.toArray(new String[0]);
        ArrayAdapter<String> spn_adapter = new ArrayAdapter<>(a, android.R.layout.simple_spinner_dropdown_item, opcoes);
        cat.setAdapter(spn_adapter);

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaTelaPendentes(a, parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // criar o query de busca nas palestras
                RealmQuery<Palestra> query = RealmControl.getRealm().where(Palestra.class);

                query = query.equalTo("aprovada", false);

                // executa o query
                RealmResults<Palestra> resultado = query.findAll(); // lista de Palestra

                adapter = new AdapterPalestrasPersonalizado(resultado, a);

                ListView listView = a.findViewById(R.id.lista_palestras_pendentes);
                listView.setAdapter(adapter);
            }
        });
    }

    public void configuraFiltroCategoriaMinhasPalestras(final Activity a){
        Spinner cat = a.findViewById(R.id.spn_filtrar_minhas_palestras);

        ArrayList<String> aux = new ArrayList<>();
        aux.add("Selecione");
        List<String> categ = Palestra.getCATEGORIAS();

        aux.addAll(categ);

        String[] opcoes = aux.toArray(new String[0]);
        ArrayAdapter<String> spn_adapter = new ArrayAdapter<>(a, android.R.layout.simple_spinner_dropdown_item, opcoes);
        cat.setAdapter(spn_adapter);

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaTelaMinhasPalestras(a, parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // criar o query de busca nas palestras
                RealmQuery<Palestra> query = RealmControl.getRealm().where(Palestra.class);

                // executa o query
                RealmResults<Palestra> resultado = query.findAll(); // lista de Palestra

                adapter = new AdapterPalestrasPersonalizado(resultado, a);

                ListView listView = a.findViewById(R.id.lista_palestras_minhas);
                listView.setAdapter(adapter);
            }
        });
    }

    public void atualizaToolBarMenus(Menu menu){
        if(usuarioLogado != null){ // logado
            menu.findItem(R.id.miDeslogar).setVisible(true);
        }
        else{
            menu.findItem(R.id.miDeslogar).setVisible(false);
        }
    }

    public void logOut(Activity a){
        SharedPreferences pref = a.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();

        Intent it = new Intent(a, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
        a.startActivity(it);
    }

    public void configuraListaPalestras(final Activity a){
        ListView listView;

        switch(a.getClass().getSimpleName()){
            case "MainActivity":
                listView = a.findViewById(R.id.lista_palestras_disp);
                break;
            case "PalestrasPendentes":
                listView = a.findViewById(R.id.lista_palestras_pendentes);
                break;
            case "MinhasPalestras":
                listView = a.findViewById(R.id.lista_palestras_minhas);
                break;
            default:
                return;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"Clicou no item " + id, Toast.LENGTH_LONG).show();
                Intent it = new Intent(view.getContext(), VisualizaPalestra.class);
                it.putExtra("id", adapter.getItemIdString(position));
                a.startActivity(it);
            }
        });
    }

    public void atualizaTelaMain(Activity a, AdapterView parent){

        int index;
        Spinner cat;
        if(parent != null){
            index = parent.getSelectedItemPosition();
        }
        else{
            cat = a.findViewById(R.id.spn_filtrar);
            index = cat.getSelectedItemPosition();
        }

        // criar o query de busca nas palestras
        RealmQuery<Palestra> query = RealmControl.getRealm().where(Palestra.class);

        // na tela inicial mostra somente as palestras aprovadas
        query = query.equalTo("aprovada", true);

        // condicoes
        if(index != 0){
            List<String> aux = Palestra.getCATEGORIAS();
            query = query.and().equalTo("categoria", aux.get(index-1));
        }

        // executa o query
        RealmResults<Palestra> resultado = query.findAll(); // lista de Palestra

        adapter = new AdapterPalestrasPersonalizado(resultado, a);

        ListView listView = a.findViewById(R.id.lista_palestras_disp);
        listView.setAdapter(adapter);
    }

    public void atualizaTelaPendentes(Activity a, AdapterView parent){

        int index;
        Spinner cat;
        if(parent != null){
            index = parent.getSelectedItemPosition();
        }
        else{
            cat = a.findViewById(R.id.spn_filtrar_pendentes);
            index = cat.getSelectedItemPosition();
        }

        // criar o query de busca nas palestras
        RealmQuery<Palestra> query = RealmControl.getRealm().where(Palestra.class);

        // na tela inicial mostra somente as palestras aprovadas
        query = query.equalTo("aprovada", false);

        // condicoes
        if(index != 0){
            List<String> aux = Palestra.getCATEGORIAS();
            query = query.and().equalTo("categoria", aux.get(index-1));
        }

        // executa o query
        RealmResults<Palestra> resultado = query.findAll(); // lista de Palestra

        adapter = new AdapterPalestrasPersonalizado(resultado, a);

        ListView listView = a.findViewById(R.id.lista_palestras_pendentes);
        listView.setAdapter(adapter);
    }

    public void atualizaTelaMinhasPalestras(Activity a, AdapterView parent){

        int index;
        Spinner cat;
        if(parent != null){
            index = parent.getSelectedItemPosition();
        }
        else{
            cat = a.findViewById(R.id.spn_filtrar_minhas_palestras);
            index = cat.getSelectedItemPosition();
        }

        // criar o query de busca nas palestras
        RealmQuery<Palestra> query = RealmControl.getRealm().where(Palestra.class);

        // condicoes
        if(index != 0){
            List<String> aux = Palestra.getCATEGORIAS();
            query = query.and().equalTo("categoria", aux.get(index-1));
        }

        // executa o query
        RealmResults<Palestra> resultado = query.findAll(); // lista de Palestra

        // filtra soh as palestras do usuario
        Usuario u = getUsuarioLogado(a);
        List<Palestra> resultado_filtrado = new ArrayList<>();

        Log.i("MINHAS_PALESTRAS", "usuario logado: "+u.getEmail());
        Log.i("MINHAS_PALESTRAS", "resultado total: "+resultado.size());
        for(int i = 0; i < resultado.size(); i++){
            if(resultado.get(i).getDonoDaPalestra().getEmail().equals(u.getEmail())){
                resultado_filtrado.add(resultado.get(i));
            }
        }

        adapter = new AdapterPalestrasPersonalizado(resultado_filtrado, a);

        ListView listView = a.findViewById(R.id.lista_palestras_minhas);
        listView.setAdapter(adapter);
    }

    public void logIn(String login, String senha, Activity a){

        if(validaLogin(login, senha, a)){
            Toast toast = Toast.makeText(a.getApplicationContext(), "Login realizado com sucesso!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.getView().setBackgroundColor(a.getResources().getColor(R.color.colorPrimary));
            toast.show();
            SharedPreferences pref = a.getSharedPreferences("usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(Usuario.SESSION_KEY, login);
            edit.apply();

            Log.i("LOGINN", "tela login: "+a.getSharedPreferences("usuario", Context.MODE_PRIVATE).getString(Usuario.SESSION_KEY, "default"));

            Intent it = new Intent(a, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
            a.startActivity(it);
        }
    }

    private boolean validaLogin(String login, String senha, Activity a){
        if(TextUtils.isEmpty(login) || TextUtils.isEmpty(senha)){
            Toast toast = Toast.makeText(a.getApplicationContext(), "Os campos não podem ser vazios!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.getView().setBackgroundColor(Color.parseColor("#ff7575"));
            toast.show();
            return false;
        }
        else {
            RealmQuery<Usuario> query = RealmControl.getRealm().where(Usuario.class).equalTo("email", login).and().equalTo("senha", senha);
            Usuario u = query.findFirst();

            if(u == null){ // login incorreto
                Toast toast = Toast.makeText(a.getApplicationContext(), "Usuario e/ou senha incorretos",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
                toast.getView().setBackgroundColor(Color.parseColor("#ff7575"));
                toast.show();
                return false;
            }
            else{
                return true;
            }
        }
    }

    public boolean isLogged(Activity a){
        usuarioLogado = null;
        if( a.getSharedPreferences("usuario", Context.MODE_PRIVATE).contains(Usuario.SESSION_KEY) ){ // contem a chave (pode estar logado)
            String emailUser = a.getSharedPreferences("usuario", Context.MODE_PRIVATE).getString(Usuario.SESSION_KEY, "");
            Log.i("LOGINN", "tentando logar como: "+emailUser);
            usuarioLogado = RealmControl.getRealm().where(Usuario.class).equalTo("email", emailUser).findFirst();

            return usuarioLogado != null;
        }
        else { // nao existe a chave
            return false;
        }
    }

    public Usuario getUsuarioLogado(Activity a){
            return usuarioLogado;
    }

    @SuppressLint("SetTextI18n")
    public void atualizaComponentesPorUsuario(Activity a){

        TextView draw_logado_nome = a.findViewById(R.id.navigation_logado_nome);

        if(isLogged(a)){ // logado

            if(usuarioLogado.getTipo() == Usuario.PALESTRANTE) {

                for(int i=0; i<visible_only_to_entidade.size(); i++){ // oculta o que eh visivel apenas para o outro tipo
                    a.findViewById(visible_only_to_entidade.get(i)).setVisibility(View.GONE);
                }
                for(int i=0; i<visible_only_to_palestrante.size(); i++){ // seta os componentes visiveis para esse tipo de usuario
                    a.findViewById(visible_only_to_palestrante.get(i)).setVisibility(View.VISIBLE);
                }

                draw_logado_nome.setText("Olá, " + usuarioLogado.getPrimeiroNome());
            }
            else if(usuarioLogado.getTipo() == Usuario.ENTIDADE) {

                for(int i=0; i<visible_only_to_palestrante.size(); i++){ // oculta o que eh visivel apenas para o outro tipo
                    a.findViewById(visible_only_to_palestrante.get(i)).setVisibility(View.GONE);
                }
                for(int i=0; i<visible_only_to_entidade.size(); i++){ // seta os componentes visiveis para esse tipo de usuario
                    a.findViewById(visible_only_to_entidade.get(i)).setVisibility(View.VISIBLE);
                }

                draw_logado_nome.setText("Olá, " + usuarioLogado.getEntidadeNome());
            }
        }
        else{ // nao logado
            // oculta o que eh visivel apenas para usuarios logados
            for(int i=0; i<visible_only_to_palestrante.size(); i++){
                a.findViewById(visible_only_to_palestrante.get(i)).setVisibility(View.GONE);
            }
            for(int i=0; i<visible_only_to_entidade.size(); i++){
                a.findViewById(visible_only_to_entidade.get(i)).setVisibility(View.GONE);
            }
        }
    }

    public void cliqueMinhaConta(Activity a){
        Log.i("CLIQUEMINHACONTA", a.getClass().getSimpleName());
        if(Tools.getInstance().isLogged(a)){
            Intent it = new Intent(a, PerfilUsuario.class);
            if( a.getClass().getSimpleName().equals("MainActivity") ){
                it.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                a.startActivity(it);
            }
            else{
                it.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                a.startActivity(it);
                a.finish();
            }
        }
        else{
            Intent it = new Intent(a, LoginActivity.class);
            if( a.getClass().getSimpleName().equals("MainActivity") ){
                it.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                a.startActivity(it);
            }
            else{
                it.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                a.startActivity(it);
                a.finish();
            }
        }
    }
}
