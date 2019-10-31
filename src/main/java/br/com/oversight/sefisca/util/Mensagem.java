package br.com.oversight.sefisca.util;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Representa uma mensagem a ser enviada
 */
public class Mensagem {
    private String de;
    private String para;
    private Date data = new Date();
    private String assunto;
    private String conteudo;
    private String responderPara;
    private String comCopiaPara;
    private String comCopiaOcultaPara;
    private List<File> anexos;

    /**
     * @return the conteudo
     * @uml.property name="conteudo"
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * @param conteudo
     *            the conteudo to set
     * @uml.property name="conteudo"
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    /**
     * @return the data
     * @uml.property name="data"
     */
    public Date getData() {
        return data != null ? (Date) data.clone() : null;
    }

    /**
     * @param data
     *            the data to set
     * @uml.property name="data"
     */
    public void setData(Date data) {
        this.data = data != null ? (Date) data.clone() : null;
    }

    /**
     * @return the de
     * @uml.property name="de"
     */
    public String getDe() {
        return de;
    }

    /**
     * @param de
     *            the de to set
     * @uml.property name="de"
     */
    public void setDe(String de) {
        this.de = de;
    }

    /**
     * @return the para
     * @uml.property name="para"
     */
    public String getPara() {
        return para;
    }

    /**
     * @param para
     *            the para to set
     * @uml.property name="para"
     */
    public void setPara(String para) {
        this.para = para;
    }

    public void addPara(String para) {
        if (this.para == null) {
            this.para = para;
        } else {
            this.para = new StringBuilder(this.para).append(",").append(para).toString();
        }
    }

    /**
     * @return the assunto
     * @uml.property name="assunto"
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * @param assunto
     *            the assunto to set
     * @uml.property name="assunto"
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getResponderPara() {
        return responderPara;
    }

    public void setResponderPara(String responderPara) {
        this.responderPara = responderPara;
    }

    public String getComCopiaPara() {
        return comCopiaPara;
    }

    public void setComCopiaPara(String comCopiaPara) {
        this.comCopiaPara = comCopiaPara;
    }

    public String getComCopiaOcultaPara() {
        return comCopiaOcultaPara;
    }

    public void setComCopiaOcultaPara(String comCopiaOcultaPara) {
        this.comCopiaOcultaPara = comCopiaOcultaPara;
    }

    public void addComCopiaOcultaPara(String comCopiaOcultaPara) {
        if (this.comCopiaOcultaPara == null) {
            this.comCopiaOcultaPara = comCopiaOcultaPara;
        } else{
            this.comCopiaOcultaPara = new StringBuilder(this.comCopiaOcultaPara).append(",").append(comCopiaOcultaPara).toString();
        }
    }

    public List<File> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<File> anexos) {
        this.anexos = anexos;
    }
}