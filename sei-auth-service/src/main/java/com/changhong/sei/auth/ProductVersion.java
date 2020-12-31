package com.changhong.sei.auth;

import com.changhong.sei.core.context.Version;

/**
 * 平台版本
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 16:29
 */
public final class ProductVersion extends Version {

    private ProductVersion() {
        super(ProductVersion.class.getPackage().getName());
    }

}
