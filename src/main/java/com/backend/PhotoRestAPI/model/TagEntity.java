package com.backend.PhotoRestAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Audited(targetAuditMode = NOT_AUDITED)
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<PhotoEntity> photos;
}
