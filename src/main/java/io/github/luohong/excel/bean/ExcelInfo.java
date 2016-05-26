package io.github.luohong.excel.bean;
import java.io.Serializable;


public class ExcelInfo implements Serializable{

	private static final long serialVersionUID = 5198858300880926713L;
	protected Long id;
	
	private String type;
	private String name;
	private String path;
	private String attachedfilename;
	private String excelid;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	

	public String getAttachedfilename() {
		return attachedfilename;
	}
	public void setAttachedfilename(String attachedfilename) {
		this.attachedfilename = attachedfilename;
	}
	

	public String getExcelid() {
		return excelid;
	}
	public void setExcelid(String excelid) {
		this.excelid = excelid;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
