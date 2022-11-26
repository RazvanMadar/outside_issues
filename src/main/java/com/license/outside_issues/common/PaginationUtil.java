package com.license.outside_issues.common;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationUtil {
    public static String createPaginationQuery(Pageable pageable) {
        if (pageable == null) {
            throw new BusinessException(ExceptionReason.INVALID_PAGE_PARAMETERS);
        }
        return " OFFSET " + pageable.getOffset() + " ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY";
    }

    public static String createOrderQuery(List<Sort.Order> orders) {
        if (orders == null) {
            throw new BusinessException(ExceptionReason.INVALID_PAGE_PARAMETERS);
        }
        StringBuilder query = new StringBuilder();
        boolean isFirstOrderColumn = true;
        if (!orders.isEmpty()) {
            query.append(" ORDER BY ");
        }
        for (Sort.Order order : orders) {
            String property = order.getProperty();
            String direction = order.getDirection().toString();
            query.append(PaginationUtil.orderQueryUtil(property, direction, isFirstOrderColumn));
            isFirstOrderColumn = false;
        }
        return query.toString();
    }

    private static String orderQueryUtil(String property, String order, boolean isFirstOrderColumn) {
        return isFirstOrderColumn ? property + " " + order + " " : ", " + property + " " + order + " ";
    }
}