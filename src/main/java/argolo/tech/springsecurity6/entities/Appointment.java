package argolo.tech.springsecurity6.entities;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@Entity
@Table(name = "tb_appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "appointment_id")
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "procedures_id")
    private Procedures procedures;

    private String healthCenter;

    private String appointmentDate;

    @CreationTimestamp
    private Instant creationTimesTamp;

    private String status;

    private Integer position;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Procedures getProcedures() {
        return procedures;
    }

    public void setProcedures(Procedures procedures) {
        this.procedures = procedures;
    }

    public String getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(String healthCenter) {
        this.healthCenter = healthCenter;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Instant getCreationTimesTamp() {
        return creationTimesTamp;
    }

    public void setCreationTimesTamp(Instant creationTimesTamp) {
        this.creationTimesTamp = creationTimesTamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }
}



