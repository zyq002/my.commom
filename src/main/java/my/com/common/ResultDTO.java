package my.com.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.remote.entity.dto.user.SiteUserDto;

import lombok.Setter;
import my.com.util.Constant;

public class ResultDTO<BaseModelDTO> implements Serializable {

	private static final long serialVersionUID = 146181311516056125L;

	private String code;
	private String msg;
	private BaseModelDTO data;
	private List<BaseModelDTO> list;
	private Integer total;

	public ResultDTO() {
		this(null);
	}

	public ResultDTO(BaseModelDTO entity) {
		this.setData(entity);
	}

	public static <BaseModelDTO> ResultDTO<BaseModelDTO> createResultDTO() {
		return new ResultDTO<BaseModelDTO>();
	}

	public static <BaseModelDTO> ResultDTO<Object> createResultDTO(BaseModelDTO dto) {
		return createResultDTO().setData(dto);
	}

	public static <BaseModelDTO> ResultDTO<Object> createResultDTO(List<BaseModelDTO> list, Integer total) {
		return createResultDTO().setTotal(total).setList(list);
	}

	private ResultDTO<BaseModelDTO> setList(List<?> list) {
		this.list = (List<BaseModelDTO>) list;
		return this;
	}

	public ResultDTO<BaseModelDTO> setTotal(Integer total) {
		this.total = total;
		return this;
	}

	public ResultDTO<BaseModelDTO> setData(BaseModelDTO dto) {
		this.data = dto;
		return this;
	}

	public static void main(String[] agr) {
		ResultDTO dto = ResultDTO.createResultDTO(new ArrayList<>(), 5);
		System.out.println();
	}

	public String getCode() {
		if (StringUtils.isBlank(this.code)) {
			return Constant.SYSTEM_STATUS_CODE_SUCCESS;
		}
		return code;
	}

	public String getMsg() {
		if (StringUtils.isBlank(this.msg)) {
			switch (this.code) {
			case Constant.SYSTEM_STATUS_CODE_AUTHORITY:
				msg = Constant.SYSTEM_MESSAGE_AUTHORITY;
				break;
			case Constant.SYSTEM_STATUS_CODE_ERROR:
				msg = Constant.SYSTEM_MESSAGE_ERROR;
				break;
			case Constant.SYSTEM_STATUS_CODE_EXCEPTION:
				msg = Constant.SYSTEM_MESSAGE_EXCEPTION;
				break;
			default:
				msg = Constant.SYSTEM_MESSAGE_SUCCESS;
				break;
			}
		}
		return msg;
	}

	public BaseModelDTO getData() {
		return data;
	}

	public List<BaseModelDTO> getList() {
		if (CollectionUtils.isEmpty(this.list)) {
			return new ArrayList<>();
		}
		return list;
	}

	public Integer getTotal() {
		if (null == this.total) {
			return 0;
		}
		return total;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
