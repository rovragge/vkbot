package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;




@Entity
@Table(name = "messages")
@Getter
@Setter
public class MessageVK {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "mes_id")
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user ;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private DialogState state;
}