package cn.wekyjay.www.wkkit.data.cdkdata;

import java.util.List;

public interface CdkData {
	/**
	 * 添加CDK数据
	 * @param CDK
	 * @param kits
	 * @param data
	 * @param mark
	 */
	void addCDKToFile(String CDK, String kits, String data,String mark);
	/**
	 * 设置指定cdk的使用情况
	 * @param CDK
	 * @param playername
	 */
	void setCDKStatus(String CDK,String playername);
	/**
	 * 修改指定mark的mark
	 * @param mark
	 * @param newmark
	 */
	void setCDKMark(String mark,String newmark);
	/**
	 * 删除mark为mark的所有CDK
	 * @param mark
	 */
	void delCDKOfMark(String mark);
	/**
	 * 删除指定的CDK
	 * @param cdk
	 */
	void delCDK(String cdk);
	/**
	 * 返回有指定mark的兑换码
	 * @param mark
	 */
	List<String> findCDK(String mark);
	/**
	 * 判断是否存在指定的CDK
	 * @param CDK
	 * @return
	 */
	boolean Contain_CDK(String CDK);
	/**
	 * 判断是否存在指定的Mark
	 * @param mark
	 * @return
	 */
	boolean Contain_CDKMark(String mark);
	/**
	 * 获取指定cdk的kits
	 * @param cdk
	 * @return
	 */
	String getCDKKits(String cdk);
	/**
	 * 获取指定CDK的Date
	 * @param cdk
	 * @return
	 */
	String getCDKDate(String cdk);
	/**
	 * 获取指定CDK的Status
	 * @param cdk
	 * @return
	 */
	String getCDKStatus(String cdk);
	/**
	 * 获取指定CDK的Mark
	 * @param cdk
	 * @return
	 */
	String getCDKMark(String cdk);
}
