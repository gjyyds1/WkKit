package cn.wekyjay.www.wkkit.data.cdkdata;

import cn.wekyjay.www.wkkit.mysql.cdksqldata.CdkSQLData;

import java.util.List;

public class CdkData_MySQL implements CdkData {

	@Override
	public void addCDKToFile(String CDK, String kits, String data, String mark) {
		new CdkSQLData().insertData(CDK, kits, data, mark);
	}

	@Override
	public void setCDKStatus(String CDK, String playername) {
		new CdkSQLData().update_Status(playername, CDK);
		
	}

	@Override
	public void setCDKMark(String mark, String newmark) {
		new CdkSQLData().update_Mark(newmark, mark);
	}

	@Override
	public void delCDKOfMark(String mark) {
		for(String cdk : findCDK(mark)) {
			new CdkSQLData().deleteData(cdk);;
		}
	}

	@Override
	public void delCDK(String cdk) {
		new CdkSQLData().deleteData(cdk);
	}

	@Override
	public List<String> findCDK(String mark) {
		return new CdkSQLData().findCDK(mark);
	}

	@Override
	public boolean Contain_CDK(String CDK) {
		return new CdkSQLData().containCDK(CDK);
	}

	@Override
	public boolean Contain_CDKMark(String mark) {
		return new CdkSQLData().containMark(mark);
	}

	@Override
	public String getCDKKits(String cdk) {
		return new CdkSQLData().findKits(cdk);
	}

	@Override
	public String getCDKDate(String cdk) {
		return  new CdkSQLData().findDate(cdk);
	}

	@Override
	public String getCDKStatus(String cdk) {
		return  new CdkSQLData().findStatus(cdk);
	}

	@Override
	public String getCDKMark(String cdk) {
		return new CdkSQLData().findMark(cdk);
	}

}
