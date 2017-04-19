package com.elend.gate.admin.constant;

import com.elend.gate.admin.cfg.GateAdminCommConfig;

/**
 * 常量定义
 * @author liuxianyan
 *
 */
public class ReconciliationConstant {
	
	/**分页每页显示记录数*/
	public static final int PAGE_SIZE=GateAdminCommConfig.getInt("page_size", 15);
	
	/**对于分页，每页最多允许1000条数据*/
	public static final int PAGE_MAX_ROWS = GateAdminCommConfig.getInt("page_max_size", 1000);
	
	/** 是否使用本地 cache(EHCache) */
	public static final boolean LOCAL_CACHE = GateAdminCommConfig.getBool("local_cache");
	
//	/**系统id**/
	public static int app_id = GateAdminCommConfig.getInt("app_id", 11);
//	/**系统标识**/
	public static String app_key = GateAdminCommConfig.get("app_key");
	//文件上传保存的路径
	public static final String UPLOAD_FILEPATH = GateAdminCommConfig.get("upload_filepath");
	//文件上传后平衡的规模
	public static final int UPLOAD_FILEDEPLOY = GateAdminCommConfig.getInt("upload_filedeploy",10);
	//生成打包文件的路径
	public static final String TARGET_FILEPATH = GateAdminCommConfig.get("target_filepath");
	//生成打包文件的路径的平衡规模
	public static final int  TARGET_FILEDEPLOY = GateAdminCommConfig.getInt("target_filedeploy",20);
	//脚本存放路径
	public static final String SHELL_FILEPATH = GateAdminCommConfig.get("shell_filepath");
	
	public static final String TOOLS_CONFIG_FILE_NAME = "tools.config";
	
	public static final String PARA_CONFIG_FILE_NAME = "para.config";
	
	/** ToolsFile.check_status: 未审核 */
	public static final int TOOLS_FILE_NOAUTHOR = 1;
	/** ToolsFile.check_status: 审核通过 */
	public static final int TOOLS_FILE_OKAUTHOR = 2;
	
	/** Tools.check_status: 未审核 */
	public static final int TOOLS_NOAUTHOR = 1;
	/** Tools.check_status: 审核通过 */
	public static final int TOOLS_OKAUTHOR = 2;
	/** Tools.check_status: 审核不通过 */
	public static final int TOOLS_NOPASS = 3;
	
	public static final short PUB_SERVER_LOG_SYNC_NO = 0;//未加入队列
	public static final short PUB_SERVER_LOG_SYNC_OK = 1;//已加入队列
	
	public static final int QUERY_CENTER_DETAIL = 1;//从主控节点获取详细信息
	public static final int QUERY_CENTER_VALUE  = 2;//从主控节点获取value值信息
	
	public static final String app_name = "reconciliation";
}
