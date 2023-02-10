package hr.java.projektnizadatak.entitet;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Promjena extends Entitet implements Serializable {
    private String podatak;
    private String staraVrijednost;
    private String novaVrijednost;
    private String user;
    private LocalDateTime vrijeme;

    public Promjena(Long id, String podatak, String staraVrijednost, String novaVrijednost, String user, LocalDateTime vrijeme) {
        super(id);
        this.podatak = podatak;
        this.staraVrijednost = staraVrijednost;
        this.novaVrijednost = novaVrijednost;
        this.user = user;
        this.vrijeme = vrijeme;
    }

    public String getPodatak() {
        return podatak;
    }

    public void setPodatak(String podatak) {
        this.podatak = podatak;
    }

    public String getStaraVrijednost() {
        return staraVrijednost;
    }

    public void setStaraVrijednost(String staraVrijednost) {
        this.staraVrijednost = staraVrijednost;
    }

    public String getNovaVrijednost() {
        return novaVrijednost;
    }

    public void setNovaVrijednost(String novaVrijednost) {
        this.novaVrijednost = novaVrijednost;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(LocalDateTime vrijeme) {
        this.vrijeme = vrijeme;
    }
}