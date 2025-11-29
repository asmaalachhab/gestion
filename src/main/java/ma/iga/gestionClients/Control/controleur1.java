/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.gestionClients.Control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import ma.iga.gestionClients.Model.Client;
import ma.iga.gestionClients.Service.clientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// un controleur (intercepter les requetes HTTP + envoyer les reponses HTTP)
@RestController
public class controleur1 {
    
    // injecter un objet de type clientService
    @Autowired
    private clientService cs;
    
    // définir un endpoint (fonction qui fait un traitement et retourne des données)
    @RequestMapping // définir les priprietes d'une requete HTTP
    (value = "url1" , method = RequestMethod.GET)
    public String f(){
        return "Bonjour tout le monde";
    }
    
    // endpoint pour retourner un objet de type Client
     @RequestMapping 
    (value = "client/{code}" , method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Client h(@PathVariable long code){
        Optional<Client> oc = cs.recupererClient(code);
        if(oc.isPresent())        return oc.get();
        else                      return new Client();
    }
    @RequestMapping 
    (value = "clients" , method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Client> g(){
        return cs.recupererClients();
     }
    // endpoint pour recuperer les clients par nom
    @RequestMapping 
    (value = "clients/{nom}" , method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Client> k(@PathVariable String nom){
        return cs.recupererClientsParNom(nom);
    }
    // endpoint pour recuperer les clients d'une ville dont code>valeur donnée
    @RequestMapping 
    (value = "clients/adresseEtCode" , method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Client> k(@RequestParam String adresse,@RequestParam long code){
        return cs.recueprerClientsParAdresseEtCode(adresse, code);
    }
    // endpoint pour inserer un client dans la BD
    @RequestMapping(
            value = "/client",method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    // responseEntity -> reponse HTTP
    public ResponseEntity<String> ajoutClient(@RequestBody Client c){
        cs.ajoutClient(c);
        return new ResponseEntity<>("Contact ajouté avec succés !", 
                                        HttpStatus.CREATED);
    }
    
    // endpoint pour supprimer un client par son id
    @RequestMapping(
            value = "/client",method = RequestMethod.DELETE
    )
    public ResponseEntity<String> supprimerClient(@RequestParam long code){
        boolean b = cs.supprimerClient(code);
        if(b==true){
            return new ResponseEntity<>("Client supprimé !",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Client non trouvé !",HttpStatus.NOT_FOUND);
        }
    }
    // endpoint pour supprimer tt lee clients d'une adresse
    @RequestMapping(
            value = "/clientParAdresse/{adresse}",method = RequestMethod.DELETE
    )
    public ResponseEntity<String> supprimerClientParAdresse(@PathVariable String adresse){
        int lignes = cs.supprimerClientParAdresse(adresse);
        if(lignes==0)
            return new ResponseEntity("aucun employé supprimé",HttpStatus.OK);
        else    
            return new ResponseEntity(lignes+" Client(s) supprimé(s) !",HttpStatus.OK);
    }
    
    // ajouter l'endpoint pour modifier integralement un client
    @RequestMapping(
            value = "/client",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> mettreAJourIntegrale(@RequestBody Client c ){
        boolean b = cs.majIntegrale(c);
        if(b){ // objet trouvé
            return new ResponseEntity<>("Objet modifié !",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Objet introuvable !",HttpStatus.NOT_FOUND);
        }
    }
    // creer un endpoint qui va remplacer les valeurs null dans l'adresse mail par la combinaison du nom avec domaine
    // nom = Ahmed ALAMI . => mail = ahmed.alami@iga.ac.ma
    @RequestMapping(
            value = "/clientMail",method = RequestMethod.PATCH
    )
    public ResponseEntity<String> mettreAJourMail( ){
        int n = cs.majAdresseMails();
        return new ResponseEntity(n+" Clients ont reçu des nouvelles adresse !",HttpStatus.OK);
    }
    
    // declarer un endpoint qui va modifier soit le nom, l'adresse ou le mail
    @RequestMapping(
            value = "/maj_client_partielle",method = RequestMethod.PATCH
    )
    public ResponseEntity<String> majPartielleClient(@RequestParam int code,
                                                     @RequestParam(required = false) String nom, 
                                                     @RequestParam(required = false) String adresse, 
                                                     @RequestParam(required = false) String mail){
        Optional<Client> oc = cs.recupererClient(code);
        if(oc.isPresent()){
            Client c = oc.get();
            if(nom != null) // si le nom a été fourni
                c.setNom(nom);
            if(adresse!=null) //si l adresse a été fourni
                c.setAdresse(adresse);
            if(mail!=null) // si le mail a été fourni
                c.setMail(mail);
            cs.majIntegrale(c);
            return new ResponseEntity<>("Client modifié ! ", HttpStatus.OK);
        }else{ // code non trouvé
            return new ResponseEntity<>("Client inexistant ! ", HttpStatus.NOT_FOUND);
        }
    }
}
