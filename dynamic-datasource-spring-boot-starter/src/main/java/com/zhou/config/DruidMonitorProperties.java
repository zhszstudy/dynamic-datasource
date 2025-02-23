package com.zhou.config;


/**
 * @author zhou22
 * @@description druid监控配置
 * @date 2025/2/22
 **/
public class DruidMonitorProperties {


    public static class WebStatFilter {
        private String urlPattern;

        private boolean enabled;

        private String exclusions;

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getExclusions() {
            return exclusions;
        }

        public void setExclusions(String exclusions) {
            this.exclusions = exclusions;
        }
    }

    public static class StatViewServlet {
        private String urlPattern;

        private boolean resetEnable;

        private boolean enabled;

        private String loginUsername;

        private String loginPassword;

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }

        public boolean isResetEnable() {
            return resetEnable;
        }

        public void setResetEnable(boolean resetEnable) {
            this.resetEnable = resetEnable;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getLoginUsername() {
            return loginUsername;
        }

        public void setLoginUsername(String loginUsername) {
            this.loginUsername = loginUsername;
        }

        public String getLoginPassword() {
            return loginPassword;
        }

        public void setLoginPassword(String loginPassword) {
            this.loginPassword = loginPassword;
        }
    }
}
