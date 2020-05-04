package com.example.dankerbell.pillManagement;

public class InfoaddPill {
//        private String user;
        private String pill_name;
        private String amount;
        private String unit_amount;
        private String count;
        private String takingTime;
        private String pilltime;
        private String times;

        public InfoaddPill(){ }

        public InfoaddPill(String pill_name, String amount, String unit_amount, String count,
                        String takingTime,String pilltime, String times ){
//            this.user = user;
            this.pill_name = pill_name;
            this.amount = amount;
            this.unit_amount = unit_amount;
            this.count = count;
            this.takingTime = takingTime;
            this.pilltime = pilltime;
            this.times= times;
        }
//        public String getUser(){return user;}
        public String getPill_name(){
            return pill_name;
        }
        public String getAmount(){
            return amount;
        }
        public String getUnit_amount(){
            return unit_amount;
        }
        public String getCount(){
            return count;
        }
        public String getTakingTime(){ return takingTime; }
        public String getPilltime(){ return pilltime; }
        public String getTimes(){ return times; }

}
