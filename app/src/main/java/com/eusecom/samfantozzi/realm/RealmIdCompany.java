package com.eusecom.samfantozzi.realm;

import io.realm.RealmObject;

public class RealmIdCompany extends RealmObject {

    private String ico;
    private String dic;
    private String icd;
    private String nai;
    private String uli;
    private String mes;
    private String psc;
    private String tel;
    private String logprx;
    private String datm;

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    public String getNai() {
        return nai;
    }

    public void setNai(String nai) {
        this.nai = nai;
    }

    public String getUli() {
        return uli;
    }

    public void setUli(String uli) {
        this.uli = uli;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLogprx() {
        return logprx;
    }

    public void setLogprx(String logprx) {
        this.logprx = logprx;
    }

    public String getDatm() {
        return datm;
    }

    public void setDatm(String datm) {
        this.datm = datm;
    }
}

