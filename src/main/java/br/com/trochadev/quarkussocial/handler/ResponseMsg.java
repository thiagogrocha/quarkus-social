package br.com.trochadev.quarkussocial.handler;

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
