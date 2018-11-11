package model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

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

    @Column(name = "secret_keyboard")
    private String secretKeyboard;

    @Column(name = "secret_keys")
    private String secretKeys;

    @Column(name = "secret_length")
    private Integer secretLength;

    public List<MessageVK> getMessages(){
        Hibernate.initialize(messages);
        return messages;
    }

}
