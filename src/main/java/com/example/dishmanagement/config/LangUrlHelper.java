package com.example.dishmanagement.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Tạo link đổi ngôn ngữ nhưng GIỮ NGUYÊN các điều kiện tìm kiếm/lọc/sắp xếp đang có trên URL.
 * Dùng trong template: ${@langUrl.base() + 'vi'}  ->  /dishes?keyword=ph&sort=price_asc&lang=vi
 */
@Component("langUrl")
public class LangUrlHelper {

    public String base() {
        HttpServletRequest req =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        StringBuilder query = new StringBuilder();
        String raw = req.getQueryString();
        if (raw != null) {
            for (String part : raw.split("&")) {
                if (part.isEmpty() || part.startsWith("lang=")) continue;
                if (query.length() > 0) query.append('&');
                query.append(part);
            }
        }
        if (query.length() > 0) query.append('&');
        return req.getRequestURI() + "?" + query + "lang=";
    }
}
