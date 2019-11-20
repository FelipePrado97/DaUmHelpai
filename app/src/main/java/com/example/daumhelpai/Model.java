package com.example.daumhelpai;

import android.graphics.Bitmap;

public class Model {

    private Bitmap image;
    private String title, desc, pagamento;

    public Model(Bitmap image, String title, String desc, String pagamento) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.pagamento = pagamento;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPagamento(){return pagamento;}
    public void setPagamento(String pagamento){this.pagamento = pagamento; }
}
