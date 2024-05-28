package argolo.tech.springsecurity6.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_procedures")
public class Procedures {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "procedures_id")
    private long id;
    private String name;

    private String status;

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
