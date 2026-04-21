package com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories = new ArrayList<>();

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private Integer depth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isUse;

    public Category(Long categoryId, Category parentCategory, String label, Integer depth, YN isUse) {
        this.categoryId = categoryId;
        this.parentCategory = parentCategory;
        this.label = label;
        this.depth = depth;
        this.isUse = isUse;
    }
}
