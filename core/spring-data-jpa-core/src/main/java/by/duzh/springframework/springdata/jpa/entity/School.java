package by.duzh.springframework.springdata.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Many-to-one relationship with unidirectional associations.
 *<p/>
 * Many instances of an entity are associated with one instance of another entity.
 *<p/>
 * Each Student can be enrolled in one School only, but each School can have multiple Students.
 */
@Data
@ToString
//@ToString(exclude = "userChats")
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "school")
public class School implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
}