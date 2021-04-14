package com.yinni.tlk.dto;

import lombok.Data;

@Data
public class Action {
    @Data
    public class DataX{
        private String from;
        private String to;
        private String quantity;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }
    String account;
    String name;
    DataX data;
}
