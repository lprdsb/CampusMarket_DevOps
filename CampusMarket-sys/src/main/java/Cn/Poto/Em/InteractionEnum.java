package Cn.Poto.Em;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InteractionEnum {

    SAVE(1, "收藏"),
    VIEW(2, "浏览"),
    LOVE(3, "想要"),
    NEW(4, "新增");

    /**
     * 状态
     */
    private final Integer type;
    /**
     * 状态解释
     */
    private final String detail;

}
