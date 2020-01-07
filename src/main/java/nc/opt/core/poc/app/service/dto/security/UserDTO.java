package nc.opt.core.poc.app.service.dto.security;

import nc.opt.core.poc.app.security.UserOPT;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities/roles and ressources.
 */
public class UserDTO implements Serializable {

    private String uid;
    private String nom;
    private String prenom;
    private List<String> authorities;
    private List<RessourceDTO> ressources;

    public UserDTO() {
        // Empty constructor needed for MapStruct.
    }

    public UserDTO(UserOPT user) {
        this.authorities = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        this.ressources = user.getRessources().entrySet().stream()
            .map(map -> new RessourceDTO(map.getKey(), map.getValue()))
            .collect(Collectors.toList());
        this.uid = user.getUid();
        this.nom = user.getNom();
        this.prenom = user.getPrenom();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<RessourceDTO> getRessources() {
        return ressources;
    }

    public void setRessources(List<RessourceDTO> ressources) {
        this.ressources = ressources;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "uid='" + uid + '\'' +
            ", nom='" + nom + '\'' +
            ", prenom='" + prenom + '\'' +
            ", authorities=" + authorities +
            ", ressources=" + ressources +
            '}';
    }
}
