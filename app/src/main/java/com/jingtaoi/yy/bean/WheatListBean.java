package com.jingtaoi.yy.bean;

import java.util.List;

public class WheatListBean {

    private String msg;
    private int code;
    private DataBean data;
    private String sys;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public static class DataBean {

        /**
         * * \num	number	等待人数
         * * state	number	是否已经排麦，1 未， 2已排
         */
        private int num;
        private int state;
        private List<UserListBean> userList;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class UserListBean {
            /**
             * id : 100
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 18382414601
             * usercoding : 19372056
             */

            /**
             * - - userList	object	无
             * id	number	用户id
             * img	string	头像
             * name	string	昵称
             * usercoding	string	用户的唯一标识
             */
            private int id;
            private String img;
            private String name;
            private String usercoding;
            private int sex;

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsercoding() {
                return usercoding;
            }

            public void setUsercoding(String usercoding) {
                this.usercoding = usercoding;
            }
        }
    }
}
