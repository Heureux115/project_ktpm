    package com.example.demo4.models;

    import java.util.List;
    import java.time.LocalDate;

    public class Household {
        private int householdId;        
        private Integer headCitizenId;  
        private String address;         
        private String street;          
        private String ward;            
        private String district;        

        private List<Citizen> members;
        private Integer ownerUserId;

        
        private String changeNote;
        private LocalDate lastChangeDate;

        public Household(int householdId, Integer headCitizenId, String address, Integer ownerUserId) {
            this.householdId = householdId;
            this.headCitizenId = headCitizenId;
            this.address = address;
            this.ownerUserId = ownerUserId;
        }

        public Household(){}

        @Override
        public String toString() {
            return "Số HK: " + householdId + " - " + address + ", " + street;
        }

        
        public int getHouseholdId() { return householdId; }
        public void setHouseholdId(int householdId) { this.householdId = householdId; }
        public Integer getHeadCitizenId() { return headCitizenId; }
        public void setHeadCitizenId(Integer headCitizenId) { this.headCitizenId = headCitizenId; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getWard() { return ward; }
        public void setWard(String ward) { this.ward = ward; }
        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }
        public List<Citizen> getMembers() { return members; }
        public void setMembers(List<Citizen> members) { this.members = members; }
        public Integer getOwnerUserId() { return ownerUserId; }
        public void setOwnerUserId(Integer ownerUserId) { this.ownerUserId = ownerUserId; }
        public String getChangeNote() { return changeNote; }
        public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
        public LocalDate getLastChangeDate() { return lastChangeDate; }
        public void setLastChangeDate(LocalDate lastChangeDate) { this.lastChangeDate = lastChangeDate; }
    }