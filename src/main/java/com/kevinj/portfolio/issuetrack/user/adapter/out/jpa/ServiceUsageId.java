package com.kevinj.portfolio.issuetrack.user.adapter.out.jpa;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ServiceUsageId implements Serializable {
    private Long userId;
    private String service;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServiceUsageId that = (ServiceUsageId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, service);
    }
}