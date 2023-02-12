package hr.java.projektnizadatak.entitet;

public class Korisnik<T extends Integer> extends Entitet{

    private String username;
    private String password;
    private T role;


    public Korisnik(Long id, String username, String password, T role) {
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

    public T getRole() {
        return role;
    }

    public void setRole(T role) {
        this.role = role;
    }
}
