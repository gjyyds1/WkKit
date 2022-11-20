package cn.wekyjay.www.wkkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public enum TabCdk {
	 	FIRST(Arrays.asList("cdk"),0,null,new int[]{1}),
		CDK_COMMAND(Arrays.asList("create","verify","exchange","export"),1,"cdk",new int[]{2}),
		CDK_NUM(Arrays.asList("<数量>"),2,"create",new int[]{3}),
		CDK_CDK(Arrays.asList("<CDK>"),2,"verify",new int[]{4}),
		CDK_CDK2(Arrays.asList("<CDK>"),2,"exchange",new int[]{4}),
		CDK_KIT(Arrays.asList("<KitName1>,<Kitname2>..."),2,"create",new int[]{4}),
		CDK_EXPORT(Arrays.asList("<标记>"),2,"export",new int[]{4});
	
		
	    private List<String> list;//返回的List
	    private int befPos;//应该识别的上一个参数的位置
	    private String bef;//应该识别的上个参数的内容
	    private int[] num;//这个参数可以出现的位置
	    
	    private TabCdk(List<String> list,int befPos, String bef, int[] num){
	        this.list = list;
	        this.befPos = befPos;
	        this.bef = bef;
	        this.num = num.clone();
	    }
	    
	    public String getBef() {
			return bef;
		}
	    public int getBefPos() {
			return befPos;
		}
	    public List<String> getList() {
	    	return list;
		}
	    public int[] getNum() {
			return num;
		}
	    
	    
	    public static List<String> returnList(String[] Para, int curNum, CommandSender sender) {
	        for(TabCdk tab : TabCdk.values()){
	            if(tab.getBefPos()-1 >= Para.length){
	                continue;
	            }
	            if((tab.getBef() == null || tab.getBef().equalsIgnoreCase(Para[tab.getBefPos()-1])) && Arrays.binarySearch(tab.getNum(),curNum)>=0){
	            	List<String> list = new ArrayList<String>();
	            	if(!(Para[tab.getNum()[0] - 1] == null)) {
	                	int length = Para[tab.getNum()[0] - 1].length();
	                	String abc = Para[tab.getNum()[0] - 1];
	                	for(String s : tab.getList()) {
	                		if(s.regionMatches(true, 0, abc, 0, length)) list.add(s);
	                	}
	                	return list;
	                }else {
	                	return tab.getList();
	                }
	            }
	        }
	        return null;
	    }
}
