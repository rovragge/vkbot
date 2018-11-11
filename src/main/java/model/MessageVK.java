package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;




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
}