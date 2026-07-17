package com.yourcompany.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 全局分布式 ID 生成器
 */
public class XueHuaiID {

    // 私有化构造方法，防止被实例化
    private XueHuaiID() {}

    // ================= 核心：全局单例生成器 =================
    // 参数说明：workerId=1, datacenterId=1
    // 注意：在微服务集群中，workerId 应从配置文件读取或通过 Redis/ZK 动态分配，避免多节点冲突
    private static final Snowflake USER_SNOWFLAKE = IdUtil.getSnowflake(1, 1);
    private static final Snowflake ORDER_SNOWFLAKE = IdUtil.getSnowflake(1, 2); // 订单使用独立的 datacenterId 区分

    // 日期格式化器（线程安全）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 生成用户 ID (Long 型，标准雪花算法)
     */
    public static long generateUserId() {
        return USER_SNOWFLAKE.nextId();
    }

    /**
     * 生成订单 ID (String 型，带日期前缀，方便客服和运营肉眼识别)
     * 示例输出: ORD20260704194059001
     */
    public static String generateOrderNo() {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        // 取雪花ID的后 6 位作为流水号，既保证唯一性，又大幅缩短订单号长度
        long snowflakeId = ORDER_SNOWFLAKE.nextId();
        String sequence = String.format("%06d", snowflakeId % 1000000);
        // 订单号格式：ORD + 日期 + 流水号
        return String.format("ORD%s%s", dateStr, sequence);
    }


}