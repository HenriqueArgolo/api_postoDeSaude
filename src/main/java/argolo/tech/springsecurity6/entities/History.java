package argolo.tech.springsecurity6.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany

    private List<Appointment> appointments;

}
