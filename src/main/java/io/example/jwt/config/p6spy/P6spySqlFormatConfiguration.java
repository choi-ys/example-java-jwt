package io.example.jwt.config.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author : choi-ys
 * @date : 2021/04/13 5:52 오후
 * @Content : 실행 쿼리 출력 시 pretty print를 위한 MessageFormatter
 */
public class P6spySqlFormatConfiguration implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        return "\n -> [Meta info] : " +
                LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(now)), TimeZone.getDefault().toZoneId()) +
                " | duration " + elapsed +"ms" + " | connection : " + connectionId + sql;
    }

    private String formatSql(String category,String sql) {
        if(sql ==null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            sql = "\n -> [Hibernate format] : " + sql;
        }

        return sql;
    }
}