package model;

import database.HibernateUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vk_id")
    private Integer vkID;

    @ManyToOne
    @JoinColumn(name="dialog_state_id")
    private DialogState dialogState ;

    @OneToMany(mappedBy = "user")
    private List<MessageVK> messages;

    @Column(name = "secret")
    private String secret;

    @Column(name = "secret_expected")
    private String secretExpected;

    @Column(name = "secret_keyboard")
    private String secretKeyboard;

    @Column(name = "secret_keys")
    private String secretKeys;

    @Column(name = "secret_length")
    private Integer secretLength;

    @ManyToOne
    @JoinColumn(name="secret_target_state")
    private DialogState secretTargetState ;

//    public List<MessageVK> getMessages(){
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Hibernate.initialize(messages);
//        session.close();
//        return messages;
//    }

}
