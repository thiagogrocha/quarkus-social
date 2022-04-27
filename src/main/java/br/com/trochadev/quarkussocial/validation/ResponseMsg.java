package br.com.trochadev.quarkussocial.validation;

import lombok.Data;

@Data
public class ResponseMsg {
    private String msg;

    public ResponseMsg() {
    }

    public ResponseMsg(String msg) {
        this.msg = msg;
    }
}
