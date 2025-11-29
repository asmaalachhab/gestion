/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.iga.gestionClients.repositories;

import java.util.List;
import ma.iga.gestionClients.Model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
public interface clientRepository extends CrudRepository<Client,Long >{
    // ajouter des methodes autres que ceux sur le CrudRepository :
    // 1 - approche basée sur les nomenclatures
    public List<Client> findByNameLikeIgnoreCase(String nom);
    public List<Client> findByCodeBetween(long min,long max);
    public List<Client> findByAdresseLikeIgnoreCaseAndCodeGreaterThan
                                            (String adresse,long code);
                                            
    // equivaut à 'SELECT * FROM client WHERE name LIKE nom'
    // 2 - basée sur le langage JPQL        
    @Query("SELECT c FROM Client c WHERE c.code BETWEEN :min AND :max") 
    // SQL equivalent : SELECT * FROM client WHERE Code BETWEEN min AND max
    public List<Client> recupererClientsParCode(@Param("min")long X,
                                                @Param("max")long Y);
    @Query("SELECT c FROM Client c WHERE c.adresse LIKE :adresse"
            + " AND c.code > :code ")
    public List<Client> recupererClientsParAdresseEtCode
        (@Param("adresse") String adresse,@Param("code")long code);
        
    // methode par nomenclature pour supprimer des clients par leurs adresses    
    public int deleteByAdresseLikeIgnoreCase(String adresse);    
    
    @Transactional // mettre la requete JPQL dans une transaction
    @Modifying     // declarer que la requete peut modifier le contenu de la BD
    @Query("DELETE FROM Client c WHERE c.adresse LIKE :adresse")    
    public int supprimerClientsParADresse(@Param("adresse") String adresse);    
    
    // methode pour mettre a jour les adresse mails vides
    @Transactional
    @Modifying
    @Query("UPDATE Client c SET c.mail = CONCAT(REPLACE(c.name,' ','.'),'@iga.ac.ma')")
    public int modifierAdressesMails();
    
    
}
