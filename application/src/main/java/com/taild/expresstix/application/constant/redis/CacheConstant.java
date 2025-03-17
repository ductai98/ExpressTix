package com.taild.expresstix.application.constant.redis;

public class CacheConstant {
    public static final String LOCK_KEY_PREFIX = "PRO_LOCK_KEY_ITEM_";
    public static final String CACHE_KEY_PREFIX = "PRO_TICKET:ITEM:";
    public static final String CACHE_STOCK_PREFIX = "PRO_EVENT:STOCK:";
    public static final int LOCK_WAIT_TIME = 1;
    public static final int LOCK_LEASE_TIME = 1;
}
