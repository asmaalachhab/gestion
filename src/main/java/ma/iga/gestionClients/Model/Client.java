package ma.iga.gestionClients.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // definir l'equivalent de la table a creer
// @Table(name="tclient") // si jamais on a besoin de changer le nom de la table
public class Client {
    // attributs
    @Id // clé primaire
    private long code;
    private String name;
    private String adresse;
    @Column(name="adresseMail")
    private String mail;
    // methodes
    // constructeurs
    public Client() {
    }
    public Client(long code, String nom, String adresse,String mail) {
        this.code = code;
        this.name = nom;
        this.adresse = adresse;
        this.mail = mail;
    }
    // accesseurs / Modificateurs
    public long getCode(){
        return code;
    }

    public String getNom() {
        return name;
    }

    public void setNom(String nom) {
        this.name = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCode(long code) {
        if(code>0)
            this.code = code;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    
}
