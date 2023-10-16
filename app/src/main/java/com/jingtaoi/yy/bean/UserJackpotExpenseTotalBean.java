package com.jingtaoi.yy.bean;

public class UserJackpotExpenseTotalBean {
    /**
     * {
     * "code": 0,
     * "data": {
     * "breakEvenMoney": -276018,
     * "expenseMoney": 36860,
     * "winMoney": 312878
     * },
     * "msg": "获取成功",
     * "sys": "1658571664646"
     * }
     */

    private String msg;
    private int code;
    private long sys;

    private UserJackpotExpenseTotal data;


  public   class UserJackpotExpenseTotal {
        /**
         * 总盈利
         */
        private long breakEvenMoney = 0;
        /**
         * 总投入
         */
        private long expenseMoney = 0;

        /**
         * 总产出
         */
        private long winMoney = 0;

        public long getBreakEvenMoney() {
            return breakEvenMoney;
        }

        public void setBreakEvenMoney(long breakEvenMoney) {
            this.breakEvenMoney = breakEvenMoney;
        }

        public long getExpenseMoney() {
            return expenseMoney;
        }

        public void setExpenseMoney(long expenseMoney) {
            this.expenseMoney = expenseMoney;
        }

        public long getWinMoney() {
            return winMoney;
        }

        public void setWinMoney(long winMoney) {
            this.winMoney = winMoney;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getSys() {
        return sys;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }

    public UserJackpotExpenseTotal getData() {
        return data;
    }

    public void setData(UserJackpotExpenseTotal data) {
        this.data = data;
    }
}
