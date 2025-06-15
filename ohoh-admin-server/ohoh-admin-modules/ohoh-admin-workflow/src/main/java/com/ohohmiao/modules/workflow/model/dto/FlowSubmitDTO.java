package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.modules.workflow.model.pojo.FlowProcessForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 提交流程
 *
 * @author ohohmiao
 * @date 2025-06-14 13:57
 */
@ApiModel("提交流程")
@Getter
@Setter
public class FlowSubmitDTO extends FlowInfoQueryDTO {

    @ApiModelProperty(value ="下一步环节办理人", required = true)
    @NotNull(message = "下一步环节办理人不能为空")
    private List<FlowNextHandlerDTO> nextHandlerList;

    @ApiModelProperty(value = "流程审核表单")
    private FlowProcessForm processForm;

    @ApiModelProperty(value ="业务表单", required = true)
    @NotNull(message = "业务表单不能为空")
    private Object businessForm;

}
