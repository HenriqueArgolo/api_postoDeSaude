package argolo.tech.springsecurity6.entities;

import jakarta.persistence.*;



@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    private String roleName;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public enum Values {
        BASIC(2L),
        ADMIN(1L);

        long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }
}
