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
    private DialogState dialogState ;

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

    @Column(name = "questions")
    private String questions;

    @ManyToOne
    @JoinColumn(name="cur_question_id")
    private Question currentQuestion ;
}
