package com.ruqimobility.precisiontest.utils;

import com.ruqi.common.message.BaseResponse;
import com.ruqimobility.precisiontest.constants.enums.SchedulerResultEnum;

/**
 * @Author ：xiansen.yang
 * @Date ：created in 2019/04/04 11:36
 * @Description ：ResponseUtils
 * @Modified_By ：
 * @Version : 1.0
 */
public class ResponseUtils {

    public static <T> BaseResponse<T> fail() {
        return new BaseResponse<>(SchedulerResultEnum.SYSTEM_ERROR.code, SchedulerResultEnum.SYSTEM_ERROR.message);
    }

    public static <T> BaseResponse<T> bad() {
        return new BaseResponse<>(SchedulerResultEnum.SYSTEM_ERROR.code, SchedulerResultEnum.SYSTEM_ERROR.message);
    }

    public static <T> BaseResponse<T> ok() {

        return new BaseResponse<>(SchedulerResultEnum.SUCCESS.code, SchedulerResultEnum.SUCCESS.message);
    }

    public static <T> BaseResponse<T> bad(T data) {
        return result(SchedulerResultEnum.SYSTEM_ERROR.getCode(), data, SchedulerResultEnum.SYSTEM_ERROR.getMessage());
    }

    public static <T> BaseResponse<T> ok(T data) {
        return result(SchedulerResultEnum.SUCCESS.getCode(), data, SchedulerResultEnum.SUCCESS.getMessage());
    }

    public static <T> BaseResponse<T> result(int code, T data, String message) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode(code);
        result.setData(data);
        result.setMessage(message);
        return result;
    }
}
