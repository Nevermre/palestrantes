package com.example.casadoempresrio.ppv.ferramentas;

import android.util.Log;

import com.example.casadoempresrio.ppv.objetos.Palestra;
import com.example.casadoempresrio.ppv.objetos.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public final class RealmControl {

    private static Realm realm = null;
    private static RealmConfiguration realmConfig;

    private RealmControl() {}

    public static Realm getRealm(){
        if(realm == null){

            // configura o realm usado (neste caso apenas o nome)
            String REALM_NAME = "ppv_v1.realm";

            realmConfig = new RealmConfiguration.Builder()
                    .name(REALM_NAME)
                    .deleteRealmIfMigrationNeeded()
                    .build();

            // realm sem criptografia
            realm = Realm.getInstance(realmConfig);

            Log.i("MY_REALM", "Realm configurado porque era null");
            Log.i("MY_REALM", realm.getPath());
        }
        Log.i("MY_REALM", "getLocalInstanceCount() = "+Realm.getLocalInstanceCount(realmConfig));
        return realm;
    }

    public static void pupularRealm(){

        RealmResults<Palestra> palestras = RealmControl.getRealm().where(Palestra.class).findAll();
        Log.i("MY_REALM", "ja tem palestras: "+palestras.size());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Usuario u = new Usuario(Usuario.PALESTRANTE);
        u.setEmail("a@a.com");
        u.setSenha("123");
        u.setPrimeiroNome("PrimeiroNome");
        u.setUltimoNome("Ultimo Nome");
        u.setTelefone("31 99999999");
        RealmControl.getRealm().beginTransaction();
        RealmControl.getRealm().insertOrUpdate(u);
        RealmControl.getRealm().commitTransaction();

        if( RealmControl.getRealm().where(Palestra.class).equalTo("titulo", "OC 3").findAll().size() == 0 ) {
            Palestra p = new Palestra();
            p.setTitulo("OC 3");
            p.setConteudo("teste");
            p.setDescricao("Verilog 3.0");
            p.setDuracao("teste");
            p.setMaterialNecessario("teste");
            p.setPublicoAlvo("teste");
            p.setCategoria("Tecnologia");
            p.setAprovada(true);
            p.setWebsite("http://verilog.com");
            try {
                p.setDataDaPalestra(df.parse("01/08/2019"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.setDonoDaPalestra(u);
            RealmControl.getRealm().beginTransaction();
            RealmControl.getRealm().insertOrUpdate(p);
            RealmControl.getRealm().commitTransaction();
            Log.i("MY_REALM", "palestra id: " + p.getId());
        }

        if( RealmControl.getRealm().where(Palestra.class).equalTo("titulo", "Saúde Mental").findAll().size() == 0 ) {
            Palestra p = new Palestra();
            p.setTitulo("Saúde Mental");
            p.setConteudo("teste");
            p.setDescricao("Viva melhor 4.0");
            p.setDuracao("teste");
            p.setMaterialNecessario("teste");
            p.setPublicoAlvo("teste");
            p.setCategoria("Saúde");
            p.setAprovada(true);
            p.setWebsite("http://coachdesaude.com");
            try {
                p.setDataDaPalestra(df.parse("15/08/2019"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.setDonoDaPalestra(u);
            RealmControl.getRealm().beginTransaction();
            RealmControl.getRealm().insertOrUpdate(p);
            RealmControl.getRealm().commitTransaction();
            Log.i("MY_REALM", "palestra id: " + p.getId());
        }

        if( RealmControl.getRealm().where(Palestra.class).equalTo("titulo", "Palestra não aprovada").findAll().size() == 0 ) {
            Palestra p = new Palestra();
            p.setTitulo("Palestra não aprovada");
            p.setConteudo("teste");
            p.setDescricao("Esta palestra ainda nao foi aprovada@!");
            p.setDuracao("teste");
            p.setMaterialNecessario("teste");
            p.setPublicoAlvo("teste");
            p.setCategoria("Saúde");
            p.setAprovada(false);
            p.setDonoDaPalestra(u);
            RealmControl.getRealm().beginTransaction();
            RealmControl.getRealm().insertOrUpdate(p);
            RealmControl.getRealm().commitTransaction();
            Log.i("MY_REALM", "palestra id: " + p.getId());
        }

        for(int i=1; i<=20; i++){ // cria 20 palestras aprovadas para teste
            if( RealmControl.getRealm().where(Palestra.class).equalTo("titulo", "Teste "+i).findAll().size() == 0 ) {
                Palestra p = new Palestra();
                p.setTitulo("Teste "+i);
                p.setConteudo("Teste "+i);
                p.setDescricao("Teste "+i+".0");
                p.setDuracao("teste");
                p.setMaterialNecessario("teste");
                p.setPublicoAlvo("teste");
                p.setCategoria(Palestra.getCATEGORIAS().get(i % Palestra.getCATEGORIAS().size()));
                p.setAprovada(true);
                p.setWebsite("http://teste"+i+".com");
                try {
                    p.setDataDaPalestra(df.parse(i+"/08/2019"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                p.setDonoDaPalestra(u);
                RealmControl.getRealm().beginTransaction();
                RealmControl.getRealm().insertOrUpdate(p);
                RealmControl.getRealm().commitTransaction();
                Log.i("MY_REALM", "palestra id: " + p.getId());
            }
        }

        u = new Usuario(Usuario.ENTIDADE);
        u.setEmail("b@b.com");
        u.setSenha("123");
        u.setEntidadeNome("Empresa BBB");
        u.setTelefone("31 88888888");
        RealmControl.getRealm().beginTransaction();
        RealmControl.getRealm().insertOrUpdate(u);
        RealmControl.getRealm().commitTransaction();

    }

    public static void limpaRealm(){
        RealmResults<Palestra> results = RealmControl.getRealm().where(Palestra.class).findAll();
        RealmControl.getRealm().beginTransaction();
        results.deleteAllFromRealm();
        RealmControl.getRealm().commitTransaction();

        RealmResults<Usuario> resultsu = RealmControl.getRealm().where(Usuario.class).findAll();
        RealmControl.getRealm().beginTransaction();
        resultsu.deleteAllFromRealm();
        RealmControl.getRealm().commitTransaction();
    }
}
