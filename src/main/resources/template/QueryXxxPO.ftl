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
    private Integer offset = null;
    private Integer limit = null;
    private Long startTime;
    private Long endTime;
    private String orderStr;
    private Long minId;
    private Long maxId;
}
