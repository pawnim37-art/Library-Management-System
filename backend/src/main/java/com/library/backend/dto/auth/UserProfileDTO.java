package com.library.backend.dto.auth;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserProfileDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String role;
    private Boolean active;
    private LocalDateTime createdAt;
    private Integer activeLoansCount;
    private Integer totalBorrowCount;
    private BigDecimal pendingFinesAmount;

    public UserProfileDTO() {}

    public UserProfileDTO(Long id, String email, String fullName, String phone, String address, String role, Boolean active, LocalDateTime createdAt, Integer activeLoansCount, Integer totalBorrowCount, BigDecimal pendingFinesAmount) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.activeLoansCount = activeLoansCount;
        this.totalBorrowCount = totalBorrowCount;
        this.pendingFinesAmount = pendingFinesAmount;
    }

    public static UserProfileDTOBuilder builder() { return new UserProfileDTOBuilder(); }

    public static class UserProfileDTOBuilder {
        private Long id;
        private String email;
        private String fullName;
        private String phone;
        private String address;
        private String role;
        private Boolean active;
        private LocalDateTime createdAt;
        private Integer activeLoansCount;
        private Integer totalBorrowCount;
        private BigDecimal pendingFinesAmount;

        public UserProfileDTOBuilder id(Long id) { this.id = id; return this; }
        public UserProfileDTOBuilder email(String email) { this.email = email; return this; }
        public UserProfileDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserProfileDTOBuilder phone(String phone) { this.phone = phone; return this; }
        public UserProfileDTOBuilder address(String address) { this.address = address; return this; }
        public UserProfileDTOBuilder role(String role) { this.role = role; return this; }
        public UserProfileDTOBuilder active(Boolean active) { this.active = active; return this; }
        public UserProfileDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserProfileDTOBuilder activeLoansCount(Integer activeLoansCount) { this.activeLoansCount = activeLoansCount; return this; }
        public UserProfileDTOBuilder totalBorrowCount(Integer totalBorrowCount) { this.totalBorrowCount = totalBorrowCount; return this; }
        public UserProfileDTOBuilder pendingFinesAmount(BigDecimal pendingFinesAmount) { this.pendingFinesAmount = pendingFinesAmount; return this; }

        public UserProfileDTO build() {
            return new UserProfileDTO(id, email, fullName, phone, address, role, active, createdAt, activeLoansCount, totalBorrowCount, pendingFinesAmount);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getActiveLoansCount() { return activeLoansCount; }
    public void setActiveLoansCount(Integer activeLoansCount) { this.activeLoansCount = activeLoansCount; }
    public Integer getTotalBorrowCount() { return totalBorrowCount; }
    public void setTotalBorrowCount(Integer totalBorrowCount) { this.totalBorrowCount = totalBorrowCount; }
    public BigDecimal getPendingFinesAmount() { return pendingFinesAmount; }
    public void setPendingFinesAmount(BigDecimal pendingFinesAmount) { this.pendingFinesAmount = pendingFinesAmount; }
}
