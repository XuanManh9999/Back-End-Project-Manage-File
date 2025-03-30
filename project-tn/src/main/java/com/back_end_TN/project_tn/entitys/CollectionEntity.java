package com.back_end_TN.project_tn.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CollectionEntity extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name; // Tên bộ sưu tập

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // Chủ sở hữu bộ sưu tập

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<FileEntity> files = new ArrayList<>();
}
