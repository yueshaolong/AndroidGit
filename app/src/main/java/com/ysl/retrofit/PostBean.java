package com.ysl.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/9/21.
 */

public class PostBean {

    /**
     * basic : {"explains":["好处","好的","好"],"phonetic":"gʊd","uk-phonetic":"gʊd","us-phonetic":"ɡʊd"}
     * errorCode : 0
     * query : good
     * translation : ["好"]
     * web : [{"key":"good","value":["良好","善","美好"]}]
     *//*

    private BasicBean basic;
    private int errorCode;
    private String query;
    private List<String> translation;
    private List<WebBean> web;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public static class BasicBean {
        *//**
         * explains : ["好处","好的","好"]
         * phonetic : gʊd
         * uk-phonetic : gʊd
         * us-phonetic : ɡʊd
         *//*

        private String phonetic;
        @SerializedName("uk-phonetic")
        private String ukphonetic;
        @SerializedName("us-phonetic")
        private String usphonetic;
        private List<String> explains;

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkphonetic() {
            return ukphonetic;
        }

        public void setUkphonetic(String ukphonetic) {
            this.ukphonetic = ukphonetic;
        }

        public String getUsphonetic() {
            return usphonetic;
        }

        public void setUsphonetic(String usphonetic) {
            this.usphonetic = usphonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

        @Override
        public String toString() {
            return "BasicBean{" +
                    "phonetic='" + phonetic + '\'' +
                    ", ukphonetic='" + ukphonetic + '\'' +
                    ", usphonetic='" + usphonetic + '\'' +
                    ", explains=" + explains +
                    '}';
        }
    }

    public static class WebBean {
        *//**
         * key : good
         * value : ["良好","善","美好"]
         *//*

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "WebBean{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostBean{" +
                "basic=" + basic +
                ", errorCode=" + errorCode +
                ", query='" + query + '\'' +
                ", translation=" + translation +
                ", web=" + web +
                '}';
    }*/

    private String type;
    private int errorCode;
    private int elapsedTime;
    private List<List<TranslateResultBean>> translateResult;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public List<List<TranslateResultBean>> getTranslateResult() {
        return translateResult;
    }

    public void setTranslateResult(List<List<TranslateResultBean>> translateResult) {
        this.translateResult = translateResult;
    }

    public static class TranslateResultBean {
        /**
         * src : merry me
         * tgt : 我快乐
         */

        public String src;
        public String tgt;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getTgt() {
            return tgt;
        }

        public void setTgt(String tgt) {
            this.tgt = tgt;
        }

        @Override
        public String toString() {
            return "TranslateResultBean{" +
                    "src='" + src + '\'' +
                    ", tgt='" + tgt + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostBean{" +
                "type='" + type + '\'' +
                ", errorCode=" + errorCode +
                ", elapsedTime=" + elapsedTime +
                ", translateResult=" + translateResult +
                '}';
    }
}
