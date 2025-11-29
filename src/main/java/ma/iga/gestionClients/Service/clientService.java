/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.gestionClients.Service;

import java.util.List;
import java.util.Optional;
import ma.iga.gestionClients.Model.Client;
import ma.iga.gestionClients.repositories.clientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class clientService {
     // instanciation à la demande, le cycle de vie géré par spring boot
    @Autowired /// inversion de controle
    private clientRepository cr; //= new clientRepository();
    public Optional<Client> recupererClient(long code){
        // recuperer un client par son code
        Optional<Client> c =  cr.findById(code);
        return c;
    }
    public List<Client> recupererClients(){
        // recuperer tout les clients depuis la BD
        return (List<Client>)cr.findAll();
    }
    // service pour recuperer tt les clients par leurs noms
    public List<Client> recupererClientsParNom(String nom){
        return cr.findByNameLikeIgnoreCase("%"+nom+"%");
    }
    // service pour recueprer tt le clients d'une ville dont le code est > valeur
    public List<Client> recueprerClientsParAdresseEtCode
                                            
        (String adresse,long code){
        return cr.findByAdresseLikeIgnoreCaseAndCodeGreaterThan
                                                ("%"+adresse+"%", code);
    }
    // service pour inserer un nouveau client  
    public void ajoutClient(Client c){
        if(!cr.existsById(c.getCode()))
            cr.save(c);
    }
    //service pour supprimer un produit par son code
    public boolean supprimerProduitParCode(long code){
        if(cr.existsById(code)){ // le produit existe -> le supprimer
            cr.deleteById(code);
            return true;
        }else{ // produit non trouvé
            return false;
        }
    }
    //service pour supprimer iun client par son code
    public boolean supprimerClient(long code){
        if(cr.existsById(code)){ // client existe
            cr.deleteById(code);
            return true; // objert supprimé
        }else{
            return false;
        }
    }
    @Transactional // mettre le service dans une transaction
    // supprimer clients par adresse
    public int supprimerClientParAdresse(String adresse){
        return cr.deleteByAdresseLikeIgnoreCase(adresse);
    }

    // service pour mettre un jour un objet integralement
    public boolean majIntegrale(Client c){
        if(cr.existsById(c.getCode())){
            cr.save(c); // mise a jour
            return true;
        }else
            return false;
    }
    // service pour mettre a jour les adresse mails
    public int majAdresseMails(){
        // recuperer les enregistrement des clients cote BD
        /*List<Client> clients = (List<Client>)cr.findAll();
        // mettre a jour les adresse mails dans la liste des clients (JAVA)
        for (Client c : clients) {
            // modifier l adresse de l'objet c
            c.setMail(  c.getNom().replace(" ",".")
                    .concat("@iga.ac.ma")  );
        }
        // sauvegarder les modification JAVA -> BD
        cr.saveAll(clients);
        return clients.size();*/
        return cr.modifierAdressesMails();
    }
}
