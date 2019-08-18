package com.example.casadoempresrio.ppv.objetos;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Usuario extends RealmObject {

    @PrimaryKey
    private String email;

    private Date criadoEm = new Date(); // salva a data que o objeto foi criado

    @LinkingObjects("EntidadesInteressadasEmOrganizar")
    private final RealmResults<Palestra> palestrasQueDemonstreiInteresseEmOrganizar = null;

    @LinkingObjects("EntidadesOrganizando")
    private final RealmResults<Palestra> palestrasQueEstouOrganizando = null;

    private String senha;
    private String telefone = "";
    private String primeiroNome = "";
    private String ultimoNome = "";
    private String entidadeNome = "";
    private String sexo = "";
    private short tipo = 0; // 0 = palestrante, 1 = entidade

    public static final short PALESTRANTE = 0, ENTIDADE = 1;
    public static final String SESSION_KEY = "sessionEmail";

    public Usuario(){ }
    public Usuario(short tipo) {
        this.tipo = tipo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telfone) {
        this.telefone = telfone;
    }

    public String getEntidadeNome() {
        return entidadeNome;
    }

    public void setEntidadeNome(String entidadeNome) {
        this.entidadeNome = entidadeNome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public short getTipo(){
        return this.tipo;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public RealmResults<Palestra> getPalestrasQueDemonstreiInteresseEmOrganizar() {
        return palestrasQueDemonstreiInteresseEmOrganizar;
    }

    public RealmResults<Palestra> getPalestrasQueEstouOrganizando() {
        return palestrasQueEstouOrganizando;
    }
}
