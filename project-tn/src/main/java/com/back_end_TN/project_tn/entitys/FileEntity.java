package com.back_end_TN.project_tn.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity extends BaseEntity<Long>{

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String cloudinaryUrl;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    public String publicID;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String fileHash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionEntity collection; // Bộ sưu tập chứa file

}