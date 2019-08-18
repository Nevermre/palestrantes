package com.example.casadoempresrio.ppv.objetos;

import android.util.Log;

import com.example.casadoempresrio.ppv.ferramentas.RealmControl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Palestra extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString(); // gera uma chave unica

    // site utilizado para cadastro / informações
    private String website;

    // quando a palestra for publicada por uma entidade, sera criada uma copia da palestra e a entidade sera a dona da palestra e o palestrante sera salvo nessa variavel
    private Usuario palestrante;

    private Date dataDaPalestra;

    private Date criadoEm = new Date(); // salva a data que o objeto foi criado

    private String titulo = "";
    private String conteudo = "";
    private String descricao = "";
    private String duracao = "";
    private String materialNecessario = "";
    private String publicoAlvo = "";
    private boolean aprovada = false;

    // o palestrante eh dono de propostas de palestras.
    private Usuario donoDaPalestra;


    private String categoria  = "";

    private RealmList<Usuario> EntidadesInteressadasEmOrganizar = new RealmList<>(); // entidades que demonstraram interesse em organizar essa palestra. O usuario pode escolher quais ele vai aceitar
    private RealmList<Usuario> EntidadesOrganizando = new RealmList<>(); // entidades que o usuario escolheu para organizar

    private static String[] s = {"Tecnologia", "Empreendedorismo", "Saúde", "Lazer", "Motivacional", "Outra"};
    private static List<String> CATEGORIAS = Arrays.asList(s);

    @Override
    public String toString() {
        return "Título: "+this.titulo+"\nDescrição: "+this.descricao;
    }

    public Palestra() { }

    public void addEntidadeInteressadaEmOrganizar(Usuario entidade){
        if(EntidadesInteressadasEmOrganizar == null){
            EntidadesInteressadasEmOrganizar = new RealmList<>();
        }
        EntidadesInteressadasEmOrganizar.add(entidade);
    }
    public List<Usuario> getEntidadesInteressadasEmOrganizar(){
        return EntidadesInteressadasEmOrganizar;
    }
    public void removeEntidadeInteressadaEmOrganizar(Usuario entidade){
        Log.i("REMOVE_INTERESSADO", "fora "+entidade.getEntidadeNome());
        if(EntidadesInteressadasEmOrganizar != null){
            Log.i("REMOVE_INTERESSADO", "entrou "+entidade.getEntidadeNome());
            RealmControl.getRealm().beginTransaction();
            EntidadesInteressadasEmOrganizar.remove(entidade);
            RealmControl.getRealm().commitTransaction();
        }
    }
    public void limpaEntidadesInteressadasEmOrganizar(){
        if(EntidadesInteressadasEmOrganizar != null){
            EntidadesInteressadasEmOrganizar.clear();
        }
    }

    public void addEntidadeOrganizando(Usuario entidade){
        if(EntidadesOrganizando == null){
            EntidadesOrganizando = new RealmList<>();
        }
        EntidadesOrganizando.add(entidade);
    }
    public List<Usuario> getEntidadesOrganizando(){
        return EntidadesOrganizando;
    }

    public void removeEntidadeOrganizando(Usuario entidade){
        if(EntidadesOrganizando != null){
            RealmControl.getRealm().beginTransaction();
            EntidadesOrganizando.remove(entidade);
            RealmControl.getRealm().commitTransaction();
        }
    }


    public static List<String> getCATEGORIAS(){
        return CATEGORIAS;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isAprovada() {
        return aprovada;
    }

    public void setAprovada(boolean aprovada) {
        this.aprovada = aprovada;
    }

    public Usuario getDonoDaPalestra() {
        return donoDaPalestra;
    }

    public void setDonoDaPalestra(Usuario donoDaPalestra) {
        this.donoDaPalestra = donoDaPalestra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getMaterialNecessario() {
        return materialNecessario;
    }

    public void setMaterialNecessario(String materialNecessario) {
        this.materialNecessario = materialNecessario;
    }

    public String getPublicoAlvo() {
        return publicoAlvo;
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public String getId() {
        return id;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public Usuario getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(Usuario palestrante) {
        this.palestrante = palestrante;
    }

    public Date getDataDaPalestra() {
        return dataDaPalestra;
    }

    public void setDataDaPalestra(Date dataDaPalestra) {
        this.dataDaPalestra = dataDaPalestra;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
