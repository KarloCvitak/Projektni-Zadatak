package hr.java.projektnizadatak.entitet;

public class Korisnik extends Entitet{

    private String username;
    private String password;
    private Integer role;


    public Korisnik(Long id, String username, String password, Integer role) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
