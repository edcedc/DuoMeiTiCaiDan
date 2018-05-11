package com.edc.dmt.bean;

import java.util.List;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/6/21 下午11:16
 * 描述:
 */
public class BannerModelBean {

    private List<String> imgs;
    private List<String> tips;

    public List<String> getImgs() {
        return imgs;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }
}