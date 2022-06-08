package cn.wekyjay.www.wkkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGroupManager;
import cn.wekyjay.www.wkkit.tool.WKTool;

public enum TabGroup {
	
	
    FIRST(Arrays.asList("group"),0,null,new int[]{1}),
	FUNCTION(Arrays.asList("create","move","delete","send","list"),1,"group",new int[]{2}),
	GROUP_CREATE(Arrays.asList("<GroupName>"),2,"create",new int[]{3}),
	GROUP_NAME(Kit.getKitNames(),2,"move",new int[]{3}),
	GROUP_NAME2(KitGroupManager.getGroups(),2,"send",new int[]{3}),
	GROUP_NAME3(KitGroupManager.getGroups(),2,"delete",new int[]{3}),
	MOVE_GROUP(KitGroupManager.getGroups(),2,"move",new int[]{4}),
	SEND_KIT(WKTool.getPlayerNames(),2,"send",new int[]{4}),
	DELETE_ALL(Arrays.asList("true","false"),2,"delete",new int[]{4});
	
	
    private List<String> list;//返回的List
    private int befPos;//应该识别的上一个参数的位置
    private String bef;//应该识别的上个参数的内容
    private int[] num;//这个参数可以出现的位置
    
    
    
    private TabGroup(List<String> list,int befPos, String bef, int[] num){
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
        for(TabGroup tab : TabGroup.values() ){
            if(tab.getBefPos()-1 >= Para.length){
                continue;
            }
            if((tab.getBef() == null || tab.getBef().equalsIgnoreCase(Para[tab.getBefPos()-1])) && Arrays.binarySearch(tab.getNum(),curNum)>=0){
            	List<String> list = new ArrayList<>();
            	if(tab.getNum()[0] == 4 && tab.getBef().equalsIgnoreCase("send")) {
            		list.add("@All");
            		list.add("@Online");
            		list.add("@Me");
            	}
            	if(!(Para[tab.getNum()[0] - 1] == null)) {
                	int length = Para[tab.getNum()[0] - 1].length();
                	String abc = Para[tab.getNum()[0] - 1];
                	for(String s : tab.getList()) {
                		if(s.regionMatches(true, 0, abc, 0, length)) list.add(s);
                	}
                	return list;
                }else {
                	tab.getList().addAll(list);
                	return tab.getList();
                }
            }
        }
        return null;
    }

    
}
