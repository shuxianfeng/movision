package com.zhuhuibao.common.pojo;

public class AuthcMember {
        private Long id;
        private String account;
        private int status;
        private String identify;
        private String role;
        private boolean isexpert;
        private Long companyId;
        
		public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
        
        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
        
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        
        public String getIdentify() {
            return identify;
        }

        public void setIdentify(String identify) {
            this.identify = identify;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

		public boolean getIsexpert() {
			return isexpert;
		}

		public void setIsexpert(boolean isexpert) {
			this.isexpert = isexpert;
		}

        public Long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Long companyId) {
            this.companyId = companyId;
        }
}