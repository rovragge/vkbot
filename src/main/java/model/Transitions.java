package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transitions")
@Getter
@Setter
public class Transitions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="dialog_state_id")
    private DialogState dialogState ;

    @Column(name = "message")
    private String message;

    @Column(name = "auth")
    private boolean auth;

    @Column(name = "regex")
    private boolean regex;

    @ManyToOne
    @JoinColumn(name="target_dialog_state_id")
    private DialogState targetDialogState ;
}
