package nc.opt.core.poc.app.service.dto.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by 2617ray on 03/03/2017.
 */
public class RessourceDTO implements Serializable {

    private String role;
    private Collection<String> ressources;

    public RessourceDTO() {

    }

    public RessourceDTO(String role, Collection<String> ressources) {
        this.role = role;
        this.ressources = ressources;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<String> getRessources() {
        return ressources;
    }

    public void setRessources(List<String> ressources) {
        this.ressources = ressources;
    }

    @Override
    public String toString() {
        return "RessourceDTO{" +
            "role='" + role + '\'' +
            ", ressources=" + ressources +
            '}';
    }
}
