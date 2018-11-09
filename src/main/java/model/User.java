package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private DialogState dialogState;
}
