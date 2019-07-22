package my.com.common;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDTO<BaseModelDTO> implements Serializable {
	
	private static final long serialVersionUID = 146181311516056125L;

	private String code;
	private String msg;
	private BaseModelDTO data;
	private List<BaseModelDTO> list;
	private Integer total;
}
