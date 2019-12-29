package ${package};

import lombok.*;

/**
 *
 * @author ${author} 
 * @date ${date}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Query${className} extends ${className} {
    private Integer offset = 0;
    private Integer limit = 20;
    private Long startTime;
    private Long endTime;
    private String orderStr;
    private Long minId;
    private Long maxId;
}
